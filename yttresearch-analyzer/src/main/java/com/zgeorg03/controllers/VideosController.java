package com.zgeorg03.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zgeorg03.controllers.helpers.GetRequest;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.controllers.helpers.Parameter;
import com.zgeorg03.controllers.helpers.ParseParameters;
import com.zgeorg03.services.VideosService;
import spark.Request;
import spark.Response;

/**
 * Created by zgeorg03 on 3/3/17.
 */
public class VideosController {

    private final VideosService videosService;

    public VideosController(VideosService videosService) {
        this.videosService = videosService;

        new GetRequest("/videos"){

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                result.addNumber("total_videos",videosService.getTotalVideos());

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

            }
        };

        new GetRequest("/videos/:id"){

            @Parameter(required = true,description = "The id of the video")
            private String videoId;

            @Parameter(description = "Specify the max number of comments to return")
            private int comments;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                if(comments!=-1){
                    JsonArray commentsArray = videosService.getComments(videoId,comments);
                    result.addString("video", videoId);
                    result.addElement("comments", commentsArray);
                    return result.build();
                }
                    JsonObject object = videosService.getVideo(videoId);
                    result.addElement("video", object);
                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {
                videoId = ParseParameters.parseStringParam(request,result,":id","",x->true,"");
                comments = ParseParameters.parseIntegerQueryParam(request,result,"comments",-1,x-> x>0 && x<=100,"Comments could be from 0 to 100");


            }
        };
    }
}
