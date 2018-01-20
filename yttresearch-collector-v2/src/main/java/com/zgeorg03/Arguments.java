package com.zgeorg03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Arguments {

    private String remote_host;
    private int service_port;
    private String database;
    private boolean debug_mode;
    private int max_videos=100;
    private int max_comments=100;
    private Arguments(){

    }
    public static Arguments loadYAML(String fileName){
        File file = Paths.get(fileName).toFile();

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(file, Arguments.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRemote_host() {
        return remote_host;
    }

    public void setRemote_host(String remote_host) {
        this.remote_host = remote_host;
    }

    public int getService_port() {
        return service_port;
    }

    public void setService_port(int service_port) {
        this.service_port = service_port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    @Override
    public String toString() {
        return "Arguments{" +
                "remote_host='" + remote_host + '\'' +
                ", service_port=" + service_port +
                ", database='" + database + '\'' +
                '}';
    }

    public boolean isDebug_mode() {
        return debug_mode;
    }

    public void setDebug_mode(boolean debug_mode) {
        this.debug_mode = debug_mode;
    }

    public int getMax_videos() {
        return max_videos;
    }

    public void setMax_videos(int max_videos) {
        this.max_videos = max_videos;
    }

    public int getMax_comments() {
        return max_comments;
    }

    public void setMax_comments(int max_comments) {
        this.max_comments = max_comments;
    }
}
