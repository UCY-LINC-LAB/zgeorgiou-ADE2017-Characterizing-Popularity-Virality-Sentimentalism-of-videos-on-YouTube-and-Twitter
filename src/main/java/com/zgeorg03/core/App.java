package com.zgeorg03.core;


import com.zgeorg03.core.controllers.IndexController;
import com.zgeorg03.core.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String args[]){
        int argsLength = args.length;
        int port=8000; //Default Port
        if(args.length==1)
            port = Integer.parseInt(args[0]);
        Spark.port(port);
        logger.info("Starting Server on port:"+port);

        IndexController controller = new IndexController();

        before((request,response)-> response.header("Access-Control-Allow-Origin", "*"));
        after(((request, response) -> response.type("application/json")));
        get("*",(request, response) -> {
            JsonResult result = new JsonResult().addError("Your request is not valid");
            result.addString("url","/");
            return result.build();
        });
    }


}
