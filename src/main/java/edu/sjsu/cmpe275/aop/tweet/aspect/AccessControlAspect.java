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
@Order(1)
public class AccessControlAspect {

	@Autowired TweetStatsServiceImpl stats;

	//reply service
	@Before("execution(* edu.sjsu.cmpe275.aop.tweet.TweetService.reply(..))")
	public void replyAccessControl(JoinPoint joinPoint) throws Throwable {
		Object[] obj = joinPoint.getArgs();

		//getting original sender of the messsage
		String currUser = null;
		for(String key: stats.userMap.keySet()){
			HashSet<String> Idset = stats.userMap.get(key)[0];
			for(String id: Idset){
				if(Idset.contains(obj[1]));
				currUser = key;
			}
		}

		//user tries to reply their own message
		if(obj[0] == currUser) {
			throw new IllegalArgumentException( joinPoint.getSignature().getName() + " service : User cannot reply to their own message!");
		}

		//user has blocked the original message sender
		if(stats.userMap.get(obj[0])[5].contains(currUser)){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : Current user has blocked the original sender");
		}

		//message is not shared with user
		if(!stats.tweetMap.get(obj[1])[3].contains(obj[0].toString())){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : Current user has not been shared with the original message");
		}
	}

	//like service
	@Before("execution(* edu.sjsu.cmpe275.aop.tweet.TweetService.like(..))")
	public void likeAccessControl(JoinPoint joinPoint) throws Throwable {
		Object[] obj = joinPoint.getArgs();

		//message does not exist
		if(!stats.tweetMap.containsKey(obj[1])){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : The given message does not exist");
		}

		//user trires to like already liked message
		if(stats.tweetMap.get(obj[1])[1].contains(obj[0].toString())){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : The message with given ID is already successfully liked by the same user");
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
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : User tries to like his own messasges");
		}

		//message has not shared with current user
		if(!stats.tweetMap.get(obj[1])[3].contains(obj[0].toString())){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : The given user has not been successfully shared with the given message");
		}
	}

	//report service
	@Before("execution (* edu.sjsu.cmpe275.aop.tweet.TweetService.report(..))")
	public void reportAccessControl(JoinPoint joinPoint){
		Object[] obj = joinPoint.getArgs();

		//message does not exist
		if(!stats.tweetMap.containsKey(obj[1])){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : The given message does not exist");
		}

		//user trires to report already liked message
		if(stats.tweetMap.get(obj[1])[2].contains(obj[0].toString())){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : The message with given ID is already successfully reported by the same user");
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
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : User tries to report his own messasges");
		}

		//message has not shared with current user
		if(!stats.tweetMap.get(obj[1])[3].contains(obj[0].toString())){
			throw new AccessControlException( joinPoint.getSignature().getName() + " service : The given user has not been successfully shared with the given message");
		}
	}
}
