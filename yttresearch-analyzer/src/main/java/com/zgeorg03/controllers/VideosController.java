package com.zgeorg03.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.analysis.Groups;
import com.zgeorg03.classification.FeatureManager;
import com.zgeorg03.controllers.helpers.GetRequest;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.controllers.helpers.Parameter;
import com.zgeorg03.controllers.helpers.ParseParameters;
import com.zgeorg03.core.PlotProducer;
import com.zgeorg03.services.VideosService;
import com.zgeorg03.utils.Categories;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class VideosController {

    private final VideosService videosService;
    private final PlotProducer plotProducer;

    public VideosController(VideosService videosService, PlotProducer plotProducer) {
        this.videosService = videosService;
        this.plotProducer = plotProducer;

        new GetRequest("/videos"){

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                result.addNumber("total_videos",videosService.getTotalVideos(0));
                result.addNumber(Categories.getArtificial_categories().get(1),videosService.getTotalVideos(1));
                result.addNumber(Categories.getArtificial_categories().get(2),videosService.getTotalVideos(2));
                result.addNumber(Categories.getArtificial_categories().get(3),videosService.getTotalVideos(3));
                result.addNumber(Categories.getArtificial_categories().get(4),videosService.getTotalVideos(4));
                result.addNumber(Categories.getArtificial_categories().get(5),videosService.getTotalVideos(5));
                result.addNumber(Categories.getArtificial_categories().get(6),videosService.getTotalVideos(6));


                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

            }
        };

        new GetRequest("/videos/classify"){

            @Parameter(description = "Specify the category",defaultValue = "0")
            private int category;


            @Parameter(description = "Labeling window, indicates the number of days for the attributes calculation",defaultValue = "1")
            private int lbl_wnd;

            @Parameter(description = "Training window, indicates the number of days to train the classifier",defaultValue = "2")
            private int train_wnd;

            @Parameter(description = "Offset, indicates the number of days we don't measure stats",defaultValue = "0")
            private int offset;

            @Parameter(description = "Return a percentage of the total videos as popular",defaultValue = "0.025")
            private float percentage;

            private String experiment;
            private int useLimit;
            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                String category_name = Categories.getArtificial_categories().get(category);
                int per = (int)(percentage*1000);
                int perRest = per%10;
                experiment = "exp_"+category_name+"_"+(per/10)+"-"+perRest+"_"+train_wnd+"_"+(offset-train_wnd)+"_"+(lbl_wnd-offset);

                if(videosService.exists(experiment)){
                    return result.addString("msg","Experiment already exists...").build();
                }
                JsonArray popular = videosService.getPopularVideos(category,offset,lbl_wnd, useLimit);
                JsonArray viral = videosService.getViralVideos(category,offset,lbl_wnd,useLimit);
                JsonArray recent = videosService.getRecentVideos(category,2,useLimit);
                JsonArray random = videosService.getRandomVideos(category,useLimit);




                Map<String,List<Integer>> videos =new HashMap<>();
                popular.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(0);
                    videos.put(key,values);
                });

                viral.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(1);
                    videos.put(key,values);
                });
                recent.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(2);
                    videos.put(key,values);
                });
                random.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(3);
                    videos.put(key,values);
                });

                Groups groups = new Groups(true,offset,lbl_wnd,videosService.getProcessVideoDBService(),videos, experiment);

                Set<String> mostPopular = groups.getPopular().stream().map(v->v.getVideo_id()).collect(Collectors.toSet());
                Set<String> mostViral = groups.getViral().stream().map(v->v.getVideo_id()).collect(Collectors.toSet());

                JsonObject object = new JsonObject();
                object.addProperty("experiment_id",experiment);
                object.addProperty("category_name", Categories.getArtificial_categories().get(category));
                object.addProperty("number_of_videos", groups.getTotalVideos());
                object.addProperty("category",category);
                object.addProperty("percentage",percentage);
                object.addProperty("train_wnd","From day 1 until day " +train_wnd);
                object.addProperty("offset", (offset==train_wnd)?"No offset": "From day "+(train_wnd+1)+" until day "+offset);
                object.addProperty("lbl_wnd","From day "+(offset+1)+" until day " +lbl_wnd);
                object.add("high_level_characterization_plots",plotProducer.produceHighLevelPlots(groups));
                object.addProperty("videos_features_csv",videosService.produceCsv(groups,mostPopular,mostViral));


                //Classification
                String ytFeatures = "1111111111111111111111111111111";
                String twFeatures = "1111111111111111111111111111111111111111111";
                int split_days = 14;
                FeatureManager featureManager = new FeatureManager(plotProducer.getPath(),experiment, videosService.getClassifyTasks(), groups,train_wnd, offset, lbl_wnd, split_days, percentage, ytFeatures, twFeatures);
                videosService.getExecutorService().execute(featureManager);

                object.addProperty("classification","/classification/"+experiment);

                object.add("groups",groups.getInfo());

                result.setData(object);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0&&x<=6,"Category should be from 0 to 6");

                train_wnd = ParseParameters.parseIntegerQueryParam(request,result,"train_wnd",1,x->x>=1&&x<14,"train_wnd should in the range of the collected data");

                offset = ParseParameters.parseIntegerQueryParam(request,result,"offset",0, x->x+train_wnd<=14,"offset should be in the range of the collected data");

                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",3,x->x>1&&x+train_wnd+offset<=15,"lbl_wnd should be in the range of the collected data");

                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0&&x<1,"Percentage must be between 0 and 1");


                offset+=train_wnd;
                lbl_wnd+=offset;

                int totalVideos = videosService.getTotalVideos(category);

                useLimit = (int) (percentage * totalVideos);

            }
        };
        new GetRequest("/videos/groups"){

            @Parameter(description = "Specify the category",defaultValue = "0")
            private int category;

            @Parameter(description = "Labeling window, indicates the number of days for the attributes calculation",defaultValue = "1")
            private int lbl_wnd;

            @Parameter(description = "Training window, indicates the number of days to train the classifier",defaultValue = "2")
            private int train_wnd;

            @Parameter(description = "Offset, indicates the number of days we don't measure stats",defaultValue = "0")
            private int offset;

            @Parameter(description = "If set to 1, it returns the daily statistics",defaultValue = "1")
            private boolean daily;

            @Parameter(description = "Return a percentage of the total videos as popular",defaultValue = "0.025")
            private float percentage;

            private int useLimit;
            @Override
            public Object execute(Request request, Response response, JsonResult result) {


                JsonArray popular = videosService.getPopularVideos(category,0,lbl_wnd,useLimit);
                JsonArray viral = videosService.getViralVideos(category,0,lbl_wnd,useLimit);
                JsonArray recent = videosService.getRecentVideos(category,2,useLimit);
                JsonArray random = videosService.getRandomVideos(category,useLimit);


                Map<String,List<Integer>> videos =new HashMap<>();
                popular.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(0);
                    videos.put(key,values);
                });

                viral.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(1);
                    videos.put(key,values);
                });
                recent.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(2);
                    videos.put(key,values);
                });
                random.forEach(x-> {
                    String key = x.getAsJsonObject().get("video_id").getAsString();
                    List<Integer> values = videos.getOrDefault(key,new LinkedList<>());
                    values.add(3);
                    videos.put(key,values);
                });
                Groups groups = new Groups(daily,offset,lbl_wnd,videosService.getProcessVideoDBService(),videos, "");

                JsonObject object = new JsonObject();
                object.addProperty("category_name", Categories.getArtificial_categories().get(category));
                object.addProperty("category",category);
                object.addProperty("percentage",percentage);
                object.addProperty("lbl_wnd",lbl_wnd);
                object.add("groups",groups.toJson());

                result.setData(object);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0&&x<=6,"Category should be from 0 to 6");

                train_wnd = ParseParameters.parseIntegerQueryParam(request,result,"train_wnd",1,x->x>=1&&x<14,"train_wnd should in the range of the collected data");

                offset = ParseParameters.parseIntegerQueryParam(request,result,"offset",0, x->x+train_wnd<=14,"offset should be in the range of the collected data");

                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",3,x->x+train_wnd+offset<=15,"lbl_wnd should be in the range of the collected data");

                daily = ParseParameters.parseBooleanQueryParam(request,result,"daily",true,x->x==0 || x==1 ,"daily must be either 0 or 1");

                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0&&x<1,"Percentage must be between 0 and 1");

                offset+=train_wnd;
                lbl_wnd+=offset;

                useLimit = (int) (percentage * videosService.getTotalVideos(category));
            }
        };

        new GetRequest("/videos/popular"){

            @Parameter(description = "Specify the category",defaultValue = "0")
            private int category;

            @Parameter(description = "Labeling window, indicates the day to begin calculating the attributes",defaultValue = "1")
            private int lbl_wnd;

            @Parameter(description = "Return a percentage of the total videos as popular",defaultValue = "0")
            private float percentage;

            @Parameter(description = "Limit the number of videos to return",defaultValue = "10")
            private int limit;


            private int useLimit;
            @Override
            public Object execute(Request request, Response response, JsonResult result) {


                JsonArray videos = videosService.getPopularVideos(category,0,lbl_wnd,useLimit);
                result.addNumber("total_videos",videos.size());
                result.addElement("videos",videos);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0&&x<=6,"Category should be from 0 to 6");

                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",1,x->x>=1&&x<=15,"lbl_wnd should be from 1 to 15");

                limit = ParseParameters.parseIntegerQueryParam(request,result,"totalVideos",10,x->x>0,"totalVideos must be greater than 0");

                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0,x->x>0&&x<1,"Percentage must be between 0 and 1");

                if(percentage!=0){
                    useLimit = (int) (percentage * videosService.getTotalVideos(category));
                }else{
                    useLimit = limit;
                }
            }
        };

        new GetRequest("/videos/viral"){

            @Parameter(description = "Specify the category",defaultValue = "0")
            private int category;

            @Parameter(description = "Labeling window, indicates the day to begin calculating the attributes",defaultValue = "1")
            private int lbl_wnd;

            @Parameter(description = "Return a percentage of the total videos as viral",defaultValue = "0")
            private float percentage;

            @Parameter(description = "Limit the number of videos to return",defaultValue = "10")
            private int limit;


            private int useLimit;
            @Override
            public Object execute(Request request, Response response, JsonResult result) {


                JsonArray videos = videosService.getViralVideos(category,0,lbl_wnd,useLimit);
                result.addNumber("total_videos",videos.size());
                result.addElement("videos",videos);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0&&x<=6,"Category should be from 0 to 6");

                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",1,x->x>=0&&x<=15,"lbl_wnd should be from 0 to 15");

                limit = ParseParameters.parseIntegerQueryParam(request,result,"totalVideos",10,x->x>0,"totalVideos must be greater than 0");

                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0,x->x>0&&x<1,"Percentage must be between 0 and 1");

                if(percentage!=0){
                    useLimit = (int) (percentage * videosService.getTotalVideos(category));
                }else{
                    useLimit = limit;
                }
            }
        };

        new GetRequest("/videos/recent"){

            @Parameter(description = "Specify the category",defaultValue = "0")
            private int category;

            @Parameter(description = "Return a percentage of the total videos as recent",defaultValue = "0")
            private float percentage;

            @Parameter(description = "The number of days to look back",defaultValue = "2")
            private int days;

            @Parameter(description = "Limit the number of videos to return",defaultValue = "10")
            private int limit;


            private int useLimit;
            @Override
            public Object execute(Request request, Response response, JsonResult result) {


                JsonArray videos = videosService.getRecentVideos(category,days,useLimit);
                result.addNumber("total_videos",videos.size());
                result.addElement("videos",videos);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0&&x<=6,"Category should be from 0 to 6");

                limit = ParseParameters.parseIntegerQueryParam(request,result,"totalVideos",10,x->x>0,"totalVideos must be greater than 0");

                days = ParseParameters.parseIntegerQueryParam(request,result,"days",2,x->x>0,"days must be greater than 0");

                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0,x->x>0&&x<1,"Percentage must be between 0 and 1");

                if(percentage!=0){
                    useLimit = (int) (percentage * videosService.getTotalVideos(category));
                }else{
                    useLimit = limit;
                }
            }
        };

        new GetRequest("/videos/random"){

            @Parameter(description = "Specify the category",defaultValue = "0")
            private int category;

            @Parameter(description = "Return a percentage of the total videos",defaultValue = "0")
            private float percentage;

            @Parameter(description = "Limit the number of videos to return",defaultValue = "10")
            private int limit;


            private int useLimit;
            @Override
            public Object execute(Request request, Response response, JsonResult result) {


                JsonArray videos = videosService.getRandomVideos(category,useLimit);
                result.addNumber("total_videos",videos.size());
                result.addElement("videos",videos);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0&&x<=6,"Category should be from 0 to 6");

                limit = ParseParameters.parseIntegerQueryParam(request,result,"totalVideos",10,x->x>0,"totalVideos must be greater than 0");

                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0,x->x>0&&x<1,"Percentage must be between 0 and 1");

                if(percentage!=0){
                    useLimit = (int) (percentage * videosService.getTotalVideos(category));
                }else{
                    useLimit = limit;
                }
            }
        };

        new GetRequest("/videos/:id"){

            @Parameter(required = true,description = "The id of the video")
            private String video_id;

            @Parameter(description = "Specify the max number of comments to return")
            private int comments;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                if(comments!=-1){
                    JsonArray commentsArray = videosService.getComments(video_id,comments);
                    result.addString("video", video_id);
                    result.addElement("comments", commentsArray);
                    return result.build();
                }
                    JsonObject object = videosService.getVideo(video_id);
                    result.addElement("video", object);
                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {
                video_id = ParseParameters.parseStringParam(request,result,":id","", x->true,"");
                comments = ParseParameters.parseIntegerQueryParam(request,result,"comments",-1,x-> x>0 && x<=100,"Comments could be from 0 to 100");


            }
        };
    }
}
