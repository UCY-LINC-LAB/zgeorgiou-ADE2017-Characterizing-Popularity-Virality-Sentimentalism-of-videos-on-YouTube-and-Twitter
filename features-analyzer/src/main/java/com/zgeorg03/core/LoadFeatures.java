package com.zgeorg03.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zgeorg03 on 4/10/17.
 */
public class LoadFeatures {
    private final Path path;
    private Map<String,FeatureValue> features = new HashMap<>();

    public LoadFeatures(String directory) throws IOException {
        path = Paths.get(directory);
        List<File> files = loadFiles();
        for (File file : files) {
            addFile(file);
        }

    }

    public List<File> loadFiles(){
        List<File> files = new LinkedList<>();
        for(File fp : path.toFile().listFiles((dir, name) -> name.endsWith(".imp")))
            files.add(fp);
        return files;
    }

    public void addFile(File fp) throws IOException {
        String name = fp.getName().substring(0,fp.getName().length()-8);

        Files.readAllLines(fp.toPath()).stream().forEach(line ->{
            String toks[]= line.split("\t");
            String feature = toks[1];
            float value = Float.parseFloat(toks[2]);
            addFeature(feature,name,value);

        });
    }

    private void addFeature(String feature, String name, float value) {
        FeatureValue val = features.get(feature);
        if(val==null){
            val = new FeatureValue();
            val.put(name,value);
            features.put(feature,val);
        }else{
            val.put(name,value);
        }
    }
    public String toString(){
        String top = "\t"+features.get("likes_1").entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
                .map(k->k.getKey()).collect(Collectors.joining("\t"));
        return top+"\n"+this.features.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
                .map(k->k.getKey()+"\t"+k.getValue()).collect(Collectors.joining("\n"));


    }
}
class FeatureValue extends HashMap<String,Float> {
    @Override
    public String toString() {
        return this.entrySet().stream().sorted(Comparator.comparing(Entry::getKey))
                .map(k->k.getValue()+"").collect(Collectors.joining("\t"));

    }
}
