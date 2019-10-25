package com.personal.use.quartz.config;

import com.personal.use.quartz.job.ScheduleJob;
import com.personal.use.quartz.util.SpringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;

/**
 * @author:
 * @Date: 2019-07-29 14:22
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public class JobFactory implements Job {
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
		Object object = SpringUtil.getBean(scheduleJob.getBeanName());
		try {
			Method method = object.getClass().getMethod(scheduleJob.getMethodName());
			method.invoke(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
