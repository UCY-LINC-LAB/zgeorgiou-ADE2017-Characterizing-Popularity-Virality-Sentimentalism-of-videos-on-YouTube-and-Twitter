package com.zgeorg03.videoprocess;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.utils.BsonModel;
import com.zgeorg03.utils.DateUtil;
import com.zgeorg03.utils.JsonModel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class DBVideo implements JsonModel,BsonModel{

    //Static
    private final String video_id;
    private final String title;
    private final String description;
    private final int category;
    private final int artificial_category;
    private final long published_at;
    private final long collected_at;
    private final long duration;

    private final List<Day> days;

    //Count these
    private long total_views;
    private long total_likes;
    private long total_dislikes;
    private long total_comments;
    private long total_tweets;
    private long total_original_tweets;
    private long total_retweets;
    private long total_channel_views;
    private long total_channel_comments;
    private long total_channel_subscribers;
    private long total_channel_videos;


    public DBVideo(String video_id, String title, String description, int category, int artificial_category, long published_at, long collected_at, long duration) {
        this.video_id = video_id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.artificial_category = artificial_category;
        this.published_at = published_at;
        this.collected_at = collected_at;
        this.duration = duration;
        this.days = new ArrayList<>();
    }

    public void setTotal_views(long total_views) {
        this.total_views = total_views;
    }

    public void setTotal_likes(long total_likes) {
        this.total_likes = total_likes;
    }

    public void setTotal_dislikes(long total_dislikes) {
        this.total_dislikes = total_dislikes;
    }

    public void setTotal_comments(long total_comments) {
        this.total_comments = total_comments;
    }

    public void setTotal_tweets(long total_tweets) {
        this.total_tweets = total_tweets;
    }

    public void setTotal_original_tweets(long total_original_tweets) {
        this.total_original_tweets = total_original_tweets;
    }

    public void setTotal_retweets(long total_retweets) {
        this.total_retweets = total_retweets;
    }

    @Override
    public String toString() {
        return "DBVideo{" +
                "video_id='" + video_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", artificial_category=" + artificial_category +
                ", published_at=" + published_at +
                ", collected_at=" + collected_at +
                ", duration=" + duration +
                ", days=" + days +
                ", total_views=" + total_views +
                ", total_likes=" + total_likes +
                ", total_dislikes=" + total_dislikes +
                ", total_comments=" + total_comments +
                ", total_tweets=" + total_tweets +
                ", total_original_tweets=" + total_original_tweets +
                ", total_retweets=" + total_retweets +
                '}';
    }

    public String getVideo_id() {
        return video_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public int getArtificial_category() {
        return artificial_category;
    }

    public long getPublished_at() {
        return published_at;
    }

    public long getCollected_at() {
        return collected_at;
    }

    public long getDuration() {
        return duration;
    }

    public List<Day> getDays() {
        return days;
    }

    public long getTotal_views() {
        return total_views;
    }

    public long getTotal_likes() {
        return total_likes;
    }

    public long getTotal_dislikes() {
        return total_dislikes;
    }

    public long getTotal_comments() {
        return total_comments;
    }

    public long getTotal_tweets() {
        return total_tweets;
    }

    public long getTotal_original_tweets() {
        return total_original_tweets;
    }

    public long getTotal_retweets() {
        return total_retweets;
    }

    @Override
    public JsonObject toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("video_id",video_id);
        result.addProperty("title",title);
        result.addProperty("description", description);
        result.addProperty("category",category);
        result.addProperty("artificial_category",artificial_category);
        result.addProperty("published_at",DateUtil.toDate(published_at));
        result.addProperty("published_at_timestamp",published_at);
        result.addProperty("collected_at", DateUtil.toDate(collected_at));
        result.addProperty("collected_at_timestamp",collected_at);
        result.addProperty("duration",duration);

        result.addProperty("total_views",total_views);
        result.addProperty("total_likes",total_likes);
        result.addProperty("total_dislikes",total_dislikes);
        result.addProperty("total_comments",total_comments);
        result.addProperty("total_tweets",total_tweets);
        result.addProperty("total_original_tweets",total_original_tweets);
        result.addProperty("total_retweets",total_retweets);
        result.addProperty("total_channel_views",total_channel_views);
        result.addProperty("total_channel_comments",total_channel_comments);
        result.addProperty("total_channel_subscribers",total_channel_subscribers);
        result.addProperty("total_channel_videos",total_channel_videos);

        JsonArray array = new JsonArray();
        days.stream().forEach(day -> array.add(day.toJson()));
        result.add("days",array);
        return result;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    @Override
    public Document toBson() {
        Document result = new Document();
        result.append("_id",video_id);
        result.append("title",title);
        result.append("description", description);
        result.append("category",category);
        result.append("artificial_category",artificial_category);
        result.append("published_at",DateUtil.toDate(published_at));
        result.append("published_at_timestamp",published_at);
        result.append("collected_at", DateUtil.toDate(collected_at));
        result.append("collected_at_timestamp",collected_at);
        result.append("duration",duration);
        result.append("total_views",total_views);
        result.append("total_likes",total_likes);
        result.append("total_dislikes",total_dislikes);
        result.append("total_comments",total_comments);
        result.append("total_tweets",total_tweets);
        result.append("total_original_tweets",total_original_tweets);
        result.append("total_retweets",total_retweets);
        result.append("total_channel_views",total_channel_views);
        result.append("total_channel_comments",total_channel_comments);
        result.append("total_channel_subscribers",total_channel_subscribers);
        result.append("total_channel_videos",total_channel_videos);

        List<Document> array = days.stream().map(day -> day.toBson()).collect(Collectors.toList());
        result.append("days",array);
        return result;
    }

    public void setTotal_channel_comments(long total_channel_comments) {
        this.total_channel_comments = total_channel_comments;
    }

    public void setTotal_channel_views(long total_channel_views) {
        this.total_channel_views = total_channel_views;
    }

    public void setTotal_channel_subscribers(long total_channel_subscribers) {
        this.total_channel_subscribers = total_channel_subscribers;
    }

    public void setTotal_channel_videos(long total_channel_videos) {
        this.total_channel_videos = total_channel_videos;
    }
}
