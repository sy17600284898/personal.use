package com.personal.use.quartz.config;

import commercial.cronjob.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author:
 * @Date: 2019-07-29 14:17
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */

@Component
public class JobSchedule implements CommandLineRunner {

	@Autowired
	private QuartzService quartzService;

	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		System.out.println("job run");
		quartzService.timingTask();
	}
}
