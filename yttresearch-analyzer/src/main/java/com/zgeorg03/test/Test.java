package com.zgeorg03.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mongodb.ServerAddress;
import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 5/6/17.
 */
public class Test {

    static Map<String,Tags> map = new HashMap<>();
    static Map<String,Videos> videos = new HashMap<>();
    public static void main(String args[]) throws Exception {
        String scripts="./scripts/";
        DBConnection dbConnection = new DBConnection("yttresearch",new ServerAddress("localhost"));
        DBServices dbServices = new DBServices(dbConnection);
        dbServices.getProcessVideoDBService();

        JsonArray array = dbServices.getProcessVideoDBService().getVideosWithTheMostViews(0,100);

        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(scripts);

        for(JsonElement el :array){
            String video  = el.getAsJsonObject().get("video_id").getAsString();
            List<Document> tweets = dbServices.getProcessVideoDBService().getTweets(video);
            tweets.stream().forEach(tweet -> {

                String text = (String) tweet.get("text");
              //  System.out.println(text);
                List<String>  hashtags = (List<String>) tweet.get("hashtags");

                hashtags.stream().forEach(tag->{
                    tag = tag.toLowerCase();
                    Tags tags = map.getOrDefault(tag,new Tags());
                    tags.count++;
                    tags.videos.add(video);
                    map.putIfAbsent(tag,tags);

                    Videos v = videos.getOrDefault(video,new Videos());
                    v.add(tag);
                    videos.putIfAbsent(video,v);
                });

            });
        }
        map.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).forEach(x-> System.out.println(x));
        videos.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).forEach(x-> System.out.println(x));
    }


}



class Tags implements Comparable<Tags>{
    int count;
    TreeSet<String> videos =new TreeSet<>();

    @Override
    public String toString() {
        return count+"\t" + videos.toString();
    }

    @Override
    public int compareTo(Tags o) {
        return (count<o.count)?1 :((count>o.count)?-1:0);
    }
}

class Videos implements Comparable<Videos>{
    String id;
    Map<String,Integer> tags =new HashMap<>();

    @Override
    public String toString() {
        String str = tags.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue))
                .map(x->x.toString()).collect(Collectors.joining(",")).toString();
        return str;
    }

    public void add(String tag){
        int count = tags.getOrDefault(tag,0);
        tags.put(tag,count+1);
    }
    @Override
    public int compareTo(Videos o) {
        int count = tags.size();
        int count2 = o.tags.size();
        return (count<count2)?1 :((count>count2)?-1:0);
    }
}
