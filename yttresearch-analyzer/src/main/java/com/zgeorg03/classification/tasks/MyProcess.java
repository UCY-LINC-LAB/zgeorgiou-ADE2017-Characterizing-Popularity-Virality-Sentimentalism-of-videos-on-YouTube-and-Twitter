package com.zgeorg03.classification.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

/**
 * Created by zgeorg03 on 4/14/17.
 */
public class MyProcess implements Callable<String> {

    private BufferedReader in;
    private BufferedReader error;
    private final String scriptPath;
    private final String arguments;

    public MyProcess(String scriptPath, String arguments) {
        this.scriptPath = scriptPath;
        this.arguments = arguments;
    }


    public String call() throws Exception {
        ProcessBuilder builder = new ProcessBuilder("bash","-c","python "+scriptPath+arguments);
        try {
            Process process = builder.start();
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            this.error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            process.waitFor();
            String input = readLines(this.in);
            String error = readLines(this.error);
            if (!error.isEmpty()) {
                return error;
            }
            return input;
        }catch (Exception ex){
            return ex.getLocalizedMessage();
        }


    }

    private String readLines(BufferedReader reader) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        String input = "";
        while ((input = reader.readLine()) != null)
            stringBuffer.append(input);

        return stringBuffer.toString();
    }
}
