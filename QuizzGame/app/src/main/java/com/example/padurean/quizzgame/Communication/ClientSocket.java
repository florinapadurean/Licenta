package com.example.padurean.quizzgame.Communication;

import android.util.Log;
import android.widget.Toast;

import com.example.padurean.quizzgame.Callbacks.SocketCallback;
import com.example.padurean.quizzgame.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Asus on 02.06.2017.
 */

public class ClientSocket extends Thread {
    private String serverMessage;
    private static final int SERVERPORT = 8080;
    private SocketCallback callback;
    private boolean mRun = false;
    private InetAddress groupOwnerAddress;

    PrintWriter out;
    BufferedReader in;


    public ClientSocket(SocketCallback callback, InetAddress groupOwnerAddress) {
        this.callback = callback;
        this.groupOwnerAddress = groupOwnerAddress;

    }


    public void send(String message) {
        Log.i("clientSocket", "send msg:" + message);
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
        if (message == "end connection") {
            mRun = false;
        }
    }


    public void stopClient() {
        mRun = false;
    }

    @Override
    public void run() {

        mRun = true;

        try {
            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(groupOwnerAddress, SERVERPORT);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();
                    if (serverMessage != null) {
                        Log.i("clientSocket", "recieve in socket msg:" + serverMessage);
                        if (serverMessage == "end connection") {
                            mRun = false;
                            if (socket != null) socket.close();
                            if (in != null) in.close();
                            if (out != null) out.close();
                            callback.disconnect();
                            break;
                        } else {
                            //call the method messageReceived from MyActivity class
                            callback.recieveMessage(serverMessage);
                        }
                    }
                    serverMessage = null;
                }
                if (socket != null) socket.close();
                if (in != null) in.close();
                if (out != null) out.close();
                callback.disconnect();


            } catch (Exception e) {
                callback.showToast("Sorry,someone disconnected!");
//                callback.disconnect();
                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                if (socket != null) socket.close();
                if (in != null) in.close();
                if (out != null) out.close();
                callback.disconnect();

            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }
}
