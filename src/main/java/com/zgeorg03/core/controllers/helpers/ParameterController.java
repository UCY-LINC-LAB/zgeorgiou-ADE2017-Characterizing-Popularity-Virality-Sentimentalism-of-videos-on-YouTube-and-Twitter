package com.zgeorg03.core.controllers.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public class ParameterController {

    public static JsonArray getRequestParameters(Class<? extends GetRequest> clazz){
        JsonArray array = new JsonArray();
        for(Field field : clazz.getDeclaredFields()){
            for(Annotation annotation : field.getDeclaredAnnotations()){
                if(!(annotation instanceof Parameter))
                    continue;

                Parameter p = (Parameter) annotation;
                JsonObject param = new JsonObject();
                param.addProperty("name",field.getName());
                param.addProperty("type",field.getType().getName());
                param.addProperty("required",p.required());
                if(p.description().isEmpty()) {
                    JsonArray desc = new JsonArray();
                    Arrays.stream(p.extendedDescription()).forEach(e->desc.add(e));
                    param.add("description",desc);
                }else
                    param.addProperty("description",p.description());

                array.add(param);
            }

        }
        return array;
    }

}
