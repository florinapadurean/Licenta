package com.example.padurean.quizzgame.DatabaseManager;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.padurean.quizzgame.Callbacks.KnowledgeLvlCallback;
import com.example.padurean.quizzgame.Domain.Question;
import com.example.padurean.quizzgame.Levels.KnowledgeLvl;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Asus on 08.04.2017.
 */

public class Manager {
    private FirebaseHelper firebaseHelper;
    private String TAG="Manager";

    public Manager(){
        firebaseHelper=new FirebaseHelper();
    }

    public void getDataKnowledge(final KnowledgeLvlCallback callback, final ProgressBar progressBar){
        firebaseHelper.getDataForKnowledgeLvl()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataSnapshot>() {
                    @Override
                    public void onCompleted() {
                        Log.v(TAG,"Good Service completed1");
//                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e( TAG,"Error while loading the events1");
                    }

                    @Override
                    public void onNext(final DataSnapshot dataSnapshot) {
                        Log.i(TAG,"on next data recieved");
                        List<Question> data=new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Question question = snapshot.getValue(Question.class);
                            data.add(question);
                        }
                        callback.setData(data);


                    }
                });
    }




}
