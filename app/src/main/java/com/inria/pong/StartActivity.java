package com.inria.pong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.inria.pong.tasks.AsyncResponse;
import com.inria.pong.tasks.GetAvailablePlayersTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import models.player.Player;
import restInterfaces.PlayerSvcApi;
import retrofit.RestAdapter;


public class StartActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = "StartActivity";
    private static final String SERVER = "http://131.254.101.102:8080/myriads";
    private Spinner spinner;
    private Collection<Player> players;

    public static String PLAYER_ID = "PLAYER_ID";

    private static PlayerSvcApi playerSvcApi = new RestAdapter.Builder().setEndpoint(SERVER)
            .build().create(PlayerSvcApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initializeFields();

    }

    private void initializeFields() {
        initializeUIFields();
        initializeNonUIFields();
    }

    private void initializeUIFields() {
        spinner = (Spinner) findViewById(R.id.ballSpinner);
    }

    private void initializeNonUIFields() {
        GetAvailablePlayersTask getPongBallsTask = new GetAvailablePlayersTask(this);
        getPongBallsTask.execute();
    }

    @Override
    public void onFinishedConnection(Collection<Player> players) {

        List<String> strings = new ArrayList();
        this.players = players;

        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            strings.add("Player " + iterator.next().getId());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void play(View view) {

        String player = spinner.getSelectedItem().toString();
        final Player p;
        if (player.contains("1")) {
            p = players.iterator().next();
        } else {
            Iterator<Player> playerIterator = players.iterator();
            playerIterator.next();
            p = playerIterator.next();
        }

        Log.e(TAG, p.getUsername() + " " + p.getId());
        p.setCanPlay(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                playerSvcApi.addPlayer(p);
            }
        }).start();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PLAYER_ID", p.getId());
        startActivity(intent);
    }
}
