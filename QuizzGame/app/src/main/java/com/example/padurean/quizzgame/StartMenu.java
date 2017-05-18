package com.example.padurean.quizzgame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class StartMenu extends Fragment {

    Button startDiscovery;

    public StartMenu() {
    }


    public static StartMenu newInstance() {
        StartMenu fragment = new StartMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_start_menu, container, false);
        startDiscovery=(Button) view.findViewById(R.id.discovery);
        startDiscovery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((StartMenuListener)getActivity()).findFriend();
            }
        });
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

    @Override
    public void onStop() {
        super.onStop();
        ((StartMenuListener)getActivity()).onStop();
    }

    public interface StartMenuListener{
        void findFriend();
        void onStop();
    }
}
