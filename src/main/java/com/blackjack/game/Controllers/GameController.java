package com.blackjack.game.Controllers;

import com.blackjack.game.GameModels.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/data")
public class GameController {

    /**
     * CreateGame
     *      returns game id
     *
     * CreatePlayer
     *      returns player id
     *
     * GetGames
     *      returns list of active games
     *
     * JoinGame (game id, player id)
     *      if accepted, returns player-game token
     *
     * GetGameVersion (game id, player id, player-game token)
     *      if accepted, returns game data version id
     *
     * GetGameData (game id, player id, player-game token, version id)
     *      if accepted, returns game data for given version of state
     *
     * game.Player/Hit (game id, player id, player-game token)
     *      updates the state locally
     *
     * game.Player/Stick(game id, player id, player-game token)
     *      updates the state locally
     */

    @Resource
    private GameManager gameManager;

    @Resource
    private PlayerManager playerManager;

    @GetMapping(value = "/CreateGame", produces = "application/json")
    @ResponseBody
    public UUID createGame() {
        return gameManager.createGame();
    }

    @GetMapping(value = "/CreatePlayer", produces = "application/json")
    @ResponseBody
    public UUID createPlayer() {
        return playerManager.createPlayer();
    }

    @GetMapping(value = "/GetGames", produces = "application/json")
    @ResponseBody
    public Set<UUID> GetGames() {
        return gameManager.getGames();
    }

    @GetMapping(value = "/GetPlayers", produces = "application/json")
    @ResponseBody
    public Set<UUID> GetPlayers() {
        return playerManager.getPlayers();
    }

    @GetMapping(value = "/JoinGame", produces = "application/json")
    @ResponseBody
    public UUID joinGame(@RequestParam String gameId,
                         @RequestParam String playerId) {
        GameData gameData = gameManager.getGame(UUID.fromString(gameId));

        if (Objects.isNull(gameData)) {
            return null;
        }

        PlayerData playerData = playerManager.getPlayer(UUID.fromString(playerId));

        if (Objects.isNull(playerData)) {
            return null;
        }

        UUID playerGameToken = gameData.addPlayer(playerData);

        return playerGameToken;
    }

    @GetMapping(value = "/GetGameVersion", produces = "application/json")
    @ResponseBody
    public String getGameVersion(@RequestParam String gameId,
                                 @RequestParam String playerId,
                                 @RequestParam String playerGameToken) {
        UUID uuidGameId = UUID.fromString(gameId);
        GameData gameData = gameManager.getGame(uuidGameId);

        if (Objects.isNull(gameData)) {
            return null;
        }

        UUID uuidPlayerId = UUID.fromString(playerId);
        PlayerData playerData = playerManager.getPlayer(uuidPlayerId);

        if (Objects.isNull(playerData)) {
            return null;
        }

        UUID uuidPlayerToken = UUID.fromString(playerGameToken);
        if (!gameData.isValidPlayer(uuidPlayerId, uuidPlayerToken)) {
            return null;
        }

        return gameData.getVersionId();
    }

    @GetMapping(value = "/GetGameData", produces = "application/json")
    @ResponseBody
    public GameState getGameData(@RequestParam String gameId,
                                 @RequestParam String playerId,
                                 @RequestParam String playerGameToken,
                                 @RequestParam String versionId) {
        UUID uuidGameId = UUID.fromString(gameId);
        GameData gameData = gameManager.getGame(uuidGameId);

        if (Objects.isNull(gameData)) {
            return null;
        }

        UUID uuidPlayerId = UUID.fromString(playerId);
        PlayerData playerData = playerManager.getPlayer(uuidPlayerId);

        if (Objects.isNull(playerData)) {
            return null;
        }

        UUID uuidPlayerToken = UUID.fromString(playerGameToken);
        if (!gameData.isValidPlayer(uuidPlayerId, uuidPlayerToken)) {
            return null;
        }

        GameState gameState = gameData.getGameState();
        if (gameState.getVersionId().equals(versionId)) {
            return null;
        }

        return gameState;
    }

    @GetMapping(value = "/PlayerHit", produces = "application/json")
    @ResponseBody
    public void playerHit(@RequestParam String gameId,
                            @RequestParam String playerId,
                            @RequestParam String playerGameToken) {

        GameData gameData = gameManager.getGame(UUID.fromString(gameId));

        if (Objects.nonNull(gameData)) {
            gameData.playerHit(UUID.fromString(playerId), UUID.fromString(playerGameToken));
        }
    }

    @GetMapping(value = "/PlayerStick", produces = "application/json")
    @ResponseBody
    public void playerStick(@RequestParam String gameId,
                              @RequestParam String playerId,
                              @RequestParam String playerGameToken) {
        GameData gameData = gameManager.getGame(UUID.fromString(gameId));

        if (Objects.nonNull(gameData)) {
            gameData.playerStick(UUID.fromString(playerId), UUID.fromString(playerGameToken));
        }
    }
}
