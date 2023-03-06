package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;

@Aspect
@Order(3)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl.*(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) {
		System.out.printf("Permission check before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] obj = joinPoint.getArgs();

		for (Object o: obj) {
			if(o == null || o == ""){
				throw new IllegalArgumentException();
			}
		}



		if(joinPoint.getSignature().getName() == "tweet"){
			System.out.println("\nValidating tweet()");
			System.out.println("Message length : "+obj[1].toString().length());
			if(obj[1].toString().length() > 140){
				throw new IllegalArgumentException();
			}
		}

	}
	
}
