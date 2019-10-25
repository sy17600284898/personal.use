package com.personal.use.quartz.schedule;

import com.personal.use.quartz.config.JobFactory;
import com.personal.use.quartz.enumutil.JobOperateEnum;
import com.personal.use.quartz.job.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author:
 * @Date: 2019-07-29 15:24
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {


    @Autowired
    private ScheduleJobMapper scheduleJobMapper;
    @Autowired
    private Scheduler scheduler;


    @Override
    public void addSchedule(ScheduleJob scheduleJob) {
        if (!isValidExpression(scheduleJob.getCronExpression())) {
            return;
        }
        scheduleJobMapper.save(scheduleJob);
        addjob(scheduleJob);
    }

    private boolean isValidExpression(final String cronExpression) {
        CronTriggerImpl trigger = new CronTriggerImpl();
        try {
            trigger.setCronExpression(cronExpression);
            Date date = trigger.computeFirstFireTime(null);

            return date != null && (date.after(new Date()));
        } catch (Exception e) {
            System.out.println("[TaskUtils.isValidExpression]:failed. throw ex:" + e);
        }
        return false;
    }

    @Override
    public void resumeSchedule(int id) {
        ScheduleJob job = scheduleJobMapper.selectByIdAndFlag(id);
        if (Objects.nonNull(job)) {
            if (job.getDeleteFlag()) {
                job.setDeleteFlag(false);
            }
            job.setStatus(JobOperateEnum.START.getValue());
            scheduleJobMapper.updateById(job);
            try {
                operateJob(JobOperateEnum.START.getValue(), job);
            } catch (SchedulerException e) {
                System.out.println("start job error" + job.getJobName());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pauseSchedule(int id) {
        ScheduleJob job = scheduleJobMapper.selectById(id);
        if (Objects.nonNull(job)) {
            job.setStatus(JobOperateEnum.PAUSE.getValue());
            scheduleJobMapper.updateById(job);
            try {
                operateJob(JobOperateEnum.PAUSE.getValue(), job);
            } catch (SchedulerException e) {
                System.out.println("pause job error" + job.getJobName());
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteSchedule(int id) {
        //此处省去数据验证
        ScheduleJob job = scheduleJobMapper.selectById(id);
        if (Objects.nonNull(job)) {
            scheduleJobMapper.deleteById(id);
            try {
                operateJob(JobOperateEnum.DELETE.getValue(), job);
            } catch (SchedulerException e) {
                System.out.println("delete job error" + job.getJobName());
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ScheduleJob> list() {
        List<ScheduleJob> list = scheduleJobMapper.list();
        return list;
    }

    @Override
    public void updateSchedule(ScheduleJob scheduleJob) {
        if (!isValidExpression(scheduleJob.getCronExpression())) {
            return;
        }
        scheduleJobMapper.updateById(scheduleJob);
        try {
            operateJob(scheduleJob.getStatus(), scheduleJob);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    public void addjob(ScheduleJob job) {
        try {
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).startNow().build();
            JobDetail jobDetail = JobBuilder.newJob(JobFactory.class).withIdentity(job.getJobName()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void operateJob(int status, ScheduleJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getJobName());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName())
                    .withSchedule(CronScheduleBuilder.cronSchedule(job.getCronExpression())).startNow().build();
            jobDetail = JobBuilder.newJob(JobFactory.class).withIdentity(job.getJobName()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            scheduler.scheduleJob(jobDetail, trigger);
        }
        switch (status) {
            case 1:
                scheduler.resumeJob(jobKey);
                break;
            case 2:
                scheduler.pauseJob(jobKey);
                break;
            case 3:
                scheduler.deleteJob(jobKey);
                break;
            default:
                break;
        }
    }
}
