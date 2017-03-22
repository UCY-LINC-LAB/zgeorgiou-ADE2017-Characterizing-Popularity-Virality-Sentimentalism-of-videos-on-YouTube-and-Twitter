package com.zgeorg03.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgeorg03 on 3/5/17.
 */
public class Categories {
    /**
     * Link : https://gist.github.com/dgp/1b24bf2961521bd75d6c
     * @return
     */
    private static final Map<Integer,String> categories = new HashMap<>();
    private static final Map<Integer,String> artificial_categories = new HashMap<>();
    static {
        categories.put( 1 , "Film & Animation");
        categories.put( 2 , "Autos & Vehicles");
        categories.put(10 , "Music");
        categories.put(15 , "Pets & Animals");
        categories.put(17 , "Sports");
        categories.put(18 , "Short Movies");
        categories.put(19 , "Travel & Events");
        categories.put(20 , "Gaming");
        categories.put(21 , "Videoblogging");
        categories.put(22 , "People & Blogs");
        categories.put(23 , "Comedy");
        categories.put(24 , "Entertainment");
        categories.put(25 , "News & Politics");
        categories.put(26 , "Howto & Style");
        categories.put(27 , "Education");
        categories.put(28 , "Science & Technology");
        categories.put(29 , "Nonprofits & Activism");
        categories.put(30 , "Movies");
        categories.put(31 , "Anime/Animation");
        categories.put(32 , "Action/Adventure");
        categories.put(33 , "Classics");
        categories.put(34 , "Comedy");
        categories.put(35 , "Documentary");
        categories.put(36 , "Drama");
        categories.put(37 , "Family");
        categories.put(38 , "Foreign");
        categories.put(39 , "Horror");
        categories.put(40 , "Sci-Fi/Fantasy");
        categories.put(41 , "Thriller");
        categories.put(42 , "Shorts");
        categories.put(43 , "Shows");
        categories.put(44 , "Trailers");
        artificial_categories.put(0,"All");
        artificial_categories.put(1,"Music");
        artificial_categories.put(2,"Games");
        artificial_categories.put(3,"People_Blogs");
        artificial_categories.put(4,"Entertainment");
        artificial_categories.put(5,"News_Politics");
        artificial_categories.put(6,"Others");
    }

    public static Map<Integer, String> getCategories() {
        return categories;
    }

    public static Map<Integer, String> getArtificial_categories() {
        return artificial_categories;
    }

    /**
     * Get artificial category
     *
     * @param category
     * @return
     */
    public static int getArtificialCategory(int category){
        if(category == 10) // Music
            return 1;
        if(category == 20) // Games
            return 2;
        if(category == 22) // People & Blogs
            return 3;
        if(category == 1 || category  == 30 ||
                category == 23 || category == 24 || category >=30) // Comedy & Entertainment & Films
            return 4;
        if(category == 25) // News & Politics
            return 5;

        return 6;//Others
    }
}
