package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;
import java.security.AccessControlException;
import java.util.HashSet;

@Aspect
@Order(0)
public class AccessControlAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     * @throws Throwable 
     */

	@Autowired TweetStatsServiceImpl stats;

	//reply service
	@Before("execution(* edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..))")
	public void replyAccessControl(JoinPoint joinPoint) throws Throwable {
		Object[] obj = joinPoint.getArgs();
		if(!stats.tweetMap.get(obj[1])[3].contains(obj[0].toString())){
			throw new AccessControlException("Current user has not been shared with the original message");
		}

		//getting original sender of the messsage
		String currUser = null;
		for(String key: stats.userMap.keySet()){
			HashSet<String> Idset = stats.userMap.get(key)[0];
			for(String id: Idset){
				if(Idset.contains(obj[1]));
				currUser = key;
			}

		}

		if(stats.userMap.get(obj[0])[5].contains(currUser)){
			throw new AccessControlException("Current user has blocked the original sender");
		}
	}

	//like service
	@Before("execution(* edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void likeAccessControl(JoinPoint joinPoint) throws Throwable {
		Object[] obj = joinPoint.getArgs();

		//message does not exist
		if(!stats.tweetMap.containsKey(obj[1])){
			throw new AccessControlException("The given message does not exist");
		}

		//user trires to like already liked message
		if(stats.tweetMap.get(obj[1])[1].contains(obj[0].toString())){
			throw new AccessControlException("The message with given ID is already successfully liked by the same user");
		}

		//getting original sender of the messsage
		String currUser = null;
		for(String key: stats.userMap.keySet()){
			HashSet<String> Idset = stats.userMap.get(key)[0];
			for(String id: Idset){
				if(Idset.contains(obj[1]));
				currUser = key;
			}
		}

		//user tried to like his own message
		if(currUser == obj[0].toString()){
			throw new AccessControlException("User tries to like his own messasges");
		}

		//message has not shared with current user
		if(!stats.tweetMap.get(obj[1])[3].contains(obj[0].toString())){
			throw new AccessControlException("The given user has not been successfully shared with the given message");
		}
	}

	//report service
	@Before("execution (* edu.sjsu.cmpe275.aop.tweet.TweetService.report(..))")
	public void reportAccessControl(JoinPoint joinPoint){
		Object[] obj = joinPoint.getArgs();

		//message does not exist
		if(!stats.tweetMap.containsKey(obj[1])){
			throw new AccessControlException("The given message does not exist");
		}

		//user trires to report already liked message
		if(stats.tweetMap.get(obj[1])[2].contains(obj[0].toString())){
			throw new AccessControlException("The message with given ID is already successfully reported by the same user");
		}

		//getting original sender of the messsage
		String currUser = null;
		for(String key: stats.userMap.keySet()){
			HashSet<String> Idset = stats.userMap.get(key)[0];
			for(String id: Idset){
				if(Idset.contains(obj[1]));
				currUser = key;
			}
		}

		//user tries to report his own message
		if(currUser == obj[0].toString()){
			throw new AccessControlException("User tries to report his own messasges");
		}

		//message has not shared with current user
		if(!stats.tweetMap.get(obj[1])[3].contains(obj[0].toString())){
			throw new AccessControlException("The given user has not been successfully shared with the given message");
		}
	}
}
