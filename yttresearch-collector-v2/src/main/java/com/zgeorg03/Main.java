package com.zgeorg03;


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

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]){

        String config_file = "yttresearch-collector-v2/config.yaml";
        if(args.length==1)
            config_file = args[0];

        logger.info("Collector service starting with configuration file:"+config_file);

        //Load Config file
        Arguments arguments = Arguments.loadYAML(config_file);
        logger.info("Loaded: "+arguments);

        // Set service port
        int service_port = arguments.getService_port();
        port(service_port);

        // Set remote connection to mongo and database
        String remoteHost = arguments.getRemote_host();
        String database = arguments.getDatabase();
        final DBConnection dbConnection = new DBConnection(database,new ServerAddress(remoteHost));
        final DBServices dbServices = new DBServices(dbConnection, arguments);


        //Core Jobs
        ExecutorService executorService = Executors.newFixedThreadPool(4);


        //Status-Tweets Monitor
        StatusMonitor statusMonitor = new StatusMonitor(arguments, dbServices);
        executorService.execute(statusMonitor);

        //Dynamic Info Monitor
        DynamicInfoMonitor dynamicInfoMonitor = new DynamicInfoMonitor(dbServices, statusMonitor, arguments);
        executorService.execute(dynamicInfoMonitor);

        //Comments Monitor
        CommentsMonitor commentsMonitor = new CommentsMonitor(dbServices, arguments.getMax_comments(), arguments);
        executorService.execute(commentsMonitor);

        //Incomplete Videos Monitor
        IncompleteVideosMonitor incompleteVideosMonitor = new IncompleteVideosMonitor(dbServices);
        executorService.execute(incompleteVideosMonitor);

        //Services
        final IndexService indexService = new IndexService(dbServices,statusMonitor);


        //Controllers
        new IndexController(indexService);
        initRemoteAccess();
        Runtime.getRuntime().addShutdownHook(new ShutDownHandler(statusMonitor));
    }
    public static void initRemoteAccess(){
        logger.info("Remote access initialized...");
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
