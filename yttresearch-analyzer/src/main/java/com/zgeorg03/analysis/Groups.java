package com.zgeorg03.analysis;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.models.Stat;
import com.zgeorg03.analysis.models.Video;
import com.zgeorg03.database.services.ProcessVideoDBService;
import com.zgeorg03.utils.JsonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by zgeorg03 on 3/5/17.
 */
public class Groups implements JsonModel{
    private Logger logger = LoggerFactory.getLogger(Groups.class);
    private final String experimentId;

   private final  Group popular;
   private final  Group viral;
   private final  Group recent;
   private final  Group random;
   private final  Group popular_viral;
   private final  Group popular_not_viral;
   private final  Group viral_not_popular;

   private final int totalVideos;
   private final List<Video> allVideos= new LinkedList<>();
   private final List<List<Boolean>> videoGroups = new LinkedList<>();

    public Groups(boolean showDailyStats, int lbl_wnd, ProcessVideoDBService services, Map<String, List<Integer>> videos, String experimentId){
        this.experimentId = experimentId;

        popular = new Group("popular", showDailyStats,lbl_wnd);
         viral = new Group("viral", showDailyStats,lbl_wnd);
         recent = new Group("recent", showDailyStats,lbl_wnd);
         random = new Group("random", showDailyStats,lbl_wnd);
         popular_viral = new Group("popular_viral", showDailyStats,lbl_wnd);
         popular_not_viral = new Group("popular_not_viral", showDailyStats,lbl_wnd);
         viral_not_popular = new Group("viral_not_popular", showDailyStats,lbl_wnd);
         totalVideos = videos.size();

        long last = System.currentTimeMillis();

        videos.entrySet().stream().forEach(entry ->{
            String videoId =  entry.getKey();
            List<Integer> groups =  entry.getValue();
            Video video = services.getVideo(videoId);
            allVideos.add(video);
            Boolean []groupsIn = new Boolean[4];
            for(int i =0;i<4;i++) groupsIn[i]=false;
            groups.forEach(i ->{
                groupsIn[i] = true;
                if(i==0)
                    popular.addVideo(video);
                if(i==1)
                    viral.addVideo(video);
                if(i==2)
                    recent.addVideo(video);
                if(i==3)
                    random.addVideo(video);
            });
            videoGroups.add(Arrays.asList(groupsIn));
            if(groupsIn[0] && groupsIn[1])
                popular_viral.addVideo(video);
            if(groupsIn[0] && !groupsIn[1])
                popular_not_viral.addVideo(video);
            if(groupsIn[1] && !groupsIn[0])
                viral_not_popular.addVideo(video);

        });

        logger.info("Groups created in: "+ (System.currentTimeMillis()-last) +" ms");

    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add("popular",popular.toJson());
        object.add("viral",viral.toJson());
        object.add("recent",recent.toJson());
        object.add("random",random.toJson());
        object.add("popular_viral",popular_viral.toJson());
        object.add("popular_not_viral",popular_not_viral.toJson());
        object.add("viral_not_popular",viral_not_popular.toJson());
        return object;
    }

    @Override
    public JsonObject toJson(Map<String, Integer> view) {
        return toJson();
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public Map<String,List<Double>> getViewsAverageDailyIncrease() {
        Map<String,List<Double>> map = new HashMap<>();
        map.put("popular",this.popular.getViewsAverageDailyIncrease(1));
        map.put("viral",this.viral.getViewsAverageDailyIncrease(1));
        map.put("recent",this.recent.getViewsAverageDailyIncrease(1));
        map.put("random",this.random.getViewsAverageDailyIncrease(1));
        map.put("popular_viral",this.popular_viral.getViewsAverageDailyIncrease(1));
        map.put("popular_not_viral",this.popular_not_viral.getViewsAverageDailyIncrease(1));
        map.put("viral_not_popular",this.viral_not_popular.getViewsAverageDailyIncrease(1));
        return map;
    }

    public Map<String,List<Double>> getTweetsAverageDailyIncrease() {
        Map<String,List<Double>> map = new HashMap<>();
        map.put("popular",this.popular.getTweetsAverageDailyIncrease(1));
        map.put("viral",this.viral.getTweetsAverageDailyIncrease(1));
        map.put("recent",this.recent.getTweetsAverageDailyIncrease(1));
        map.put("random",this.random.getTweetsAverageDailyIncrease(1));
        map.put("popular_viral",this.popular_viral.getTweetsAverageDailyIncrease(1));
        map.put("popular_not_viral",this.popular_not_viral.getTweetsAverageDailyIncrease(1));
        map.put("viral_not_popular",this.viral_not_popular.getTweetsAverageDailyIncrease(1));
        return map;
    }
    public Map<String,List<Double>> getRatioOriginalTotalTweets() {
        Map<String,List<Double>> map = new HashMap<>();
        map.put("popular",this.popular.getRatioOriginalTotalTweets(1));
        map.put("viral",this.viral.getRatioOriginalTotalTweets(1));
        map.put("recent",this.recent.getRatioOriginalTotalTweets(1));
        map.put("random",this.random.getRatioOriginalTotalTweets(1));
        map.put("popular_viral",this.popular_viral.getRatioOriginalTotalTweets(1));
        map.put("popular_not_viral",this.popular_not_viral.getRatioOriginalTotalTweets(1));
        map.put("viral_not_popular",this.viral_not_popular.getRatioOriginalTotalTweets(1));
        return map;
    }

    public Map<String,List<Double>> getAverageUsersReached() {
        Map<String,List<Double>> map = new HashMap<>();
        map.put("popular",this.popular.getAverageUsersReached(1));
        map.put("viral",this.viral.getAverageUsersReached(1));
        map.put("recent",this.recent.getAverageUsersReached(1));
        map.put("random",this.random.getAverageUsersReached(1));
        map.put("popular_viral",this.popular_viral.getAverageUsersReached(1));
        map.put("popular_not_viral",this.popular_not_viral.getAverageUsersReached(1));
        map.put("viral_not_popular",this.viral_not_popular.getAverageUsersReached(1));
        return map;
    }
    public Map<String,List<Map.Entry<Integer,Double>>> getVideosDistribution(){
        Map<String,List<Map.Entry<Integer,Double>>> map = new HashMap<>();

        map.put("popular", popular.getVideosAgeDistribution());
        map.put("viral", viral.getVideosAgeDistribution());
        map.put("random", random.getVideosAgeDistribution());
        map.put("popular_viral",popular_viral.getVideosAgeDistribution());



        return map;
    }


    public Map<String,Stat<Integer>> getAverageDuration() {
        Map<String,Stat<Integer>> map = new HashMap<>();
        map.put("Popular",this.popular.getAverageDuration());
        map.put("Viral",this.viral.getAverageDuration());
        map.put("Recent",this.recent.getAverageDuration());
        map.put("Random",this.random.getAverageDuration());
        map.put("Popular & Viral",this.popular_viral.getAverageDuration());
        map.put("Popular & not Viral",this.popular_not_viral.getAverageDuration());
        map.put("Viral & not Popular",this.viral_not_popular.getAverageDuration());
        return map;
    }

    public Map<String,Stat<Double>> getAverageNegativeSentiment() {
        Map<String,Stat<Double>> map = new HashMap<>();
        map.put("Popular",this.popular.getAverageNegativeSentiment());
        map.put("Viral",this.viral.getAverageNegativeSentiment());
        map.put("Recent",this.recent.getAverageNegativeSentiment());
        map.put("Random",this.random.getAverageNegativeSentiment());
        map.put("Popular & Viral",this.popular_viral.getAverageNegativeSentiment());
        map.put("Popular & not Viral",this.popular_not_viral.getAverageNegativeSentiment());
        map.put("Viral & not Popular",this.viral_not_popular.getAverageNegativeSentiment());
        return map;
    }
    public Map<String,Stat<Double>> getAveragePositiveSentiment() {
        Map<String,Stat<Double>> map = new HashMap<>();
        map.put("Popular",this.popular.getAveragePositiveSentiment());
        map.put("Viral",this.viral.getAveragePositiveSentiment());
        map.put("Recent",this.recent.getAveragePositiveSentiment());
        map.put("Random",this.random.getAveragePositiveSentiment());
        map.put("Popular & Viral",this.popular_viral.getAveragePositiveSentiment());
        map.put("Popular & not Viral",this.popular_not_viral.getAveragePositiveSentiment());
        map.put("Viral & not Popular",this.viral_not_popular.getAveragePositiveSentiment());
        return map;
    }

    /**
     * 0 -> Popular
     * 1 -> Viral
     * 2 -> Recent
     * 3 -> Random
     * @return
     */
    public List<Double> getGroupsPercentages() {
        List<Double> map = new LinkedList<>();

        int popular=0;
        int viral=0;
        int popular_viral=0;
        int recent=0;
        int popular_recent=0;
        int recent_viral=0;
        int popular_viral_recent=0;

        int total = videoGroups.size();
        for(List<Boolean> groups : videoGroups) {
            boolean p = groups.get(0);
            boolean v = groups.get(1);
            boolean r = groups.get(2);
            if(p && !v && !r)
                popular++;
            else if(!p && v && !r)
                viral++;
            else if(p && v && !r)
                popular_viral++;
            else if(!p && !v && r)
                recent++;
            else if(p && !v && r)
                popular_recent++;
            else if(!p && v && r)
                recent_viral++;
            else if(p && v && r)
                popular_viral_recent++;
        }
        map.add(popular/(double)total*100);
        map.add(viral/(double)total*100);
        map.add(popular_viral/(double)total*100);
        map.add(recent/(double)total*100);
        map.add(popular_recent/(double)total*100);
        map.add(recent_viral/(double)total*100);
        map.add(popular_viral_recent/(double)total*100);

        System.out.println(map);
        return map;
    }



    public String getExperimentId() {
        return experimentId;
    }

    public JsonElement getInfo() {
        JsonObject result= new JsonObject();

        JsonObject object = new JsonObject();
        object.add("popular",popular.getInfo());
        object.add("viral",viral.getInfo());
        object.add("recent",recent.getInfo());
        object.add("random",random.getInfo());
        object.add("popular_viral",popular_viral.getInfo());
        object.add("popular_not_viral",popular_not_viral.getInfo());
        object.add("viral_not_popular",viral_not_popular.getInfo());
        result.add("groups_info",object);
        //result.add("videos",videosInfo());
        return result;
    }


    private JsonElement videosInfo() {
        JsonArray array = new JsonArray();
        allVideos.forEach(v->{
            array.add(v.getVideo_id());
        });
        return array;
    }

    public List<Video> getAllVideos() {
        return allVideos;
    }
}
