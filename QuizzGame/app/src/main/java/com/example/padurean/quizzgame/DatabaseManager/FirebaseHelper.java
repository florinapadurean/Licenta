package com.example.padurean.quizzgame.DatabaseManager;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.padurean.quizzgame.Domain.Question;
import com.example.padurean.quizzgame.Levels.KnowledgeLvl;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Asus on 28.03.2017.
 */

public class FirebaseHelper {

    private static FirebaseDatabase mDatabase;
    private static DatabaseReference mRef;
    private static String TAG = "FirebaseHelper";

    public FirebaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
    }

    @NonNull
    public Observable<DataSnapshot> getDataForKnowledgeLvl() {
        mRef = mDatabase.getReference().child("KnowledgeLvl");
        return Observable.create(new Observable.OnSubscribe<DataSnapshot>() {
            @Override
            public void call(final Subscriber<? super DataSnapshot> subscriber) {
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(dataSnapshot);
                            subscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(new FirebaseException(error.toString()));
                        }
                    }
                });
            }
        });
    }

    public void recieveDateOnce(final OnGetDataListener listener) {
        mRef = mDatabase.getReference().child("KnowledgeLvl");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("KnowledgeLvl", "onCancelled", databaseError.toException());
            }

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }
        });
    }

    public void saveScores(String child, Integer score) {
        mRef = mDatabase.getReference().child("KnowledgeLvl");
    }


    public interface OnGetDataListener {
        public void onStart();

        public void onSuccess(DataSnapshot data);

        public void onFailed(DatabaseError databaseError);
    }
}

