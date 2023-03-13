package edu.sjsu.cmpe275.aop.tweet.aspect;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.io.IOException;


@Aspect
@Order(0)
public class ValidationAspect {
	@Autowired
	TweetStatsServiceImpl validate;

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl.*(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) throws IOException {
		Object[] obj = joinPoint.getArgs();

		//checking null or empty
		for (Object o: obj) {
			if(o == null ){
				throw new IllegalArgumentException( joinPoint.getSignature().getName() + " service : Null value detected for the given parameter");
			} else if (o == "") {
				throw new IllegalArgumentException( joinPoint.getSignature().getName() + " service : Empty value detected for the given parameter");
			}
		}

		//validating tweet()
		if(joinPoint.getSignature().getName() == "tweet"){
			System.out.println("\nValidating tweet");
			System.out.println("Message length : "+obj[1].toString().length());
			//validating message length
			if(obj[1].toString().length() > 140){
				throw new IllegalArgumentException( joinPoint.getSignature().getName() + " service : Message length must be less than 140 characters");
			}
		}

		//validating reply()
		if(joinPoint.getSignature().getName() == "reply"){
			System.out.println("\nValidating tweet reply");

			//validating message length
			if(obj[2].toString().length() > 140){
				throw new IllegalArgumentException( joinPoint.getSignature().getName() + " service : Message length must be less than 140 characters");
			}

			//checking whether message is exist or not
			if(!validate.tweetMap.containsKey(obj[1])){
					throw new IllegalArgumentException("Invalid UUID");
				}

		System.out.printf("User %s tweeted replied to message %s with message: %s\n", (String) obj[0], obj[1], (String) obj[2]);
		}

		//validating follow()
		if(joinPoint.getSignature().getName() == "follow"){
			System.out.println("Validating follow method");

			//checking if the user is trying to follow themselves
			if(obj[0] == obj[1]){
				throw new IllegalArgumentException( joinPoint.getSignature().getName() + " service : User is trying to follow him/her self");
			}
		}

		//validating block()
		if(joinPoint.getSignature().getName() == "block"){
			System.out.println("\nValidating block method");

			//checking if the user is trying to block themselves
			if(obj[0] == obj[1]){
				throw new IllegalArgumentException( joinPoint.getSignature().getName() + " service : User is trying to block him/her self");
			}
		}
	}
}
