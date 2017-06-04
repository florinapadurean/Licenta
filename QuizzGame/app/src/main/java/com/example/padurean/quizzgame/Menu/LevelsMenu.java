package com.example.padurean.quizzgame.Menu;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.padurean.quizzgame.Levels.ImagePuzzleHardLvl;
import com.example.padurean.quizzgame.Levels.KnowledgeLvl;
import com.example.padurean.quizzgame.Levels.ImagePuzzleLvl;
import com.example.padurean.quizzgame.R;


public class LevelsMenu extends Fragment {

    private LinearLayout generalKnowledge;
    private LinearLayout puzzle;
    private LinearLayout puzzleHard;
    private KnowledgeLvl knowledgeLvl;
    private ImagePuzzleLvl imagePuzzleLvl;
    private ImagePuzzleHardLvl imagePuzzleHardLvl;


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
        imagePuzzleLvl =new ImagePuzzleLvl();
        imagePuzzleHardLvl=new ImagePuzzleHardLvl();
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.v("lvls","aici");
        final View view=inflater.inflate(R.layout.fragment_menu, container, false);
        generalKnowledge=(LinearLayout)view.findViewById(R.id.general_knowledge);
        puzzle=(LinearLayout)view.findViewById(R.id.puzzle);
        puzzleHard=(LinearLayout)view.findViewById(R.id.puzzle_hard);
//        btnGeneralKnowledge=(Button) view.findViewById(R.id.general_knowledge);
        generalKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LevelPressedListener) getActivity()).send("knowledge");
//                LinearLayout container=(LinearLayout) view.findViewById(R.id.menu_container);
//                container.setVisibility(View.GONE);
                HorizontalScrollView horizontalScrollView=(HorizontalScrollView) view.findViewById(R.id.horizontal_menu_scroll);
                horizontalScrollView.setVisibility(View.GONE);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,knowledgeLvl,"knowledgelvlfragment")
                        .addToBackStack(null)
                        .commit();

            }

        });
        puzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LevelPressedListener) getActivity()).send("puzzle");
                HorizontalScrollView horizontalScrollView=(HorizontalScrollView) view.findViewById(R.id.horizontal_menu_scroll);
                horizontalScrollView.setVisibility(View.GONE);
                Log.v("1","aici");
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu, imagePuzzleLvl,"puzzlelvlfragment")
                        .addToBackStack(null)
                        .commit();
//                getActivity().getFragmentManager().beginTransaction()
//                        .addToBackStack(null)
//                        .add(R.id.frag_menu,imagePuzzleLvl,"puzzlelvlfragment")
//                        .commit();

//                ImagePuzzleLvl puzzlelvl=(ImagePuzzleLvl)getActivity().getFragmentManager().findFragmentByTag("puzzlelvlfragment");

            }

        });


        puzzleHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LevelPressedListener) getActivity()).send("puzzle hard");
                HorizontalScrollView horizontalScrollView=(HorizontalScrollView) view.findViewById(R.id.horizontal_menu_scroll);
                horizontalScrollView.setVisibility(View.GONE);

                getActivity().getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frag_menu, imagePuzzleHardLvl,"puzzlehardlvlfragment")
                        .commit();
            }

        });


        return view;

    }

    @Override
    public void onStart() {
        puzzleHard.setVisibility(View.VISIBLE);
        Log.v("lvls","aici");
        super.onStart();
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
