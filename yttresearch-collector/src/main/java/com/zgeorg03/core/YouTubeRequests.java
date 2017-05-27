package com.zgeorg03.core;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by zgeorg03 on 2/24/17.
 */
public class YouTubeRequests {
    private static final String BASE_VIDEOS = "https://www.googleapis.com/youtube/v3/videos";
    private static final String BASE_COMMENTS = "https://www.googleapis.com/youtube/v3/commentThreads";
    private static final String BASE_CHANNELS = "https://www.googleapis.com/youtube/v3/channels";

    private final String key;
    private final String video_id;
    private int cost;

    /**
     * Create a new YouTube Request object
     * @param video_id
     */
    public YouTubeRequests(String video_id, String key) {
        this.key = key;
        this.video_id = video_id;
        cost=0;
    }

    /**
     * Get latest max comments if exist
     * @param max
     * @return
     */
    public JsonObject getLatestComments(int max){
        cost+=2;
        String part = "snippet";
        String url = BASE_COMMENTS + "?videoId=" + video_id + "&key=" + key + "&part=" + part+"&maxResults="+max+"&order=relevance&textFormat=plainText";
        JsonObject result = getRequest(url);

        if(!isRequestOk(result)) {
            if(result.get("error")==null)
                result.addProperty("error","No topic details");
            return result;
        }

        JsonArray myComments = new JsonArray();
        JsonArray comments = result.getAsJsonArray("items");
        for(JsonElement el :comments){
            JsonObject tmp = el.getAsJsonObject().get("snippet").getAsJsonObject().get("topLevelComment")
                    .getAsJsonObject();
            String id = tmp.get("id").getAsString();
            JsonObject comment = tmp.get("snippet").getAsJsonObject();

            String publishedAt = comment.get("publishedAt").getAsString();
            long published_at = ZonedDateTime.parse(publishedAt, DateTimeFormatter.ISO_DATE_TIME).toEpochSecond()*1000;
            long like_count = comment.get("likeCount").getAsLong();
            String text = comment.get("textDisplay").getAsString();

            JsonObject object =new JsonObject();
            object.addProperty("id",id);
            object.addProperty("published_at",published_at);
            object.addProperty("text",text);
            object.addProperty("like_count",like_count);

            myComments.add(object);
        }


        JsonObject object = new JsonObject();
        object.addProperty("size",myComments.size());
        object.add("comments",myComments);
        return object;

    }

    /**
     * Get the static content of a video
     * @return
     */
    public JsonObject getStaticData(){
        JsonObject snippet = getSnippet();
        JsonObject content = getContentDetails();
        JsonObject topics = getTopics();
        JsonObject result = new JsonObject();
        if(snippet.get("error")!=null){
           result.addProperty("error",snippet.get("error").getAsString());
        }
        if(content.get("error")!=null){
            result.addProperty("error",content.get("error").getAsString());
        }

        result.add("title",snippet.get("title"));
        result.add("channel_id",snippet.get("channel_id"));
        result.add("description",snippet.get("description"));
        result.add("category_id",snippet.get("category_id"));
        result.add("published_at",snippet.get("published_at"));
        result.add("duration",content.get("duration"));

        if(topics.get("error")!=null)
            result.add("topics",topics.get("topics"));
        else
            result.add("topics",new JsonArray());

        return result;
    }

    /**
     * Get the dynamic content of a video
     * @param channelId
     * @return
     */
    public JsonObject getDynamicData(String channelId){
        long timestamp = System.currentTimeMillis();
        JsonObject statistics = getStatistics();
        JsonObject channelStats = getChannelStatistics(channelId);

        JsonObject result = new JsonObject();
        if(statistics.get("error")!=null){
            result.addProperty("error",statistics.get("error").getAsString());
        }
        if(channelStats.get("error")!=null){
            result.addProperty("error",channelStats.get("error").getAsString());
        }

        result.addProperty("timestamp",timestamp);
        result.add("view_count",statistics.get("view_count"));
        result.add("like_count",statistics.get("like_count"));
        result.add("dislike_count",statistics.get("dislike_count"));
        result.add("favorite_count",statistics.get("favorite_count"));
        result.add("comment_count",statistics.get("comment_count"));
        result.add("channel_view_count",channelStats.get("view_count"));
        result.add("channel_comment_count",channelStats.get("comment_count"));
        result.add("channel_subscriber_count",channelStats.get("subscriber_count"));
        result.add("channel_video_count",channelStats.get("video_count"));

        return result;
    }

    /**
     * Dynamic Info for the channel that the video belongs to
     * @param channelId
     * @return
     */
    private JsonObject getChannelStatistics(String channelId) {
        cost+=2;
        String part = "statistics";
        String url = BASE_CHANNELS + "?id=" + channelId + "&key=" + key + "&part=" + part;
        JsonObject result = getRequest(url);

        if(!isRequestOk(result)) {
            if(result.get("error")==null)
                result.addProperty("error","No channel statistics");
            return result;
        }

        JsonObject statistics = result.getAsJsonArray("items").get(0)
                .getAsJsonObject().get("statistics").getAsJsonObject();


        JsonObject object =new JsonObject();

        object.addProperty("view_count",statistics.get("viewCount").getAsLong());

        JsonElement commentCount = statistics.get("commentCount");
        if(commentCount!=null)
            object.addProperty("comment_count",commentCount.getAsLong());
        else
            object.addProperty("comment_count",-1);

        JsonElement subscriberCount = statistics.get("subscriberCount");
        if(subscriberCount!=null)
            object.addProperty("subscriber_count",subscriberCount.getAsLong());
        else
            object.addProperty("subscriber_count",-1);

        JsonElement videoCount = statistics.get("videoCount");
        if(videoCount!=null)
            object.addProperty("video_count",videoCount.getAsLong());
        else
            object.addProperty("video_count",-1);

        return object;
    }

    /**
     * Get the static topics of the video. The topics are auto-generated by YouTube algoritmhs
     * @return
     */
    private JsonObject getTopics() {
        cost+=2;
        String part = "topicDetails";
        String url = BASE_VIDEOS + "?id=" + video_id + "&key=" + key + "&part=" + part;
        JsonObject result = getRequest(url);

        if(!isRequestOk(result)) {
            if(result.get("error")==null)
                result.addProperty("error","No topic details");
            return result;
        }

        JsonArray topics =result.getAsJsonArray("items");
        if(topics.size()==0){
            JsonObject obj = new JsonObject();
            obj.add("topics",new JsonArray());
            return obj;
        }
        JsonElement topicDetails = topics.get(0) .getAsJsonObject().get("topicDetails");
        if(topicDetails==null){
            JsonObject obj = new JsonObject();
            obj.add("topics",new JsonArray());
            return obj;
        }
        JsonArray tp = topicDetails.getAsJsonObject().get("topicCategories").getAsJsonArray();


        JsonObject object =new JsonObject();
        JsonArray myArray = new JsonArray();
        for(JsonElement element : tp){
            String u[] = element.getAsString().split("/");
            if(u.length!=0)
                myArray.add(u[u.length-1]);
        }
        object.add("topics",myArray);

        return object;
    }

    /**
     * Dynamic statistics about the video
     * @return
     */
    private JsonObject getStatistics() {
        cost+=2;
        String part = "statistics";
        String url = BASE_VIDEOS + "?id=" + video_id + "&key=" + key + "&part=" + part;
        JsonObject result = getRequest(url);

        if(!isRequestOk(result)) {
            if(result.get("error")==null)
                result.addProperty("error","No statistics details");
            return result;
        }

        JsonObject statistics = result.getAsJsonArray("items").get(0)
                .getAsJsonObject().get("statistics").getAsJsonObject();

        if(statistics==null) {
            result.addProperty("error", "No statistics details");
            return result;
        }

        JsonObject object =new JsonObject();

        JsonElement  viewCount = statistics.get("viewCount");
        if(viewCount!=null)
            object.addProperty("view_count",viewCount.getAsLong());
        else
            object.addProperty("view_count",0L);

        JsonElement  likeCount = statistics.get("likeCount");
        if(likeCount!=null)
            object.addProperty("like_count",likeCount.getAsLong());
        else
            object.addProperty("like_count",0L);

        JsonElement  dislikeCount = statistics.get("dislikeCount");
        if(dislikeCount!=null)
            object.addProperty("dislike_count",dislikeCount.getAsLong());
        else
            object.addProperty("dislike_count",0L);

        JsonElement  favoriteCount = statistics.get("favoriteCount");
        if(favoriteCount!=null)
            object.addProperty("favorite_count",favoriteCount.getAsLong());
        else
            object.addProperty("favorite_count",0L);

        JsonElement  commentCount = statistics.get("commentCount");
        if(commentCount!=null)
            object.addProperty("comment_count",commentCount.getAsLong());
        else
            object.addProperty("comment_count",0L);

        return object;
    }

    /**
    * Static information about the video
    */
    private JsonObject getSnippet() {
        cost+=2;
        String part = "snippet";
        String url = BASE_VIDEOS + "?id=" + video_id + "&key=" + key + "&part=" + part;
        JsonObject result = getRequest(url);

        if(!isRequestOk(result)) {
            if(result.get("error")==null)
                result.addProperty("error","No snippet details");
            return result;
        }

        JsonObject snippet = result.getAsJsonArray("items").get(0)
                .getAsJsonObject().get("snippet").getAsJsonObject();

        String publishedAt = snippet.get("publishedAt").getAsString();
        long published_at = ZonedDateTime.parse(publishedAt, DateTimeFormatter.ISO_DATE_TIME).toEpochSecond()*1000;
        String channel_id = snippet.get("channelId").getAsString();

        JsonObject object =new JsonObject();
        object.addProperty("published_at",published_at);
        object.addProperty("channel_id",channel_id);
        object.addProperty("title",snippet.get("title").getAsString());
        object.addProperty("description",snippet.get("description").getAsString());
        object.addProperty("category_id",snippet.get("categoryId").getAsInt());

        return object;
    }

    /**
     * Get the content details of a video
     * @return
     */
    private JsonObject getContentDetails() {
        cost+=2;
        String part = "contentDetails";
        String url = BASE_VIDEOS + "?id=" + video_id + "&key=" + key + "&part=" + part;
        JsonObject result = getRequest(url);

        if(!isRequestOk(result)) {
            if(result.get("error")==null)
                result.addProperty("error","No content details");
            return result;
        }

        JsonObject contentDetails = result.getAsJsonArray("items").get(0)
                .getAsJsonObject().get("contentDetails").getAsJsonObject();
        String durationString = contentDetails.get("duration").getAsString();

        JsonObject object =new JsonObject();
        long duration =  getDuration(durationString);
        if(duration!=-1)
            object.addProperty("duration",duration);
        else
            result.addProperty("error","No duration");

        return object;
    }

    /**
     * Make a get request and return response as a jsonObject
     * @param urlString
     * @return
     */
    public JsonObject getRequest(String urlString) {
        JsonObject result;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() != 200)
                throw new Exception("Error : " + connection.getResponseCode());
            this.cost+=1;

            return new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();


        } catch (Exception e) {
            result = new JsonObject();
            result.addProperty("error", e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Check if the response object doesn't contain any error
     * @param object
     * @return
     */
    private boolean isRequestOk(JsonObject object) {
        if (object.get("error") != null)
            return false;
        int size = object.getAsJsonArray("items").size();
        return size != 0;
    }

    /**
     * Convert YouTube video duration to millis
     * @param durationString
     * @return
     */
    private long getDuration(String durationString) {
        try{
            String time = durationString.substring(2);
            long duration = 0L;
            Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
            for(int i = 0; i < indexs.length; i++) {
                int index = time.indexOf((String) indexs[i][0]);
                if(index != -1) {
                    String value = time.substring(0, index);
                    duration += Integer.parseInt(value) * (int) indexs[i][1] * 1000;
                    time = time.substring(value.length() + 1);
                }
            }
            return duration;

        }catch (Exception ex){
            return -1;
        }
    }

    /**
     * Get the current cost of this request object
     * @return
     */
    public int getCost() {
        return cost;
    }
}
