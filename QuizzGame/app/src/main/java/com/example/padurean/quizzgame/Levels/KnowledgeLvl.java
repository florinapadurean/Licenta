package com.example.padurean.quizzgame.Levels;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.padurean.quizzgame.Callbacks.GetMessageListener;
import com.example.padurean.quizzgame.Callbacks.KnowledgeLvlCallback;
import com.example.padurean.quizzgame.DatabaseManager.Manager;
import com.example.padurean.quizzgame.Domain.Question;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageLoose;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageWin;
import com.example.padurean.quizzgame.Menu.LevelsMenu;
import com.example.padurean.quizzgame.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Observable;

import static java.lang.Boolean.TRUE;


public class KnowledgeLvl extends Fragment implements KnowledgeLvlCallback{

    private Manager manager;
    private Button answerButton1;
    private Button answerButton2;
    private Button answerButton3;
    private Button answerButton4;
    private TextView questionTextView;
    private TextView winner;
    private Integer goodAnswerButton;
    private List<Question> data;
    private String TAG="Knowledgelvl";
    private ProgressBar progressBar;
    private LinearLayout containerQuestion;
    private Long myTime;
    private Long otherPlayerTime;
    private Boolean dataSet=false;
    private Boolean player2IsReady=false;
    private BackgroundTimer timer;
    private BackgroundTimer timerForProgressBar;
    private Thread t;
    private Thread tt;
    private Boolean lost=false;
    private ProgressBar timeProgressBar;
    private ProgressDialog progressDialog;
    private Integer viewQuestion;
    private Boolean showPuzzleHard=false;
    private GetMessageListener callback;




    public KnowledgeLvl() {
        // Required empty public constructor
    }

    public static KnowledgeLvl newInstance(Boolean param,GetMessageListener callback) {
        KnowledgeLvl fragment = new KnowledgeLvl();
        fragment.showPuzzleHard=param;
        fragment.callback=callback;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        manager=new Manager();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_knowledge_lvl, container, false);
        answerButton1=(Button) view.findViewById(R.id.button1);
        answerButton2=(Button) view.findViewById(R.id.button2);
        answerButton3=(Button) view.findViewById(R.id.button3);
        answerButton4=(Button) view.findViewById(R.id.button4);
        progressBar=(ProgressBar) view.findViewById(R.id.progress);
        int color = ContextCompat.getColor(getActivity(), R.color.verdeAlbastrui);
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
//        progressBar.getIndeterminateDrawable().setColorFilter(0xFF4081, android.graphics.PorterDuff.Mode.MULTIPLY);
        questionTextView=(TextView) view.findViewById(R.id.question);
        containerQuestion=(LinearLayout) view.findViewById(R.id.questionContainer);
        winner=(TextView) view.findViewById(R.id.winner);
        timeProgressBar=(ProgressBar) view.findViewById(R.id.progressbar1);
        timeProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
        timeProgressBar.setMax(60);
        myTime=null;
        otherPlayerTime=null;

        answerButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clicked","view:"+viewQuestion+" button2");
                if(viewQuestion==1){
                    if(goodAnswerButton==1){
                        setView2Data();
                    } else{
                        t.interrupt();
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                }
                else if(viewQuestion==2){
                    if(goodAnswerButton==1){
                        setView3Data();
                    }
                    else{
                        t.interrupt();
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                }
                else if(viewQuestion==3){
                    if(goodAnswerButton==1){
                        if (t.isAlive()) {
                            Log.i(TAG, "kill thread");
                            myTime = timer.getMyTime();
                            timer.stopRunning();
                            t.interrupt();
                            Log.i(TAG, "i answered in: " + String.valueOf(myTime));
                            callback.send("mytime:"+String.valueOf(myTime));
                            progressDialog = ProgressDialog.show(getActivity(), "Waiting for your friend to finish tis level", " Please Wait...", true, true);
                            if (winner.getText().equals("") && otherPlayerTime!=null){
                                progressDialog.dismiss();
                                if (myTime - otherPlayerTime > 0) {
                                    goToMessageLoose();
                                } else if(myTime==otherPlayerTime) {
                                    goToMessageWin("You did it in the same time!");
                                  }else{
                                    goToMessageWin("");
                                  }
                            }
                        }
                    }
                    else{
                        t.interrupt();
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                }

            }
        });
        answerButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clicked","view:"+viewQuestion+" button2");
                if(viewQuestion==1){
                    if(goodAnswerButton==2){
                        setView2Data();
                    } else{
                        t.interrupt();
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                } else if (viewQuestion == 2) {
                    if (goodAnswerButton == 2) {
                        setView3Data();
                    } else {
                        t.interrupt();
                        lost = true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                } else if (viewQuestion == 3) {
                    if (goodAnswerButton == 2) {
                        if (t.isAlive()) {
                            Log.i(TAG, "kill thread");
                            myTime = timer.getMyTime();
                            timer.stopRunning();
                            t.interrupt();
                            Log.i(TAG, "i answered in: " + String.valueOf(myTime));
                            callback.send("mytime:"+String.valueOf(myTime));
                            progressDialog = ProgressDialog.show(getActivity(), "Waiting for your friend to finish tis level", " Please Wait...", true, true);
                            if (winner.getText().equals("") && otherPlayerTime!=null){
                                progressDialog.dismiss();
                                if (myTime - otherPlayerTime > 0) {
                                    goToMessageLoose();
                                } else if(myTime==otherPlayerTime) {
                                    goToMessageWin("You did it in the same time!");
                                }else{
                                    goToMessageWin("");
                                }
                            }
                        }
                    }
                    else{
                        t.interrupt();
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                }
            }
        });
        answerButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clicked","view:"+viewQuestion+" button3");
                if(viewQuestion==2){
                    if(goodAnswerButton==3){
                        setView3Data();
                    }
                    else{
                        t.interrupt();
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }

                }
                else if(viewQuestion==3){
                    if(goodAnswerButton==3){
                        if (t.isAlive()) {
                            Log.i(TAG, "kill thread");
                            myTime = timer.getMyTime();
                            timer.stopRunning();
                            t.interrupt();
                            Log.i(TAG, "i answered in: " + String.valueOf(myTime));
                            callback.send("mytime:"+String.valueOf(myTime));
                            progressDialog = ProgressDialog.show(getActivity(), "Waiting for your friend to finish tis level", " Please Wait...", true, true);
                            if (winner.getText().equals("") && otherPlayerTime!=null){
                                progressDialog.dismiss();
                                if (myTime - otherPlayerTime > 0) {
                                    goToMessageLoose();
                                } else if(myTime==otherPlayerTime) {
                                    goToMessageWin("You did it in the same time!");
                                }else{
                                    goToMessageWin("");
                                }
                            }
                        }
                    }
                    else{
                        t.interrupt();
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                }

            }
        });
        answerButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("clicked","view:"+viewQuestion+" button4");
                if(viewQuestion==3){
                    if(goodAnswerButton==4){
                        if (t.isAlive()) {
                            Log.i(TAG, "kill thread");
                            myTime = timer.getMyTime();
                            timer.stopRunning();
                            t.interrupt();
                            Log.i(TAG, "i answered in: " + String.valueOf(myTime));
                            callback.send("mytime:"+String.valueOf(myTime));
                            progressDialog = ProgressDialog.show(getActivity(), "Waiting for your friend to finish tis level", " Please Wait...", true, true);
                            if (winner.getText().equals("") && otherPlayerTime!=null){
                                progressDialog.dismiss();
                                if (myTime - otherPlayerTime > 0) {
                                    goToMessageLoose();
                                } else if(myTime==otherPlayerTime) {
                                    goToMessageWin("You did it in the same time!");
                                }else{
                                    goToMessageWin("");
                                }
                            }
                        }
                    }
                    else{
                        lost=true;
                        callback.send("i lost");
                        goToMessageLoose();
                    }
                }

            }
        });




        Log.i("knowledglvl","1");
        data=new ArrayList<>();
        getDataForKnowledgeLvl();
//        data=((KnowledgeLvlData)getActivity()).getDataForKnowledgeLvl();

        timerForProgressBar=new BackgroundTimer(System.currentTimeMillis(),60000,timeProgressBar,this,Boolean.TRUE);
        tt=new Thread(timerForProgressBar);
        tt.start();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void getDataForKnowledgeLvl(){
        containerQuestion.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        manager.getDataKnowledge(this);
    }


    @Override
    public void setData(List<Question> data) {
        viewQuestion=1;
        this.data=data;
        Random ran = new Random();
        goodAnswerButton=ran.nextInt() % 2 == 0 ? 1 : 2;
        Log.i("ganswerbuttonView1",goodAnswerButton.toString());
        if(goodAnswerButton==1){
            answerButton1.setText(data.get(0).getGoodAnswer());
            answerButton2.setText(data.get(0).getBadAnswer1());
        }
        else if(goodAnswerButton==2){
            answerButton2.setText(data.get(0).getGoodAnswer());
            answerButton1.setText(data.get(0).getBadAnswer1());
        }
        questionTextView.setText(data.get(0).getQuestion());
        Log.i("knowledglvl","dataset");
        dataSet=true;
        if(dataSet && player2IsReady){
            Log.i("knowledge","set");
            containerQuestion.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            if(tt!=null && tt.isAlive()){
                timerForProgressBar.stopRunning();
                tt.interrupt();
                tt=null;
            }
            startClock();
        }
//        containerQuestion.setVisibility(View.VISIBLE);
    }

    public void setView2Data(){
        viewQuestion=2;
        Random ran = new Random();
        goodAnswerButton=ran.nextInt(3 - 1 + 1) + 1;
        Log.i("ganswerbuttonView2",goodAnswerButton.toString());
        if(goodAnswerButton==1){
            answerButton1.setText(data.get(1).getGoodAnswer());
            answerButton2.setText(data.get(1).getBadAnswer1());
            answerButton3.setText(data.get(1).getBadAnswer2());

        }
        else if(goodAnswerButton==2){
            answerButton2.setText(data.get(1).getGoodAnswer());
            answerButton1.setText(data.get(1).getBadAnswer1());
            answerButton3.setText(data.get(1).getBadAnswer2());
        }
        else if(goodAnswerButton==3){
            answerButton2.setText(data.get(1).getBadAnswer2());
            answerButton1.setText(data.get(1).getBadAnswer1());
            answerButton3.setText(data.get(1).getGoodAnswer());
        }
        questionTextView.setText(data.get(1).getQuestion());
        answerButton3.setVisibility(View.VISIBLE);
    }

    public void setView3Data(){
        viewQuestion=3;
        Random ran = new Random();
        goodAnswerButton=ran.nextInt(4 - 1 + 1) + 1;
        Log.i("ganswerbuttonView3",goodAnswerButton.toString());
        if(goodAnswerButton==1){
            answerButton1.setText(data.get(2).getGoodAnswer());
            answerButton2.setText(data.get(2).getBadAnswer1());
            answerButton3.setText(data.get(2).getBadAnswer2());
            answerButton4.setText(data.get(2).getBadAnswer3());
        }
        else if(goodAnswerButton==2){
            answerButton2.setText(data.get(2).getGoodAnswer());
            answerButton1.setText(data.get(2).getBadAnswer1());
            answerButton3.setText(data.get(2).getBadAnswer2());
            answerButton4.setText(data.get(2).getBadAnswer3());
        }
        else if(goodAnswerButton==3){
            answerButton2.setText(data.get(2).getBadAnswer2());
            answerButton1.setText(data.get(2).getBadAnswer1());
            answerButton3.setText(data.get(2).getGoodAnswer());
            answerButton4.setText(data.get(2).getBadAnswer3());
        }
        else if(goodAnswerButton==4){
            answerButton2.setText(data.get(2).getBadAnswer2());
            answerButton1.setText(data.get(2).getBadAnswer1());
            answerButton3.setText(data.get(2).getBadAnswer3());
            answerButton4.setText(data.get(2).getGoodAnswer());
        }
        questionTextView.setText(data.get(2).getQuestion());
        answerButton3.setVisibility(View.VISIBLE);
        answerButton4.setVisibility(View.VISIBLE);
    }


    public void setMessage(String message) {
        if (message.equals("knowledge")) {
            callback.setLastMessageEmpty();
            Log.i("knowledge","setmsgknowledge");
            player2IsReady=true;
            if(dataSet && player2IsReady){
                containerQuestion.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if(tt!=null && tt.isAlive()){
                    timerForProgressBar.stopRunning();
                    tt.interrupt();
                    tt=null;
                }
                startClock();
            }

        }
        if (message.startsWith("mytime:")) {
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            String[] l = message.split(":");
            if(Integer.parseInt(l[1])==0 && myTime!=null){
                goToMessageWin("");
            }
            else{
                otherPlayerTime = Long.parseLong(l[1]);
                Log.i(TAG,"am primit timp"+otherPlayerTime);
                if (!lost && myTime!=null) {
                    if (myTime - otherPlayerTime > 0) {
                        goToMessageLoose();
                    } else {
                        goToMessageWin("");
                    }
                }
            }
        }

    }


    public void startClock(){
        timeProgressBar.setVisibility(View.VISIBLE);
        timer=new BackgroundTimer(System.currentTimeMillis(),60000,timeProgressBar,this,Boolean.FALSE);
        t=new Thread(timer);
        t.start();
    }

    public Boolean getShowPuzzleHard(){
        return this.showPuzzleHard;
    }

    public void timerDone() {
        Log.i("knowledge","timer done");
        t.interrupt();
        if (myTime == null) {
            goToMessageLoose();
        }
        callback.send("mytime:"+timer.getMyTime());
    }

    public void timeWaitingDone() {
        Log.i("knowledge","time waiting done");
        tt.interrupt();
        tt=null;
        callback.showToast("Your friend didn't press on the same level");
        LevelsMenu lm;
        if (this.getShowPuzzleHard()) {
            lm = LevelsMenu.newInstance(TRUE);
        } else {
            lm = new LevelsMenu();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu, lm)
                .commit();
    }

    public void getLastMessage(){
        String s=callback.getLastMessage();
        if(!s.equals("")) setMessage(s);
    }


    private void goToMessageLoose() {
        MessageLoose lost= MessageLoose.newInstance("",this.showPuzzleHard);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu,lost,"loose")
                .commit();
    }

    private void goToMessageWin(String message){
        MessageWin win= MessageWin.newInstance(message,this.showPuzzleHard);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu,win,"win")
                .commit();
    }

    public void stopLevel(){
        if(t!=null && t.isAlive()){
            timer.stopRunning();
            t.interrupt();
            t=null;
        }
        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }



}
