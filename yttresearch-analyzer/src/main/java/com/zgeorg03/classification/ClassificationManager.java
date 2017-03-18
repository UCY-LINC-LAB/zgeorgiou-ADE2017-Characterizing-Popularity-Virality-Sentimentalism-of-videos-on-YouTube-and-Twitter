package com.zgeorg03.classification;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zgeorg03.utils.JsonModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 3/17/17.
 */
public class ClassificationManager implements Runnable{
    private final Logger logger = LoggerFactory.getLogger(ClassificationManager.class);
    private final File root;

    private final Map<String,ClassificationFiles> experiments = new HashMap<>();

    public ClassificationManager(String workingDir) {
        this.root = Paths.get(workingDir).toFile();
        if(!this.root.exists()){
            if(this.root.mkdirs())
                logger.info("Created dir:"+root.getAbsolutePath());
        }
    }


    @Override
    public void run() {
        logger.info("Starting Classification Manager...");
        while (true){

            checkDirs();

            checkExperiments();

            try { TimeUnit.SECONDS.sleep(30); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    private void checkExperiments() {
        experiments.entrySet().stream().forEach(entry -> {
            ClassificationFiles classificationFiles = entry.getValue();
            if (classificationFiles.getPath().exists())
                if (classificationFiles.isModified())
                    classificationFiles.setReady(false);

            if (!classificationFiles.isReady()) {
                if (classificationFiles.process())
                    logger.info("Experiment: " + entry.getKey() + " is ready!");
            }
        });
    }

    private void checkDirs() {
        for(File dir :root.listFiles((dir, name) -> dir.isDirectory())){
            String experimentId = dir.getName();
            if(!experiments.containsKey(experimentId)) {
                logger.info("Found new experiment:" + experimentId);
                experiments.put(experimentId,new ClassificationFiles(dir, experimentId));
            }
        }
    }

    public JsonObject getClassificationResults(String id) {
        JsonObject result = new JsonObject();
        ClassificationFiles files = experiments.get(id);
        if(files==null) {
            result.addProperty("msg", "Experiment " + id + " doesn't exist");
            return result;
        }
        if(!files.isReady()) {
            result.addProperty("msg", "Experiment " + id + " is not ready. Please be patient");
            return result;
        }

       return files.toJson();

    }


    class ClassificationFiles implements JsonModel {
        private final File classificationPath;
        boolean ready;
        boolean changed;
        private Map<String,JsonObject> evaluations = new HashMap<>();
        private Map<String,JsonObject> features = new HashMap<>();

        private Map<String,Long> lastModified = new HashMap<>();
        private final String experiment;

        ClassificationFiles(File root, String experiment) {
            classificationPath = Paths.get(root.getAbsolutePath(),"classification_data").toFile();
            this.experiment = experiment;
        }

        public boolean process() {
            if(!classificationPath.exists())
                return false;
            boolean ready = true;

            for(File file : classificationPath.listFiles((dir, name) -> name.endsWith(".eva"))){
                String name = getName(file);

                if(changed || !evaluations.containsKey(name)){
                    try {
                        List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
                        if(lines.size()!=5) {
                            ready = false;
                            continue;
                        }
                        JsonObject object = new JsonObject();
                        object.addProperty("all_old",Double.parseDouble(lines.get(0).split("\t")[1]));
                        object.addProperty("baseline_old",Double.parseDouble(lines.get(1).split("\t")[1]));
                        object.addProperty("all_recent",Double.parseDouble(lines.get(2).split("\t")[1]));
                        object.addProperty("baseline_recent",Double.parseDouble(lines.get(3).split("\t")[1]));
                        object.addProperty("f1_score",Double.parseDouble(lines.get(4).split("\t")[1]));
                        evaluations.put(name,object);
                        lastModified.put(name,file.lastModified());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(ready) {
                for(File file : classificationPath.listFiles((dir, name) -> name.endsWith(".imp"))){
                    String name = getName(file);
                    try {
                        List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
                        if(lines.isEmpty()){
                            ready=false;
                            continue;
                        }
                        JsonObject object = new JsonObject();
                        lines.stream().forEach(line->{
                            String toks[]=line.split("\t");
                            double value = Double.parseDouble(toks[2]);
                            object.addProperty(toks[1],value);
                        });
                        features.put(name,object);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(ready) {
                    this.ready = true;
                    this.changed = false;
                }
            }
            return ready;
        }
        public boolean isReady() {
            return ready;
        }

        public String getName(File file){
            List<String> list = Arrays.asList(file.getName().split("[.]")[0].split("_"));
            List<String>res = new LinkedList<>();
            for(int i=0;i<list.size()-1;i++)
                res.add(list.get(i));
            return res.stream().collect(Collectors.joining("_"));
        }

        @Override
        public JsonObject toJson() {
            JsonObject object = new JsonObject();
            object.add("popular_youtube",allToJson("youtube_popular"));
            object.add("popular_twitter",allToJson("twitter_popular"));
            object.add("popular_both",allToJson("both_popular"));

            object.add("viral_youtube",allToJson("youtube_viral"));
            object.add("viral_twitter",allToJson("twitter_viral"));
            object.add("viral_both",allToJson("both_viral"));

            object.add("popular_viral_youtube",allToJson("youtube_viral_and_popular"));
            object.add("popular_viral_twitter",allToJson("twitter_viral_and_popular"));
            object.add("popular_viral_both",allToJson("both_viral_and_popular"));


            return object;
        }

        JsonObject allToJson(String key){
            JsonObject object = new JsonObject();
            object.add("evaluation", evaluations.get(key));
            object.addProperty("graph", "/plots/classification/"+experiment+"/"+key);
            object.add("features_importance", getFeatureImportance(key));
            return object;
        }

        private JsonElement getFeatureImportance(String key) {
            JsonObject object = new JsonObject();
            object.add("all_old",features.get(key+"_all_old"));
            object.add("all_recent",features.get(key+"_all_recent"));

            return object;
        }

        @Override
        public JsonObject toJson(Map<String, Integer> view) {
            return toJson();
        }

        public void setReady(boolean ready) {
            this.ready = ready;
        }

        public boolean isModified() {
            for(File file : classificationPath.listFiles((dir, name) -> name.endsWith(".eva"))){
                String name = getName(file);
                if(lastModified.containsKey(name)){
                     if(lastModified.get(name)!=file.lastModified()) {
                         lastModified.put(name,file.lastModified());
                         changed=true;
                         return true;
                     }
                }
            }
            return false;
        }

        public File getPath() {
            return classificationPath;
        }
    }


}
