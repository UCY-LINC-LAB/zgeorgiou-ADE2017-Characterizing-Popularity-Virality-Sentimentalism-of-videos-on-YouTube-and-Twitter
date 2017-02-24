package com.zgeorg03.core.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.zgeorg03.core.models.VideoRecord;
import com.zgeorg03.core.models.VideoRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 2/24/17.
 */
public class VideoDurationCollector {

    private final static Logger logger = LoggerFactory.getLogger(VideoDurationCollector.class);

    private final VideoRecords videoRecords;
    private final String path;

    private final File fp;
    private final int batchSize;

    private Map<String,Long> videosDuration = new HashMap<>();

    public VideoDurationCollector(VideoRecords videoRecords, String path, int batchSize) {
        this.videoRecords = videoRecords;
        this.path = path;
        fp = Paths.get(path,"videos.duration").toFile();
        this.batchSize = batchSize;

        if(!fp.exists()){
           logger.info("Videos duration file doesn't exist! Creating...");
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(fp));
                pw.close();
            } catch (IOException e) {
                logger.error("Cannot create  duration file");

            }
        }else{
        }
    }

    public void putDuration() throws FileNotFoundException {
        BufferedReader bf = new BufferedReader(new FileReader(fp));
        try {
            String line="";
            bf.readLine(); // Skip count
            while ((line=bf.readLine())!=null){
                String toks[] = line.split("\t");
                String id = toks[0];
                long duration = Long.parseLong(toks[1]);
                videosDuration.put(id,duration);
                VideoRecord exists = videoRecords.get(id);
                if(exists!=null)
                    exists.setDuration(duration);
            }
            bf.close();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    public void getDurations(){
        try {
            putDuration();
        } catch (FileNotFoundException e) {
            logger.error("Cannot read  duration file");
        }

        List<VideoRecord> withNoDuration = videoRecords.values().stream().filter(video -> !video.hasDuration()).limit(batchSize).collect(Collectors.toList());
        if(withNoDuration.size()==0) {
            logger.info("All videos have duration");
            return;
        }
        logger.info("Picked "+withNoDuration.size()+" videos with no duration");

        withNoDuration.stream().map(VideoRecord::getVideo_id).forEach(id -> {
            long duration = getVideoDuration(id);
            videosDuration.put(id,duration);
            videoRecords.get(id).setDuration(duration);
        });

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(fp));
            writeDuration(pw);
            pw.close();
        } catch (IOException e) {
            logger.error("Cannot write to duration file");

        }

    }

    public void writeDuration(PrintWriter pw) {
        logger.info("Duration: Writing "+ videosDuration.size() + " videos");
        pw.print(videosDuration.size()+"\n");
        videosDuration.entrySet().forEach((entry) ->
                pw.print(entry.getKey()+"\t"+entry.getValue()+"\n")
        );
    }
    /**
     * Convert duration into millis
     * @param str
     * @return
     */
    private long getMillis(String str) {
        long duration = -1L;
        if (str==null){
            return -1;
        }
        try {
            String time = str.substring(2);
            Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
            for(int i = 0; i < indexs.length; i++) {
                int index = time.indexOf((String) indexs[i][0]);
                if(index != -1) {
                    String value = time.substring(0, index);
                    duration += Integer.parseInt(value) * (int) indexs[i][1] * 1000;
                    time = time.substring(value.length() + 1);
                }
            }
        }catch (Exception e){
            logger.error("Not parsed:" + str);
            return -1;
        }
        return duration;
    }


    /**
     * Request video duration
     * @param videoId
     * @return
     */
    public long getVideoDuration(String videoId) {
        String duration = null;
        String base = "https://www.googleapis.com/youtube/v3/";
        String API_KEY = "AIzaSyDnSWFkRmnODsd5u7jmOAr74FNl4mPj1KA";
        String urlString = base + "videos?part=contentDetails&id=" + videoId + "&key=" + API_KEY;
        URL url = null;
        try {

            // Request Json element
            url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)  url.openConnection(Proxy.NO_PROXY);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != 200)
                throw new Exception("Error : "+ connection.getResponseCode());
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            JsonElement root = new JsonParser().parse(new JsonReader(in));
            in.close();

            // Parse Json element
            JsonObject json = root.getAsJsonObject();
            JsonObject page = json.get("pageInfo").getAsJsonObject();


            int total = page.get("totalResults").getAsInt();
            if(total>0){
                JsonArray items = json.get("items").getAsJsonArray();
                duration = items.get(0).getAsJsonObject().get("contentDetails")
                        .getAsJsonObject().get("duration").getAsString();
            }else {
                logger.error("No Json Element in response");
            }
        } catch (ProtocolException e) {
            logger.error(e.getLocalizedMessage());
            return -1;
        } catch (MalformedURLException e) {
            logger.error(e.getLocalizedMessage());
            return -1;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return -1;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return -1;
        }
        return this.getMillis(duration);
    }
}
