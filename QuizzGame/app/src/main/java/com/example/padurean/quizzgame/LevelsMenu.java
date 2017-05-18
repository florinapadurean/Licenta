package com.example.padurean.quizzgame;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.padurean.quizzgame.Levels.KnowledgeLvl;


public class LevelsMenu extends Fragment {
    private Button btnGeneralKnowledge;
    private KnowledgeLvl knowledgeLvl;


//    private OnFragmentInteractionListener mListener;

    public LevelsMenu() {
        // Required empty public constructor
    }

    public static LevelsMenu newInstance(String param1, String param2) {
        LevelsMenu fragment = new LevelsMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        knowledgeLvl=new KnowledgeLvl();
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_menu, container, false);
        btnGeneralKnowledge=(Button) view.findViewById(R.id.general_knowledge);
        btnGeneralKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LevelPressedListener) getActivity()).send("knowledge");
                LinearLayout container=(LinearLayout) view.findViewById(R.id.menu_container);
                container.setVisibility(View.GONE);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,knowledgeLvl,"knowledgelvlfragment")
                        .addToBackStack(null)
                        .commit();

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

    public interface LevelPressedListener{
        void send(String string);
    }

}
