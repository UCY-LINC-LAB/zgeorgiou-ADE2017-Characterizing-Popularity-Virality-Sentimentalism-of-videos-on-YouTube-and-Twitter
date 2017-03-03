package com.zgeorg03.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.database.services.ProcessVideosDBService;
import com.zgeorg03.services.helpers.Service;
import com.zgeorg03.utils.DateUtil;
import com.zgeorg03.videoprocess.DBVideo;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class VideosService extends Service {

    private final ProcessVideosDBService processVideosDBService;
    public VideosService(DBServices dbServices) {
        super(dbServices);
        processVideosDBService = dbServices.getProcessVideosDBService();
    }


    /**
     * Total number of videos ready for analysis
     * @return
     */
    public int getTotalVideos(){
        return dbServices.getProcessVideosDBService().getTotalVideos();
    }


    public JsonObject getVideo(String videoId) {
        JsonObject object = new JsonObject();
        DBVideo video = processVideosDBService.getVideo(videoId);
        if(video==null){
            object.addProperty("error","Video " + videoId + " not found!");
            return object;
        }
        return  video.toJson();
    }

    public JsonArray getComments(String videoId, int comments) {
        List<JsonObject> documentList = processVideosDBService.getComments(videoId).stream()
                .map(document -> {
                    JsonObject comment = new JsonObject();
                    comment.addProperty("id",document.getString("_id"));
                    comment.addProperty("published_at", DateUtil.toDate(document.getLong("published_at")));
                    comment.addProperty("published_at_timestamp",document.getLong("published_at"));
                    comment.addProperty("text",document.getString("text"));
                    comment.addProperty("like_count",document.getLong("like_count"));
                    return comment;
                })
                .limit(comments).collect(Collectors.toList());
        JsonArray array = new JsonArray();
        documentList.stream().forEach(x -> array.add(x));
        return array;
    }
}
