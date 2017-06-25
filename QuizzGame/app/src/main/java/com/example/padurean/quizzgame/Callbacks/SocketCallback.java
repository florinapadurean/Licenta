package com.example.padurean.quizzgame.Callbacks;

/**
 * Created by Asus on 02.06.2017.
 */

public interface SocketCallback {
    void recieveMessage(String messageAsString);

    void disconnect();

    void showToast(String message);
}
