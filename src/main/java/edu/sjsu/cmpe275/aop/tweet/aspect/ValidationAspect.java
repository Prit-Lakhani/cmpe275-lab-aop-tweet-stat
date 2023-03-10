package edu.sjsu.cmpe275.aop.tweet.aspect;

import edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl;
import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.util.UUID;


@Aspect
@Order(3)
public class ValidationAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

	@Autowired
	TweetStatsServiceImpl validate;

	@Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetServiceImpl.*(..))")
	public void dummyBeforeAdvice(JoinPoint joinPoint) throws IOException {
		System.out.printf("Permission check before the executuion of the metohd %s\n", joinPoint.getSignature().getName());
		Object[] obj = joinPoint.getArgs();

		for (Object o: obj) {
			if(o == null ){
				throw new IllegalArgumentException("Null value detected for the given parameter");
			} else if (o == "") {
				throw new IllegalArgumentException("Empty value detected for the given parameter");
			}
		}

		System.out.println( "in validation aspects :"+validate.userMap);

		//validating tweet()
		if(joinPoint.getSignature().getName() == "tweet"){
			System.out.println("\nValidating tweet");
			System.out.println("Message length : "+obj[1].toString().length());
			if(obj[1].toString().length() > 140){
				throw new IllegalArgumentException("Message length must be less than 140 characters");
			}
		}

		//validating reply()
		if(joinPoint.getSignature().getName() == "reply"){
			System.out.println("\nValidating tweet reply");

			if(obj[2].toString().length() > 140){
				throw new IllegalArgumentException("Message length must be less than 140 characters");
			}

			if(!validate.tweetMap.containsKey(obj[1])){
					throw new IllegalArgumentException("Invalid UUID");
				}

		System.out.printf("User %s tweeted replied to message %s with message: %s\n", (String) obj[0], obj[1], (String) obj[2]);
		}

		//validating follow()
		if(joinPoint.getSignature().getName() == "follow"){
			System.out.println("Validating follow method");
			if(obj[0] == obj[1]){
				throw new IllegalArgumentException("User is trying to follow him/her self");
			}
		}

		//validating block()
		if(joinPoint.getSignature().getName() == "block"){
			System.out.println("Validating block method");
			if(obj[0] == obj[1]){
				throw new IllegalArgumentException("User is trying to block him/her self");
			}
		}

		//validating like()
		if(joinPoint.getSignature().getName() == "like"){
			System.out.println("Validating like method");
			if(!validate.tweetMap.containsKey(obj[1])){
				throw new IllegalArgumentException("Invalid UUID");
			}

//			if(validate.userMap.get(obj[0])[0].contains(obj[1].toString())){
//				throw new IllegalArgumentException("User is trying to like self-posted message");
//			}

			//try to like same message more than one time
//			if(validate.tweetMap.get(obj[1])[1].contains(obj[0].toString())){
//				throw new IllegalArgumentException("User is trying like the same message again");
//			}
		}

		//validating report()
		if(joinPoint.getSignature().getName() == "report"){
			System.out.println("Validating like method");
			if(!validate.tweetMap.containsKey(obj[1])){
				throw new IllegalArgumentException("Invalid UUID");
			}

		}

	}
	
}
