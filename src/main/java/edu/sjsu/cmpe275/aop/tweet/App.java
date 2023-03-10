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
			tweeter.follow("bob", "alice");
			tweeter.follow("dice", "alice");
			tweeter.follow("dice", "alice");
			tweeter.tweet("alice", "hi");
			UUID msg = tweeter.tweet("alice", "first tweet");
			tweeter.follow("dice", "alice");
			tweeter.follow("lim", "alice");
			tweeter.block("lim", "bob");
			tweeter.tweet("lim", "cmpe275");
//			System.out.println("provided id :" + msg);
			UUID reply=tweeter.reply("bob", msg, "that was brilliant");
			UUID toBob = tweeter.reply("prit", reply, "hello bob");
//			System.out.println("reply :"+reply);
			tweeter.like("lim", msg);
			tweeter.like("kim", msg);
//			tweeter.like("lim", msg);
			tweeter.report("kim", msg);
			tweeter.report("lim", msg);

//			tweeter.reply("alice", reply, "no comments!");
//			tweeter.block("bob", "alice");
//			tweeter.block("bob", "dice");
//			tweeter.tweet("dice", "hello");
//			tweeter.tweet("prit", "hi");
//			tweeter.tweet("prit", "tesla");
//			tweeter.tweet("prit", "hi");
//			tweeter.tweet("prit", "tesla");
//			tweeter.tweet("prit", "hi");
//			tweeter.tweet("prit", "tesla");


//			tweeter.tweet("Elon", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

//		System.out.println("Length of the longest tweet: " + stats.getLengthOfLongestTweet());
//		System.out.println("Most popular message: " + stats.getMostPopularMessage());
//		System.out.println("Maximum fanout: " + stats.getMaximumMessageFanout());
		ctx.close();
	}
}
