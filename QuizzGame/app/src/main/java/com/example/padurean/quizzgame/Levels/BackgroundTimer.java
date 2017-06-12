package com.example.padurean.quizzgame.Levels;

import android.app.Fragment;
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
    private Fragment callback;


    public BackgroundTimer(long start, long howLong, ProgressBar progressBar,Fragment callback){
        this.start=start;
        this.howLong=howLong;
        this.running=true;
        this.progressBar=progressBar;
        this.callback=callback;
    }

    @Override
    public void run() {
        while (running){
            now = System.currentTimeMillis();
            elapsed = now - start;
            progressBar.setProgress((int)elapsed/1000);
//            Log.i(TAG,"elapsed "+elapsed+" howlong "+ howLong);
            if (elapsed/1000 >= howLong/1000) {
                Log.i(TAG,"game over");
                this.running=false;
                if(callback instanceof ImagePuzzleLvl){
                    ((ImagePuzzleLvl)callback).timerDone();
                }else if(callback instanceof ImagePuzzleHardLvl){
                    ((ImagePuzzleHardLvl)callback).timerDone();
                }else if(callback instanceof KnowledgeLvl){
                    ((KnowledgeLvl)callback).timerDone();
                }

            }

        }
    }


    public void stopRunning(){
        this.running=false;
    }

    public Boolean isRunning(){
        return this.running;
    }


    public long getMyTime(){
        return this.elapsed;
    }

}
