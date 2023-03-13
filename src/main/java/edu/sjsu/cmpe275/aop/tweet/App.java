package edu.sjsu.cmpe275.aop.tweet;

import java.util.UUID;

import org.aspectj.bridge.IMessage;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
		TweetService tweeter = (TweetService) ctx.getBean("tweetService");
		TweetStatsService stats = (TweetStatsService) ctx.getBean("tweetStatsService");

		try {

			UUID msg1 = tweeter.tweet("demo", "barbar");
			tweeter.follow("atest", "demo");
			tweeter.follow("atest", "prit");

			tweeter.follow("demo", "test");
			tweeter.follow("demo", "prit");


			UUID msg2 = tweeter.tweet("demo", "barbar");

			tweeter.like("atest",msg2);
			tweeter.report("atest",msg2);
			tweeter.reply("atest",msg2, "reply");

			tweeter.block("demo", "barbar");
			tweeter.block("test", "barbar");
			tweeter.block("demo", "barbar");
			tweeter.block("test", "palice");



		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Length : " + stats.getLengthOfLongestTweet());
		System.out.println("Maximum fanout: " + stats.getMostUnpopularFollower());

		ctx.close();
	}
}
