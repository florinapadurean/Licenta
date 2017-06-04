package com.example.padurean.quizzgame.GameFinishedMessages;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.padurean.quizzgame.R;

/**
 * Created by Asus on 28.05.2017.
 */

public class ImagePuzzleLoose extends Fragment{

    public ImagePuzzleLoose() {
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
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
