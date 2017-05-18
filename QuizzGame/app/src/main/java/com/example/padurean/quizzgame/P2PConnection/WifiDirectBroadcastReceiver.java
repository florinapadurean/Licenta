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

import com.example.padurean.quizzgame.R;

/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    public WifiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // UI update to indicate wifi p2p status.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wifi Direct mode is enabled
                mActivity.setIsWifiP2pEnabled(true);
            } else {
                mActivity.setIsWifiP2pEnabled(false);

            }
            Log.d(MainActivity.TAG, "P2P state changed - " + state);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
//                mManager.requestPeers(mChannel,(WifiP2pManager.PeerListListener) mActivity.getFragmentManager());
                mManager.requestPeers(mChannel, (WifiP2pManager.PeerListListener) mActivity.getFragmentManager()
                        .findFragmentById(R.id.frag_list));
            }
            Log.d(MainActivity.TAG, "P2P peers changed");
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
                mManager.requestGroupInfo(mChannel,mActivity);
            } else {
                // It's a disconnect
//                mActivity.resetData();
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            switch (device.status) {
                case WifiP2pDevice.CONNECTED:
                    Log.v(MainActivity.TAG,"mConnected");
                    break;
                case WifiP2pDevice.INVITED:
                    Log.v(MainActivity.TAG,"mInvited");
                    break;
                case WifiP2pDevice.FAILED:
                    Log.v(MainActivity.TAG,"mFailed");
                    break;
                case WifiP2pDevice.AVAILABLE:
                    Log.v(MainActivity.TAG,"mAvailable");
                    break;
                case WifiP2pDevice.UNAVAILABLE:
                    Log.v(MainActivity.TAG,"mUnavailable");
                default:
                    Log.v(MainActivity.TAG,"mUnknown");
                    break;
            }
        }
    }


}