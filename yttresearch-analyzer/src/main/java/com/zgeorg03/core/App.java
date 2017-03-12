package com.zgeorg03.core;

import com.mongodb.ServerAddress;
import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.controllers.IndexController;
import com.zgeorg03.controllers.PlotsController;
import com.zgeorg03.controllers.VideosController;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.rawvideos.FinishedVideosMonitor;
import com.zgeorg03.services.IndexService;
import com.zgeorg03.services.PlotsService;
import com.zgeorg03.services.VideosService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static spark.Spark.after;
import static spark.Spark.before;

import static spark.Spark.*;
/**
 * Created by zgeorg03 on 3/2/17.
 */
public class App {

    public static void main(String args[]) throws Exception {
        String workingPath="/tmp/thesis";
        String scripts="./scripts/";

        port(8000);

        PlotProducer plotProducer = new PlotProducer(workingPath);
        CsvProducer csvProducer = new CsvProducer(workingPath);

        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(scripts);

        DBConnection dbConnection = new DBConnection("yttresearch",new ServerAddress("localhost"));
        DBServices dbServices = new DBServices(dbConnection);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        FinishedVideosMonitor finishedVideosMonitor = new FinishedVideosMonitor(dbServices,0, sentimentAnalysis);
        executorService.execute(finishedVideosMonitor);


        //Services
        final IndexService indexService = new IndexService(dbServices, csvProducer);
        final VideosService videosService = new VideosService(dbServices);
        final PlotsService plotsService = new PlotsService(plotProducer, csvProducer);


        //Controllers
        new IndexController(indexService);
        new VideosController(videosService, plotProducer);
        new PlotsController(plotsService);



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
