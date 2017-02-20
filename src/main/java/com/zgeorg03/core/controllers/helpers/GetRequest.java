package com.zgeorg03.core.controllers.helpers;

import com.zgeorg03.core.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public abstract class GetRequest {

    private static final Logger logger = LoggerFactory.getLogger(GetRequest.class);

    private final String url;

    @Parameter(description = "Request help")
    public int help;

    protected GetRequest(String url) {
        this.url = url;

        Spark.get(url, (req,res) -> request(req,res));
    }
    protected Object request(Request request,Response response){
        logger.info("Request from:" + request.ip());
        JsonResult result = new JsonResult();
        if(!tokenCheck(request))
            return result.addError("Token needed!").build();


        help = ParseParameters.parseIntegerQueryParam(request,result,"help", 0,x->x==0||x==1 ,"Should be 0 or 1");

        handleParams(request,response,result);

        if(result.hasError() || help==1)
            return result.addElement("parameters",ParameterController.getRequestParameters(this.getClass())).build();

        return execute(request,response,result);

    }


    /***
     * Authorized Access
     * //TODO For now it is not needed
     * @param request
     * @return
     */
    public boolean tokenCheck(Request request){
        String token = request.headers("token");
        return true;
    }
    public abstract Object  execute(Request request, Response response, JsonResult result);

    public abstract void handleParams(Request request, Response response, JsonResult result);

}
