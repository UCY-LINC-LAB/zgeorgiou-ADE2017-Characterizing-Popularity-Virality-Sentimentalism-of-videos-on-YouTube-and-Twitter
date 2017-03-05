package com.zgeorg03.analysis;

import com.google.gson.JsonObject;
import com.zgeorg03.database.services.ProcessVideoDBService;
import com.zgeorg03.analysis.models.Video;
import com.zgeorg03.utils.JsonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by zgeorg03 on 3/5/17.
 */
public class Groups implements JsonModel{
    private Logger logger = LoggerFactory.getLogger(Groups.class);

   private final  Group popular;
   private final  Group viral;
   private final  Group recent;
   private final  Group random;
   private final  Group popular_viral;
   private final  Group popular_not_viral;
   private final  Group viral_not_popular;


    public Groups(boolean showDailyStats,int lbl_wnd,ProcessVideoDBService services, Map<String,List<Integer>> videos){

         popular = new Group("popular", showDailyStats,lbl_wnd);
         viral = new Group("viral", showDailyStats,lbl_wnd);
         recent = new Group("recent", showDailyStats,lbl_wnd);
         random = new Group("random", showDailyStats,lbl_wnd);
         popular_viral = new Group("popular_viral", showDailyStats,lbl_wnd);
         popular_not_viral = new Group("popular_not_viral", showDailyStats,lbl_wnd);
         viral_not_popular = new Group("viral_not_popular", showDailyStats,lbl_wnd);

        long last = System.currentTimeMillis();

        videos.entrySet().stream().forEach(entry ->{
            String videoId =  entry.getKey();
            List<Integer> groups =  entry.getValue();
            Video video = services.getVideo(videoId);
            boolean groupsIn [] = new boolean[4];
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
}
