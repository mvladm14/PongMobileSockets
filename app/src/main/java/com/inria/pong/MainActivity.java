package com.inria.pong;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.inria.pong.tcp.TCPClient;

import models.sensors.LinearAcceleration;

public class MainActivity extends Activity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private static int PLAYER_PORT;

    private TCPClient mTcpClient;

    private SensorManager sManager;

    private long counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long playerID = getIntent().getLongExtra(StartActivity.PLAYER_ID, -1);
        PLAYER_PORT = 4443 + (int)playerID;

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // connect to the server
        new connectTask().execute("");
    }

    //when this Activity starts
    @Override
    protected void onResume() {
        super.onResume();

        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop() {

        //unregister the sensor listener
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mTcpClient.stopClient();
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

            if (counter > 1000 && mTcpClient != null) {
                LinearAcceleration linearAcceleration =
                        new LinearAcceleration(sensorEvent.values[1], sensorEvent.timestamp);

                mTcpClient.sendMessage(linearAcceleration);
            }
        }
        counter++;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class connectTask extends AsyncTask<String, String, TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(PLAYER_PORT, new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    //publishProgress(message);
                    Log.e(TAG, "Received: " + message);
                }
            });
            mTcpClient.run();

            return null;
        }
    }
}
