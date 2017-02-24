package com.zgeorg03.core;

import com.google.api.services.youtube.YouTube;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by zgeorg03 on 2/24/17.
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String args[]){

        logger.info("Starting...");

        VideoStreamListener videoStreamListener = new VideoStreamListener();
        TwitterStream stream = new TwitterStreamFactory().getInstance();
        stream.setOAuthConsumer("bQWU9pLmD0ocir5MbpDBsRJig", "FiJDkms8cZx82zeSuIwUjlMGcwAZjWlo2Rm3NhuL4c8XZYj4Ye");
        stream.setOAuthAccessToken(new AccessToken("720596767-rdtRwpiDaeG12g5YWNfdqdYFrZD9chzBewLyVCrh","Pmh8QCdlhCJV0AHcCihup60cWiNqlZRzz5vdQvJOkNj1o"));
        stream.addListener(videoStreamListener);
        stream.filter("youtube");

    }
}
