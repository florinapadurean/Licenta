package com.example.padurean.quizzgame.Communication;

import android.util.Log;

import com.example.padurean.quizzgame.Callbacks.ServerCallback;
import com.example.padurean.quizzgame.MainActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

    public static final int SERVERPORT = 4444;
    private boolean running = false;
    private PrintWriter mOut;
    ServerCallback callback;


    public MyServerSocket(ServerCallback callback){
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
    }

    @Override
    public void run() {
        super.run();
        running = true;
        try {
            System.out.println("S: Connecting...");

            //create a server socket. A server socket waits for requests to come in over the network.
            ServerSocket serverSocket = new ServerSocket(SERVERPORT);

            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            Socket client = serverSocket.accept();
            System.out.println("S: Receiving...");

            try {

                //sends the message to the client
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);

                //read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                //in this while we wait to receive messages from client (it's an infinite loop)
                //this while it's like a listener for messages
                while (running) {
                    String message = in.readLine();

                    if (message != null ) {
                        Log.i("serverSocket","recieve msg:"+message);
                        //call the method messageReceived from ServerBoard class
                        callback.recieveMessage(message);
                    }
                }

            } catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
            } finally {
                client.close();
                System.out.println("S: Done.");
            }

        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }

    }

//    static final int SocketServerPORT = 8080;
//    java.net.ServerSocket serverSocket;
//    Socket socket;
//    DataInputStream dataInputStream = null;
//    DataOutputStream dataOutputStream = null;
//    boolean open=true;
//    String message;
//
//
//
//    public void open() throws IOException {
//        try{
//            dataInputStream = new DataInputStream(socket.getInputStream());
//            dataOutputStream = new DataOutputStream(socket.getOutputStream());
//        }
//        catch(IOException ioe)
//        {  System.out.println("open() server error " + ioe.getMessage()); }
//    }
//
//    public void close() {
//        try{
//            if (socket != null) socket.close();
//            if (dataInputStream != null) dataInputStream.close();
//            open=false;
//        }
//        catch(IOException ioe)
//        {  System.out.println("close socket server error " + ioe.getMessage()); }
//
//    }
//
//    @Override
//    public void run() {
//        try {
//            if(serverSocket==null){
//                serverSocket = new ServerSocket();
//                serverSocket.setReuseAddress(true);
//                serverSocket.bind(new InetSocketAddress(SocketServerPORT));
//            }
//
//
//            while (open) {
//                socket = serverSocket.accept();
//                open();
//                Log.v("serversocket","open");
//                String messageFromClient = dataInputStream.readUTF();
//
//                message = messageFromClient;
//                if(message=="done"){
//                   close();
//                }
//
//                callback.recieveMessage(message);
//
//
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            System.out.println("run error " + e.getMessage());
//            e.printStackTrace();
////            final String errMsg = e.toString();
//        }
//    }
//
//
//    public void send(String message){
//        while(socket==null){
//
//        }
//        if(socket!=null){
//            try{
//                dataOutputStream.writeUTF(message);
//            }
//            catch (IOException e) {
//                System.out.println("send error " + e.getMessage());
//            }
//        }
//    }




//    public synchronized void handle(int ID, String input) {
//        if (input.equals(".bye")) {
//            clients[findClient(ID)].send(".bye");
//            remove(ID);
//        } else
//            for (int i = 0; i < clientCount; i++)
//                clients[i].send(ID + ": " + input);
//    }

}
