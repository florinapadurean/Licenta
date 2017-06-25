package com.example.padurean.quizzgame.Callbacks;

import android.net.wifi.p2p.WifiP2pGroup;

/**
 * Created by Asus on 15.06.2017.
 */

public interface GetMessageListener {
    void send(String string);

    WifiP2pGroup getGroupInfo();

    void showToast(String message);

    String getLastMessage();

    void setLastMessageEmpty();
}