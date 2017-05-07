package com.zgeorg03.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    private final String key;
    private Map<String,FeatureValue> features = new HashMap<>();

    public LoadFeatures(String directory,String key) throws IOException {
        path = Paths.get(directory);
        this.key=key;
        List<File> files = loadFiles();
        for (File file : files) {
            addFile(file);
        }

    }


    public void writeAll(String name) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(new File(name+"_"+key+"_features.csv")));
        pw.print(this);
        pw.close();
    }

    public List<File> loadFiles(){
        List<File> files = new LinkedList<>();
        for(File fp : path.toFile().listFiles((dir, name) -> name.endsWith(".imp") && name.contains(key)))
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
        FeatureValue val = features.getOrDefault(feature,new FeatureValue());
        val.put(name,value);
        features.putIfAbsent(feature,val);
    }
    public String toString(){
        String top = "\t"+features.values().stream().findFirst().get().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
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
