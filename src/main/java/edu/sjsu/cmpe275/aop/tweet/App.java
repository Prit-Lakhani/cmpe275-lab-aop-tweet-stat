package edu.sjsu.cmpe275.aop.tweet;

import java.util.UUID;

import org.aspectj.bridge.IMessage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {
		/***
		 * Following is a dummy implementation of App to demonstrate bean creation with
		 * Application context. You may make changes to suit your need, but this file is
		 * NOT part of the submission.
		 */

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
		TweetService tweeter = (TweetService) ctx.getBean("tweetService");
		TweetStatsService stats = (TweetStatsService) ctx.getBean("tweetStatsService");

		try {
			tweeter.follow("prit", "275");
			tweeter.follow("shail", "prit");
			tweeter.follow("shail", "275");

			UUID testID =tweeter.tweet("275", "test");
			UUID msg1 = tweeter.tweet("275", "cmpe");
//			tweeter.reply("prit", msg1, "demo reply");
			stats.resetStatsAndSystem();
			tweeter.report("prit", msg1);
			tweeter.report("prit", testID);
			tweeter.report("prit", testID);


//			System.out.println(stats.getMostContraversialMessage());


		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
		System.out.println("Most popular message: " + stats.getMostPopularMessage());
		System.out.println("Maximum fanout: " + stats.getMaximumMessageFanout());
		ctx.close();
	}
}
