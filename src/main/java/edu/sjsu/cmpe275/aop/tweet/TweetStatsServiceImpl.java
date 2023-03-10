package edu.sjsu.cmpe275.aop.tweet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class TweetStatsServiceImpl implements TweetStatsService {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */

	public HashMap<UUID, HashSet<String>[]> tweetMap = new HashMap<>();
	public HashMap<String, HashSet<String>[]> userMap = new HashMap<>();

	@Override
	public void resetStatsAndSystem() {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UUID getMostPopularMessage() {
		// TODO Auto-generated method stub
		return null;
	}	

	@Override
	public String getMostUnpopularFollower() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMostActiveFollower() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getMostContraversialMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaximumMessageFanout() {
		// TODO Auto-generated method stub
		return 0;
	}


}



