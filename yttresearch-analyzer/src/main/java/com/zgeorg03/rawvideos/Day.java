package com.zgeorg03.videoprocess;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class Day {
   private final long views_added;
   private final long likes_added;
   private final long dislikes_added;
   private final long comments_added;
   private final long tweets_added;
   private final long original_tweets_added;
   private final long retweets_added;

   public Day(long views_added, long likes_added, long dislikes_added, long comments_added, long tweets_added, long original_tweets_added, long retweets_added) {
      this.views_added = views_added;
      this.likes_added = likes_added;
      this.dislikes_added = dislikes_added;
      this.comments_added = comments_added;
      this.tweets_added = tweets_added;
      this.original_tweets_added = original_tweets_added;
      this.retweets_added = retweets_added;
   }

   public long getViews_added() {
      return views_added;
   }

   public long getLikes_added() {
      return likes_added;
   }

   public long getDislikes_added() {
      return dislikes_added;
   }

   public long getComments_added() {
      return comments_added;
   }

   public long getTweets_added() {
      return tweets_added;
   }

   public long getOriginal_tweets_added() {
      return original_tweets_added;
   }

   public long getRetweets_added() {
      return retweets_added;
   }
}
