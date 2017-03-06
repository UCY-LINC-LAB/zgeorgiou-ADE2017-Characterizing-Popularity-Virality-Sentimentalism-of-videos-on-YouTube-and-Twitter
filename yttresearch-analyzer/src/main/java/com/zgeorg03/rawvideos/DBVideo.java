package com.zgeorg03.videoprocess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class DBVideo {

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
                ", total_views=" + total_views +
                ", total_likes=" + total_likes +
                ", total_dislikes=" + total_dislikes +
                ", total_comments=" + total_comments +
                ", total_tweets=" + total_tweets +
                ", total_original_tweets=" + total_original_tweets +
                ", total_retweets=" + total_retweets +
                '}';
    }
}
