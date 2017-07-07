package com.example.padurean.quizzgame.Levels;

/**
 * Created by Asus on 28.05.2017.
 */

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
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


import com.example.padurean.quizzgame.Callbacks.GetMessageListener;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageLoose;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageWin;
import com.example.padurean.quizzgame.Menu.LevelsMenu;
import com.example.padurean.quizzgame.R;

import static java.lang.Boolean.TRUE;


/**
 * Created by Asus on 27.05.2017.
 */

public class ImagePuzzleHardLvl extends Fragment {

    private FrameLayout box1;
    private FrameLayout box2;
    private FrameLayout box3;
    private FrameLayout box4;
    private FrameLayout box5;
    private FrameLayout box6;
    private FrameLayout box7;
    private FrameLayout box8;
    private FrameLayout box9;
    private FrameLayout box10;
    private FrameLayout box11;
    private FrameLayout box12;


    private LinearLayout linearLayout;
    private Dialog settingsDialog;
    private Long myTime;
    private Long otherPlayerTime;
    private BackgroundTimer timer;
    private ProgressBar progressBar;
    private ProgressBar timeProgressBar;
    private ProgressDialog progressDialog;
    private Thread t;
    private com.example.padurean.quizzgame.Callbacks.GetMessageListener callback;
    private BackgroundTimer timerForProgressBar;
    private Thread tt;
    private View dialogView;

    public ImagePuzzleHardLvl() {
        super();
    }

    public static ImagePuzzleHardLvl newInstance(GetMessageListener callback) {
        ImagePuzzleHardLvl fragment = new ImagePuzzleHardLvl();
        fragment.callback = callback;
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_puzzle_hard, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        int color = ContextCompat.getColor(getActivity(), R.color.verdeAlbastrui);
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        timeProgressBar = (ProgressBar) view.findViewById(R.id.progressbar1);
        timeProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
        timeProgressBar.setMax(100);
        myTime = null;
        otherPlayerTime = null;

        dialogView = inflater.inflate(R.layout.puzzle_dialog, container, false);
        ImageView img = (ImageView) dialogView.findViewById(R.id.imgView);
        img.setBackground(getResources().getDrawable(R.drawable.clock));
        settingsDialog = new Dialog(getActivity());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(dialogView);
        Button ok = (Button) dialogView.findViewById(R.id.okbtn);
        ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("tangram", "ok pressed");
                        settingsDialog.dismiss();
                        startClock();
            }
        });

        box1 = (FrameLayout) view.findViewById(R.id.empty_box1);
        box2 = (FrameLayout) view.findViewById(R.id.empty_box2);
        box3 = (FrameLayout) view.findViewById(R.id.empty_box3);
        box4 = (FrameLayout) view.findViewById(R.id.empty_box4);
        box5 = (FrameLayout) view.findViewById(R.id.empty_box5);
        box6 = (FrameLayout) view.findViewById(R.id.empty_box6);
        box7 = (FrameLayout) view.findViewById(R.id.empty_box7);
        box8 = (FrameLayout) view.findViewById(R.id.empty_box8);
        box9 = (FrameLayout) view.findViewById(R.id.empty_box9);
        box10 = (FrameLayout) view.findViewById(R.id.empty_box10);
        box11 = (FrameLayout) view.findViewById(R.id.empty_box11);
        box12 = (FrameLayout) view.findViewById(R.id.empty_box12);


        box1.setOnTouchListener(new MyTouchListener());
        box2.setOnTouchListener(new MyTouchListener());
        box3.setOnTouchListener(new MyTouchListener());
        box4.setOnTouchListener(new MyTouchListener());
        box5.setOnTouchListener(new MyTouchListener());
        box6.setOnTouchListener(new MyTouchListener());
        box7.setOnTouchListener(new MyTouchListener());
        box8.setOnTouchListener(new MyTouchListener());
        box9.setOnTouchListener(new MyTouchListener());
        box10.setOnTouchListener(new MyTouchListener());
        box11.setOnTouchListener(new MyTouchListener());
        box12.setOnTouchListener(new MyTouchListener());


        box1.setOnDragListener(new MyDragListener());
        box2.setOnDragListener(new MyDragListener());
        box3.setOnDragListener(new MyDragListener());
        box4.setOnDragListener(new MyDragListener());
        box5.setOnDragListener(new MyDragListener());
        box6.setOnDragListener(new MyDragListener());
        box7.setOnDragListener(new MyDragListener());
        box8.setOnDragListener(new MyDragListener());
        box9.setOnDragListener(new MyDragListener());
        box10.setOnDragListener(new MyDragListener());
        box11.setOnDragListener(new MyDragListener());
        box12.setOnDragListener(new MyDragListener());
        linearLayout = (LinearLayout) view.findViewById(R.id.grid);
        linearLayout.setOnDragListener(new MyDragListener());

        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        timerForProgressBar = new BackgroundTimer(System.currentTimeMillis(), 30000, timeProgressBar, this, Boolean.TRUE);
        tt = new Thread(timerForProgressBar);
        tt.start();
        return view;
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
                    if (v instanceof LinearLayout) {
                        FrameLayout view1 = (FrameLayout) event.getLocalState();
                        view1.setVisibility(View.VISIBLE);
                        break;
                    }
                    if (event.getLocalState() instanceof FrameLayout && v instanceof FrameLayout) {
                        FrameLayout view1 = (FrameLayout) event.getLocalState();
                        //unde
                        FrameLayout view2 = (FrameLayout) v;
                        Log.v("puzzle", view1.toString() + " " + view2.toString());
                        Drawable d1 = view1.getBackground();
                        Drawable d2 = view2.getBackground();
                        view1.setBackground(d2);
                        view1.setVisibility(View.VISIBLE);
                        view2.setBackground(d1);
                        view2.setVisibility(View.VISIBLE);
                    } else {
                        Log.v("puzzle", event.getLocalState().toString());
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {
                        Log.v("tanagram", "The drop was handled.");

                    } else {
                        Log.v("tanagram", "The drop didn't work.");
                    }
                    //check if everything is ok


                    if (box1.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock0_0).getConstantState() &&
                            box2.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock0_1).getConstantState() &&
                            box3.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock0_2).getConstantState() &&
                            box4.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock1_0).getConstantState() &&
                            box5.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock1_1).getConstantState() &&
                            box6.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock1_2).getConstantState() &&
                            box7.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock2_0).getConstantState() &&
                            box8.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock2_1).getConstantState() &&
                            box9.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock2_2).getConstantState() &&
                            box10.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock3_0).getConstantState() &&
                            box11.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock3_1).getConstantState() &&
                            box12.getBackground().getConstantState() == getResources().getDrawable(R.drawable.clock3_2).getConstantState()) {
                        Log.v("tangram", "ok :)");
                        if (t.isAlive()) {
                            myTime = timer.getMyTime();
//                            timer.stopRunning();
//                            t.interrupt();
                            callback.send("mytime:" + String.valueOf(myTime));
                            if (progressDialog == null)
                                progressDialog = ProgressDialog.show(getActivity(), "Waiting for your friend to finish tis level", " Please Wait...", true, true);
                            if (otherPlayerTime != null) {
                                progressDialog.dismiss();
                                if (myTime - otherPlayerTime > 0) {
                                    goToPuzzleLoose();
                                } else if (myTime == otherPlayerTime) {
                                    goToPuzzleWin();
                                } else {
                                    goToPuzzleWin();
                                }
                            }
                        }
                    } else {
                        if (!t.isAlive()) {
                            goToPuzzleLoose();
                        }

                    }


                default:
                    break;
            }
            return true;
        }
    }

    public void startClock() {
        if(settingsDialog.isShowing()){
            settingsDialog.hide();
//            dialogView.setVisibility(View.GONE);
        }
        timeProgressBar.setVisibility(View.VISIBLE);
        timer = new BackgroundTimer(System.currentTimeMillis(), 100000, timeProgressBar, this, Boolean.FALSE);
        t = new Thread(timer);
        t.start();
    }

    public void setMessage(String message) {
        if (message.equals("puzzle hard")) {
            callback.setLastMessageEmpty();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.setVisibility(View.VISIBLE);

                    settingsDialog.show();
                    progressBar.setVisibility(View.GONE);
                    if (tt != null && tt.isAlive()) {
                        timerForProgressBar.stopRunning();
                        tt.interrupt();
                        tt = null;
                    }

            }
            });

        }

        if (message.startsWith("mytime:")) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            String[] l = message.split(":");
            otherPlayerTime = Long.parseLong(l[1]);
            Log.i("puzzle", "am primit timp" + otherPlayerTime);
            if (myTime != null) {
                if (myTime - otherPlayerTime > 0) {
                    goToPuzzleLoose();
                } else {
                    goToPuzzleWin();
                }
            }
        }
    }

    public void getLastMessage() {
        String s = callback.getLastMessage();
        if (!s.equals("")) setMessage(s);
    }

    public void timeWaitingDone() {
        Log.i("ImagePuzzle", "time waiting done");
        tt.interrupt();
        tt = null;
        callback.showToast("Your friend didn't press on the same level");
        LevelsMenu lm = LevelsMenu.newInstance(TRUE);
        getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu, lm)
                .commit();
    }

    public void goToPuzzleWin() {
        MessageWin win = MessageWin.newInstance("", Boolean.TRUE);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu, win, "win")
                .commit();
    }

    public void goToPuzzleLoose() {
        MessageLoose lost = MessageLoose.newInstance("", Boolean.TRUE);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu, lost, "loose")
                .commit();
    }

    public void stopLevel() {
        if (t != null && t.isAlive()) {
            timer.stopRunning();
            t.interrupt();
        }
        if (tt != null && tt.isAlive()) {
            timerForProgressBar.stopRunning();
            tt.interrupt();
            tt = null;
        }
        if (settingsDialog.isShowing()) {
            settingsDialog.dismiss();
        }

    }

    public void timerDone() {
        Log.i("ImageHArdPuzzle", "timerdDOne");
        t.interrupt();
        if (myTime != null) {
            Log.i("ImageHArdPuzzle", myTime.toString());
//            goToPuzzleWin();
        } else {
            goToPuzzleLoose();
            callback.send("myTime:" + timer.getMyTime());
        }
    }

}
