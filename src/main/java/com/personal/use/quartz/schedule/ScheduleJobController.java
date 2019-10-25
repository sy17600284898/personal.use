package com.personal.use.quartz.schedule;

import com.personal.use.quartz.job.ScheduleJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * ScheduleJobController
 *
 * @author:zy
 * @Date: 2019-07-29 15:42
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
@Api(value = "schedule job controller", tags = {"schedule Job"})
@RestController
@RequestMapping("/scheduleJob")
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService jobService;

    /**
     * addSchedule
     *
     * @param job
     * @return
     */
    @ApiOperation(value = "Add a new task")
    @PostMapping("/addSchedule")
    public String addSchedule(ScheduleJob job) {
        jobService.addSchedule(job);
        return "addSchedule job success";
    }

    /**
     * resumeSchedule
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "Resume a task")
    @GetMapping("/resumeSchedule/{id}")
    public String resumeSchedule(@PathVariable("id") Integer id) {
        jobService.resumeSchedule(id);
        return "resumeSchedule job success";
    }

    /**
     * pauseSchedule
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "Pause a task")
    @GetMapping("/pauseSchedule/{id}")
    public String pauseSchedule(@PathVariable("id") Integer id) {
        jobService.pauseSchedule(id);
        return "pauseSchedule job success";
    }

    /**
     * deleteSchedule
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "Delete a task")
    @DeleteMapping("deleteSchedule/{id}")
    public String deleteSchedule(@PathVariable("id") Integer id) {
        jobService.deleteSchedule(id);
        return "deleteSchedule job success";
    }

    /**
     * updateSchedule
     *
     * @param scheduleJob
     * @return
     */
    @ApiOperation(value = "Modify a task")
    @PostMapping("/updateSchedule")
    public String updateSchedule(ScheduleJob scheduleJob) {
        jobService.updateSchedule(scheduleJob);
        return "update job success";
    }

}
