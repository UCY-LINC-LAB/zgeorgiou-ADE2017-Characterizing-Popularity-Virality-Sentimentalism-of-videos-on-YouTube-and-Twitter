package com.zgeorg03.core;

import com.mongodb.ServerAddress;
import com.zgeorg03.analysis.SentimentAnalysis;
import com.zgeorg03.classification.ClassificationManager;
import com.zgeorg03.classification.tasks.ClassifyTasks;
import com.zgeorg03.controllers.ClassificationController;
import com.zgeorg03.controllers.IndexController;
import com.zgeorg03.controllers.PlotsController;
import com.zgeorg03.controllers.VideosController;
import com.zgeorg03.controllers.helpers.JsonResult;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import com.zgeorg03.rawvideos.FinishedVideosMonitor;
import com.zgeorg03.services.ClassificationService;
import com.zgeorg03.services.IndexService;
import com.zgeorg03.services.PlotsService;
import com.zgeorg03.services.VideosService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static spark.Spark.after;
import static spark.Spark.before;

import static spark.Spark.*;
/**
 * Entry Point
 * Created by zgeorg03 on 3/2/17.
 */
public class App {

    /**
     * Entry Point
     * @param args Command line arguments
     * @throws Exception Exception
     */
    public static void main(String args[]) throws Exception {
        String workingPath="/tmp/thesis";
        String scripts="./scripts/";
        scripts="./yttresearch-analyzer/scripts/";
        String db="yttresearch1";


        if(args.length==1)
            db= args[0];
        port(8000);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        PlotProducer plotProducer = new PlotProducer(workingPath);
        CsvProducer csvProducer = new CsvProducer(workingPath);
        ClassifyTasks classifyTasks = new ClassifyTasks(scripts);
        executorService.submit(classifyTasks);

        ClassificationManager classificationManager = new ClassificationManager(workingPath);

        SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(scripts);

        DBConnection dbConnection = new DBConnection(db,new ServerAddress("10.16.3.12"));
        DBServices dbServices = new DBServices(dbConnection);

        FinishedVideosMonitor finishedVideosMonitor = new FinishedVideosMonitor(dbServices,500, sentimentAnalysis);
        executorService.execute(finishedVideosMonitor);


        //Services
        final ClassificationService classificationService = new ClassificationService(dbServices, classificationManager);
        final IndexService indexService = new IndexService(dbServices);
        final VideosService videosService = new VideosService(dbServices, csvProducer, executorService, classifyTasks, classificationManager);
        final PlotsService plotsService = new PlotsService(plotProducer, csvProducer);


        //Controllers
        new IndexController(indexService);
        new VideosController(videosService, plotProducer);
        new PlotsController(plotsService);
        new ClassificationController(classificationService);


        new Thread(classificationManager).start();

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
