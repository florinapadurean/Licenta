package com.example.padurean.quizzgame.GameFinishedMessages;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.padurean.quizzgame.R;

/**
 * Created by Asus on 28.05.2017.
 */

public class MessageWin extends Fragment {

    private String text = null;
    private Boolean showHardLvl;

    public MessageWin() {
    }

    public static MessageWin newInstance(String param, boolean showHardLvl) {
        MessageWin messageWin = new MessageWin();
        messageWin.text = param;
        messageWin.showHardLvl = showHardLvl;
        return messageWin;
    }

    public Boolean getShowHardLvl() {
        return showHardLvl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.puzzle_win, container, false);
        if (text != null) {
            TextView additionalText = (TextView) view.findViewById(R.id.additional_text);
            additionalText.setText(text);
        }
        return view;
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }
}
