package com.zgeorg03.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.Groups;
import com.zgeorg03.classification.ClassificationManager;
import com.zgeorg03.classification.tasks.ClassifyTasks;
import com.zgeorg03.core.CsvProducer;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.database.services.ProcessVideoDBService;
import com.zgeorg03.services.helpers.Service;
import com.zgeorg03.utils.DateUtil;
import com.zgeorg03.analysis.models.Video;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class VideosService extends Service {

    private final ProcessVideoDBService processVideoDBService;
    private final CsvProducer csvProducer;
    private final ExecutorService executorService;
    private final ClassifyTasks classifyTasks;
    private final ClassificationManager manager;
    public VideosService(DBServices dbServices, CsvProducer csvProducer, ExecutorService executorService, ClassifyTasks classifyTasks, ClassificationManager manager) {
        super(dbServices);
        processVideoDBService = dbServices.getProcessVideoDBService();
        this.csvProducer = csvProducer;
        this.executorService = executorService;
        this.classifyTasks = classifyTasks;
        this.manager = manager;
    }


    /**
     * Total number of videos ready for analysis
     * @return
     */
    public int getTotalVideos(int artificialCategory){
        return dbServices.getProcessVideoDBService().getTotalVideos(artificialCategory);
    }


    public JsonObject getVideo(String videoId) {
        JsonObject object = new JsonObject();
        Video video = processVideoDBService.getVideo(videoId);
        if(video ==null){
            object.addProperty("error","Video " + videoId + " not found!");
            return object;
        }
        return  video.toJson();
    }

    public JsonArray getPopularVideos(int artificial_category,int offset,int lbl_wnd,int limit){
        return dbServices.getProcessVideoDBService().getVideosWithTheMostViews(artificial_category,offset,lbl_wnd,limit);
    }

    public JsonArray getViralVideos(int artificial_category,int offset,int lbl_wnd,int limit){
        return dbServices.getProcessVideoDBService().getVideosWithTheMostTweets(artificial_category,offset,lbl_wnd,limit);
    }

    public JsonArray getRecentVideos(int artificial_category,int days, int limit){
        return dbServices.getProcessVideoDBService().getRecentVideos(days,artificial_category,limit);
    }

    public JsonArray getRandomVideos(int artificial_category, int limit){
        return dbServices.getProcessVideoDBService().getRandomVideos(artificial_category,limit);
    }

    public JsonArray getComments(String videoId, int comments) {
        List<JsonObject> documentList = processVideoDBService.getComments(videoId).stream()
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

    public ProcessVideoDBService getProcessVideoDBService() {
        return processVideoDBService;
    }

    public String produceCsv(Groups groups, Set<String> popular, Set<String> viral) {
        CsvProducer.WriteCSVAnalysis analysis =  csvProducer.new WriteCSVAnalysis(groups.getExperimentId(),groups.getAllVideos(),popular,viral);
        executorService.submit(analysis);
        return "/"+groups.getExperimentId()+"/videos_features";
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ClassifyTasks getClassifyTasks() {
        return classifyTasks;
    }

    public boolean exists(String experiment) {
       return csvProducer.exists(experiment);
    }

    public boolean removeExperiment(String experiment) {
        return  manager.removeExperiment(experiment);
    }
}
