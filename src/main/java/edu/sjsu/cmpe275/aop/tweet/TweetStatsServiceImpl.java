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

		tweetMap = new HashMap<>();
		userMap = new HashMap<>();
		System.out.println("After resting stats : " + tweetMap + "and " + "user map is :" +userMap);
		
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		// TODO Auto-generated method stub
		int length = 0;
		for(UUID key : tweetMap.keySet()){
			HashSet<String> set = tweetMap.get(key)[0];
			for(String setItem : set){
				if(setItem.length() > length) {
					length = setItem.length();
				}
			}
		}
		System.out.println( "lenght :" + length);
		return length;
	}

	@Override
	public UUID getMostPopularMessage() {
		// TODO Auto-generated method stub

		UUID mostPopMsgID = null;
		int maxSharedWithSetSize = 0;

		for(UUID key: tweetMap.keySet()){

			int currSharedWithSetSize = tweetMap.get(key)[3].size();

			if(currSharedWithSetSize > maxSharedWithSetSize) {
				maxSharedWithSetSize = currSharedWithSetSize;
				mostPopMsgID = key;
			}else{
				if(currSharedWithSetSize == maxSharedWithSetSize){
					if(mostPopMsgID == null){
						maxSharedWithSetSize = currSharedWithSetSize;
						mostPopMsgID = key;
					} else if (key.compareTo(mostPopMsgID) < 0) {
						maxSharedWithSetSize = currSharedWithSetSize;
						mostPopMsgID = key;
					}
				}
			}
		}
		return mostPopMsgID;
	}	

	@Override
	public String getMostUnpopularFollower() {
		int maxBlockedBySize = 0;
		String maxBlockedUser = (String) userMap.keySet().toArray()[0];

		for(String key: userMap.keySet()){
			int blocksCount = userMap.get(key)[2].size();

			if(blocksCount > maxBlockedBySize || (blocksCount == maxBlockedBySize && key.compareTo(maxBlockedUser) < 0)){
				maxBlockedBySize = blocksCount;
				maxBlockedUser = key;
			}
		}
		return maxBlockedUser;
	}

	@Override
	public String getMostActiveFollower() {
		// TODO Auto-generated method stub
		int maxFollowSetSize = 0;
		String mostPopUser = (String) userMap.keySet().toArray()[0];

		for(String key: userMap.keySet()){
			int followsCount = userMap.get(key)[1].size();

			if(followsCount > maxFollowSetSize || (followsCount == maxFollowSetSize && key.compareTo(mostPopUser) < 0)){
				maxFollowSetSize = followsCount;
				mostPopUser = key;
			}
		}
		return mostPopUser;

	}

	@Override
	public UUID getMostContraversialMessage() {
		// TODO Auto-generated method stub
		double cScore = 0;
		double finalcScore = 0;
		UUID ansKey = null;

		HashMap<UUID, Double> cScoreMap;
		for(UUID key : tweetMap.keySet()){

			double l = tweetMap.get(key)[1].size();
			double r = tweetMap.get(key)[2].size();
			double lsq, rsq, lr = 0;

			lsq = l * l;
			rsq = r *r;
			lr = 2*l*r;

			cScore = ((lsq + lr + rsq) / ((lsq - lr + rsq) + 1));

			if (cScore > finalcScore) {
				finalcScore = cScore;
				ansKey = key;
			} else if(cScore == finalcScore) {
				if(ansKey == null) {
					finalcScore = cScore;
					ansKey = key;
				}
				else {
					if (key.compareTo(ansKey) < 0) {
						finalcScore = cScore;
						ansKey = key;
					}
				}
			}
		}
		System.out.println("Key : " +ansKey + " Controversial score :" + finalcScore);
		return ansKey;

	}

	@Override
	public int getMaximumMessageFanout() {
		// TODO Auto-generated method stub
		return 0;
	}


}



