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
@Order(2)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

    @Autowired
    TweetStatsServiceImpl stats;

    //Tweet map
    HashSet<String>  tweetSet ;
    HashSet<String> likeSet ;
    HashSet<String> reportSet ;
    HashSet<String> sharedWithSet; ;


    @After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
    public void dummyAfterAdvice(JoinPoint joinPoint) {

        //mapping users with their followers
        if(joinPoint.getSignature().getName() == "follow"){
            Object[] obj = joinPoint.getArgs();

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

            stats.userMap.get((String)obj[0])[1].add((String) obj[1]);
            stats.userMap.get((String)obj[1])[4].add((String) obj[0]);
        }

        if(joinPoint.getSignature().getName() == "block"){
            System.out.println(stats.userMap);
            Object[] obj = joinPoint.getArgs();

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

            stats.userMap.get((String) obj[1])[2].add((String) obj[0]);
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

          HashSet<String>[] tweetArr = new HashSet[4];

            tweetArr[0] = tweetSet;
            tweetArr[1] = likeSet;
            tweetArr[2] = reportSet;
            tweetArr[3] = sharedWithSet;

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
            tweetSet.add((String) obj[1]);
            stats.userMap.get((String)obj[0])[0].add(String.valueOf((UUID) id));

            // sharing message with..
            sharedWithSet.addAll(stats.userMap.get((String) obj[0])[4]);
            sharedWithSet.removeAll(stats.userMap.get((String) obj[0])[5]);

            //mapping id and tweets
            stats.tweetMap.put((UUID) id, tweetArr);
        }

        if(joinPoint.getSignature().getName() == "reply"){
            Object[] obj = joinPoint.getArgs();
            HashSet<String> tweetSet = new HashSet<>();
            HashSet<String> likeSet = new HashSet<>();
            HashSet<String> reportSet = new HashSet<>();
            HashSet<String> sharedWithSet = new HashSet<>();

            HashSet<String>[] tweetArr = new HashSet[4];

            tweetSet.add((String) obj[2]);

            tweetArr[0] = tweetSet;
            tweetArr[1] = likeSet;
            tweetArr[2] = reportSet;
            tweetArr[3] = sharedWithSet;

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
                stats.userMap.get((String)obj[0])[0].add(String.valueOf((UUID) id));
                stats.userMap.get((String)obj[0])[3].add(String.valueOf((UUID) obj[1]));

            sharedWithSet.addAll(stats.userMap.get((String) obj[0])[4]);
            sharedWithSet.removeAll(stats.userMap.get((String) obj[0])[5]);

            //sharing reply message with original sender
            for(String key: stats.userMap.keySet()){
                HashSet<String> tweetIdSet = stats.userMap.get(key)[0];

                if(tweetIdSet.contains(obj[1].toString())){
                    sharedWithSet.add(key);
                }
            }
            stats.tweetMap.put((UUID) id, tweetArr);
        }

        System.out.println("After returning all userMap : " + stats.userMap);
        System.out.println("After returning all tweetMap : " + stats.tweetMap);


    }
    @Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
    public void dummyBeforeAdvice(JoinPoint joinPoint) {
        Object[] obj = joinPoint.getArgs();

        if(joinPoint.getSignature().getName() == "like"){
            System.out.println(stats.userMap);

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
            stats.tweetMap.get(obj[1])[1].add((String) obj[0]);
        }

        if(joinPoint.getSignature().getName() == "report"){
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
            stats.tweetMap.get(obj[1])[2].add((String) obj[0]);
        }
    }
}
