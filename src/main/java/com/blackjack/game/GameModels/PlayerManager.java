package com.blackjack.game.GameModels;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerManager {
    private static final int PLAYER_LIMIT = 64;

    private Map<UUID, PlayerData> registeredPlayers = new HashMap<>();

    public UUID createPlayer() {
        if (registeredPlayers.size() < PLAYER_LIMIT) {
            PlayerData playerData = new PlayerData();
            UUID playerId = playerData.getPlayerId();
            registeredPlayers.put(playerId, playerData);
            return playerId;
        }

        return null;
    }

    public PlayerData getPlayer(UUID playerId) {
        return registeredPlayers.getOrDefault(playerId, null);
    }

    public void removePlayer(UUID playerId) {
        if (registeredPlayers.containsKey(playerId)) {
            registeredPlayers.remove(playerId);
        }
    }

    public Set<UUID> getPlayers() {
        return registeredPlayers.keySet();
    }
}
