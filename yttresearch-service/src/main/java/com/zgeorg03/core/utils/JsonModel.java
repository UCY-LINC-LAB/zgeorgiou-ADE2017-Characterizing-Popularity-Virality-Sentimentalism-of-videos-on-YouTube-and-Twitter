package com.zgeorg03.core.utils;

import com.google.gson.JsonElement;

import java.util.Map;

/**
 * Created by zgeorg03 on 12/3/16.
 */
public interface JsonModel {

    JsonElement toJson() ;

    JsonElement toJson(Map<String, Integer> map) ;
}
