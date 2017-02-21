package com.zgeorg03.core.controllers.helpers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 2/20/17.
 */
public class ParameterController {

    public static JsonObject getRequestParameters(Class<? extends GetRequest> clazz){
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        JsonArray arrayMd = new JsonArray();
        for(Field field : clazz.getDeclaredFields()){
            for(Annotation annotation : field.getDeclaredAnnotations()){
                if(!(annotation instanceof Parameter))
                    continue;

                StringBuilder sb = new StringBuilder();
                Parameter p = (Parameter) annotation;
                JsonObject param = new JsonObject();

                String name = field.getName();
                param.addProperty("name",name);
                sb.append("| "+ name );

                String type = field.getType().getSimpleName();
                param.addProperty("type",type);
                sb.append(" | "+ type );

                String required = (p.required())?"yes":"no";
                param.addProperty("required", required);
                sb.append(" | "+ required );

                String deafult = p.defaultValue();
                param.addProperty("default", deafult);
                sb.append(" | "+ deafult );

                if(p.description().isEmpty()) {
                    JsonArray desc = new JsonArray();
                    Arrays.stream(p.extendedDescription()).forEach(e->desc.add(e));
                    sb.append(" |" +Arrays.stream(p.extendedDescription()).collect(Collectors.joining(","))+" |");
                    param.add("description",desc);
                }else
                    param.addProperty("description",p.description());
                sb.append(" | "+ p.description()+" |" );

                array.add(param);
                arrayMd.add(sb.toString());
            }

        }
        object.add("params",array);
        object.add("markdown",arrayMd);
        return object;
    }

}
