package com.zgeorg03.core;

import com.mongodb.ServerAddress;
import com.zgeorg03.controllers.IndexController;
import com.zgeorg03.controllers.VideosController;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.services.IndexService;
import com.zgeorg03.services.VideosService;

import static spark.Spark.after;
import static spark.Spark.before;

import static spark.Spark.*;
/**
 * Created by zgeorg03 on 3/2/17.
 */
public class App {

    public static void main(String args[]){


        port(8000);

        DBConnection dbConnection = new DBConnection("yttresearch",new ServerAddress("localhost"));
        DBServices dbServices = new DBServices(dbConnection);

        //Services
        final IndexService indexService = new IndexService(dbServices);
        final VideosService videosService = new VideosService(dbServices);


        //Controllers
        new IndexController(indexService);
        new VideosController(videosService);



        CORSEnable();
    }
    private static void CORSEnable(){
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        after(((request, response) -> response.type("application/json")));
        get("*", (request, response) -> {
            JsonResult result = new JsonResult().addError("Your request is not valid");
            result.addString("url", "/");
            return result.build();
        });
        put("*", (request, response) -> {
            JsonResult result = new JsonResult().addError("Your request is not valid");
            result.addString("url", "/");
            return result.build();
        });
    }
}
