package com.zgeorg03.controllers;

import com.zgeorg03.controllers.helpers.GetRequest;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.services.IndexService;
import spark.Request;
import spark.Response;

/**
 * Created by zgeorg03 on 3/2/17.
 */
public class IndexController {

    public IndexController(final IndexService indexService){

        new GetRequest("/"){

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                result.addString("database",indexService.getDatabaseName());
                result.addNumber("videos_finished_but_not_proccesed",indexService.getFinishedButNotProcessedVideosCount());
                result.addNumber("videos_finished_and_proccesed",indexService.getFinishedAndProcessedVideosCount());


                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

            }
        };
        /**
        new GetRequest("/csv"){

            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                result.addString("database",indexService.getDatabaseName());
                result.addNumber("videos_finished_and_proccesed",indexService.getFinishedAndProcessedVideosCount());
                long timestamp =System.currentTimeMillis();
                //TODO Remove in production
                timestamp=1;
                //result.addString("url", indexService.getCsv(timestamp));


                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {

            }
        };
         **/
    }
}
