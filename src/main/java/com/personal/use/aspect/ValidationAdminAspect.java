/**
 * @Title: ValidationAdminAspect.java
 * @Package: commercial.admin.common.aspect
 * @author: Lenovo
 * @date: 2019年7月17日 下午5:51:16
 * @version V1.0
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.
 */
package com.personal.use.aspect;


import devutility.external.javax.validation.ValidationUtils;
import devutility.external.javax.validation.annotation.Validation;
import devutility.external.javax.validation.model.ValidationResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * ValidationAdminAspect  所有非空检验
 *
 * @author: Lenovo
 * @version: 2019年7月17日
 * @Copyright: 2019 www.lenovo.com Inc. All rights reserved.  
 */
@Aspect
@Order(2)
@Component
public class ValidationAdminAspect {
    @Before("com.personal.use.aspect.AdminPointcut.pointcutForValidationAdminAction()")
    public void doBeforeAdvice(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object param : args) {
            if (param != null && param.getClass().isAnnotationPresent(Validation.class)) {
                ValidationResult result = ValidationUtils.validate(param);
                if (result.isFailed()) {
                    throw new RuntimeException(result.getFirstErrorMessage());
                }
            }
        }
    }
}
