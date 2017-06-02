package com.example.padurean.quizzgame.Levels;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.padurean.quizzgame.GameFinishedMessages.ImagePuzzleLoose;
import com.example.padurean.quizzgame.GameFinishedMessages.ImagePuzzleWin;
import com.example.padurean.quizzgame.R;

import rx.Observable;

/**
 * Created by Asus on 27.05.2017.
 */

public class ImagePuzzleLvl extends Fragment {

    private FrameLayout box1;
    private FrameLayout box2;
    private FrameLayout box3;
    private FrameLayout box4;
    private LinearLayout linearLayout;
    private Dialog settingsDialog;
    private Long myTime;
    private Long otherPlayerTime;
    private BackgroundTimer timer;
    private ProgressBar progressBar;
    private ProgressBar timeProgressBar;
    private Thread t;

    public ImagePuzzleLvl() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("2","aici");
        View view=inflater.inflate(R.layout.fragment_imagepuzzle, container, false);

        progressBar=(ProgressBar) view.findViewById(R.id.progress);
        int color = ContextCompat.getColor(getActivity(), R.color.verdeAlbastrui);
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
//        progressBar.getIndeterminateDrawable().setColorFilter(0xFF4081, android.graphics.PorterDuff.Mode.MULTIPLY);
        timeProgressBar=(ProgressBar) view.findViewById(R.id.progressbar1);
        timeProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
        timeProgressBar.setMax(30);
        myTime=null;
        otherPlayerTime=null;

        View dialogView=inflater.inflate(R.layout.puzzle_dialog, container, false);
        Button ok=(Button)dialogView.findViewById(R.id.okbtn);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.v("tangram","ok pressed");
                settingsDialog.dismiss();
                startClock();
            }
        });

        box1= (FrameLayout) view.findViewById(R.id.empty_box1);
        box2= (FrameLayout) view.findViewById(R.id.empty_box2);
        box3= (FrameLayout) view.findViewById(R.id.empty_box3);
        box4= (FrameLayout) view.findViewById(R.id.empty_box4);
        box1.setOnTouchListener(new MyTouchListener());
        box2.setOnTouchListener(new MyTouchListener());
        box3.setOnTouchListener(new MyTouchListener());
        box4.setOnTouchListener(new MyTouchListener());
        box1.setOnDragListener(new MyDragListener());
        box2.setOnDragListener(new MyDragListener());
        box3.setOnDragListener(new MyDragListener());
        box4.setOnDragListener(new MyDragListener());
        linearLayout=(LinearLayout) view.findViewById(R.id.grid);
        linearLayout.setOnDragListener(new MyDragListener());
        settingsDialog = new Dialog(getActivity());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(dialogView);
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
//        checkIfMessageAlreadyRecieved();
        Log.v("3","aici");
        return view;
    }

    private void checkIfMessageAlreadyRecieved() {
        String lastMsg=((ImagePuzzleLvl.GetMessageListener) getActivity()).getLastMessageRecieved();
        if(lastMsg!="" && lastMsg=="puzzle"){
            Log.v("4","aici");
            linearLayout.setVisibility(View.VISIBLE);
            settingsDialog.show();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }


    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    if(v instanceof LinearLayout){
                        FrameLayout view1=(FrameLayout)event.getLocalState();
                        view1.setVisibility(View.VISIBLE);
                        break;
                    }
                    if(event.getLocalState() instanceof FrameLayout && v instanceof FrameLayout){
                        FrameLayout view1=(FrameLayout)event.getLocalState();
                        //unde
                        FrameLayout view2=(FrameLayout)v;
                        Log.v("puzzle",view1.toString()+" "+view2.toString());
                        Drawable d1=view1.getBackground();
                        Drawable d2=view2.getBackground();
                        view1.setBackground(d2);
                        view1.setVisibility(View.VISIBLE);
                        view2.setBackground(d1);
                        view2.setVisibility(View.VISIBLE);
                    }
                    else{
                        Log.v("puzzle",event.getLocalState().toString());
                    }

//                    ImageView view = (ImageView) event.getLocalState();
//                    Log.v("Tangram", "x:" + x+"y:"+y);
//
//                    ViewGroup owner = (ViewGroup) view.getParent();
//                    Log.v("Tangram", "owner" + owner);
//                    Log.v("Tangram", "v" + v);
//                    Log.v("Tangram", "vIEW" + view);
//
//                    v.setBackground(view.getDrawable());
//                    view.setVisibility(View.INVISIBLE);

                    // Returns true. DragEvent.getResult() will return true.
//                    return true;
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {
                        Log.v("tanagram", "The drop was handled.");

                    } else {
                        Log.v("tanagram", "The drop didn't work.");
                    }
                    //check if everything is ok

//                    Log.v("asss",box1.getBackground().getConstantState().toString() + getResources().getDrawable(R.drawable.white).getConstantState().toString());
                    if (box1.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn1_1).getConstantState() &&
                            box2.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn2_1).getConstantState() &&
                            box3.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn1_2).getConstantState() &&
                            box4.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn2_2).getConstantState()) {
                        Log.v("tangram", "ok :)");
                        if (t.isAlive()) {
                            myTime = timer.getMyTime();
                            timer.stopRunning();
                            t.interrupt();
                            ((ImagePuzzleLvl.GetMessageListener) getActivity()).send("mytime:" + String.valueOf(myTime));
                            if (otherPlayerTime != null) {
                                if (myTime - otherPlayerTime > 0) {
                                    ImagePuzzleLoose lost=new ImagePuzzleLoose();
                                    getActivity().getFragmentManager().beginTransaction()
                                            .replace(R.id.frag_menu,lost,"puzzleloose")
                                            .addToBackStack("levelsMenuFragment")
                                            .commit();
                                } else if (myTime == otherPlayerTime) {
                                    ImagePuzzleWin win=new ImagePuzzleWin();
                                    getActivity().getFragmentManager().beginTransaction()
                                            .replace(R.id.frag_menu,win,"puzzlewin")
                                            .addToBackStack("levelsMenuFragment")
                                            .commit();
                                } else {
                                    ImagePuzzleWin win=new ImagePuzzleWin();
                                    getActivity().getFragmentManager().beginTransaction()
                                            .replace(R.id.frag_menu,win,"puzzlewin")
                                            .addToBackStack("levelsMenuFragment")
                                            .commit();
                                }
                            }
                        }
                    } else {
                        if (!t.isAlive()) {
                            ImagePuzzleLoose lost=new ImagePuzzleLoose();
                            getActivity().getFragmentManager().beginTransaction()
                                    .replace(R.id.frag_menu,lost,"puzzleloose")
                                    .addToBackStack("levelsMenuFragment")
                                    .commit();
                        }

                    }


                default:
                    break;
            }
            return true;
        }
    }

    public void startClock(){
        timeProgressBar.setVisibility(View.VISIBLE);
        timer=new BackgroundTimer(System.currentTimeMillis(),60000,timeProgressBar);
        t=new Thread(timer);
        t.start();
    }

    public void setMessage(String message) {
        if (message.equals("puzzle")) {
            Log.v("4","aici");
            linearLayout.setVisibility(View.VISIBLE);
            settingsDialog.show();
            progressBar.setVisibility(View.GONE);
        }

        if (message.startsWith("mytime:")) {
            String[] l = message.split(":");
            otherPlayerTime = Long.parseLong(l[1]);
            Log.i("puzzle","am primit timp"+otherPlayerTime);
            if (myTime!=null) {
                if (myTime - otherPlayerTime > 0) {
                    ImagePuzzleLoose lost=new ImagePuzzleLoose();
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.frag_menu,lost,"puzzleloose")
                            .addToBackStack("levelsMenuFragment")
                            .commit();
                } else {
                    ImagePuzzleWin win=new ImagePuzzleWin();
                    getActivity().getFragmentManager().beginTransaction()
                            .replace(R.id.frag_menu,win,"puzzlewin")
                            .addToBackStack("levelsMenuFragment")
                            .commit();
                }
            }

        }
    };


    public interface GetMessageListener{
        void send(String string);

        String getLastMessageRecieved();
    }
}
