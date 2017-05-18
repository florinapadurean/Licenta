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

public class BackgoundTimer implements Runnable {

    private long start;
    private long elapsed;
    private long howLong;
    private String TAG="Timer";
    private long now=0;
    private Boolean running;
    private ProgressBar progressBar;


    public BackgoundTimer(long start, long howLong, ProgressBar progressBar){
        this.start=start;
        this.howLong=howLong;
        this.running=true;
        this.progressBar=progressBar;
    }

    @Override
    public void run() {
        while (running  ){
            now = System.currentTimeMillis()/1000;
//            && !Thread.currentThread().isInterrupted()
            elapsed = now - start;
            progressBar.setProgress((int)elapsed);
//            Log.i(TAG,"elapsed "+elapsed+" howlong "+ howLong);
            if (elapsed >= howLong) {
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
