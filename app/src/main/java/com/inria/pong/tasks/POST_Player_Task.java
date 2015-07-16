package com.inria.pong.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.inria.pong.tcp.Util;

import models.player.Player;
import restInterfaces.PlayerSvcApi;
import retrofit.RestAdapter;

/**
 * Created by Vlad on 7/16/2015.
 */
public class POST_Player_Task extends AsyncTask<Player, Void, Void> {

    private static final String TAG = "POST_Player_Task";

    private static final PlayerSvcApi playerSvcApi = new RestAdapter.Builder().setEndpoint(Util.SERVER)
            .build().create(PlayerSvcApi.class);

    @Override
    protected Void doInBackground(Player... players) {
        playerSvcApi.addPlayer(players[0]);
        Log.e(TAG, "POST request submitted.");
        return null;
    }
}
