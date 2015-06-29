package com.inria.pong.tcp;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import models.sensors.LinearAcceleration;

public class TCPClient {

    private String serverMessage;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;

    private ObjectOutputStream out;
    private DataInputStream in;

    public static final String SERVERIP = "131.254.101.102"; //your computer IP address
    public static final int SERVERPORT = 4444;

    /**
     * Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     *
     * @param linearAcceleration text entered by client
     */
    public void sendMessage(LinearAcceleration linearAcceleration) {
        if (out != null) {
            try {
                out.writeObject(linearAcceleration);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopClient() {
        mRun = false;
    }

    public void run() {

        mRun = true;

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);

            try {

                //send the message to the server
                out = new ObjectOutputStream(socket.getOutputStream());

                Log.e("TCP Client", "C: Sent.");

                Log.e("TCP Client", "C: Done.");

                //receive the message which the server sends back
                in = new DataInputStream(socket.getInputStream());
                //in = new InputStream(new InputStream(socket.getInputStream()));
                //in = new ObjectInputStream(socket.getInputStream());

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();

                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
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

    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
