package com.example.padurean.quizzgame.Levels;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ProgressBar;

import com.example.padurean.quizzgame.R;

/**
 * Created by Asus on 12.06.2017.
 */

public class BattleshipLvl extends Fragment {

    public Boolean showPuzzleHard;
    public Long myTime;
    public Long otherPlayerTime;
    public ProgressBar progressBar;
    public GridLayout myGrid;
    public GridLayout otherPlayerGrid;

    public BattleshipLvl(){

    }

    public static BattleshipLvl newInstance(Boolean param) {
        BattleshipLvl fragment = new BattleshipLvl();
        fragment.showPuzzleHard=param;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_battleship, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        myGrid=(GridLayout) v.findViewById(R.id.myGrid);
        otherPlayerGrid=(GridLayout) v.findViewById(R.id.player2Grid);
        View.OnClickListener handler =new View.OnClickListener() {
            public void onClick(View v) {
                String txt=v.getTag().toString();
                Log.i("battleship",txt);
            }};
        myGrid.setOnClickListener(handler);
        otherPlayerGrid.setOnClickListener(handler);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void setMessage(String message) {
        if (message.equals("battleship")) {
            Log.v("battleship","aici");
//            linearLayout.setVisibility(View.VISIBLE);
//            settingsDialog.show();
            progressBar.setVisibility(View.GONE);
        }

        if (message.startsWith("mytime:")) {
            String[] l = message.split(":");
            otherPlayerTime = Long.parseLong(l[1]);
            Log.i("puzzle","am primit timp"+otherPlayerTime);
            if (myTime!=null) {
                if (myTime - otherPlayerTime > 0) {
//                    goToPuzzleLoose();
                } else {
//                    goToPuzzleWin();
                }
            }

        }
    }
}
