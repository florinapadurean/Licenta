package com.example.padurean.quizzgame.Levels;

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
    private ProgressDialog progressDialog;
    private Thread t;
    private Boolean showPuzzleHard = false;
    private com.example.padurean.quizzgame.Callbacks.GetMessageListener callback;
    private BackgroundTimer timerForProgressBar;
    private Thread tt;


    public ImagePuzzleLvl() {
        super();
    }

    public static ImagePuzzleLvl newInstance(Boolean param, GetMessageListener callback) {
        ImagePuzzleLvl fragment = new ImagePuzzleLvl();
        fragment.showPuzzleHard = param;
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
        Log.v("2", "aici");
        View view = inflater.inflate(R.layout.fragment_imagepuzzle, container, false);

        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        int color = ContextCompat.getColor(getActivity(), R.color.verdeAlbastrui);
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        timeProgressBar = (ProgressBar) view.findViewById(R.id.progressbar1);
        timeProgressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.OVERLAY);
        timeProgressBar.setMax(40);
        myTime = null;
        otherPlayerTime = null;

        View dialogView = inflater.inflate(R.layout.puzzle_dialog, container, false);
        Button ok = (Button) dialogView.findViewById(R.id.okbtn);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("imagepuzzle", "ok pressed");
                settingsDialog.dismiss();
                startClock();
            }
        });

        box1 = (FrameLayout) view.findViewById(R.id.empty_box1);
        box2 = (FrameLayout) view.findViewById(R.id.empty_box2);
        box3 = (FrameLayout) view.findViewById(R.id.empty_box3);
        box4 = (FrameLayout) view.findViewById(R.id.empty_box4);
        box1.setOnTouchListener(new MyTouchListener());
        box2.setOnTouchListener(new MyTouchListener());
        box3.setOnTouchListener(new MyTouchListener());
        box4.setOnTouchListener(new MyTouchListener());
        box1.setOnDragListener(new MyDragListener());
        box2.setOnDragListener(new MyDragListener());
        box3.setOnDragListener(new MyDragListener());
        box4.setOnDragListener(new MyDragListener());
        linearLayout = (LinearLayout) view.findViewById(R.id.grid);
        linearLayout.setOnDragListener(new MyDragListener());
        settingsDialog = new Dialog(getActivity());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(dialogView);
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Log.v("3", "aici");

        timerForProgressBar = new BackgroundTimer(System.currentTimeMillis(), 60000, timeProgressBar, this, Boolean.TRUE);
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
            //v unde faci drop si event.getLocalState() ia viewul care a fost dragguit
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
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
                    //daca ce tragi e pozica si v unde lasi e tot pozica adica frame layout
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

                    if (box1.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn1_1).getConstantState() &&
                            box2.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn2_1).getConstantState() &&
                            box3.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn1_2).getConstantState() &&
                            box4.getBackground().getConstantState() == getResources().getDrawable(R.drawable.unicorn2_2).getConstantState()) {
                        Log.v("tangram", "ok :)");
                        if (t.isAlive()) {
                            myTime = timer.getMyTime();
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
        timeProgressBar.setVisibility(View.VISIBLE);
        timer = new BackgroundTimer(System.currentTimeMillis(), 40000, timeProgressBar, this, Boolean.FALSE);
        t = new Thread(timer);
        t.start();
    }

    public void setMessage(String message) {
        if (message.equals("puzzle")) {
            callback.setLastMessageEmpty();
            Log.v("4", "aici");
            linearLayout.setVisibility(View.VISIBLE);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    settingsDialog.show();
                }
            });

            progressBar.setVisibility(View.GONE);
            if (tt != null && tt.isAlive()) {
                timerForProgressBar.stopRunning();
                tt.interrupt();
                tt = null;
            }
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

    public void goToPuzzleWin() {
        timer.stopRunning();
        t.interrupt();
        MessageWin win;
        if (showPuzzleHard) {
            win = MessageWin.newInstance("", Boolean.TRUE);
        } else {
            win = MessageWin.newInstance("A new level was unlocked, let's see if you win that one", Boolean.TRUE);
//            win = new MessageWin();
        }
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu, win, "win")
                .commit();
    }

    public void goToPuzzleLoose() {
        timer.stopRunning();
        t.interrupt();
        MessageLoose lost;
        if (showPuzzleHard) {
            lost = MessageLoose.newInstance("", Boolean.TRUE);
        } else {
            lost = MessageLoose.newInstance("A new level was unlocked, maybe you will win that one!", Boolean.TRUE);

//            lost=new MessageLoose();
        }
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.frag_menu, lost, "loose")
                .commit();
    }


    public Boolean getShowPuzzleHard() {
        return this.showPuzzleHard;
    }

    public void stopLevel() {
        if (t != null && t.isAlive()) {
            timer.stopRunning();
            t.interrupt();
            t = null;
        }
        if (settingsDialog != null && settingsDialog.isShowing()) {
            settingsDialog.dismiss();
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

    public void timerDone() {
        Log.i("imagepuzzle", "timer done");
        t.interrupt();
        if (myTime != null) {
//            goToPuzzleWin();
        } else {
            goToPuzzleLoose();
            callback.send("mytime:" + timer.getMyTime());
        }
    }

}
