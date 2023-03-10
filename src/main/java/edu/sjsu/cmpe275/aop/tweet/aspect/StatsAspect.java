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
    //UUID for each tweet for particular user

    //Tweet map
    HashSet<String>  tweetSet ;
    HashSet<String> likeSet ;
    HashSet<String> reportSet ;

//    HashSet<String>[] tweetArr = new HashSet[3];

    //User map
    HashSet<String>[] userArr = new HashSet[5];


    @After("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
    public void dummyAfterAdvice(JoinPoint joinPoint) {
        System.out.printf("After the execution of the method %s\n", joinPoint.getSignature().getName());
//        Object[] obj = joinPoint.getArgs();
        //stats.resetStats();

        //mapping users with their followers
        if(joinPoint.getSignature().getName() == "follow"){
            Object[] obj = joinPoint.getArgs();

            System.out.println("follow method executed");

            if(!stats.userMap.containsKey(obj[1])){
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[5];

                //user's followers
                followerSet.add((String) obj[0]);

                userArr[1] = followerSet;
                userArr[0] = idSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;

                stats.userMap.put((String) obj[1], userArr);
                System.out.println("Follower added: "+  stats.userMap);

            }else if (!stats.userMap.containsKey(obj[0])) {
                System.out.println("User not found in the userMap");
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[5];

                userArr[0] = idSet;
                userArr[1] = followerSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;

//                blockSet.add((String) obj[1]);
//                userArr[2] = blockSet;
                stats.userMap.put((String) obj[0], userArr);
//                System.out.println("BlockSet initialized"+ stats.userMap.get((String) obj[0])[2]);
            }

            else {
                System.out.println("User exist :" + stats.userMap.get((String)obj[1])[1].add((String) obj[0]));
                System.out.println("Follower map" + stats.userMap.get((String)obj[1])[1]);
                System.out.println("User successfully added and map is: " + stats.userMap.get((String) obj[1])[1]);
            }
        }

        if(joinPoint.getSignature().getName() == "block"){
            System.out.println("------------------After execution of method block-------------------");
            System.out.println(stats.userMap);
            Object[] obj = joinPoint.getArgs();

            if(!stats.userMap.containsKey(obj[0])) {
                System.out.println("User not found in the userMap");
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[5];

                blockSet.add((String) obj[1]);
                userArr[0] = idSet;
                userArr[1] = followerSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;

                stats.userMap.put((String) obj[0], userArr);
                System.out.println("BlockSet initialized"+ stats.userMap.get((String) obj[0])[2]);

            } else if (!stats.userMap.containsKey(obj[1])) {
                System.out.println("User not found in the userMap");
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[5];
                userArr[0] = idSet;
                userArr[1] = followerSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;
                stats.userMap.put((String) obj[1], userArr);
            }else{
                System.out.println("User already exist");
                stats.userMap.get((String) obj[0])[2].add((String) obj[1]);
                System.out.println("BlockSet initialized"+ stats.userMap.get((String) obj[0])[2]);
            }
        }



        System.out.println("After all userMap : " + stats.userMap.size());
    }

    //Mapping UUIDs and tweets
    @AfterReturning(value = "execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))", returning = "id")
    public void getReturn(JoinPoint joinPoint, Object id) {
//        Object[] obj = joinPoint.getArgs();

        if(joinPoint.getSignature().getName() == "tweet"){
            Object[] obj = joinPoint.getArgs();
            System.out.println("\nRetrieving UUID of the tweet : " + id);
            System.out.println("Retrieving Obj of the tweet : " + obj[0] + "," + obj[1]);

           tweetSet = new HashSet<>();
            likeSet = new HashSet<>();
          reportSet = new HashSet<>();
            HashSet<String>[] tweetArr = new HashSet[3];
            //storing tweets
            tweetSet.add((String) obj[1]);

            //adding tweets to array
            tweetArr[0] = tweetSet;
            tweetArr[1] = likeSet;
            tweetArr[2] = reportSet;
            System.out.println("Tweet set :" + tweetArr);

            //mapping id and tweets
            stats.tweetMap.put((UUID) id, tweetArr);
            System.out.println("Tweet Map : " + stats.tweetMap);

            if(!stats.userMap.containsKey((String) obj[0])){
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[5];
                idSet.add(String.valueOf((UUID) id));

                userArr[0] = idSet;
                userArr[1] = followerSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;

                stats.userMap.put((String) obj[0], userArr);

            }else{
                System.out.println("Adding existing user  " + stats.userMap.get((String)obj[0])[0].add(String.valueOf((UUID) id)));
                System.out.println("Existing user found..");
            }
        }

        if(joinPoint.getSignature().getName() == "reply"){
            Object[] obj = joinPoint.getArgs();
            System.out.println("Returned replying id......" + id);
            HashSet<String> tweetSet = new HashSet<>();
            HashSet<String> likeSet = new HashSet<>();
            HashSet<String> reportSet = new HashSet<>();
            HashSet<String>[] tweetArr = new HashSet[3];


            tweetSet.add((String) obj[2]);

            tweetArr[0] = tweetSet;
            tweetArr[1] = likeSet;
            tweetArr[2] = reportSet;
            stats.tweetMap.put((UUID) id, tweetArr);


            if(!stats.userMap.containsKey((String) obj[0])){
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[5];

                idSet.add(String.valueOf((UUID) id));
                replySet.add(String.valueOf((UUID) obj[1]));

                userArr[0] = idSet;
                userArr[1] = followerSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;

                stats.userMap.put((String) obj[0], userArr);

            }else{

                System.out.println("Adding existing user  " + stats.userMap.get((String)obj[0])[0].add(String.valueOf((UUID) id)));
                stats.userMap.get((String)obj[0])[3].add(String.valueOf((UUID) obj[1]));

            }
        }

        System.out.println("After returning all userMap : " + stats.userMap);
        System.out.println("After returning all tweetMap : " + stats.tweetMap);

//        for(String key: userMap.keySet()){
//            System.out.println("Key :" + userMap.get(key));
//            HashSet<String>[] Key = (userMap.get(key));
//            System.out.println(Key.length);
//
//            for(int i =0 ; i < Key.length; i++ ){
//                if(!(Key[i] == null)){
//                    String[] setVal = Key[i].toArray(new String[0]);
//                    for(int j=0; j<setVal.length; j++){
//                        System.out.println("J :" +setVal[j]);
//                    }
//                }
//
//            }
//        }
    }
    @Before("execution(public * edu.sjsu.cmpe275.aop.tweet.TweetService.*(..))")
    public void dummyBeforeAdvice(JoinPoint joinPoint) {
//        System.out.printf("Before the execution of the method %s\n", joinPoint.getSignature().getName());
        Object[] obj = joinPoint.getArgs();

        if(joinPoint.getSignature().getName() == "like"){
            System.out.println("------------------Before execution of method like-------------------");
            System.out.println(stats.userMap);

            if(!stats.userMap.containsKey(obj[0])) {
                System.out.println("User not found in the userMap");
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[4];

                userArr[0] = idSet;
                userArr[1] = followerSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;

                stats.userMap.put((String) obj[0], userArr);
            }
            stats.tweetMap.get(obj[1])[1].add((String) obj[0]);
            System.out.println("Like added :" + stats.tweetMap);

        }

        if(joinPoint.getSignature().getName() == "report"){
            System.out.println("------------------Before execution of method report-------------------");
            System.out.println(stats.userMap);

            if(!stats.userMap.containsKey(obj[0])) {
                System.out.println("User not found in the userMap");
                HashSet<String> followerSet = new HashSet<>();
                HashSet<String> idSet = new HashSet<>();
                HashSet<String> blockSet = new HashSet<>();
                HashSet<String> replySet = new HashSet<>();
                HashSet<String>[] userArr = new HashSet[4];

                userArr[0] = idSet;
                userArr[1] = followerSet;
                userArr[2] = blockSet;
                userArr[3] = replySet;

                stats.userMap.put((String) obj[0], userArr);
            }
            stats.tweetMap.get(obj[1])[2].add((String) obj[0]);
            System.out.println("Like added :" + stats.tweetMap);

        }


    }

}
