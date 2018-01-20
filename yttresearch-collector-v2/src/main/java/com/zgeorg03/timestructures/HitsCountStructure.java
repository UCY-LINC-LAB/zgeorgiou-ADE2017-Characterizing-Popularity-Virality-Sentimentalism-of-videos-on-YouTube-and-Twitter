package com.zgeorg03.timestructures;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HitsCountStructure {
    private final int max_size;
    private final int data[];
    private long lastTime;
    private long millis;
    private  int currIndex;

    public HitsCountStructure(int max_size, int timeInterval, TimeUnit timeUnit) {
        millis = timeUnit.toMillis(timeInterval);
        this.max_size = max_size;
        data = new int[max_size];
        lastTime = System.currentTimeMillis();
    }

    public void addHit() {
        long now = System.currentTimeMillis();

        //Increment a new bucket
        if ((now-lastTime) > millis) {
            currIndex = (currIndex + 1) % max_size;
            lastTime = now; //Reset time
            data[currIndex] = 0;
        }

        data[currIndex]++;
    }
    public List<Integer> getLastNHits(int n){
        List<Integer> result = new LinkedList<>();
        if(n>max_size)
            n=max_size;
        int count=1;
        int index = currIndex;
        while((count++)<n){
            result.add(data[index]);
            index--;
            if(index<=0)
                index=max_size-1;
        }
        return result;
    }

    public int getLastHit(){
        if ((System.currentTimeMillis()-lastTime) > millis) {
            return 0;

        }
        return data[currIndex];
    }
    @Override
    public String toString() {

        return getLastNHits(5).toString();
    }
}
