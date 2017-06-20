package com.example.padurean.quizzgame.Communication;

import android.util.Log;

import com.example.padurean.quizzgame.Callbacks.SocketCallback;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Asus on 02.06.2017.
 */

public class MyServerSocket extends Thread{

    public static final int SERVERPORT = 8080;
    private boolean running = false;
    private PrintWriter mOut;
    private BufferedReader in;
    SocketCallback callback;
    Socket client;


    public MyServerSocket(SocketCallback callback){
        this.callback=callback;
//        serverSocket=null;
//        socket=null;
    }

    public void send(String message){
        Log.i("serverSocket","send msg:"+message);
        if (mOut != null && !mOut.checkError()) {
            mOut.println(message);
            mOut.flush();
        }
        if(message=="end connection"){
            running=false;
        }
    }

    @Override
    public void run() {
        super.run();
        running = true;
        try {
            System.out.println("S: Connecting...");

            //create a server socket. A server socket waits for requests to come in over the network.
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(SERVERPORT));

            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            client= serverSocket.accept();
            System.out.println("S: Receiving...");

            try {

                //sends the message to the client
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

                //read the message received from client
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                //in this while we wait to receive messages from client (it's an infinite loop)
                //this while it's like a listener for messages
                while (running) {
                    String message = in.readLine();
                    if (message != null ) {
                        Log.i("serverSocket","recieve msg:"+message);
                        if(message=="end connection"){
                            Log.v("Client","aici alo");
                            client.close();
                            if (client != null) client.close();
                            if (in != null) in.close();
                            if(mOut!=null) mOut.close();
                            callback.disconnect();
                            running=false;
                            break;
                        }
                        else{
                            callback.recieveMessage(message);
                        }
                    }
                }


            } catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
                callback.showToast("Sorry,someone disconnected!");
                client.close();
                serverSocket.close();
//                callback.disconnect();
            } finally {
//                client.close();
                if (client != null) client.close();
                if (in != null) in.close();
                if(mOut!=null) mOut.close();
                callback.disconnect();
                System.out.println("S: Done.");
            }

        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }

    }

}
