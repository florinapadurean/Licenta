package com.example.padurean.quizzgame.Communication;

import android.util.Log;

import com.example.padurean.quizzgame.Callbacks.ServerCallback;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Asus on 02.06.2017.
 */

public class ClientSocket extends Thread {
    private String serverMessage;
    public static final int SERVERPORT = 4444;
    ServerCallback callback;
    private boolean mRun = false;
    InetAddress groupOwnerAddress;

    PrintWriter out;
    BufferedReader in;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public ClientSocket(ServerCallback callback, InetAddress groupOwnerAddress){
        this.callback=callback;
        this.groupOwnerAddress=groupOwnerAddress;

    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void send(String message){
        Log.i("clientSocket","send msg:"+message);
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient(){
        mRun = false;
    }

    @Override
    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(groupOwnerAddress, SERVERPORT);

            try {

                //send the message to the server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null ) {
                        Log.i("clientSocket","recieve in while msg:"+serverMessage);
                        //call the method messageReceived from MyActivity class
                        callback.recieveMessage(serverMessage);
                    }
                    serverMessage = null;

                }

                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("TCP", "C: Error", e);

        }

    }
//    static final int SocketServerPORT = 8080;

//    ServerCallback callback;
//    Socket socket=null;
//    DataInputStream dataInputStream = null;
//    DataOutputStream dataOutputStream = null;
//    Boolean open=true;
//
//
//
//    public void open()  {
//        try{
//        dataInputStream = new DataInputStream(socket.getInputStream());
//        dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        }
//        catch(IOException ioe)
//        {  System.out.println("open() client error " + ioe.getMessage()); }
//
//    }
//
//    public void close() {
//        try{
//            if (socket != null) socket.close();
//            if (dataInputStream != null) dataInputStream.close();
//            open=false;
//        }
//        catch(IOException ioe)
//        {  System.out.println("close client error " + ioe.getMessage()); }
//
//    }
//    @Override
//    public void run() {
//        try {
//            socket=new Socket();
////            socket.bind(null);
//            socket.connect(new InetSocketAddress(groupOwnerAddress.getHostAddress(), SocketServerPORT));
//            open();
//            Log.v("clientsocket","open");
//            while (open) {
//                String messageFromServer = dataInputStream.readUTF();
//                if(messageFromServer=="done"){
//                    close();
//                    open=false;
//                }
//                callback.recieveMessage(messageFromServer);
//
//
//            }
//        } catch (IOException e) {
//            System.out.println("run error " + e.getMessage());
//        }
//    }
//
//    public void send(String message){
//        if(socket.isConnected()){
//            try{
//                dataOutputStream.writeUTF(message);
//            }
//            catch (IOException e) {
//                System.out.println("send error " + e.getMessage());
//            }
//        }
//    }
}