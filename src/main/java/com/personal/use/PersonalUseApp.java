package com.personal.use;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@ComponentScan(basePackages = { "com" })
@EnableAsync
@MapperScan("commercial.cronjob.cbi.mapper")
/**
 * 
 * CommercialCronJobApp
 * 
 * @author: dongmw2
 * @version: 2019-06-06 17:49:14
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public class PersonalUseApp {
	public static void main(String[] args) {
		SpringApplication.run(PersonalUseApp.class, args);
		System.out.println(" CommercialCronJobApp run successful......");
	}
}