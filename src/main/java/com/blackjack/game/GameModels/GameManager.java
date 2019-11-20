package com.blackjack.game.GameModels;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GameManager {
    private static final int GAME_LIMIT = 16;

    private Map<UUID, GameData> registeredGames = new HashMap<>();

    public UUID createGame() {
        if (registeredGames.size() < GAME_LIMIT) {
            GameData gameData = new GameData();
            UUID gameId = gameData.getGameId();
            registeredGames.put(gameId, gameData);
            return gameId;
        }

        return null;
    }

    public GameData getGame(UUID gameId) {
        return registeredGames.getOrDefault(gameId, null);
    }

    public void removeGame(UUID gameId) {
        if (registeredGames.containsKey(gameId)) {
            registeredGames.remove(gameId);
        }
    }

    public Set<UUID> getGames() {
        return registeredGames.keySet();
    }
}
