package com.zgeorg03.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zgeorg03 on 9/9/16.
 */
public class YoutubeTool {

    private static final Pattern pattern = Pattern.compile("(https?://youtu\\.be)|(https?://www\\.youtube\\.com/watch\\?v=)");

    /**Extract Youtube ID */
    private static final Pattern pattern1 = Pattern.compile("\\?v=[a-zA-Z0-9_-]+");
    private static final Pattern pattern2 = Pattern.compile("youtu\\.be/([a-zA-Z0-9_-])+");

    public static boolean applyYoutubeFilter(String link){
        Matcher m = pattern.matcher(link);
        return m.find();
    }


    /**
     * Extract youtube id from the given link
     * @param text
     * @return
     */
    public static String extractID(String text){
        Matcher m = pattern1.matcher(text);
        if(m.find()){
            String res = m.group();
            String tok[] =  res.split("\\?v=");
            return  tok[1];
        }
        m = pattern2.matcher(text);
        if(m.find()){
            String res = m.group();
            String tok[] =  res.split("youtu\\.be/");
            return  tok[1];
        }
        return "ERROR";
    }

}
