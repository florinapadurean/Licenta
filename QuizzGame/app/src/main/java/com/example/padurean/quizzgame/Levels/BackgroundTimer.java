package com.example.padurean.quizzgame.Levels;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Asus on 09.04.2017.
 */

public class BackgroundTimer implements Runnable {

    private long start;
    private long elapsed;
    private long howLong;
    private String TAG="Timer";
    private long now=0;
    private Boolean running;
    private ProgressBar progressBar;


    public BackgroundTimer(long start, long howLong, ProgressBar progressBar){
        this.start=start;
        this.howLong=howLong;
        this.running=true;
        this.progressBar=progressBar;
    }

    @Override
    public void run() {
        while (running ){
            now = System.currentTimeMillis();
//            && !Thread.currentThread().isInterrupted()
            elapsed = now - start;
            progressBar.setProgress((int)elapsed/1000);
//            Log.i(TAG,"elapsed "+elapsed+" howlong "+ howLong);
            if (elapsed/1000 >= howLong/1000) {
                Log.i(TAG,"game over");
                this.running=false;
            }

        }
    }


    public void stopRunning(){
        this.running=false;
    }


    public long getMyTime(){
        return this.elapsed;
    }

}
