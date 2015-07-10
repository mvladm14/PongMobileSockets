package com.inria.pong.tasks;

import android.os.AsyncTask;

import java.util.Collection;

import models.player.Player;
import restInterfaces.PlayerSvcApi;
import retrofit.RestAdapter;

/**
 * Created by Vlad on 7/10/2015.
 */
public class GetAvailablePlayersTask extends AsyncTask<Void, Void, Collection<Player>> {

    private static final String SERVER = "http://131.254.101.102:8080/myriads";

    private static PlayerSvcApi playerSvcApi = new RestAdapter.Builder().setEndpoint(SERVER)
            .build().create(PlayerSvcApi.class);

    private AsyncResponse delegate = null;

    public GetAvailablePlayersTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Collection<Player> players) {
        delegate.onFinishedConnection(players);
    }

    @Override
    protected Collection<Player> doInBackground(Void... voids) {
        return playerSvcApi.getPlayersList();
    }
}