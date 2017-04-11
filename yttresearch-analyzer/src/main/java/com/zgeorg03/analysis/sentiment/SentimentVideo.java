package com.zgeorg03.analysis.sentiment;

/**
 * Created by zgeorg03 on 4/11/17.
 */
public class SentimentVideo {
    private final String videoId;
    private final double views;
    private final double tweets;
    private final double retweets;
    private final double negative;
    private final double positive;
    private final double neutral;
    private final double compound;

    public SentimentVideo(String videoId, double views, double tweets, double retweets, double negative, double positive, double neutral, double compound) {
        this.videoId = videoId;
        this.views = views;
        this.tweets = tweets;
        this.retweets = retweets;
        this.negative = negative;
        this.positive = positive;
        this.neutral = neutral;
        this.compound = compound;
    }

    @Override
    public String toString() {
        return "SentimentVideo{" +
                "videoId='" + videoId + '\'' +
                ", views=" + views +
                ", tweets=" + tweets +
                ", retweets=" + retweets +
                ", negative=" + negative +
                ", positive=" + positive +
                ", neutral=" + neutral +
                ", compound=" + compound +
                '}';
    }

    public String getVideoId() {
        return videoId;
    }

    public double getViews() {
        return views;
    }

    public double getTweets() {
        return tweets;
    }

    public double getRetweets() {
        return retweets;
    }

    public double getNegative() {
        return negative;
    }

    public double getPositive() {
        return positive;
    }

    public double getNeutral() {
        return neutral;
    }

    public double getCompound() {
        return compound;
    }
}
