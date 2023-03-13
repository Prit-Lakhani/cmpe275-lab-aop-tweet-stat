package edu.sjsu.cmpe275.aop.tweet.aspect;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

import java.io.IOException;
import java.util.UUID;

@Aspect
@Order(2)
public class RetryAspect {

	//maximum attempts
	int attempts = 4	;
	@Around("execution(* edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
	public Object dummyAdviceOne(ProceedingJoinPoint joinPoint) throws Throwable {
		Object res;

		for(int i =0; i< attempts; i++){
			try {
				System.out.println(i);
				res = joinPoint.proceed();
				return res;
			}
			catch (Throwable e) {
			}
		}
		throw new IOException(joinPoint.getSignature().getName()   +" cannot be executed due to network failure");
	}
}
