package com.example.padurean.quizzgame.GameFinishedMessages;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.padurean.quizzgame.R;

/**
 * Created by Asus on 28.05.2017.
 */

public class MessageLoose extends Fragment{

    private String text=null;
    private Boolean showHardLvl;

    public Boolean getShowHardLvl() {
        return showHardLvl;
    }

    public MessageLoose() {
    }

    public static MessageLoose newInstance(String param,Boolean showHardLvl){
        MessageLoose imagePuzzleWin= new MessageLoose();
        imagePuzzleWin.text=param;
        imagePuzzleWin.showHardLvl=showHardLvl;
        return imagePuzzleWin;
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
        View view=inflater.inflate(R.layout.puzzle_loose, container, false);
        if(text!=null){
            TextView additionalText=(TextView) view.findViewById(R.id.additional_text);
            additionalText.setText(text);
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
