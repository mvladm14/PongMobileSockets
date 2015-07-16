package com.inria.pong;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.inria.pong.tasks.AsyncResponse;
import com.inria.pong.tasks.GetAvailablePlayersTask;
import com.inria.pong.tasks.POST_Player_Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import models.player.Player;
import models.player.PlayerState;


public class StartActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = "StartActivity";
    private Spinner spinner;
    private Collection<Player> players;

    public static String PLAYER_ID = "PLAYER_ID";

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
        GetAvailablePlayersTask getAvailablePlayersTask = new GetAvailablePlayersTask(this);
        getAvailablePlayersTask.execute();
    }

    @Override
    public void onFinishedConnection(Collection<Player> players) {

        List<String> strings = new ArrayList();
        this.players = players;

        for (Player player : players) {
            strings.add("Player " + player.getId());
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void play(View view) {

        String player = spinner.getSelectedItem().toString();
        final Player selectedPlayer;
        if (player.contains("1")) {
            selectedPlayer = players.iterator().next();
        } else {
            Iterator<Player> playerIterator = players.iterator();
            playerIterator.next();
            selectedPlayer = playerIterator.next();
        }

        Log.e(TAG, selectedPlayer.toString());
        if (selectedPlayer.getPlayerState() == PlayerState.AVAILABLE) {
            selectedPlayer.setPlayerState(PlayerState.PLAYING);
            new POST_Player_Task().execute(selectedPlayer);

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("PLAYER_ID", selectedPlayer.getId());
            startActivity(intent);
        } else {
            initializeNonUIFields();
            Toast.makeText(this, "Please choose another player.", Toast.LENGTH_LONG).show();
        }
    }
}
