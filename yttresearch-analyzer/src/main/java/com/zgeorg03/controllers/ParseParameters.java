package com.zgeorg03.controllers;

import spark.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by zgeorg03 on 12/5/16.
 */
public class ParseParameters {

    public static Map<String,Boolean> viewParameters(final Request request, final JsonResult result){
        Map<String,Boolean> view = new HashMap<>();
        for(String param : request.queryParams()){
            if(!param.startsWith("v_"))
                continue;
            String v = param.substring(2);
            String valParam = request.queryParams(param);
            int val=1;
            try {
                val = Integer.parseInt(valParam);
                if(val!=0 && val!=1)
                    result.addError("View parameter: " + v +" must be 0 or 1");
                view.put(v,val==1);
            }catch (NumberFormatException e){ result.addError("View parameter: " + v +" must be 0 or 1"); }

        }
        return view;
    }

    /**
     * Parse an integer parameter
     * @param request
     * @param result
     * @param id
     * @param defaultValue
     * @param validation
     * @param errorMsg
     * @return
     */
    public static int parseIntegerParam(final Request request, final JsonResult result, String id, int defaultValue, Predicate<Integer> validation, String errorMsg){
        int x=defaultValue;
        String param = request.params(id);
        if(param==null)
            return x;
        try {
            x = Integer.parseInt(param);
            if(!validation.test(x))
                result.addError(errorMsg);
        }catch (NumberFormatException e){ result.addError(id + " parameter should be an integer"); }
        return x;
    }

    /**
     *
     * @param request
     * @param result
     * @param id
     * @param defaultValue
     * @param validation
     * @param errorMsg
     * @return
     */
    public static String parseStringParam(final Request request, final JsonResult result, String id, String defaultValue, Predicate<String> validation, String errorMsg){
        String x=defaultValue;
        String param = request.params(id);
        if(param==null)
            return x;
        x=param;
        if(!validation.test(x))
            result.addError(errorMsg);
        return x;
    }
    /**
     *
     * @param request
     * @param result
     * @param id
     * @param defaultValue
     * @param validation
     * @param errorMsg
     * @return
     */
    public static String parseStringQueryParam(final Request request, final JsonResult result, String id, String defaultValue, Predicate<String> validation, String errorMsg){
        String x=defaultValue;
        String param = request.queryParams(id);
        if(param==null)
            return x;
        x=param;
        if(!validation.test(x))
            result.addError(errorMsg);
        return x;
    }
    /**
     * Parse an integer query parameter
     * @param request
     * @param result
     * @param id
     * @param defaultValue
     * @param validation
     * @param errorMsg
     * @return
     */
    public static int parseIntegerQueryParam(final Request request, final JsonResult result, String id, int defaultValue, Predicate<Integer> validation, String errorMsg){
       int x=defaultValue;
        String param = request.queryParams(id);
        if(param==null)
            return x;
        try {
            x = Integer.parseInt(param);
            if(!validation.test(x))
                result.addError(errorMsg);
        }catch (NumberFormatException e){ result.addError(id + " parameter should be an integer"); }
        return x;
    }

    /**
     * Parse a long query parameter
     * @param request
     * @param result
     * @param id
     * @param defaultValue
     * @param validation
     * @param errorMsg
     * @return
     */
    public static long parseLongQueryParam(final Request request, final JsonResult result, String id, long defaultValue, Predicate<Long> validation, String errorMsg){
        long x=defaultValue;
        String param = request.queryParams(id);
        if(param==null)
            return x;
        try {
            x = Long.parseLong(param);
            if(!validation.test(x))
                result.addError(errorMsg);
        }catch (NumberFormatException e){ result.addError(id + " parameter should be an integer64"); }
        return x;
    }
    /**
     * Parse an boolean query parameter
     * @param request
     * @param result
     * @param id
     * @param defaultValue
     * @param validation
     * @param errorMsg
     * @return
     */
    public static boolean parseBooleanQueryParam(final Request request, final JsonResult result, String id, boolean defaultValue, Predicate<Integer> validation, String errorMsg){
        boolean x=defaultValue;
        String param = request.queryParams(id);
        if(param==null)
            return x;
        try {
            int t = Integer.parseInt(param);
            if(!validation.test(t))
                result.addError(errorMsg);
            x = t==1;
        }catch (NumberFormatException e){ result.addError(id + " parameter should be 0 or 1"); }
        return x;
    }
    /**
     * Parse an integer query parameter
     * @param request
     * @param result
     * @param id
     * @param defaultValue
     * @param validation
     * @param errorMsg
     * @return
     */
    public static float parseFloatQueryParam(final Request request, final JsonResult result, String id, float defaultValue, Predicate<Float> validation, String errorMsg){
        float x=defaultValue;
        String param = request.queryParams(id);
        if(param==null)
            return x;
        try {
            x = Float.parseFloat(param);
            if(!validation.test(x))
                result.addError(errorMsg);
        }catch (NumberFormatException e){ result.addError(id + " parameter should be a float"); }
        return x;
    }

    /***
     * Authorized Access
     * @param request
     * @return
     */
    public static boolean tokenCheck(Request request){
        String token = request.headers("token");
        if(token==null || !token.equals("yttresearch2016"))
            return true;
        return true;
    }
}
