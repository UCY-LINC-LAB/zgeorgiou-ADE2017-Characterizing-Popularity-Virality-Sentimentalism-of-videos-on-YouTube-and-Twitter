package com.zgeorg03.core;


import com.zgeorg03.core.controllers.IndexController;
import com.zgeorg03.core.controllers.VideosController;
import com.zgeorg03.core.services.VideosService;
import com.zgeorg03.core.utils.JsonResult;
import com.zgeorg03.core.utils.VideosLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String args[]) {
        int port = 8000; //Default Port
        String path = "./data";
        if (args.length >= 1) {
            port = Integer.parseInt(args[0]);
            if (args.length >= 2) {
                path = args[1];
            }
        }

        final ExecutorService executorService = Executors.newFixedThreadPool(4);
        VideosLoader videosLoader =null;
        try {
            videosLoader = new VideosLoader(path,500, 60);
            executorService.execute(videosLoader);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            System.exit(1);
        }

        /**Server stuff **/
        Spark.port(port);
        logger.info("Starting Server on port:" + port);

        //Services
        final VideosService videosService = new VideosService(videosLoader.getVideoRecords());

        //Controllers
        final IndexController controller = new IndexController();
        final VideosController videosController = new VideosController(videosService);




        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        after(((request, response) -> response.type("application/json")));
        get("*", (request, response) -> {
            JsonResult result = new JsonResult().addError("Your request is not valid");
            result.addString("url", "/");
            return result.build();
        });
    }



}
