package com.zgeorg03.core;

import com.mongodb.ServerAddress;
import com.zgeorg03.controllers.IndexController;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.services.IndexService;
import com.zgeorg03.utils.ShutDownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static spark.Spark.*;

/**
 * Main Class, Collector Entry Point
 * Created by zgeorg03 on 2/24/17.
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String args[]){


        port(8001);

        String db="yttresearch1";
        if(args.length==1)
            db = args[0];


        DBConnection dbConnection = new DBConnection(db,new ServerAddress("localhost"));
        DBServices dbServices = new DBServices(dbConnection);

        //Core Jobs
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        //Status-Tweets Monitor
        StatusMonitor statusMonitor = new StatusMonitor(dbServices);
        executorService.execute(statusMonitor);

        //Dynamic Info Monitor
        DynamicInfoMonitor dynamicInfoMonitor = new DynamicInfoMonitor(dbServices, statusMonitor);
        executorService.execute(dynamicInfoMonitor);

        //Comments Monitor
        CommentsMonitor commentsMonitor = new CommentsMonitor(dbServices, 100);
        executorService.execute(commentsMonitor);

        //Incomplete Videos Monitor
        IncompleteVideosMonitor incompleteVideosMonitor = new IncompleteVideosMonitor(dbServices);
        executorService.execute(incompleteVideosMonitor);


        //Services
        final IndexService indexService = new IndexService(dbServices,statusMonitor);


        //Controllers
        final IndexController indexController = new IndexController(indexService);

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

        Runtime.getRuntime().addShutdownHook(new ShutDownHandler(statusMonitor));
    }
}
