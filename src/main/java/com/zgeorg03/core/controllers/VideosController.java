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


        new GetRequest("/videos/popular"){

            @Parameter(description = "Label window indicates the starting period for measuring the attributes. Default is 3")
            public int lbl_wnd;

            @Parameter(description = "Category of the videos. Default is 0, which is all")
            public int category;

            @Parameter(description = "Limit. Default is 10")
            public int limit;

            @Parameter(description = "Percentage of videos to return. Limit parameter overrides percentage if specified")
            public float percentage;

            private boolean useLimit;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonObject object;
                final JsonArray videos;
                if(useLimit) {
                    object = videosService.getMostPopularInLabelWindow(lbl_wnd, limit, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    object = videosService.getMostPopularInLabelWindow(lbl_wnd, limit, category);
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

                if(request.queryParams("limit")!=null)
                    useLimit = true;
                else if(request.queryParams("percentage")!=null)
                    useLimit = false;
                else
                    useLimit = true;


            }
        };

        new GetRequest("/videos/viral"){

            @Parameter(description = "Label window indicates the starting for measuring the attributes. Default is 3")
            public int lbl_wnd;

            @Parameter(description = "Category of the videos. Default is 0, which is all")
            public int category;

            @Parameter(description = "Limit. Default is 10")
            public int limit;

            @Parameter(description = "Percentage of videos to return. Limit parameter overrides percentage if specified")
            public float percentage;

            private boolean useLimit;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonArray videos;
                if(useLimit) {
                    videos = videosService.getMostViralInLabelWindow(lbl_wnd, limit, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    videos = videosService.getMostViralInLabelWindow(lbl_wnd, limit, category);
                }
                result.addNumber("total_videos",videos.size());
                result.addElement("videos",videos);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0 && x<=6,"Category must be a number between 0 and 5");
                int minDays =  videosService.getVideoRecords().getMinDays(category);
                lbl_wnd = ParseParameters.parseIntegerQueryParam(request,result,"lbl_wnd",3,x->x>0 && x<=minDays,"Labeling wnd must be in the range of collected data");
                limit = ParseParameters.parseIntegerQueryParam(request,result,"limit",10,x->x>0,"Limit must be positive");
                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0 && x<1,"Percentage must be in the range of 0 and 1");

                if(request.queryParams("limit")!=null)
                    useLimit = true;
                else if(request.queryParams("percentage")!=null)
                    useLimit = false;
                else
                    useLimit = true;


            }
        };

        new GetRequest("/videos/recent"){

            @Parameter(description = "Category of the videos. Default is 0, which is all")
            public int category;

            @Parameter(description = "Limit. Default is 10")
            public int limit;

            @Parameter(description = "Random seed. Default is 1")
            public int seed;

            @Parameter(description = "Percentage of videos to return. Limit parameter overrides percentage if specified")
            public float percentage;

            private boolean useLimit;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonArray videos;
                final Random random;

                if(seed==-1)
                    random = new Random(System.currentTimeMillis());
                else
                    random = new Random(seed);

                if(useLimit) {
                    videos = videosService.getRecentVideos(limit,2, random, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    videos = videosService.getRecentVideos(limit,2, random, category);
                }
                result.addNumber("total_videos",videos.size());
                result.addElement("videos",videos);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0 && x<=6,"Category must be a number between 0 and 5");
                int minDays =  videosService.getVideoRecords().getMinDays(category);
                seed = ParseParameters.parseIntegerQueryParam(request,result,"seed",1,x->x>=-1 ,"Seed must be positive");
                limit = ParseParameters.parseIntegerQueryParam(request,result,"limit",10,x->x>0,"Limit must be positive");
                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0 && x<1,"Percentage must be in the range of 0 and 1");


                if(request.queryParams("limit")!=null)
                    useLimit = true;
                else if(request.queryParams("percentage")!=null)
                    useLimit = false;
                else
                    useLimit = true;


            }
        };

        new GetRequest("/videos/random"){

            @Parameter(description = "Category of the videos. Default is 0, which is all")
            public int category;

            @Parameter(description = "Limit. Default is 10")
            public int limit;

            @Parameter(description = "Random seed. Default is 1")
            public int seed;

            @Parameter(description = "Percentage of videos to return. Limit parameter overrides percentage if specified")
            public float percentage;

            private boolean useLimit;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                final JsonArray videos;
                final Random random;

                if(seed==-1)
                    random = new Random(System.currentTimeMillis());
                else
                    random = new Random(seed);

                if(useLimit) {
                    videos = videosService.getRandomVideos(limit, random, category);
                }else{
                    int limit = (int) ((videosService.getTotalVideosFromCategory(category)  * percentage));
                    videos = videosService.getRandomVideos(limit, random, category);
                }
                result.addNumber("total_videos",videos.size());
                result.addElement("videos",videos);

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                category = ParseParameters.parseIntegerQueryParam(request,result,"category",0,x->x>=0 && x<=6,"Category must be a number between 0 and 5");
                int minDays =  videosService.getVideoRecords().getMinDays(category);
                seed = ParseParameters.parseIntegerQueryParam(request,result,"seed",1,x->x>=-1 ,"Seed must be positive");
                limit = ParseParameters.parseIntegerQueryParam(request,result,"limit",10,x->x>0,"Limit must be positive");
                percentage = ParseParameters.parseFloatQueryParam(request,result,"percentage",0.025f,x->x>0 && x<1,"Percentage must be in the range of 0 and 1");


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
