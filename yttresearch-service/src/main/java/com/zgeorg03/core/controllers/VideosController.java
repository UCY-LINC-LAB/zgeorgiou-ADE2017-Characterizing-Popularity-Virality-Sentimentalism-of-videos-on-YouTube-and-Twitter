package com.zgeorg03.core.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.core.controllers.helpers.GetRequest;
import com.zgeorg03.core.controllers.helpers.Parameter;
import com.zgeorg03.core.controllers.helpers.ParseParameters;
import com.zgeorg03.core.services.VideosService;
import com.zgeorg03.core.utils.JsonResult;
import spark.Request;
import spark.Response;

import java.util.Random;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public class VideosController {

    private final VideosService videosService;

    public VideosController(VideosService videosService){
        this.videosService = videosService;

        new GetRequest("/videos"){

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonObject object;


                JsonArray array = videosService.getCategories();
                result.addNumber("total_videos",videosService.getTotalVideos());
                result.addNumber("min_days",videosService.getVideoRecords().getMinDays(0));
                result.addElement("categories",array);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

            }
        };

        new GetRequest("/videos/popular"){

            @Parameter(description = "Label window indicates the starting day for measuring the attributes. Default is 3", defaultValue = "3")
            public int lbl_wnd;

            @Parameter(description = "Category of the videos.",defaultValue = "0")
            public int category;

            @Parameter(description = "Number of videos to return",defaultValue = "10")
            public int limit;

            @Parameter(description = "Return the percentage of the total videos. Limit parameter overrides percentage if specified",defaultValue = "0.025")
            public float percentage;

            @Parameter(description = "Returns statistics about the videos",defaultValue = "1")
            public int stats;
            private boolean statsEnabled;

            private boolean useLimit;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonObject object;
                if(useLimit) {
                    object = videosService.getMostPopularInLabelWindow(statsEnabled,lbl_wnd, limit, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    object = videosService.getMostPopularInLabelWindow(statsEnabled,lbl_wnd, limit, category);
                }
                result.setData(object);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0 && x<=6,"Category must be a number between 0 and 5");
                int minDays =  videosService.getVideoRecords().getMinDays(category);
                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",3,x->x>0 && x<=minDays,"Labeling wnd must be in the range of collected data");
                limit = ParseParameters.parseIntegerQueryParam(request,result,"limit",10,x->x>0,"Limit must be positive");
                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0 && x<1,"Percentage must be in the range of 0 and 1");

                stats = ParseParameters.parseIntegerQueryParam(request,result,"stats",1,x->x== 0 || x==1,"Must be 0 or 1");
                statsEnabled = stats==1;

                if(request.queryParams("limit")!=null)
                    useLimit = true;
                else if(request.queryParams("percentage")!=null)
                    useLimit = false;
                else
                    useLimit = true;


            }
        };

        new GetRequest("/videos/viral"){

            @Parameter(description = "Label window indicates the starting day for measuring the attributes",defaultValue = "3")
            public int lbl_wnd;

            @Parameter(description = "Category of the videos",defaultValue = "0")
            public int category;

            @Parameter(description = "Number of videos to return",defaultValue = "10")
            public int limit;

            @Parameter(description = "Return the percentage of the total videos. Limit parameter overrides percentage if specified",defaultValue = "0.025")
            public float percentage;

            @Parameter(description = "Returns statistics about the videos",defaultValue = "1")
            public int stats;
            private boolean statsEnabled;

            private boolean useLimit;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonObject object;
                if(useLimit) {
                    object = videosService.getMostViralInLabelWindow(statsEnabled, lbl_wnd, limit, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    object = videosService.getMostViralInLabelWindow(statsEnabled, lbl_wnd, limit, category);
                }
                result.setData(object);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0 && x<=6,"Category must be a number between 0 and 5");
                int minDays =  videosService.getVideoRecords().getMinDays(category);
                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",3,x->x>0 && x<=minDays,"Labeling wnd must be in the range of collected data");
                limit = ParseParameters.parseIntegerQueryParam(request,result,"limit",10,x->x>0,"Limit must be positive");
                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0 && x<1,"Percentage must be in the range of 0 and 1");

                stats = ParseParameters.parseIntegerQueryParam(request,result,"stats",1,x->x== 0 || x==1,"Must be 0 or 1");
                statsEnabled = stats==1;

                if(request.queryParams("limit")!=null)
                    useLimit = true;
                else if(request.queryParams("percentage")!=null)
                    useLimit = false;
                else
                    useLimit = true;


            }
        };

        new GetRequest("/videos/recent"){

            @Parameter(description = "Label window indicates the starting day for measuring the attributes",defaultValue = "3")
            public int lbl_wnd;

            @Parameter(description = "Category of the videos.",defaultValue = "0")
            public int category;

            @Parameter(description = "Number of videos to return",defaultValue = "10")
            public int limit;

            @Parameter(description = "Random seed",defaultValue = "1")
            public int seed;

            @Parameter(description = "Return the percentage of the total videos. Limit parameter overrides percentage if specified",defaultValue = "0.025")
            public float percentage;

            private boolean useLimit;

            @Parameter(description = "Returns statistics about the videos",defaultValue = "1")
            public int stats;
            private boolean statsEnabled;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonObject object;
                final Random random;

                if(seed==-1)
                    random = new Random(System.currentTimeMillis());
                else
                    random = new Random(seed);

                if(useLimit) {
                    object = videosService.getRecentVideos(statsEnabled,lbl_wnd,limit,2, random, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    object = videosService.getRecentVideos(statsEnabled,lbl_wnd,limit,2, random, category);
                }

                result.setData(object);
                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0 && x<=6,"Category must be a number between 0 and 5");
                int minDays =  videosService.getVideoRecords().getMinDays(category);
                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",3,x->x>0 && x<=minDays,"Labeling wnd must be in the range of collected data");
                seed = ParseParameters.parseIntegerQueryParam(request,result,"seed",1,x->x>=-1 ,"Seed must be positive");
                limit = ParseParameters.parseIntegerQueryParam(request,result,"limit",10,x->x>0,"Limit must be positive");
                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0 && x<1,"Percentage must be in the range of 0 and 1");

                stats = ParseParameters.parseIntegerQueryParam(request,result,"stats",1,x->x== 0 || x==1,"Must be 0 or 1");
                statsEnabled = stats==1;

                if(request.queryParams("limit")!=null)
                    useLimit = true;
                else if(request.queryParams("percentage")!=null)
                    useLimit = false;
                else
                    useLimit = true;


            }
        };

        new GetRequest("/videos/random"){

            @Parameter(description = "Label window indicates the starting day for measuring the attributes",defaultValue = "3")
            public int lbl_wnd;

            @Parameter(description = "Category of the videos.",defaultValue = "0")
            public int category;

            @Parameter(description = "Number of videos to return",defaultValue = "10")
            public int limit;

            @Parameter(description = "Random seed",defaultValue = "1")
            public int seed;

            @Parameter(description = "Return the percentage of the total videos. Limit parameter overrides percentage if specified",defaultValue = "0.025")
            public float percentage;

            private boolean useLimit;

            @Parameter(description = "Returns statistics about the videos",defaultValue = "1")
            public int stats;
            private boolean statsEnabled;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonObject object;
                final Random random;

                if(seed==-1)
                    random = new Random(System.currentTimeMillis());
                else
                    random = new Random(seed);

                if(useLimit) {
                    object = videosService.getRandomVideos(statsEnabled,lbl_wnd,limit,2, random, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    object = videosService.getRandomVideos(statsEnabled,lbl_wnd,limit,2, random, category);
                }

                result.setData(object);
                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0 && x<=6,"Category must be a number between 0 and 5");
                int minDays =  videosService.getVideoRecords().getMinDays(category);
                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",3,x->x>0 && x<=minDays,"Labeling wnd must be in the range of collected data");
                seed = ParseParameters.parseIntegerQueryParam(request,result,"seed",1,x->x>=-1 ,"Seed must be positive");
                limit = ParseParameters.parseIntegerQueryParam(request,result,"limit",10,x->x>0,"Limit must be positive");
                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0 && x<1,"Percentage must be in the range of 0 and 1");

                stats = ParseParameters.parseIntegerQueryParam(request,result,"stats",1,x->x== 0 || x==1,"Must be 0 or 1");
                statsEnabled = stats==1;

                if(request.queryParams("limit")!=null)
                    useLimit = true;
                else if(request.queryParams("percentage")!=null)
                    useLimit = false;
                else
                    useLimit = true;


            }
        };
        new GetRequest("/videos/:id"){

            @Parameter(description = "Video Id",required = true)
            public String id;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                JsonObject video = videosService.getVideo(id);
                if(video.get("error")!=null)
                    return result.addError(video.get("error").getAsString()).build();

                result.addString("video_id",id);
                result.addElement("video",video.get("video"));

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                id = ParseParameters.parseStringParam(request,result,":id","",x->true,"");


            }
        };
    }
}
