import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.ServerAddress;
import com.zgeorg03.core.YouTubeRequests;
import com.zgeorg03.database.DBConnection;
import com.zgeorg03.database.DBServices;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgeorg03 on 2/25/17.
 */
public class DatabaseTest {

    private final DBServices services;

    public DatabaseTest() {
        DBConnection connection = new DBConnection("yttresearch",new ServerAddress("localhost"));
        services = new DBServices(connection);
    }

    @Test
    public void createAVideo() throws InterruptedException {
        /**
        String video_id = "testId";
        if(services.checkVideoExistenceOnly(video_id))
            services.deleteVideo(video_id);
        JsonArray topics = new JsonArray();
        topics.add("test1");
        topics.add("test2");
        JsonObject video_data = new JsonObject();
        video_data.addProperty("title","Test");
        video_data.addProperty("channel_id","ChannelId");
        video_data.addProperty("description","desc");
        video_data.addProperty("category_id",10);
        video_data.addProperty("duration",22013);
        video_data.addProperty("published_at",System.currentTimeMillis());
        video_data.add("topics",topics);
        System.out.println(services.addNewVideo(video_id,video_data));
        Thread.sleep(100);

        JsonObject dynamicData = new JsonObject();
        dynamicData.addProperty("timestamp",System.currentTimeMillis());
        dynamicData.addProperty("view_count",System.currentTimeMillis());
        dynamicData.addProperty("like_count",System.currentTimeMillis());
        dynamicData.addProperty("dislike_count",System.currentTimeMillis());
        dynamicData.addProperty("favorite_count",System.currentTimeMillis());
        dynamicData.addProperty("comment_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_view_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_comment_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_subscriber_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_video_count",System.currentTimeMillis());

        services.addDynamicData(video_id,dynamicData);
         **/
    }

    @Test()
    public void addDynamic(){
        /**
        String video_id = "testId";
        JsonObject dynamicData = new JsonObject();
        dynamicData.addProperty("timestamp",System.currentTimeMillis());
        dynamicData.addProperty("view_count",System.currentTimeMillis());
        dynamicData.addProperty("like_count",System.currentTimeMillis());
        dynamicData.addProperty("dislike_count",System.currentTimeMillis());
        dynamicData.addProperty("favorite_count",System.currentTimeMillis());
        dynamicData.addProperty("comment_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_view_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_comment_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_subscriber_count",System.currentTimeMillis());
        dynamicData.addProperty("channel_video_count",System.currentTimeMillis());

        services.addDynamicData(video_id,dynamicData);

         **/
    }

    @Test()
    public void addYoutubeKey(){
        /**
        String video_id = "AIzaSyDnSWFkRmnODsd5u7jmOAr74FNl4mPj1KA";
        System.out.println(services.addYouTubeAPIKey(video_id));
         */
    }
    @Test()
    public void addTwitterApp(){
        /**
        String name = "tetweetsIndexCreatedst";
        String consumer_key = "bQWU9pLmD0ocir5MbpDBsRJig";
        String consumer_secret = "FiJDkms8cZx82zeSuIwUjlMGcwAZjWlo2Rm3NhuL4c8XZYj4Ye";
        String token = "720596767-rdtRwpiDaeG12g5YWNfdqdYFrZD9chzBewLyVCrh";
        String token_secret = "Pmh8QCdlhCJV0AHcCihup60cWiNqlZRzz5vdQvJOkNj1o";
        System.out.println(services.addTwitterApp(name,consumer_key,consumer_secret,token,token_secret));
         **/
    }

    @Test()
    public void getMonitoredVideos(){
        /**
        String video_id = "testId";
        System.out.println(services.setVideoAsFinished(video_id));
        System.out.println(services.getTotalMonitoredVideosAndNotFinished());
         **/
    }

    @Test()
    public void getVideosNeedUpdate(){
        System.out.println(services.getVideosThatNeedDynamicUpdate(5, TimeUnit.MINUTES));
    }

    @Test
    public void testVideosBeingMonitored(){
        System.out.println(services.getMaxVideosBeingMonitored());
    }

    @Test
    public void testComments(){
        /**
        Map<String,Integer> comments = services.getVideosThatNeedComments(5);
        String apiKey = "AIzaSyDnSWFkRmnODsd5u7jmOAr74FNl4mPj1KA";
        for(Map.Entry<String,Integer> entry :comments.entrySet()){
                String videoId = entry.getKey();
                int value = entry.getValue();
                YouTubeRequests requests = new YouTubeRequests(videoId,apiKey);
                JsonObject commentsData = requests.getLatestComments(value);

            System.out.println(commentsData.get("size").getAsInt());
                int added =services.addComments(videoId,commentsData);
                System.out.println(videoId+" Added:" + added +" of "+value);
                return;
        }
         **/
    }
}
