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

			tweeter.follow("prit", "shail");
			tweeter.follow("abc", "prit");
			tweeter.follow("bob", "prit");


			UUID msg = tweeter.tweet("shail", "abc");
			UUID msg2 = tweeter.reply("prit", msg, "abc");
			UUID msg3 = tweeter.reply("abc", msg2, "abc");

			System.out.println("Shail Tweet " + msg);
			System.out.println("Prit Reply " + msg2);


		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Length : " + stats.getLengthOfLongestTweet());
		System.out.println("Maximum fanout: " + stats.getMaximumMessageFanout());
		System.out.println("Most unpopular follower : "+ stats.getMostUnpopularFollower());
		System.out.println("Most active follower : "+ stats.getMostActiveFollower());
		System.out.println("Most popular messsage : "+ stats.getMostPopularMessage());
		System.out.println("Most contraversial messsage : "+ stats.getMostContraversialMessage());

		ctx.close();
	}
}
