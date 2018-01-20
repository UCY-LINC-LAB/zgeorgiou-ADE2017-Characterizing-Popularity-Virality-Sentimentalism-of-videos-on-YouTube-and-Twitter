package com.zgeorg03.controllers;

import com.zgeorg03.controllers.helpers.*;
import com.zgeorg03.services.IndexService;
import spark.Request;
import spark.Response;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class IndexController {


    public IndexController(final IndexService service){

        new GetRequest("/") {

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                result.addString("msg","Welcome to collection service");

                result.setData(service.getStats());
                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

            }
        };

        new PutRequest("/twitterApp") {

            @Parameter(description = "Name of the Twitter Application" ,required = true)
            String name ;

            @Parameter(description = "Consumer key",required = true)
            String consumer_key ;

            @Parameter(description = "Consumer secret" ,required = true)
            String consumer_secret ;

            @Parameter(description = "Token", required = true)
            String token ;

            @Parameter(description = "Token secret", required = true)
            String token_secret ;

            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                result.addString("msg","Welcome to collection service");


                result.setData(service.createApp(name,consumer_key,consumer_secret,token,token_secret));

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                name = ParseParameters.parseStringQueryParam(request,result,"name","", x->true,"");
                consumer_key = ParseParameters.parseStringQueryParam(request,result,"consumer_key","",x->true,"");
                consumer_secret = ParseParameters.parseStringQueryParam(request,result,"consumer_secret","",x->true,"");
                token = ParseParameters.parseStringQueryParam(request,result,"token","",x->true,"");
                token_secret = ParseParameters.parseStringQueryParam(request,result,"token_secret","",x->true,"");

                if(name.isEmpty())
                    result.addError("Name required");
                if(consumer_key.isEmpty())
                    result.addError("Consumer key required");
                if(consumer_secret.isEmpty())
                    result.addError("Consumer secret required");
                if(token.isEmpty())
                    result.addError("Token  required");
                if(token_secret.isEmpty())
                    result.addError("Token secret required");

            }
        };

        new PutRequest("/youtubeKey") {


            @Parameter(description = "API key",required = true)
            String api_key ;


            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                result.setData(service.createYouTubeApp(api_key));

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                api_key = ParseParameters.parseStringQueryParam(request,result,"api_key","",x->true,"");

                if(api_key.isEmpty())
                    result.addError("API key required");

            }
        };

        new PutRequest("/") {


            @Parameter(description = "Max videos being monitored",required = true)
            int max_videos ;


            @Override
            public Object execute(Request request, Response response, JsonResult result) {
                result.setData(service.setMaxVideosBeingMonitored(max_videos));
                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

                max_videos = ParseParameters.parseIntegerQueryParam(request,result,"max_videos",-1,x->x>0,"It must be a positive number");

                if(max_videos==-1)
                    result.addError("max_videos is required");

            }
        };
    }
}
