package com.example.padurean.quizzgame.Levels;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.p2p.WifiP2pGroup;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.padurean.quizzgame.Callbacks.GetMessageListener;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageLoose;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageWin;
import com.example.padurean.quizzgame.Menu.LevelsMenu;
import com.example.padurean.quizzgame.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Boolean.TRUE;

/**
 * Created by Asus on 12.06.2017.
 */

public class BattleshipLvl extends Fragment {

    //1x Aircraft Carrier-5patratele
    //1* Battleship-4 paratratele
    //1*Cruiser-3partatele
    //2*Destroyer-2 patratele
    //2*Submarine-1 partratele
    public Boolean showPuzzleHard = false;
    public ProgressBar progressBar;
    public LinearLayout myGrid;
    public HorizontalScrollView horizontalScroll;
    public LinearLayout otherPlayerGrid;
    private Dialog chooseShipDialog;
    private TextView textChooseShip;
    private Button checkShip;
    private Map<String, List<String>> shipsPosition;
    private Integer counter = 0;
    View dialogView;
    ProgressDialog progressDialog;
    Boolean allShipsSetFriend = false;
    Boolean allShipsSetMe = false;
    Boolean myTurn = false;
    private GetMessageListener callback;
    private BackgroundTimer timerForProgressBar;
    private Thread tt;

    public BattleshipLvl() {

    }

    public static BattleshipLvl newInstance(Boolean param, GetMessageListener callback) {
        BattleshipLvl fragment = new BattleshipLvl();
        fragment.showPuzzleHard = param;
        fragment.callback = callback;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_battleship, container, false);
        myGrid = (LinearLayout) v.findViewById(R.id.myGrid);
        otherPlayerGrid = (LinearLayout) v.findViewById(R.id.enemy_grid);
        otherPlayerGrid.setVisibility(View.GONE);
        myGrid.setVisibility(View.GONE);
        progressBar = (ProgressBar) v.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        int color = ContextCompat.getColor(getActivity(), R.color.verdeAlbastrui);
        progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        checkShip = (Button) v.findViewById(R.id.btnCheckShip);
        checkShip.setOnClickListener(handlerCheckShip);

        horizontalScroll = (HorizontalScrollView) v.findViewById(R.id.horizontal_scroll);
        shipsPosition = new HashMap<String, List<String>>();
        shipsPosition.put("Aircraft Carrier", new ArrayList<String>());
        shipsPosition.put("Battleship", new ArrayList<String>());
        shipsPosition.put("Cruiser", new ArrayList<String>());
        shipsPosition.put("Destroyer", new ArrayList<String>());
        shipsPosition.put("Submarine", new ArrayList<String>());

        List<String> l = new ArrayList<>();
        l.add("A");
        l.add("B");
        l.add("C");
        l.add("D");
        l.add("E");
        l.add("F");
        l.add("G");
        l.add("H");
        l.add("I");
        l.add("J");


        for (String i : l) {
            for (int j = 1; j <= 10; j++) {
                int id = this.getResources().getIdentifier("r" + i + "c" + j, "id", getActivity().getPackageName());
                int idP2 = this.getResources().getIdentifier("P2r" + i + "c" + j, "id", getActivity().getPackageName());
                Log.i("battleship", "r" + i + "c" + j);
                ImageView iv = (ImageView) v.findViewById(id);
                ImageView ivP2 = (ImageView) v.findViewById(idP2);
                iv.setOnClickListener(handlerClickOnYourGrid);
                ivP2.setOnClickListener(handlerClickOnEnemyGrid);

                dialogView = inflater.inflate(R.layout.choose_ship, container, false);
                Button ok = (Button) dialogView.findViewById(R.id.okbtn);
                textChooseShip = (TextView) dialogView.findViewById(R.id.textView);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.v("Battleship", "ok pressed");
                        chooseShipDialog.dismiss();
                    }
                });
                chooseShipDialog = new Dialog(getActivity());
                chooseShipDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                chooseShipDialog.setContentView(dialogView);

            }
        }
        timerForProgressBar = new BackgroundTimer(System.currentTimeMillis(), 30000, null, this, Boolean.TRUE);
        tt = new Thread(timerForProgressBar);
        tt.start();
        return v;
    }

    View.OnClickListener handlerCheckShip = new View.OnClickListener() {
        public void onClick(View v) {
            switch (counter) {
                //1x Aircraft Carrier-5patratele
                case 0: {
                    Boolean ship = checkShipOnePiece(5, "Aircraft Carrier");
                    if (ship) {
                        counter = 1;
                        Log.i("ship", "good");
                        textChooseShip = (TextView) dialogView.findViewById(R.id.textView);
                        textChooseShip.setText("Place one Battleship having size of 4 boxes.");
                        chooseShipDialog.show();
                        break;
                        //next ship dialog;
                    } else {
                        Log.i("ship", "bad");
                        setWater("Aircraft Carrier");
                        Toast.makeText(getActivity(), "Please set Aircraft Carrier horizontally or vertically only,having 5 boxes length,try again!", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                // 1* Battleship-4 paratratele
                case 1: {
                    Boolean ship = checkShipOnePiece(4, "Battleship");
                    if (ship) {
                        counter++;
                        Log.i("ship", "good");
                        textChooseShip = (TextView) dialogView.findViewById(R.id.textView);
                        textChooseShip.setText("Place one Cruiser having size of 3 boxes.");
                        chooseShipDialog.show();
                        //next ship dialog;
                        break;
                    } else {
                        Log.i("ship", "bad");
                        Toast.makeText(getActivity(), "Please set Battleship horizontally or vertically only,having 4 boxes length,try again!", Toast.LENGTH_LONG).show();
                        setWater("Battleship");
                        break;
                    }
                }
                // 1*Cruiser-3partatele
                case 2:
                    Boolean ship = checkShipOnePiece(3, "Cruiser");
                    if (ship) {
                        counter++;
                        Log.i("ship", "good");
                        textChooseShip = (TextView) dialogView.findViewById(R.id.textView);
                        textChooseShip.setText("Place two Destroyers having size of 2 boxes.");
                        chooseShipDialog.show();
                        //next ship dialog;
                        break;
                    } else {
                        Log.i("ship", "bad");
                        Toast.makeText(getActivity(), "Please set Cruiser horizontally or vertically only,having 3 boxes length,try again!", Toast.LENGTH_LONG).show();
                        setWater("Cruiser");
                        break;
                    }
                    //2*Destroyer-2 patratele
                case 3:
                    ship = checkShipTwoPieces(4, "Destroyer");
                    if (ship) {
                        counter++;
                        Log.i("ship", "good");
                        textChooseShip = (TextView) dialogView.findViewById(R.id.textView);
                        textChooseShip.setText("Place two Submarines having size of 2 boxes.");
                        chooseShipDialog.show();
                        //next ship dialog;
                        break;
                    } else {
                        Log.i("ship", "bad");
                        setWater("Destroyer");
                        Toast.makeText(getActivity(), "Please set two Destroyers horizontally or vertically only,having 2 boxes length,try again!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    //2*Submarine-1 partratele
                case 4:
                    List<String> l = shipsPosition.get("Submarine");
                    if (l.size() == 2) {
                        counter = 5;
                        Log.i("ship", "good");
                        checkShip.setVisibility(View.GONE);

                        callback.send("Ships all set");
                        if (!allShipsSetFriend) {
                            progressDialog = ProgressDialog.show(getActivity(), "Waiting for your friend to set his ships ", " Please Wait...", true, true);
                        }
                        allShipsSetMe = true;
                        WifiP2pGroup group = callback.getGroupInfo();
                        if (group.isGroupOwner() && allShipsSetFriend) {
                            callback.send("I start");
                            myTurn = true;
                            horizontalScroll.scrollTo((getView().getWidth()), 0);
                            Toast.makeText(getActivity(), "You start!Hit one box!", Toast.LENGTH_LONG).show();
                        }
                        otherPlayerGrid.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        Log.i("ship", "bad");
                        setWater("Submarine");
                        Toast.makeText(getActivity(), "Please set two Submarines horizontally or vertically only,having 1 boxes length,try again!", Toast.LENGTH_SHORT).show();
                        break;
                    }

            }
        }
    };

    View.OnClickListener handlerClickOnYourGrid = new View.OnClickListener() {
        public void onClick(View v) {
            String txt = v.getTag().toString();
            Log.i("battleship your ships", txt);

            ImageView iv = (ImageView) v.findViewWithTag(v.getTag());
            ColorDrawable buttonColor = (ColorDrawable) iv.getBackground();
            int colorId = buttonColor.getColor();


            //add for aircaft carrier
            if (colorId == getResources().getColor(R.color.com_facebook_messenger_blue) && counter == 0) {
                iv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                List<String> l = shipsPosition.get("Aircraft Carrier");
                l.add(v.getTag().toString());
                shipsPosition.put("Aircraft Carrier", l);
                Log.i("handler", "add");
            }
            //remove for aircaft carrier
            if (colorId == getResources().getColor(R.color.colorAccent) && counter == 0) {
                iv.setBackgroundColor(getResources().getColor(R.color.com_facebook_messenger_blue));
                List<String> l = shipsPosition.get("Aircraft Carrier");
                l.remove(v.getTag().toString());
                shipsPosition.put("Aircraft Carrier", l);
                Log.i("handler", "delete");
            }

            //add for battleship
            if (colorId == getResources().getColor(R.color.com_facebook_messenger_blue) && counter == 1) {
                iv.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                List<String> l = shipsPosition.get("Battleship");
                l.add(v.getTag().toString());
                shipsPosition.put("Battleship", l);
                Log.i("handler", "add");
            }
            //remove for battleship
            if (colorId == getResources().getColor(R.color.cardview_light_background) && counter == 1) {
                iv.setBackgroundColor(getResources().getColor(R.color.com_facebook_messenger_blue));
                List<String> l = shipsPosition.get("Battleship");
                l.remove(v.getTag().toString());
                shipsPosition.put("Battleship", l);
                Log.i("handler", "delete");
            }

            //add for cruiser
            if (colorId == getResources().getColor(R.color.com_facebook_messenger_blue) && counter == 2) {
                iv.setBackgroundColor(getResources().getColor(R.color.rozut));
                List<String> l = shipsPosition.get("Cruiser");
                l.add(v.getTag().toString());
                shipsPosition.put("Cruiser", l);
                Log.i("handler", "add");
            }
            //remove for criuser
            if (colorId == getResources().getColor(R.color.rozut) && counter == 2) {
                iv.setBackgroundColor(getResources().getColor(R.color.com_facebook_messenger_blue));
                List<String> l = shipsPosition.get("Cruiser");
                l.remove(v.getTag().toString());
                shipsPosition.put("Cruiser", l);
                Log.i("handler", "delete");
            }

            //add for destroyer
            if (colorId == getResources().getColor(R.color.com_facebook_messenger_blue) && counter == 3) {
                iv.setBackgroundColor(Color.GREEN);
                List<String> l = shipsPosition.get("Destroyer");
                l.add(v.getTag().toString());
                shipsPosition.put("Destroyer", l);
                Log.i("handler", "add");
            }
            //remove for destroyer
            if (colorId == Color.GREEN && counter == 3) {
                iv.setBackgroundColor(getResources().getColor(R.color.com_facebook_messenger_blue));
                List<String> l = shipsPosition.get("Destroyer");
                l.remove(v.getTag().toString());
                shipsPosition.put("Destroyer", l);
                Log.i("handler", "delete");
            }

            //add for submarine
            if (colorId == getResources().getColor(R.color.com_facebook_messenger_blue) && counter == 4) {
                iv.setBackgroundColor(Color.YELLOW);
                List<String> l = shipsPosition.get("Submarine");
                l.add(v.getTag().toString());
                shipsPosition.put("Submarine", l);
                Log.i("handler", "add");
            }
            //remove for submarine
            if (colorId == Color.YELLOW && counter == 4) {
                iv.setBackgroundColor(getResources().getColor(R.color.com_facebook_messenger_blue));
                List<String> l = shipsPosition.get("Submarine");
                l.remove(v.getTag().toString());
                shipsPosition.put("Submarine", l);
                Log.i("handler", "delete");
            }

        }
    };

    View.OnClickListener handlerClickOnEnemyGrid = new View.OnClickListener() {
        public void onClick(View v) {
            String txt = v.getTag().toString();
            Log.i("battleship enemy ships", txt);
            if (myTurn) {
                callback.send("hit:" + txt);
            }

        }
    };


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

    public Boolean checkShipOnePiece(Integer n, String type) {
        List<String> l = shipsPosition.get(type);
        Boolean goodShip = true;
        if (l.size() != n) return false;
        List<String> listToCheck = new ArrayList<>();
        String elem = "";
        for (String pos : l) {
            Log.i("pos", pos + " " + pos.split("c")[1]);
            elem = pos.split("c")[1];
            listToCheck.add(elem);
        }
        //if elems on the same column
        for (String s : listToCheck) {
            if (!s.equals(elem)) {
                goodShip = false;
            }
        }
        if (!goodShip) {
            goodShip = true;
            listToCheck.clear();
            for (String pos : l) {
                Log.i("pos", pos + " " + pos.split("r")[1]);
                elem = pos.split("r")[1].split("c")[0];
                listToCheck.add(elem);
            }
            //if elems on the same column
            for (String s : listToCheck) {
                if (!s.equals(elem)) {
                    goodShip = false;
                }
            }
        }

        return goodShip;
    }


    public Boolean checkShipTwoPieces(Integer n, String type) {
        List<String> l = shipsPosition.get(type);
        Boolean goodShip = true;
        Integer shipsFound = 0;
        if (l.size() != n) return false;
        List<String> listToCheck = new ArrayList<>();
        String elem = "";
        for (String pos : l) {
            Log.i("pos", pos + " " + pos.split("c")[1]);
            elem = pos.split("c")[1];
            listToCheck.add(elem);
        }
        //if elems on the same column
        if (listToCheck.get(0).equals(listToCheck.get(1)) || listToCheck.get(2).equals(listToCheck.get(3)) ||
                listToCheck.get(0).equals(listToCheck.get(2)) || listToCheck.get(1).equals(listToCheck.get(3)) ||
                listToCheck.get(0).equals(listToCheck.get(3)) || listToCheck.get(1).equals(listToCheck.get(2)))
            shipsFound++;
        Log.i("shipsFound", shipsFound.toString());
        if (shipsFound == 2) {
            return true;
        } else {
            for (String pos : l) {
                Log.i("pos", pos + " " + pos.split("r")[1].split("c")[0]);
                elem = pos.split("r")[1].split("c")[0];
                listToCheck.add(elem);
            }
            if (listToCheck.get(0).equals(listToCheck.get(1)) || listToCheck.get(2).equals(listToCheck.get(3)) ||
                    listToCheck.get(0).equals(listToCheck.get(2)) || listToCheck.get(1).equals(listToCheck.get(3)) ||
                    listToCheck.get(0).equals(listToCheck.get(3)) || listToCheck.get(1).equals(listToCheck.get(2)))
                shipsFound++;
            Log.i("shipsFound", shipsFound.toString());
            if (shipsFound == 2) {
                return true;
            } else {
                return false;
            }

        }
    }

    public void setWater(String shipType) {
        List<String> l = shipsPosition.get(shipType);
        for (String position : l) {
            ImageView iv = (ImageView) getView().findViewWithTag(position);
            iv.setBackgroundColor(getResources().getColor(R.color.com_facebook_messenger_blue));
        }
        shipsPosition.put(shipType, new ArrayList<String>());

    }

    public void setMessage(String message) {
        if (message.equals("battleship")) {
            Log.v("battleship", "aici");
            callback.setLastMessageEmpty();
//            linearLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            myGrid.setVisibility(View.VISIBLE);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chooseShipDialog.show();
                }
            });
            if (tt != null && tt.isAlive()) {
                timerForProgressBar.stopRunning();
                tt.interrupt();
                tt = null;
            }
        }

        if (message.startsWith("hit:")) {
            Log.i("hit", message);
            String[] l = message.split(":");
            String tagP2 = l[1];
            Log.i("tagP2", tagP2);
            String tag = tagP2.substring(tagP2.indexOf("2") + 1);
            Log.i("tag", tag);
            String s = checkIfShipOrWater(tag);
            if (s.equals("water")) callback.send("water:" + tag);
            if (s.equals("ship")) callback.send("ship:" + tag);
            if (checkIfAllBoatsAreHit()) {
                callback.send("You won");
                MessageLoose lost = MessageLoose.newInstance("", this.showPuzzleHard);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu, lost, "loose")
                        .commit();
            }
            horizontalScroll.scrollTo(0, 0);
        }

        if (message.equals("You won")) {
            MessageWin win;
            win = MessageWin.newInstance("", this.showPuzzleHard);
            getActivity().getFragmentManager().beginTransaction()
                    .replace(R.id.frag_menu, win, "win")
                    .commit();
        }

        if (message.startsWith("water:")) {
            String[] l = message.split(":");
            String tag = l[1];
            ImageView iv = (ImageView) getView().findViewWithTag("P2" + tag);
            iv.setImageDrawable(getResources().getDrawable(R.drawable.roundedx));
            myTurn = false;
            callback.send("Your turn");

        }

        if (message.equals("Your turn")) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    myTurn = true;
                    horizontalScroll.scrollTo((getView().getWidth()), 0);
//            Toast.makeText(getActivity(),"Your turn!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getView(), "Your turn!", Snackbar.LENGTH_SHORT).show();
                }
            }, 3000);

        }

        if (message.startsWith("ship:")) {
            String[] l = message.split(":");
            String tag = l[1];
            ImageView iv = (ImageView) getView().findViewWithTag("P2" + tag);
            iv.setBackgroundColor(Color.RED);
            myTurn = false;
            callback.send("Your turn");
        }

        if (message.equals("Ships all set")) {
            allShipsSetFriend = true;
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            WifiP2pGroup group = callback.getGroupInfo();
            if (group.isGroupOwner() && allShipsSetMe) {
                callback.send("I start");
                myTurn = true;
                horizontalScroll.scrollTo((getView().getWidth()), 0);
                Toast.makeText(getActivity(), "You start!Hit one box!", Toast.LENGTH_LONG).show();
            }

        }
    }

    public void getLastMessage() {
        String s = callback.getLastMessage();
        if (!s.equals("")) setMessage(s);
    }

    public void timeWaitingDone() {
        Log.i("Battleship", "time waiting done");
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

    private String checkIfShipOrWater(String tag) {
        List<String> l = shipsPosition.get("Aircraft Carrier");
        if (l.contains(tag)) {
            l.remove(tag);
            ImageView iv = (ImageView) getView().findViewWithTag(tag);
            iv.setBackgroundColor(Color.RED);
            shipsPosition.put("Aircraft Carrier", l);
            return "ship";
        }
        l = shipsPosition.get("Battleship");
        if (l.contains(tag)) {
            l.remove(tag);
            ImageView iv = (ImageView) getView().findViewWithTag(tag);
            iv.setBackgroundColor(Color.RED);
            shipsPosition.put("Battleship", l);
            return "ship";
        }
        l = shipsPosition.get("Cruiser");
        if (l.contains(tag)) {
            l.remove(tag);
            ImageView iv = (ImageView) getView().findViewWithTag(tag);
            iv.setBackgroundColor(Color.RED);
            shipsPosition.put("Cruiser", l);
            return "ship";
        }
        l = shipsPosition.get("Destroyer");
        if (l.contains(tag)) {
            l.remove(tag);
            ImageView iv = (ImageView) getView().findViewWithTag(tag);
            iv.setBackgroundColor(Color.RED);
            shipsPosition.put("Destroyer", l);
            return "ship";
        }
        l = shipsPosition.get("Submarine");
        if (l.contains(tag)) {
            l.remove(tag);
            ImageView iv = (ImageView) getView().findViewWithTag(tag);
            iv.setBackgroundColor(Color.RED);
            shipsPosition.put("Submarine", l);
            return "ship";
        }

        ImageView iv = (ImageView) getView().findViewWithTag(tag);
        iv.setImageDrawable(getResources().getDrawable(R.drawable.roundedx));
        return "water";

    }

    public Boolean checkIfAllBoatsAreHit() {
        List<String> l = shipsPosition.get("Aircraft Carrier");
        List<String> l1 = shipsPosition.get("Battleship");
        List<String> l2 = shipsPosition.get("Cruiser");
        List<String> l3 = shipsPosition.get("Destroyer");
        List<String> l4 = shipsPosition.get("Submarine");

        if (l.size() == 0 && l1.size() == 0 && l2.size() == 0 && l3.size() == 0 && l4.size() == 0) {
            return true;
        } else return false;
    }

    public void stopLevel() {
        if (chooseShipDialog != null && chooseShipDialog.isShowing()) {
            chooseShipDialog.dismiss();
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (tt != null && tt.isAlive()) {
            timerForProgressBar.stopRunning();
            tt.interrupt();
            tt = null;
        }

    }

    public Boolean getShowPuzzleHard() {
        return this.showPuzzleHard;
    }


}
