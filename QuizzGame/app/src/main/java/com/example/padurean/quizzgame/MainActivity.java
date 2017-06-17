package com.example.padurean.quizzgame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.padurean.quizzgame.Callbacks.GetMessageListener;
import com.example.padurean.quizzgame.Callbacks.SocketCallback;
import com.example.padurean.quizzgame.Communication.ClientSocket;
import com.example.padurean.quizzgame.Communication.MyServerSocket;
import com.example.padurean.quizzgame.Errors.NoDevices;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageLoose;
import com.example.padurean.quizzgame.GameFinishedMessages.MessageWin;
import com.example.padurean.quizzgame.Levels.BattleshipLvl;
import com.example.padurean.quizzgame.Levels.ImagePuzzleHardLvl;
import com.example.padurean.quizzgame.Levels.ImagePuzzleLvl;
import com.example.padurean.quizzgame.Levels.KnowledgeLvl;
import com.example.padurean.quizzgame.Menu.LevelsMenu;
import com.example.padurean.quizzgame.Login.Login;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.example.padurean.quizzgame.P2PConnection.DeviceList;
import com.example.padurean.quizzgame.P2PConnection.WifiDirectBroadcastReceiver;
import com.example.padurean.quizzgame.Menu.StartGameMenu.StartMenuListener;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements SocketCallback, GetMessageListener,LevelsMenu.LevelPressedListener,NoDevices.RetryInterface,WifiP2pManager.GroupInfoListener, DeviceList.DeviceActionListener,StartMenuListener,WifiP2pManager.ChannelListener{

    private WifiP2pManager mManager;
    private Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private Boolean wifiEnabled=FALSE;
    static String TAG="MAin";
    private Thread t;

    private boolean groupFlag=false;
    private WifiP2pGroup groupInfo;
    private String messageAsString;
    private MyServerSocket serverSocket=null;
    private ClientSocket clientSocket=null;

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.wifiEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disconnect();
        ActionBar toolbar=getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setDisplayUseLogoEnabled(true);
        toolbar.setLogo(R.drawable.smart);

        // add necessary intent values to be matched.
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);
        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fm.findFragmentById(R.id.frag_list));
            ft.show(fm.findFragmentById(R.id.frag_start));
            ft.hide(fm.findFragmentById(R.id.frag_menu));
            ft.hide(fm.findFragmentById(R.id.frag_err_no_devices));
            ft.commit();
        }
    }

    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        Log.i(TAG,"onResume()");
        super.onResume();

    }

    @Override
    public void onPause() {
        Log.i(TAG,"onPause()");
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_items, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out:
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    FirebaseAuth.getInstance().signOut();
                }
                else{
                    LoginManager.getInstance().logOut();
                }
                send("end connection");
                disconnect();
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void findFriend(){

        if (!wifiEnabled) {
            WifiP2pManager manager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);
            Channel channel = manager.initialize(this,this.getMainLooper(), null);

            try {
                Method method1 = manager.getClass().getMethod("enableP2p", Channel.class);
                method1.invoke(manager, channel);
            } catch (Exception e) {
               Log.d(TAG, "method did not found");
            }
        }
        discoverPeers();
    }


    private void discoverPeers() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fm.findFragmentById(R.id.frag_list));
        ft.hide(fm.findFragmentById(R.id.frag_start));
        ft.hide(fm.findFragmentById(R.id.frag_menu));
        ft.hide(fm.findFragmentById(R.id.frag_err_no_devices));
        ft.commit();

        final DeviceList fragment = (DeviceList) getFragmentManager().findFragmentById(R.id.frag_list);
        fragment.onInitiateDiscovery();
        fragment.onInitiateDiscovery();
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Discovery Initiated");
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Log.d(TAG, "Discovery Failed : " + reasonCode);
                    }
                });

    }


    @Override
    public void connect(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                // WiFiDirectBroadcastReceiver will notify us
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(MainActivity.this, "Connect failed. Retry.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void disconnect() {
        try{
            getFragmentManager().beginTransaction()
                    .hide(getFragmentManager().findFragmentById(R.id.frag_menu))
                    .show(getFragmentManager().findFragmentById(R.id.frag_start))
                    .commit();
            mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

                @Override
                public void onFailure(int reasonCode) {
                    Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG,"dissconecteddddddd");
                }
            });
        }
        catch (Exception e){
            Log.e(TAG,e.toString());
        }
    }

    @Override
    public void onChannelDisconnected() {
        // we will try once more
        if (mManager != null ) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            mManager.initialize(this, getMainLooper(), this);
        } else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void stopPeerDiscovery(){
        mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Peer search stopped");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Peer stopped doesn't work Reason :" + reason);
            }
        });

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fm.findFragmentById(R.id.frag_list));
        ft.hide(fm.findFragmentById(R.id.frag_start));
        ft.hide(fm.findFragmentById(R.id.frag_menu));
        ft.show(fm.findFragmentById(R.id.frag_err_no_devices));
        ft.commit();
    }

    @Override
    protected void onStart() {
        Log.i(TAG,"onstart()");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.i(TAG,"onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG,"onDestroy()");
        unregisterReceiver(mReceiver);
        if (mManager != null && mChannel != null) {
            mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onFailure(int reasonCode) {
                    Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "Disconnected");
                }

            });
        }
        try {
            Method method1 = mManager.getClass().getMethod("disableP2p", Channel.class);
            method1.invoke(mManager, mChannel);
        } catch (Exception e) {
            Log.i(TAG,"method did not found");
        }
        super.onDestroy();
    }


    @Override
    public void showMenu(WifiP2pInfo info) {
       if(info.isGroupOwner){
           Log.i(TAG,"server start");
           serverSocket=new MyServerSocket(this);
           serverSocket.start();
       }
        else{
           Log.i(TAG,"client start");
           clientSocket=new ClientSocket(this,info.groupOwnerAddress);
           clientSocket.start();
       }

        this.groupFlag=true;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(fm.findFragmentById(R.id.frag_list));
        ft.hide(fm.findFragmentById(R.id.frag_start));
        ft.show(fm.findFragmentById(R.id.frag_menu));
        ft.hide(fm.findFragmentById(R.id.frag_err_no_devices));
        ft.commit();
    }
    @Override
    public void onBackPressed() {
        if(getFragmentManager().findFragmentById(R.id.frag_menu).isVisible()){
            Fragment fragment_menu=getFragmentManager().findFragmentById(R.id.frag_menu);
            if(fragment_menu instanceof MessageLoose){
                LevelsMenu lm=LevelsMenu.newInstance(TRUE);
//                LevelsMenu lm=new LevelsMenu();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,lm)
                        .commit();
            }
            if(fragment_menu instanceof MessageWin){
                LevelsMenu lm=LevelsMenu.newInstance(TRUE);
//                LevelsMenu lm=new LevelsMenu();
                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,lm)
                        .commit();
            }

            if(fragment_menu instanceof KnowledgeLvl){
                KnowledgeLvl kl=(KnowledgeLvl)fragment_menu;
                LevelsMenu lm;
                if(kl.getShowPuzzleHard()){
                    lm=LevelsMenu.newInstance(TRUE);
                }
                else{
                    lm=new LevelsMenu();
                }

                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,lm)
                        .commit();
            }
            if(fragment_menu instanceof ImagePuzzleLvl){
                ImagePuzzleLvl plvl=(ImagePuzzleLvl)fragment_menu;
                LevelsMenu lm;
                if(plvl.getShowPuzzleHard()){
                    lm=LevelsMenu.newInstance(TRUE);
                    send("I exited ImagePuzzleLvl");
                }else{
                    lm=new LevelsMenu();
                    send("I exited ImagePuzzleLvl");
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,lm)
                        .commit();
            }
            if(fragment_menu instanceof ImagePuzzleHardLvl){
                LevelsMenu lm=LevelsMenu.newInstance(TRUE);
                send("I exited ImagePuzzleHardLvl");
                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,lm)
                        .commit();
            }

            if(fragment_menu instanceof BattleshipLvl){
                BattleshipLvl kl=(BattleshipLvl) fragment_menu;
                LevelsMenu lm;
                if(kl.getShowPuzzleHard()){
                    lm=LevelsMenu.newInstance(TRUE);
                }
                else{
                    lm=new LevelsMenu();
                }
                send("I exited BattleshipLvl");
                getFragmentManager().beginTransaction()
                        .replace(R.id.frag_menu,lm)
                        .commit();
            }
            if(fragment_menu instanceof LevelsMenu){
                send("end connection");
                disconnect();
            }
        }
        if(getFragmentManager().findFragmentById(R.id.frag_start).isVisible() ||
                getFragmentManager().findFragmentById(R.id.frag_list).isVisible()){
            super.onBackPressed();
        }
    }

    public void showToastDisconnected(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Sorry,someone disconnected!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onGroupInfoAvailable(WifiP2pGroup group) {
        this.groupInfo=group;
    }


    public WifiP2pGroup getGroupInfo(){
        return groupInfo;
    }


    public void send(String string) {
        if(serverSocket!=null){
            serverSocket.send(string);
        }
        if(clientSocket!=null){
            clientSocket.send(string);
        }

    }

    public void recieveMessage(final String messageAsString){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Boolean msgNotSent=true;

                //the other player exited imgpuzzlelvl
                Fragment fragment_menu=getFragmentManager().findFragmentById(R.id.frag_menu);
                Log.i(TAG,messageAsString);
                if(messageAsString.equals("I exited ImagePuzzleLvl") && fragment_menu.isVisible() && fragment_menu instanceof  ImagePuzzleLvl){
                    Log.i(TAG,"recieved I exited ImagePuzzleLvl");
                    ImagePuzzleLvl plvl=(ImagePuzzleLvl)fragment_menu;
                    LevelsMenu lm;
                    if(plvl.getShowPuzzleHard()){
                        lm=LevelsMenu.newInstance(TRUE);
                    }else{
                        lm=new LevelsMenu();
                    }
                    plvl.stopLevel();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frag_menu,lm)
                            .commit();
                    Toast.makeText(MainActivity.this, "Sorry,your friend exited Image Puzzle Level", Toast.LENGTH_SHORT).show();
                }
                if(messageAsString.equals("I exited ImagePuzzleHardLvl") && fragment_menu.isVisible() && fragment_menu instanceof  ImagePuzzleLvl){
                    Log.i(TAG,"recieved I exited ImagePuzzleHardLvl");
                    ImagePuzzleHardLvl phlvl=(ImagePuzzleHardLvl)fragment_menu;
                    LevelsMenu lm=LevelsMenu.newInstance(TRUE);
                    phlvl.stopLevel();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frag_menu,lm)
                            .commit();
                    Toast.makeText(MainActivity.this, "Sorry,your friend exited Image Puzzle Hard Level", Toast.LENGTH_SHORT).show();
                }
                if(messageAsString.equals( "I exited BattleshipLvl") && fragment_menu.isVisible() && fragment_menu instanceof  BattleshipLvl){
                    Log.i(TAG,"recieved I exited BattleshipLvl");
                    BattleshipLvl bLvl=(BattleshipLvl) fragment_menu;
                    LevelsMenu lm=LevelsMenu.newInstance(TRUE);
                    bLvl.stopLevel();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.frag_menu,lm)
                            .commit();
                    Toast.makeText(MainActivity.this, "Sorry,your friend exited Battleship Level", Toast.LENGTH_SHORT).show();
                }

                if(messageAsString.equals("I exited ImagePuzzleLvl") || messageAsString.equals("I exited ImagePuzzleHardLvl") || messageAsString.equals("I exited BattleshipLvl")){
                    msgNotSent=false;
                }else{
                    //strat game message
                    KnowledgeLvl f=(KnowledgeLvl)getFragmentManager().findFragmentByTag("knowledgelvlfragment");
                    ImagePuzzleLvl f1=(ImagePuzzleLvl)getFragmentManager().findFragmentByTag("puzzlelvlfragment");
                    ImagePuzzleHardLvl f2=(ImagePuzzleHardLvl) getFragmentManager().findFragmentByTag("puzzlehardlvlfragment");
                    BattleshipLvl f3=(BattleshipLvl) getFragmentManager().findFragmentByTag("battleshipfragment");

                    //wait
                    if (f != null && f.isVisible()) {
                        Log.i("knowledge", "setMessage:" + messageAsString);
                        f.setMessage(messageAsString);
                        msgNotSent = false;
                    }
                    if (f1 != null && f1.isVisible()) {
                        Log.i("puzzle", "setMessage:" + messageAsString);
                        f1.setMessage(messageAsString);
                        msgNotSent = false;
                    }
                    if (f2 != null && f2.isVisible()) {
                        Log.i("puzzleHArd", "setMessage:" + messageAsString);
                        f2.setMessage(messageAsString);
                        msgNotSent = false;
                    }

                    if (f3 != null && f3.isVisible()) {
                        Log.i("battleship", "setMessage:" + messageAsString);
                        f3.setMessage(messageAsString);
                        msgNotSent = false;
                    }
                }



                if(msgNotSent){
                    Runnable r=new Runnable() {
                        @Override
                        public void run() {
                            Boolean msgNotSent=true;
                            int counter=50;
                            //wait a few seconds
                            while (counter>0){
                                counter--;
                            }
                            recieveMessage(messageAsString);
                        }
                    };
                    t=new Thread(r);
                    t.start();

                }

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_RESOLVE_ERROR) {
//            if (resultCode == RESULT_OK) {
//                mGoogleApiClient.connect();
//            } else {
//                Log.e(TAG, "GoogleApiClient connection failed. Unable to resolve.");
//            }
//        } else {
            super.onActivityResult(requestCode, resultCode, data);
//        }
    }


}

