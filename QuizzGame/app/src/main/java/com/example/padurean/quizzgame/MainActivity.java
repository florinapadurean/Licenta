package com.example.padurean.quizzgame;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.padurean.quizzgame.Callbacks.ServerCallback;
import com.example.padurean.quizzgame.Communication.ClientSocket;
import com.example.padurean.quizzgame.Communication.MyServerSocket;
import com.example.padurean.quizzgame.Domain.Question;
import com.example.padurean.quizzgame.Errors.NoDevices;
import com.example.padurean.quizzgame.Levels.ImagePuzzleHardLvl;
import com.example.padurean.quizzgame.Levels.ImagePuzzleLvl;
import com.example.padurean.quizzgame.Levels.KnowledgeLvl;
import com.example.padurean.quizzgame.Menu.LevelsMenu;
import com.example.padurean.quizzgame.Login.Login;


import static java.lang.Boolean.FALSE;

import com.example.padurean.quizzgame.P2PConnection.DeviceList;
import com.example.padurean.quizzgame.P2PConnection.WifiDirectBroadcastReceiver;
import com.example.padurean.quizzgame.Menu.StartGameMenu.StartMenuListener;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements ServerCallback,ImagePuzzleHardLvl.GetMessageListener,ImagePuzzleLvl.GetMessageListener,KnowledgeLvl.GetMessageListener, LevelsMenu.LevelPressedListener,GoogleApiClient.OnConnectionFailedListener,NoDevices.RetryInterface,WifiP2pManager.GroupInfoListener, DeviceList.DeviceActionListener,StartMenuListener,WifiP2pManager.ChannelListener{
//    GoogleApiClient.ConnectionCallbacks
    private WifiP2pManager mManager;
    private Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private Boolean wifiEnabled=FALSE;
    static String TAG="MAin";
    private Thread t;

    private boolean groupFlag=false;
    private WifiP2pGroup groupInfo;
    private List<Question> knowledgelevel;
//    private GoogleApiClient mGoogleApiClient;
    private MessageListener messageListener;
    private Message message;
    private String messageAsString;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private Boolean mResolvingError=false;
    private MyServerSocket serverSocket=null;
    private ClientSocket clientSocket=null;
    private String lastMessageRecievedAndNotSent="";

    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
        this.wifiEnabled = isWifiP2pEnabled;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        disconnect();
        knowledgelevel=new ArrayList<>();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar toolbar=getSupportActionBar();
        toolbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
        toolbar.setDisplayUseLogoEnabled(true);
        toolbar.setLogo(R.drawable.smart);
//        getDataFromDb();

//         Create an instance of MessageListener
//        messageListener = new MessageListener() {
//            @Override public void onFound(Message message) {
//                messageAsString = new String(message.getContent());
//                KnowledgeLvl f=(KnowledgeLvl)getFragmentManager().findFragmentByTag("knowledgelvlfragment");
//                ImagePuzzleLvl f1=(ImagePuzzleLvl)getFragmentManager().findFragmentByTag("puzzlelvlfragment");
//                ImagePuzzleHardLvl f2=(ImagePuzzleHardLvl) getFragmentManager().findFragmentByTag("puzzlehardlvlfragment");
//                if (f !=null && f.isVisible()){
//                    f.setMessage(messageAsString);
//                }
//                if (f1 !=null && f1.isVisible()){
//                    f1.setMessage(messageAsString);
//                }
//                if (f2 !=null && f2.isVisible()){
//                    f2.setMessage(messageAsString);
//                }
//                Log.i(TAG,"recieve:"+messageAsString);
//            }
//            // Called when a message is no longer nearby.
//            @Override public void onLost(Message message) {
//                String messageAsString2 = new String(message.getContent());
//                Log.i(TAG, "Lost message: " + messageAsString2);
//            }
//        };
        // Subscribe to receive messages

        // add necessary intent values to be matched.
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        if (savedInstanceState == null) {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            StartGameMenu fragment = new StartGameMenu();
//            transaction.replace(R.id.main_container, fragment);
//            transaction.commit();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(fm.findFragmentById(R.id.frag_list));
            ft.show(fm.findFragmentById(R.id.frag_start));
            ft.hide(fm.findFragmentById(R.id.frag_menu));
            ft.hide(fm.findFragmentById(R.id.frag_err_no_devices));
            ft.commit();
        }
    }


    @Override
    public Observable<String> observableListenerWrapper() {
        Observable<String> myObservable= Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(messageAsString);
                    subscriber.onCompleted();
                }
            }
        });
        return myObservable;
    }


    /** register the BroadcastReceiver with the intent values to be matched */
    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
//        if(clientSocket!=null){
//            clientSocket.close();
//        }
//        if(serverSocket!=null){
//            serverSocket.close();
//        }
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
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void findFriend(){
        if (!wifiEnabled) {
//            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//            Toast.makeText(MainActivity.this, "Enable wifi",
//                    Toast.LENGTH_SHORT).show();
            WifiP2pManager manager = (WifiP2pManager) this.getSystemService(Context.WIFI_P2P_SERVICE);
            Channel channel = manager.initialize(this,this.getMainLooper(), null);

            try {
                Method method1 = manager.getClass().getMethod("enableP2p", Channel.class);
                method1.invoke(manager, channel);
            } catch (Exception e) {
                Toast.makeText(this, "method did not found",
                   Toast.LENGTH_SHORT).show();
            }
        }
        startRegistrationAndDiscovery();
    }


    private void startRegistrationAndDiscovery() {

        discoverService();
    }

    private void discoverService() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fm.findFragmentById(R.id.frag_list));
        ft.hide(fm.findFragmentById(R.id.frag_start));
        ft.hide(fm.findFragmentById(R.id.frag_menu));
        ft.hide(fm.findFragmentById(R.id.frag_err_no_devices));
//        ft.hide(fm.findFragmentById(R.id.frag_knowledgeLvl));
        ft.commit();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        final DeviceList fragment = new DeviceList();
//        transaction.replace(R.id.main_container, fragment);
//        transaction.commit();
        final DeviceList fragment = (DeviceList) getFragmentManager().findFragmentById(R.id.frag_list);
        fragment.onInitiateDiscovery();
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Discovery Initiated",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(MainActivity.this, "Discovery Failed : " + reasonCode,
                                Toast.LENGTH_SHORT).show();
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
                // WiFiDirectBroadcastReceiver will notify us. Ignore for now.
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
            mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

                @Override
                public void onFailure(int reasonCode) {
                    Log.d(TAG, "Disconnect failed. Reason :" + reasonCode);
                }

                @Override
                public void onSuccess() {

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
    public void onStop() {
        if (mManager != null && mChannel != null ) {
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

        super.onStop();
    }

    @Override
    protected void onDestroy() {
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
//        if(clientSocket!=null){
//            clientSocket.close();
//        }
//        if(serverSocket!=null){
//            serverSocket.close();
//        }
//        if (mGoogleApiClient!= null && mGoogleApiClient.isConnected()) {
//            // Clean up when the user leaves the activity.
//            Nearby.Messages.unpublish(mGoogleApiClient, message);
//            Nearby.Messages.unsubscribe(mGoogleApiClient, messageListener);
//            mGoogleApiClient.disconnect();
//        }

        super.onDestroy();
    }


    @Override
    public void showMenu(WifiP2pInfo info) {
//        Log.i(TAG,mGoogleApiClient.toString());
        //there is a group to remove at the end of app
//        if (mGoogleApiClient==null || !mGoogleApiClient.isConnected()) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(Nearby.MESSAGES_API)
//                    .addConnectionCallbacks(this)
//                    .enableAutoManage(this, this)
//                    .build();
//            mGoogleApiClient.connect();
//        }
       if(info.isGroupOwner){
           Log.i(TAG,"server start");
           serverSocket=new MyServerSocket(this);
           serverSocket.start();
//           t=new Thread(serverSocket);
//           t.start();
//           serverSocket.run();
       }
        else{
           Log.i(TAG,"client start");
           clientSocket=new ClientSocket(this,info.groupOwnerAddress);
           clientSocket.start();
//           t=new Thread(clientSocket);
//           t.start();
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
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
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

//        publish(message);
    }

    public void recieveMessage(final String messageAsString){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KnowledgeLvl f=(KnowledgeLvl)getFragmentManager().findFragmentByTag("knowledgelvlfragment");
                ImagePuzzleLvl f1=(ImagePuzzleLvl)getFragmentManager().findFragmentByTag("puzzlelvlfragment");
                ImagePuzzleHardLvl f2=(ImagePuzzleHardLvl) getFragmentManager().findFragmentByTag("puzzlehardlvlfragment");
                Boolean msgNotSent=true;
                Log.i(TAG, "recieve:" + messageAsString);

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
                    //try again
//                    int interval=10;
//                    while(interval>0){
//                        interval--;
//                    }
//                    recieveMessage(messageAsString);
                }

            }
        });

    }

    @Override
    public String getLastMessageRecieved() {
        return lastMessageRecievedAndNotSent;
    }

    //nearby messages callbacks

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        Log.i("MAIN","CONNESCTED");
////        publishAndSubscribe();
//
//    }

//    @Override
//    public void onConnectionSuspended(int cause) {
//        Log.e(TAG, "GoogleApiClient disconnected with cause: " + cause);
//    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (result.hasResolution()) {
            try{
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            }catch (IntentSender.SendIntentException e) {
                Log.e("Onconnfailed"," failed with exception: " + e);
            }

        } else {
            Log.e(TAG, "GoogleApiClient connection failed");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
//            if (resultCode == RESULT_OK) {
//                mGoogleApiClient.connect();
//            } else {
//                Log.e(TAG, "GoogleApiClient connection failed. Unable to resolve.");
//            }
//        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    private void publishAndSubscribe() {
//        // We automatically subscribe to messages from nearby devices once
//        // GoogleApiClient is connected. If we arrive here more than once during
//        // an activity's lifetime, we may end up with multiple calls to
//        // subscribe(). Repeated subscriptions using the same MessageListener
//        // are ignored.
//        Log.i(TAG,"aici");
//        String s="Hello from" +Build.MANUFACTURER+Build.MODEL;
//        message=new Message(s.getBytes());
//        publish(message);
//        Nearby.Messages.publish(mGoogleApiClient, message);
//        Nearby.Messages.subscribe(mGoogleApiClient, messageListener);
//    }
//
//    private void publish(Message message){
//        Nearby.Messages.publish(mGoogleApiClient,message);
//    }

}

