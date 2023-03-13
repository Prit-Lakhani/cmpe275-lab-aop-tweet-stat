package edu.sjsu.cmpe275.aop.tweet.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.tweet.TweetStatsServiceImpl;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.*;

@Aspect
@Order(3)
public class StatsAspect {
    @Autowired
    TweetStatsServiceImpl stats;

    //Tweet map
    HashSet<String>  tweetSet ; // tweet message (string)
    HashSet<String> likeSet ; // likes (users)
    HashSet<String> reportSet ; // report (users)
    HashSet<String> sharedWithSet; // sharedWith =  (followers set - blocked Set)
    HashSet<String> repliesSet;  // Ids of the replied message of the original tweet


    @After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
    public void dummyAfterAdvice(JoinPoint joinPoint) {

        //follow stats
        if(joinPoint.getSignature().getName() == "follow"){
            Object[] obj = joinPoint.getArgs();

            //adding followee in the user map if not exist
            if(!stats.userMap.containsKey(obj[1])){
                HashSet<String> following = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> followers = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[1] = following;
                userArr[0] = idSet;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[1], userArr);
            }

            //adding follower in the user map if not exist
            if (!stats.userMap.containsKey(obj[0])) {
                HashSet<String> following = new HashSet<>();
                HashSet<String> followers = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[0] = idSet;
                userArr[1] = following;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[0], userArr);
            }

            //adding followee (user is following the mentioned users)
            stats.userMap.get((String)obj[0])[1].add((String) obj[1]);

            //adding followers (user is followed by the mentioned users)
            stats.userMap.get((String)obj[1])[4].add((String) obj[0]);
        }

        //block stats
        if(joinPoint.getSignature().getName() == "block"){
            System.out.println(stats.userMap);
            Object[] obj = joinPoint.getArgs();

            //adding blocked by user (Bob is blocked by Alice) adding entry of Bob in user map, if not exist
            if(!stats.userMap.containsKey(obj[0])) {
                HashSet<String> following = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> followers = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[0] = idSet;
                userArr[1] = following;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[0], userArr);
            }

            //adding blocked user (Alice has blocked Bob) adding entry of Alice in user map, if not exist
            if (!stats.userMap.containsKey(obj[1])) {
                HashSet<String> following = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> followers = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[0] = idSet;
                userArr[1] = following;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[1], userArr);
            }

            //adding to the blocked by (user is blocked by the following users)
            stats.userMap.get((String) obj[1])[2].add((String) obj[0]);

            //adding to the blocked set (user has blocked the following users)
            stats.userMap.get((String) obj[0])[5].add((String) obj[1]);
        }
    }

    //Mapping UUIDs and tweets
    @AfterReturning(value = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))", returning = "id")
    public void getReturn(JoinPoint joinPoint, Object id) {

        if(joinPoint.getSignature().getName() == "tweet"){
            Object[] obj = joinPoint.getArgs();

           tweetSet = new HashSet<>();
           likeSet = new HashSet<>();
          reportSet = new HashSet<>();
          sharedWithSet = new HashSet<>();
          repliesSet = new HashSet<>();

          HashSet<String>[] tweetArr = new HashSet[5];

            tweetArr[0] = tweetSet;
            tweetArr[1] = likeSet;
            tweetArr[2] = reportSet;
            tweetArr[3] = sharedWithSet;
            tweetArr[4] = repliesSet;

            //adding user
            if(!stats.userMap.containsKey((String) obj[0])){
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> following = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();
                HashSet<String> followers = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[0] = idSet;
                userArr[1] = following;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[0], userArr);
            }
            //adding tweets(string)
            tweetSet.add((String) obj[1]);

            //users has tweeted the following tweets (tweet ids)
            stats.userMap.get((String)obj[0])[0].add(String.valueOf((UUID) id));

            //sharing message with all the followers
            sharedWithSet.addAll(stats.userMap.get((String) obj[0])[4]);

            //removing all the blocked users
            sharedWithSet.removeAll(stats.userMap.get((String) obj[0])[5]);

            //mapping id and tweets
            stats.tweetMap.put((UUID) id, tweetArr);
        }

        //reply stats
        if(joinPoint.getSignature().getName() == "reply"){
            Object[] obj = joinPoint.getArgs();
            HashSet<String> tweetSet = new HashSet<>();
            HashSet<String> likeSet = new HashSet<>();
            HashSet<String> reportSet = new HashSet<>();
            HashSet<String> sharedWithSet = new HashSet<>();
            HashSet<String> repliesSet = new HashSet<>();

            HashSet<String>[] tweetArr = new HashSet[5];

            tweetSet.add((String) obj[2]);

            tweetArr[0] = tweetSet;
            tweetArr[1] = likeSet;
            tweetArr[2] = reportSet;
            tweetArr[3] = sharedWithSet;
            tweetArr[4] = repliesSet;

            //adding user
            if(!stats.userMap.containsKey((String) obj[0])){
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> following = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> followers = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[0] = idSet;
                userArr[1] = following;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[0], userArr);
            }
            //adding tweet ID in tweet ID set of the user map
            stats.userMap.get((String)obj[0])[0].add(String.valueOf((UUID) id));

            //adding ID of the original message in reply set of the user map
            stats.userMap.get((String)obj[0])[3].add(String.valueOf((UUID) obj[1]));

            //adding and removing users to/from the sharedWith set based on the followers and blocked set
            sharedWithSet.addAll(stats.userMap.get((String) obj[0])[4]);
            sharedWithSet.removeAll(stats.userMap.get((String) obj[0])[5]);

            //sharing reply message with original sender
            for(String key: stats.userMap.keySet()){
                HashSet<String> tweetIdSet = stats.userMap.get(key)[0];

                if(tweetIdSet.contains(obj[1].toString())){
                    sharedWithSet.add(key);
                }
            }

            //adding reply message to the tweet map as a new tweet
            stats.tweetMap.put((UUID) id, tweetArr);

            //getting reply set from tweet map
            HashSet<String> replySet =  stats.tweetMap.get(obj[1])[4];

            //adding replying id to set
            replySet.add(id.toString());
        }
    }
    @Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
    public void dummyBeforeAdvice(JoinPoint joinPoint) {
        Object[] obj = joinPoint.getArgs();

        //like stats
        if(joinPoint.getSignature().getName() == "like"){

            //adding user
            if(!stats.userMap.containsKey(obj[0])) {
                HashSet<String> following = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> followers = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[0] = idSet;
                userArr[1] = following;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[0], userArr);
            }

            //adding user who has liked the messsage to the tweet map
            stats.tweetMap.get(obj[1])[1].add((String) obj[0]);
        }

        //report stats
        if(joinPoint.getSignature().getName() == "report"){

            //adding user
            if(!stats.userMap.containsKey(obj[0])) {
                HashSet<String> following = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockedBy = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String> followers = new HashSet<>();
                HashSet<String> blocked = new HashSet<>();

                HashSet<String>[] userArr = new HashSet[6];

                userArr[0] = idSet;
                userArr[1] = following;
                userArr[2] = blockedBy;
                userArr[3] = replySet;
                userArr[4] = followers;
                userArr[5] = blocked;

                stats.userMap.put((String) obj[0], userArr);
            }

            //adding user who has reported the message to tweet map
            stats.tweetMap.get(obj[1])[2].add((String) obj[0]);
        }
    }
}
