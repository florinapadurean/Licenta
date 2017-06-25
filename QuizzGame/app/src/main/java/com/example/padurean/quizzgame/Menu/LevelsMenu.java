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

import com.example.padurean.quizzgame.Callbacks.GetMessageListener;
import com.example.padurean.quizzgame.Levels.BattleshipLvl;
import com.example.padurean.quizzgame.Levels.ImagePuzzleHardLvl;
import com.example.padurean.quizzgame.Levels.KnowledgeLvl;
import com.example.padurean.quizzgame.Levels.ImagePuzzleLvl;
import com.example.padurean.quizzgame.R;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class LevelsMenu extends Fragment {

    private LinearLayout generalKnowledge;
    private LinearLayout puzzle;
    private LinearLayout puzzleHard;
    private LinearLayout spacePuzzleHard;
    private LinearLayout battleship;
    private BattleshipLvl battleshipLvl;
    private KnowledgeLvl knowledgeLvl;
    private ImagePuzzleLvl imagePuzzleLvl;
    private ImagePuzzleHardLvl imagePuzzleHardLvl;
    private Boolean showPuzzleHard=false;


    public LevelsMenu() {
        // Required empty public constructor
    }

    public static LevelsMenu newInstance(Boolean param) {
        LevelsMenu fragment = new LevelsMenu();
        fragment.showPuzzleHard=param;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_menu, container, false);
        generalKnowledge=(LinearLayout)view.findViewById(R.id.general_knowledge);
        puzzle=(LinearLayout)view.findViewById(R.id.puzzle);
        puzzleHard=(LinearLayout)view.findViewById(R.id.puzzle_hard);
        spacePuzzleHard=(LinearLayout)view.findViewById(R.id.space_puzzle_hard);
        battleship=(LinearLayout)view.findViewById(R.id.battleship);

        if(showPuzzleHard!=null && showPuzzleHard== TRUE){
            puzzleHard.setVisibility(View.VISIBLE);
            spacePuzzleHard.setVisibility(View.VISIBLE);
            knowledgeLvl=KnowledgeLvl.newInstance(TRUE,(GetMessageListener) getActivity());
            imagePuzzleLvl =ImagePuzzleLvl.newInstance(TRUE,(GetMessageListener) getActivity());
            battleshipLvl=BattleshipLvl.newInstance(TRUE,(GetMessageListener) getActivity());
            imagePuzzleHardLvl=ImagePuzzleHardLvl.newInstance((GetMessageListener) getActivity());
        }
        else{
            knowledgeLvl=KnowledgeLvl.newInstance(FALSE,(GetMessageListener) getActivity());
            imagePuzzleLvl =ImagePuzzleLvl.newInstance(FALSE,(GetMessageListener) getActivity());
            battleshipLvl=BattleshipLvl.newInstance(FALSE,(GetMessageListener) getActivity());
            imagePuzzleHardLvl=ImagePuzzleHardLvl.newInstance((GetMessageListener) getActivity());
        }

        generalKnowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LevelPressedListener) getActivity()).send("knowledge");
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

        battleship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LevelPressedListener) getActivity()).send("battleship");
                HorizontalScrollView horizontalScrollView=(HorizontalScrollView) view.findViewById(R.id.horizontal_menu_scroll);
                horizontalScrollView.setVisibility(View.GONE);

                getActivity().getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frag_menu,battleshipLvl,"battleshipfragment")
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
