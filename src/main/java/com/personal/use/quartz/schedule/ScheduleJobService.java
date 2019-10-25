package com.personal.use.quartz.schedule;

import com.personal.use.quartz.job.ScheduleJob;

import java.util.List;

/**
 *
 * ScheduleJobService
 *
 * @author: Lenovo
 * @date: 2019-7-28 11:40:39
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public interface ScheduleJobService {

    /**
     * addSchedule
     * @param job
     */
    void addSchedule(ScheduleJob job);

    /**
     * resumeSchedule
     * @param id
     */
    void resumeSchedule(int id);

    /**
     * pauseSchedule
     * @param id
     */
    void pauseSchedule(int id);

    /**
     * deleteSchedule
     * @param id
     */
    void deleteSchedule(int id);

    /**
     * list
     * @return
     */
    List<ScheduleJob> list();

    /**
     * updateSchedule
     * @param scheduleJob
     */
    void updateSchedule(ScheduleJob scheduleJob);
}
