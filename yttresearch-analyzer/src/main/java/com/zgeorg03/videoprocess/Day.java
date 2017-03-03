package com.zgeorg03.videoprocess;

import com.google.gson.JsonObject;
import com.zgeorg03.utils.BsonModel;
import com.zgeorg03.utils.JsonModel;
import org.bson.Document;

import java.util.Map;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class Day implements JsonModel,BsonModel{
   private final String date;
   private final long views_added;
   private final long likes_added;
   private final long dislikes_added;
   private final long favorites_added;
   private final long comments_added;
   private final long channel_views_added;
   private final long channel_comments_added;
   private final long channel_subscribers_added;
   private final long channel_videos_added;

   private  long tweets_added;
   private  long original_tweets_added;
   private  long retweets_added;

   public Day(String date, long views_added, long likes_added, long dislikes_added, long favorites_added, long comments_added, long channel_views_added, long channel_comments_added, long channel_subscribers_added, long channel_videos_added){
      this.date = date;
      this.views_added = views_added;
      this.likes_added = likes_added;
      this.dislikes_added = dislikes_added;
      this.favorites_added = favorites_added;
      this.comments_added = comments_added;
      this.channel_views_added = channel_views_added;
      this.channel_comments_added = channel_comments_added;
      this.channel_subscribers_added = channel_subscribers_added;
      this.channel_videos_added = channel_videos_added;
   }

   @Override
   public String toString() {
      return "Day{" +
              "views_added=" + views_added +
              ", likes_added=" + likes_added +
              ", dislikes_added=" + dislikes_added +
              ", favorites_added=" + favorites_added +
              ", comments_added=" + comments_added +
              ", channel_views_added=" + channel_views_added +
              ", channel_comments_added=" + channel_comments_added +
              ", channel_subscribers_added=" + channel_subscribers_added +
              ", channel_videos_added=" + channel_videos_added +
              ", tweets_added=" + tweets_added +
              ", original_tweets_added=" + original_tweets_added +
              ", retweets_added=" + retweets_added +
              '}';
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

   public long getFavorites_added() {
      return favorites_added;
   }

   public long getChannel_views_added() {
      return channel_views_added;
   }

   public long getChannel_comments_added() {
      return channel_comments_added;
   }

   public long getChannel_subscribers_added() {
      return channel_subscribers_added;
   }

   public long getChannel_videos_added() {
      return channel_videos_added;
   }

   public String getDate() {
      return date;
   }

   @Override
   public JsonObject toJson() {
      JsonObject result = new JsonObject();
      result.addProperty("date",date);
      result.addProperty("views_added",views_added);
      result.addProperty("likes_added", likes_added);
      result.addProperty("dislikes_added",dislikes_added);
      result.addProperty("favorites_added",favorites_added);
      result.addProperty("comments_added",comments_added);
      result.addProperty("channel_views_added",channel_views_added);
      result.addProperty("channel_comments_added",channel_comments_added);
      result.addProperty("channel_subscriber_added",channel_subscribers_added);
      result.addProperty("channel_videos_added",channel_videos_added);
      return result;
   }

   @Override
   public JsonObject toJson(Map<String, Integer> view) {
      return toJson();
   }

   @Override
   public Document toBson() {
      Document result = new Document();
      result.append("date",date);
      result.append("views_added",views_added);
      result.append("likes_added", likes_added);
      result.append("dislikes_added",dislikes_added);
      result.append("favorites_added",favorites_added);
      result.append("comments_added",comments_added);
      result.append("channel_views_added",channel_views_added);
      result.append("channel_comments_added",channel_comments_added);
      result.append("channel_subscriber_added",channel_subscribers_added);
      result.append("channel_videos_added",channel_videos_added);
      return result;
   }
}
