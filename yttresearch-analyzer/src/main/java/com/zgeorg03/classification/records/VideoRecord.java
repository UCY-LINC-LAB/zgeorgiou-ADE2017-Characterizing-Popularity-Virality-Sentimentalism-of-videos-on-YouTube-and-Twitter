package com.zgeorg03.classification.records;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdemos01 on 3/15/2017.
 */
public class VideoRecord {

    private final int yt_category;
    private final int yt_artificial_category;
    private final long yt_uploaded ;
    private final long duration;
    private final double comments_sentiment_neg;
    private final double comments_sentiment_neu;
    private final double comments_sentiment_pos;
    private final double comments_sentiment_compound;
    private final List<DayRecord> days;

    /**
    public VideoRecord() {
    }
     **/

    public VideoRecord(int yt_category, int yt_artificial_category, long yt_uploaded, long duration,
                       double comments_sentiment_neg, double comments_sentiment_neu, double comments_sentiment_pos,
                       double comments_sentiment_compound) {
        this.yt_category = yt_category;
        this.yt_artificial_category = yt_artificial_category;
        this.yt_uploaded = yt_uploaded;
        this.duration = duration;
        this.comments_sentiment_neg = comments_sentiment_neg;
        this.comments_sentiment_neu = comments_sentiment_neu;
        this.comments_sentiment_pos = comments_sentiment_pos;
        this.comments_sentiment_compound = comments_sentiment_compound;
        this.days = new ArrayList<>();
    }

    public void addDay(DayRecord dayRecord){
        days.add(dayRecord);
    }

    public int getYt_category() {
        return yt_category;
    }

    public int getYt_artificial_category() {
        return yt_artificial_category;
    }

    public long getYt_uploaded() {
        return yt_uploaded;
    }

    public long getDuration() {
        return duration;
    }

    public double getComments_sentiment_neg() {
        return comments_sentiment_neg;
    }

    public List<DayRecord> getDays() {
        return days;
    }

    public double getComments_sentiment_neu() {
        return comments_sentiment_neu;
    }

    public double getComments_sentiment_pos() {
        return comments_sentiment_pos;
    }

    public double getComments_sentiment_compound() {
        return comments_sentiment_compound;
    }

}
