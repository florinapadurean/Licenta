package com.example.padurean.quizzgame.P2PConnection;

/**
 * Created by Asus on 26.02.2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.padurean.quizzgame.MainActivity;
import com.example.padurean.quizzgame.R;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            //  indicate wifi p2p status.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi Direct mode is enabled
                mActivity.setIsWifiP2pEnabled(true);
            } else {
                mActivity.setIsWifiP2pEnabled(false);

            }
            Log.d("wifiBroadcastReciever", "P2P state changed - " + state);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // Device list is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, (WifiP2pManager.PeerListListener) mActivity.getFragmentManager()
                        .findFragmentById(R.id.frag_list));
            }
            Log.d("wifiBroadcastReciever", "P2P peers changed");
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP
                DeviceList fragment = (DeviceList) mActivity.getFragmentManager().findFragmentById(R.id.frag_list);
                mManager.requestConnectionInfo(mChannel, fragment);
                mManager.requestGroupInfo(mChannel, mActivity);
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            switch (device.status) {
                case WifiP2pDevice.CONNECTED:
                    Log.v("wifiBroadcastReciever", "mConnected");
                    break;
                case WifiP2pDevice.INVITED:
                    Log.v("wifiBroadcastReciever", "mInvited");
                    break;
                case WifiP2pDevice.FAILED:
                    Log.v("wifiBroadcastReciever", "mFailed");
                    break;
                case WifiP2pDevice.AVAILABLE:
                    Log.v("wifiBroadcastReciever", "mAvailable");
                    break;
                case WifiP2pDevice.UNAVAILABLE:
                    Log.v("wifiBroadcastReciever", "mUnavailable");
                default:
                    Log.v("wifiBroadcastReciever", "mUnknown");
                    break;
            }
        }
    }


}