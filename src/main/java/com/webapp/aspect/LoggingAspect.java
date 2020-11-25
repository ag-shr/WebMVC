package com.webapp.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Log4j2
public class LoggingAspect {

	@Pointcut("within(com.webapp.services.*)" +
	  " || within(com.webapp.repository.*)" +
	  " || within(com.webapp.controllers.*)" +
	  " || this(com.webapp.utilities.ServiceCallUtil)")
	public void applicationPackagePointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}
	@Pointcut("within(com.webapp.config.*)" +
	  "|| execution(* com.webapp.jwt.AwsCognitoIdTokenProcessor.*(..))" +
	  "|| this(com.webapp.jwt.JwtAuthentication)" +
	  "|| this(com.webapp.jwt.JwtConfiguration)")
	public void jwtConfigPointcut() {
		// Method is empty as this is just a Pointcut, the implementations are in the advices.
	}

	@AfterThrowing(pointcut = "applicationPackagePointcut() || jwtConfigPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
		  joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	@Around("applicationPackagePointcut() || jwtConfigPointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
		  joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		try {
			Object result = joinPoint.proceed();
			if (log.isDebugEnabled()) {
				log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
				  joinPoint.getSignature().getName(), result);
			}
			return result;
		} catch (IllegalArgumentException e) {
			log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
			  joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
			throw e;
		}
	}
}
