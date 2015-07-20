package com.inria.pong.tasks;

import android.os.AsyncTask;

import com.microsoft.band.BandException;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;

public class BandConnectionTask extends AsyncTask<BandPendingResult<ConnectionState>, Void, ConnectionState> {

    private BandConnectionResponse delegate = null;

    public BandConnectionTask(BandConnectionResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected ConnectionState doInBackground(BandPendingResult<ConnectionState>... bandPendingResults) {
        ConnectionState connectionState = null;
        try {
            connectionState = bandPendingResults[0].await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BandException e) {
            e.printStackTrace();
        }
        return connectionState;
    }

    @Override
    protected void onPostExecute(ConnectionState result) {
        delegate.onFinishedConnection(result);
    }
}
