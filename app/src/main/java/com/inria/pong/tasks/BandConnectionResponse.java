package com.inria.pong.tasks;

import com.microsoft.band.ConnectionState;

/**
 * Created by Vlad on 7/17/2015.
 */
public interface BandConnectionResponse {
    void onFinishedConnection(ConnectionState connectionState);
}
