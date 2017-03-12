package com.zgeorg03.classification;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by zgeorg03 on 2/2/17.
 */
public class ClassificationManager implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(ClassificationManager.class);
    private final File root;

    private final Map<String,ClassificationFiles> directories;
    public ClassificationManager(String path) {
        this.root = Paths.get(path).toFile();
        directories = new HashMap<>();


        findDirectories();
    }

    private boolean findDirectories(){
        boolean found = false;
        File[] fileList =root.listFiles((file, s) -> file.isDirectory() && s.startsWith("classify"));
        for(File dir : fileList){
            if(directories.putIfAbsent(dir.getName(),new ClassificationFiles(dir))==null)
                found=true;
        }

        //Check if someting is deleted
        for( String name : directories.keySet()){
            boolean toRemove=true;
            for(File dir : fileList){
                if(dir.getName().equals(name))
                    toRemove =false;
            }
            if(toRemove) {
                if(directories.remove(name)!=null){
                    logger.info("Removed: "+name );
                }
            }
        }

        return found;
    }

    @Override
    public void run() {
        long sleepTime = 5000;
        boolean foundNewFile;

        while(true){

            //logger.info("Running...");

            //Reset SleepTime
            foundNewFile = findDirectories();


            processDirectories();

            if(foundNewFile) {
                logger.info("New dirs,found!");
                sleepTime = 5000;
            }else{
                if(sleepTime<10000)
                    sleepTime *=2;
            }


            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public ClassificationFiles getClassificationFiles(String dir){
        return  directories.get(dir);
    }

    public JsonObject getAUC_PRplots(int category, long seed, String auc){
        String dir = "classify-"+category+"-"+seed;
        JsonObject object = new JsonObject();
        ClassificationFiles classificationFiles  = directories.get(dir);
        if(classificationFiles==null) {
            object.addProperty("info", "Classification for: " + dir + " doesn't exist");
            return object;
        }
        JsonObject aucObject = classificationFiles.getStats(auc);
        if(aucObject == null){
            object.addProperty("info", "Not available yet");
            return object;
        }
        JsonObject features_young = classificationFiles.getFeatures(auc+"_all_recent");
        JsonObject features_old = classificationFiles.getFeatures(auc+"_all_old");
        if(features_young == null)
            aucObject.addProperty("features_recent", "Not available yet");
        else
            aucObject.add("features_recent", features_young);
        if(features_old == null)
            aucObject.addProperty("features_old", "Not available yet");
        else
            aucObject.add("features_old", features_old);


        aucObject.addProperty("precision_recall_plot","/classify-plots/"+category+"/"+seed+"/"+auc);
        object.add(auc,aucObject);
        object.addProperty("info","OK");
        return object;
    }

    public byte[] getAUCPlot(String dir, String name){
        ClassificationFiles classificationFiles  = directories.get(dir);
        if(classificationFiles==null) {
            logger.error("Classification for: " + dir + " doesn't exist");
            return null;
        }
        JsonElement element = classificationFiles.getStats(name);
        if(element == null){
            logger.error("Classification Stats for: " + dir + " doesn't exist");
            return null;
        }
        String toks[] = name.split("_");
        String plot = toks[1]+"_"+toks[0]+"_features";
        try {
            return Files.readAllBytes(Paths.get(this.root.getAbsolutePath(),dir,plot+".png"));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }
    private void processDirectories() {
       directories.entrySet().stream().forEach(entry->{
           ClassificationFiles classificationFiles = entry.getValue();
           try {
               for (File statsFile : classificationFiles.getDirectory().listFiles((file, s) -> s.endsWith(".eva"))) {
                   String fileName = statsFile.getName();
                   String stats = statsFile.getName().substring(0, fileName.length() - 4);

                   List<String> statsLines = getStatsFile(statsFile);
                   if (statsLines.size() == 5) {
                       if (classificationFiles.addStats(stats, statsLines))
                           logger.info("Stats:" + stats + " is ready and added");
                   } else {
                       /* TODO

                       String png = auc + ".png";
                       File pngFile = Paths.get(statsFile.getParent(), png).toFile();
                       boolean pngExists = pngFile.exists();
                       if (pngExists && statsFile.delete() && pngFile.delete()) {
                           logger.info("File: " + statsFile.getAbsolutePath() + " erased");
                           logger.info("File: " + pngFile.getAbsolutePath() + " erased");
                       }
                   */ }
               }
               for (File featuresFile : classificationFiles.getDirectory().listFiles((file, s) -> s.endsWith(".imp"))) {
                   String fileName = featuresFile.getName();
                   String features = fileName.substring(0, fileName.length() - 4);

                   List<String> featuresLines = getStatsFile(featuresFile);
                   if (classificationFiles.addFeatures(features, featuresLines))
                       logger.info("Feature:" + features + " is ready and added");

               }
           }
           catch(Exception e){
               logger.error("File system changed!");

           }

       });
    }

    private List<String> getStatsFile(File fp){
        List<String> lines = new LinkedList<>();
        try {
            lines = Files.readAllLines(fp.toPath());
            return lines;
        } catch (IOException e) {
            logger.error("File: "+fp.getAbsolutePath()+" couldn't be opened");
            return  lines;
        }
    }


    public String mapToFile(int type,int feature){
       if(type ==0 && feature ==0)
           return "twitter_viral";
        if(type ==0 && feature ==1)
            return "youtube_viral";
        if(type ==0 && feature ==2)
            return "both_viral";

        if(type ==1 && feature ==0)
            return "twitter_popular";
        if(type ==1 && feature ==1)
            return "youtube_popular";
        if(type ==1 && feature ==2)
            return "both_popular";

        if(type ==2 && feature ==0)
            return "twitter_popular-viral";
        if(type ==2 && feature ==1)
            return "youtube_popular-viral";
        if(type ==2 && feature ==2)
            return "both_popular-viral";

        return "unknown";
    }

    public JsonObject getAnalysisGraphs(int category, long seed) {
        String dir = "classify-"+category+"-"+seed;
        JsonObject result = new JsonObject();
        ClassificationFiles classificationFiles  = directories.get(dir);
        if(classificationFiles==null) {
            result.addProperty("info", "Classification for: " + dir + " doesn't exist");
            return result;
        }
        result.addProperty("average_daily_views_increase","/analysis-plots/"+category+"/"+seed+"/"+"average-daily-views-increase");
        result.addProperty("average_daily_tweets_increase","/analysis-plots/"+category+"/"+seed+"/"+"average-daily-tweets-increase");
        result.addProperty("average_ratio_original_tweets","/analysis-plots/"+category+"/"+seed+"/"+"average-ratio-original-tweets");
        result.addProperty("average_users_reached","/analysis-plots/"+category+"/"+seed+"/"+"average-users-reached");
        result.addProperty("videos_age_distribution","/analysis-plots/"+category+"/"+seed+"/"+"videos-age-distribution");
        return result;
    }

    public byte[] getAnalysisPlot(String dir, String plot_name) {
        try {
            return Files.readAllBytes(Paths.get(this.root.getAbsolutePath(),dir,plot_name+".png"));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }
}

class ClassificationFiles {

    private final File directory;

    private final Map<String,JsonObject> stats;
    private final Map<String,JsonObject> features;

    public ClassificationFiles(File directory) {
        this.directory = directory;
        stats = new HashMap<>();
        features = new HashMap<>();
    }

    public File getDirectory() {
        return directory;
    }

    public boolean addStats(String auc, List<String> lines){
        JsonObject jsonObject = new JsonObject();
        lines.stream().map(line -> line.split("\t"))
                .filter(x->x.length==2)
                .forEach(x ->{
                    String toks[] = x[0].split("_");
                    if(toks.length!=4)
                        return;
                    String name;
                    if(!toks[2].startsWith("f1"))
                        name = "auc_" + toks[2]+"_"+toks[3];
                    else
                        name = toks[2]+"_"+toks[3];
                    jsonObject.addProperty(name,x[1]);
                });

        return stats.putIfAbsent(auc,jsonObject)==null;
    }

    public boolean addFeatures(String features,List<String> lines){
        JsonObject jsonObject = new JsonObject();
        lines.stream().map(line -> line.split("\t"))
                .filter(x->x.length==3)
                .forEach(x -> jsonObject.addProperty(x[1],x[2]) );

        return this.features.putIfAbsent(features,jsonObject)==null;
    }

    public JsonObject getFeatures(String fatures){ return  features.get(fatures);}
    public JsonObject getStats(String auc){
       return stats.get(auc);
    }

    @Override
    public String toString() {

        return stats.toString();

    }
}

