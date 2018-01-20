package com.zgeorg03;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.timestructures.AverageCountStructure;
import com.zgeorg03.timestructures.HitsCountStructure;

import java.util.concurrent.TimeUnit;

public class Stats {
    private long totalAdded;
    private long totalProcessed;

   private final  HitsCountStructure statusesReceived = new HitsCountStructure(60,5, TimeUnit.SECONDS);

   private final  HitsCountStructure statusesProcessed = new HitsCountStructure(60,5, TimeUnit.SECONDS);

   private final  HitsCountStructure statusesThatAreRetweet = new HitsCountStructure(60,5, TimeUnit.SECONDS);
   private final  HitsCountStructure statusesThatAreOriginal = new HitsCountStructure(60,5, TimeUnit.SECONDS);
   private final  HitsCountStructure statusesThatAreQuote = new HitsCountStructure(60,5, TimeUnit.SECONDS);

    private final AverageCountStructure statsQueueLength = new AverageCountStructure(60,5, TimeUnit.SECONDS);

    private final HitsCountStructure videosThatAreRetweet = new HitsCountStructure(60,5, TimeUnit.SECONDS);
    private final HitsCountStructure videosThatAreOriginal = new HitsCountStructure(60,5, TimeUnit.SECONDS);
    private final HitsCountStructure videosThatAreQuoted = new HitsCountStructure(60,5, TimeUnit.SECONDS);



    public Stats(){
    }
    public void addStatus() {
        totalAdded++;
        statusesReceived.addHit();
    }
    public void addOriginalTweet() {
        statusesThatAreOriginal.addHit();
    }
    public void addRetweet() {
        statusesThatAreRetweet.addHit();
    }
    public void addVideoRetweet() { videosThatAreRetweet.addHit(); }
    public void addVideoQuoted() { videosThatAreQuoted.addHit(); }
    public void addVideoOriginal() { videosThatAreOriginal.addHit(); }

    public void addQuote() {
        statusesThatAreQuote.addHit();
    }
    public void addProcessedStatus() {
        totalProcessed++;
        statusesProcessed.addHit();
    }

    public void setAverageQueueSize(int size) {
        statsQueueLength.addHit(size);
    }
    public String toCsv(){
        StringBuilder sb = new StringBuilder();
        sb.append(totalAdded+"\t")
                .append(totalProcessed+"\t\t")
                .append(statusesReceived.getLastHit()+"\t")
                .append(statusesProcessed.getLastHit()+"\tQ=")
                .append(statsQueueLength.getLastHit()+"\t\t")
                .append(statusesThatAreOriginal.getLastHit()+"\t")
                .append(statusesThatAreRetweet.getLastHit()+"\t")
                .append(statusesThatAreQuote.getLastHit()+"\t\t")
                .append(videosThatAreOriginal.getLastHit()+"\t")
                .append(videosThatAreRetweet.getLastHit()+"\t")
                .append(videosThatAreQuoted.getLastHit()+"\t")
                ;
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Stats{" +
                "totalAdded=" + totalAdded +
                ", totalProcessed=" + totalProcessed +
                ", statusesReceived=" + statusesReceived +
                ", statusesProcessed=" + statusesProcessed +
                ", statsQueueLength=" + statsQueueLength +
                '}';
    }

    public JsonElement toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("total_tweets_stream",totalAdded);
        object.addProperty("tweets_stream_per_sec",statusesReceived.getLastHit());
        object.addProperty("total_tweets_processed",totalProcessed);
        object.addProperty("tweets_processed_per_sec",statusesProcessed.getLastHit());
        object.addProperty("queue_length_avg",statsQueueLength.getLastHit());
        object.addProperty("statuses_original_per_sec",statusesThatAreOriginal.getLastHit());
        object.addProperty("statuses_retweets_per_sec",statusesThatAreRetweet.getLastHit());
        object.addProperty("statuses_quoted_per_sec",statusesThatAreQuote.getLastHit());
        object.addProperty("videos_original_tweets_per_sec",videosThatAreOriginal.getLastHit());
        object.addProperty("videos_retweeted_per_sec",videosThatAreRetweet.getLastHit());
        object.addProperty("videos_quoted_per_sec",videosThatAreQuoted.getLastHit());
        return object;
    }
}
