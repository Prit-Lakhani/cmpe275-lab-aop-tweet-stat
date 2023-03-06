package edu.sjsu.cmpe275.aop.tweet;

import java.util.UUID;

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
//			tweeter.follow("bob", "alice");
//			UUID msg = tweeter.tweet("alice", "first tweet");
//			UUID reply=tweeter.reply("bob", msg, "that was brilliant");
//			tweeter.like("bob", msg);
//			tweeter.reply("alice", reply, "no comments!");
//			tweeter.block("alice", "bob");
			tweeter.tweet("Elon", "Musk");
//			tweeter.tweet("Elon", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
		System.out.println("Most popular message: " + stats.getMostPopularMessage());
		System.out.println("Maximum fanout: " + stats.getMaximumMessageFanout());
		ctx.close();
	}
}
