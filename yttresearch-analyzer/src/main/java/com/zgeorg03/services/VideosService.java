package com.zgeorg03.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.Groups;
import com.zgeorg03.analysis.models.Stat;
import com.zgeorg03.analysis.models.Video;
import com.zgeorg03.analysis.sentiment.SentimentVideo;
import com.zgeorg03.classification.ClassificationManager;
import com.zgeorg03.classification.tasks.ClassifyTasks;
import com.zgeorg03.core.CsvProducer;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.database.services.ProcessVideoDBService;
import com.zgeorg03.services.helpers.Service;
import com.zgeorg03.utils.DateUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
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

    /**
     * Total number of videos ready for analysis, for each category
     * @return
     */
    public int getTotalVideosBaseCategory(int category){
        return dbServices.getProcessVideoDBService().getTotalVideosBaseCategory(category);
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
    public List<SentimentVideo> getVideosForSentiment(int n,int category){
        return processVideoDBService.getVideos(n,category);
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

    public void proccessSentiment(List<SentimentVideo> videos) throws IOException {
        PrintWriter pw0 = new PrintWriter(new FileWriter(new File("/tmp/thesis/v.txt")));
        PrintWriter pw1 = new PrintWriter(new FileWriter(new File("/tmp/thesis/t.txt")));
        PrintWriter pw2 = new PrintWriter(new FileWriter(new File("/tmp/thesis/r.txt")));
        videos.stream().sorted((e1,e2)-> {
            double v1 = e1.getNegative();
            double v2 = e2.getNegative();
            if(v1<v2)
                return 1;
            if(v1>v2)
                return -1;
            return 0;
        }) .forEach(x->{
            pw0.print(x.getViews()+"\t"+x.getNegative()+"\n");
            pw1.print(x.getTweets()+"\t"+x.getNegative()+"\n");
            pw2.print(x.getRetweets()+"\t"+x.getNegative()+"\n");
        });
        pw0.close();
        pw1.close();
        pw2.close();
    }

    public List<Stat<Double>> proccessSentimentBuckets(List<SentimentVideo> videos,int n) throws IOException {
        PrintWriter pw1 = new PrintWriter(new FileWriter(new File("/tmp/thesis/t.txt")));
        double max =videos.stream().mapToDouble(x->x.getTweets()).max().getAsDouble();
        int buckets= (int) Math.ceil(max/n);
        Double negativeArray[]=new Double[buckets];
        Double stds[]=new Double[buckets];
        int totalArray[]=new int[buckets];
        for(int i=0;i<buckets;i++) {
            negativeArray[i]=0d;
            stds[i]=0d;
        }
        videos.stream().forEach(x->{
            int bucket = (int) (x.getTweets()/n);
            negativeArray[bucket]+=x.getNegative();
            totalArray[bucket]++;
        });

        for(int i=0;i<buckets;i++) {
            negativeArray[i]/=(float)totalArray[i];
            if(negativeArray[i].isNaN()) {
                negativeArray[i]=0d;
            }
            pw1.print(i+"\t"+negativeArray[i]+"\n");
        }
        videos.stream().forEach(x->{
            int bucket = (int) (x.getTweets()/n);
            double avg=negativeArray[bucket];
            stds[bucket]+=(x.getNegative()-avg)*(x.getNegative()-avg);
        });
        List<Stat<Double>> stats= new LinkedList<>();
        for(int i=0;i<buckets;i++) {
            Double sum = Math.sqrt(stds[i]/totalArray[i]);
            if(sum.isNaN())
                sum=0d;
            Stat<Double> x = new Stat<Double>(negativeArray[i],-1d,sum);
            stats.add(x);
        }
        pw1.close();

        return stats;
    }

    public List<SentimentVideo> getVideosWithComments(int n){
        List<String> videos =  processVideoDBService.getVideosWithComments(n);
        List<SentimentVideo> allVideos = new LinkedList<>();
        videos.stream().forEach(videoId -> {
            Video video = processVideoDBService.getVideo(videoId);
            if(video!=null) {
                double views = video.getAverageViewsPerDay();
                double tweets = video.getAverageTweetsPerDay();
                double retweets = video.getAverageRetweetsPerDay();
                double likes = video.getAverageLikesPerDay();
                double friends = video.getAverageFriendsPerDay();
                double followers = video.getAverageFollowersPerDay();
                if(tweets>=1 && tweets<=600) {
                    SentimentVideo sentimentVideo = new SentimentVideo(video.getVideo_id(), views
                            , tweets, retweets
                            , video.getComments_sentiment().getNeg().getAverage()
                            , video.getComments_sentiment().getPos().getAverage()
                            , video.getComments_sentiment().getNeg().getAverage()
                            , video.getComments_sentiment().getCompound().getAverage(),
                            likes, friends, followers);
                    allVideos.add(sentimentVideo);
                }
            }
        });
        return allVideos;
    }

}
