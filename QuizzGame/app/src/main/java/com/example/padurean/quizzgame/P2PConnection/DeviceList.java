package com.example.padurean.quizzgame.P2PConnection;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.padurean.quizzgame.MainActivity;
import com.example.padurean.quizzgame.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 26.02.2017.
 */

public class DeviceList extends ListFragment implements WifiP2pManager.PeerListListener,WifiP2pManager.ConnectionInfoListener{

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    ProgressDialog progressDialog = null;
    View mContentView = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.devices_list, container,false);
        this.setListAdapter(new WiFiPeerListAdapter(getActivity(), R.layout.row_devices, peers));
        return mContentView;
    }

    /**
     * @return this device
     */


    private static String getDeviceStatus(int deviceStatus) {
        Log.d("deviceList", "Peer status :" + deviceStatus);
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }

    /**
     * Initiate a connection with the peer.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
//        ((DeviceActionListener) getActivity()).showDetails(device);
        ((DeviceActionListener)getActivity()).connect(device);
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
        if (peers.size() == 0) {
            Log.d("devicelist","No devices found!");
            return;
        }

    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        // The owner IP is now known.
        if (info.groupFormed && info.isGroupOwner) {
            Log.v("onconn","owner");

//            Intent i=new Intent(getActivity(), com.example.padurean.quizzgame.Menu.class);
//            startActivity(i);

        } else if (info.groupFormed) {
            Log.v("onconn","peer");
//            Intent i=new Intent(getActivity(), com.example.padurean.quizzgame.Menu.class);
//            startActivity(i);
            // The other device acts as the client. In this case, we enable the
            // get file button.
        }
        ((DeviceActionListener)getActivity()).showMenu(info);
    }

    /**
     *
     */
    public void onInitiateDiscovery() {
//        progressDialog=pD;
//        this.getView().setVisibility(View.VISIBLE);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "Seaching For People, Please Wait...", true,
                true, new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ((DeviceActionListener)getActivity()).stopPeerDiscoveryAtOnCancelDialog();
                    }
                });
        nothinghappens();
    }

    public void nothinghappens(){
        new CountDownTimer(20000,1000){
            @Override
            public void onFinish() {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                    ((DeviceActionListener)getActivity()).stopPeerDiscovery();
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if(!progressDialog.isShowing()){
                    this.cancel();
                }
            }
        }.start();

    }

    /**
     * Array adapter for ListFragment that maintains WifiP2pDevice list.
     */
    public class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

        private List<WifiP2pDevice> items;

        /**
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public WiFiPeerListAdapter(Context context, int textViewResourceId,
                                   List<WifiP2pDevice> objects) {
            super(context, textViewResourceId, objects);
            items = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_devices, null);
            }
            WifiP2pDevice device = items.get(position);
            if (device != null) {
                TextView top = (TextView) v.findViewById(R.id.device_name);
                if (top != null) {
                    top.setText(device.deviceName+"-"+getDeviceStatus(device.status));
                }
            }

            return v;

        }
    }

    /**
     * An interface-callback for the activity to listen to fragment interaction
     * events.
     */
    public interface DeviceActionListener {
        void showMenu(WifiP2pInfo info);

        void connect(WifiP2pDevice device);

        void stopPeerDiscovery();

        void stopPeerDiscoveryAtOnCancelDialog();
    }

}