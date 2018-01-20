package com.zgeorg03.controllers;

import com.zgeorg03.services.ClassificationService;
import spark.Request;
import spark.Response;

/**
 * Created by zgeorg03 on 3/17/17.
 */
public class ClassificationController   {

    public ClassificationController(ClassificationService service){

        new GetRequest("/classification/:id"){

            @Parameter(description = "Id", required = true)
            String id;
            @Override
            public Object execute(Request request, Response response, JsonResult result) {

                result.setData(service.getClassification(id));

                return result.build();
            }

            @Override
            public void handleParams(Request request, Response response, JsonResult result) {
                id = ParseParameters.parseStringParam(request, result, ":id", "", x -> true, "Error with experiment");

            }
        };
    }
}
