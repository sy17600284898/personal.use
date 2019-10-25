package com.personal.use.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 
 * AdminPointcut
 * 
 * @author: Lenovo
 * @date: 2019-04-09 10:37:19
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
public class AdminPointcut {
	/**
	 * Point cut for all actions.
	 */
	@Pointcut("execution(public * com.*.*(..))")
	public void pointcutForValidationAdminAction() {
		
	}
}