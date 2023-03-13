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
			tweeter.follow("test", "demo");
			UUID msg2 = tweeter.tweet("demo", "barbar");

			tweeter.like("test",msg2);
			tweeter.report("test",msg2);
			tweeter.reply("test",msg2, "reply");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Length : " + stats.getLengthOfLongestTweet());
		System.out.println("Maximum fanout: " + stats.getMaximumMessageFanout());

		ctx.close();
	}
}
