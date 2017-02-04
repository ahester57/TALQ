package edu.umsl.hester.superclickers;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


class TCPClient {

    private static final String TAG = "TCPClient";
    private final Handler mHandler;
    private String ipNumber, incomingMessage, command;
    private BufferedReader in;
    private PrintWriter out;
    private MessageCallback listener = null;
    private boolean mRun = false;

    TCPClient(Handler mHandler, String command, String ipNumber,
                MessageCallback listener) {
        this.listener         = listener;
        this.ipNumber         = ipNumber;
        this.command          = command ;
        this.mHandler         = mHandler;
    }

    void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            mHandler.sendEmptyMessageDelayed(69, 1000);
            Log.d(TAG,"Sent message: " + message);

        }
    }

    void stopClient() {
        Log.d(TAG, "Client stoopped");
        mRun = false;
    }

    boolean isRunning() { return mRun; }

    void run() {
        mRun = true;
        int count = 0;

        try {

            InetAddress serverAddr = InetAddress.getByName(ipNumber);
            Log.d(TAG, "Connecting...");

            mHandler.sendEmptyMessageDelayed(11, 1000);
            Socket socket = new Socket(serverAddr, 39909);

            try {

                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d(TAG, "In/Out created");
                this.sendMessage(command);

                mHandler.sendEmptyMessageDelayed(87, 2000);

                //listen for incloming message while mRun
                // i know loop is dumb anymore but hey its in dev
                while (count < 1) {
                    incomingMessage = in.readLine();

                    if (incomingMessage != null && listener != null) {
                        listener.callbackMessageReceiver(incomingMessage);
                    }
                    Log.d(TAG, "Received Message: " +incomingMessage);
                    incomingMessage = null;
                    count++;
                }



            } catch (Exception e) {

                Log.d(TAG, "Error", e);
                mHandler.sendEmptyMessageDelayed(45, 2000);

            } finally {
                try {
                    out.flush();
                    out.close();
                    in.close();
                    socket.close();
                    mHandler.sendEmptyMessageDelayed(67, 3000);
                    Log.d(TAG, "Socket closed");
                } catch (IOException ioe) {
                    Log.d(TAG, ioe.getMessage());
                }

            }

        } catch (Exception e) {
            Log.d(TAG, "Error", e);
            mHandler.sendEmptyMessageDelayed(45, 2000);
        }
    }



     interface MessageCallback {
         void callbackMessageReceiver(String message);
    }

    interface MessageSend {
        void callbackMessageSend(String message);
    }
}
