package com.inria.pong.tasks;

import java.util.Collection;

import models.player.Player;

/**
 * Created by Vlad on 7/10/2015.
 */
public interface AsyncResponse {
    void onFinishedConnection(Collection<Player> players);
}