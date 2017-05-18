package com.example.padurean.quizzgame.Errors;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.padurean.quizzgame.R;


public class NoDevices extends Fragment {

    public NoDevices() {
        // Required empty public constructor
    }


    public static NoDevices newInstance() {
        NoDevices fragment = new NoDevices();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("nodevices","aici");
        View v=inflater.inflate(R.layout.fragment_no_devices, container, false);
        ImageButton retrybtn=(ImageButton) v.findViewById(R.id.retry_btn);
        Log.i("nodevices","aici2");
        retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RetryInterface)getActivity()).findFriend();
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface RetryInterface{
        void findFriend();
    }
}
