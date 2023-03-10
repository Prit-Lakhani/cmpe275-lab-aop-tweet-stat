package edu.sjsu.cmpe275.aop.tweet;

import java.util.*;

public class TweetStatsServiceImpl implements TweetStatsService {

	public HashMap<UUID, HashSet<String>[]> tweetMap = new HashMap<>();
	public HashMap<String, HashSet<String>[]> userMap = new HashMap<>();

	@Override
	public void resetStatsAndSystem() {
		tweetMap.clear();
		userMap.clear();
	}
    
	@Override
	public int getLengthOfLongestTweet() {
		int length = 0;
		for(UUID key : tweetMap.keySet()){
			//getting message of each tweet from tweet map
			HashSet<String> set = tweetMap.get(key)[0];
			for(String setItem : set){
				if(setItem.length() > length) {
					length = setItem.length();
				}
			}
		}
		return length;
	}

	@Override
	public UUID getMostPopularMessage() {

		UUID mostPopMsgID = null;
		int maxSharedWithSetSize = 0;

		for(UUID key: tweetMap.keySet()){

			//getting shared message size from tweet map
			int currSharedWithSetSize = tweetMap.get(key)[3].size();

			//comparing size of the current tweet's sharedWithSet's size and maximum sharedWithSet
			if(currSharedWithSetSize > 0){
				if(currSharedWithSetSize > maxSharedWithSetSize ||(currSharedWithSetSize == maxSharedWithSetSize && key.compareTo(mostPopMsgID) < 0 ) ) {
					maxSharedWithSetSize = currSharedWithSetSize;
					mostPopMsgID = key;
				}
			}
		}
		return mostPopMsgID;
	}	

	@Override
	public String getMostUnpopularFollower() {
		int maxBlockedBySize = 0;
		String maxBlockedUser = null;

		for(String key: userMap.keySet()){
			int blocksCount = userMap.get(key)[2].size();
			if(blocksCount > 0){
				//updating the variables
				if(blocksCount > maxBlockedBySize || (blocksCount == maxBlockedBySize && key.compareTo(maxBlockedUser) < 0)){
					maxBlockedBySize = blocksCount;
					maxBlockedUser = key;
				}
			}
		}
		return maxBlockedUser;
	}

	@Override
	public String getMostActiveFollower() {
		int maxFollowSetSize = 0;
		String mostPopUser =  null;

		for(String key: userMap.keySet()){
			int followsCount = userMap.get(key)[1].size();
			if(followsCount > 0){
				if(followsCount > maxFollowSetSize || (followsCount == maxFollowSetSize && key.compareTo(mostPopUser) < 0)){
					maxFollowSetSize = followsCount;
					mostPopUser = key;
				}
			}
		}
		return mostPopUser;
	}

	@Override
	public UUID getMostContraversialMessage() {
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
			if(cScore > 0){
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

		}
		return ansKey;

	}

	@Override
	public int getMaximumMessageFanout() {
		int maximumFanout = 0;
		for (UUID id: tweetMap.keySet()) {
			int currentFanout = getTweetFanout(id, tweetMap.get(id));
			maximumFanout = Math.max(maximumFanout, currentFanout);
		}
		return maximumFanout;
	}

	public int getTweetFanout(UUID tweetId, HashSet<String>[] tweetArray) {
		Queue<String> queue = new LinkedList<>();
		String originalUser = getUserOfTweet(tweetId.toString());
		HashSet<String> set = new HashSet<>();
		queue.addAll(tweetArray[4]);
		while( !queue.isEmpty() ) {
			int len = queue.size();
			for (int i=0; i<len; i++) {
				String id = queue.poll();
				HashSet<String> tweetReplies = tweetMap.get(UUID.fromString(id))[4];
				queue.addAll(tweetReplies);
				String user = getUserOfTweet(id);
				set.add(user);
			}
		}
		set.remove(originalUser);
		return set.size();
	}

	public String getUserOfTweet(String tweetId) {
		for(String key: userMap.keySet()){
			HashSet<String> Idset = userMap.get(key)[0];
			for(String id: Idset){
				if(Idset.contains(tweetId)) {
					return key;
				}
			}
		}
		return null;
	}
}



