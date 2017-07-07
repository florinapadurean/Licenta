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
    private String TAG = "Timer";
    private long now = 0;
    private Boolean running;
    private ProgressBar progressBar;
    private Fragment callback;
    private Boolean progressBarSpinning;


    public BackgroundTimer(long start, long howLong, ProgressBar progressBar, Fragment callback, Boolean b) {
        this.start = start;
        this.howLong = howLong;
        this.running = true;
        this.progressBar = progressBar;
        this.callback = callback;
        this.progressBarSpinning = b;
    }

    @Override
    public void run() {
        while (running) {
            if (callback instanceof KnowledgeLvl) {
                ((KnowledgeLvl) callback).getLastMessage();
            }
            if (callback instanceof ImagePuzzleLvl) {
                ((ImagePuzzleLvl) callback).getLastMessage();
            }
            if (callback instanceof ImagePuzzleHardLvl) {
                ((ImagePuzzleHardLvl) callback).getLastMessage();
            }
            if (callback instanceof BattleshipLvl) {
                ((BattleshipLvl) callback).getLastMessage();
            }
            now = System.currentTimeMillis();
            elapsed = now - start;
            if (progressBar != null) progressBar.setProgress((int) elapsed / 1000);
//            Log.i(TAG,"elapsed "+elapsed+" howlong "+ howLong);
            if (elapsed / 1000 >= howLong / 1000) {
                Log.i(TAG, "game over");
                this.running = false;
                if (callback instanceof ImagePuzzleLvl) {
                    if (progressBarSpinning) {
                        ((ImagePuzzleLvl) callback).timeWaitingDone();
                    } else ((ImagePuzzleLvl) callback).timerDone();
                } else if (callback instanceof ImagePuzzleHardLvl) {
                    if (progressBarSpinning) ((ImagePuzzleHardLvl) callback).timeWaitingDone();
                    ((ImagePuzzleHardLvl) callback).timerDone();
                } else if (callback instanceof KnowledgeLvl) {
                    if (progressBarSpinning) ((KnowledgeLvl) callback).timeWaitingDone();
                    else ((KnowledgeLvl) callback).timerDone();
                }
                if (callback instanceof BattleshipLvl) {
                    ((BattleshipLvl) callback).timeWaitingDone();
                }

            }

        }
    }


    public void stopRunning() {
        this.running = false;
    }


    public long getMyTime() {
        return this.elapsed;
    }

}
