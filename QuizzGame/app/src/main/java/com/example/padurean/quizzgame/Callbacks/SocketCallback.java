package com.example.padurean.quizzgame.Callbacks;

/**
 * Created by Asus on 02.06.2017.
 */

public interface SocketCallback {
    public void recieveMessage(String messageAsString);
    public void disconnect();
    public void showToastDisconnected();
}
