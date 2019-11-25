package com.blackjack.game.Controllers;

import com.blackjack.game.GameModels.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/data")
@Slf4j
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

    @GetMapping(value = "/game/create", produces = "application/json")
    @ResponseBody
    public UUID createGame() {
        return gameManager.createGame();
    }

    @GetMapping(value = "/player/create", produces = "application/json")
    @ResponseBody
    public UUID createPlayer() {
        return playerManager.createPlayer();
    }

    @GetMapping(value = "/game/getAll", produces = "application/json")
    @ResponseBody
    public Set<UUID> getGames() {
        return gameManager.getGames();
    }

    @GetMapping(value = "/player/getAll", produces = "application/json")
    @ResponseBody
    public Set<UUID> getPlayers() {
        return playerManager.getPlayers();
    }

    @GetMapping(value = "/game/join", produces = "application/json")
    @ResponseBody
    public UUID joinGame(@RequestParam String gameId,
                         @RequestParam String playerId) {
        log.info("joining game. gameId: " + gameId + ", playerId: " + playerId);
        GameData gameData = gameManager.getGame(UUID.fromString(gameId));

        if (Objects.isNull(gameData)) {
            log.info("cancel joining game, no game data. gameId: " + gameId + ", playerId: " + playerId);
            return null;
        }

        PlayerData playerData = playerManager.getPlayer(UUID.fromString(playerId));

        if (Objects.isNull(playerData)) {
            log.info("cancel joining game, no player data. gameId: " + gameId + ", playerId: " + playerId);
            return null;
        }

        UUID playerGameToken = gameData.addPlayer(playerData);

        log.info("joined game. gameId: " + gameId + ", playerId: " + playerId + ", token: " + playerGameToken);

        return playerGameToken;
    }

    @GetMapping(value = "/game/version", produces = "application/json")
    @ResponseBody
    public List<String> getGameVersion(@RequestParam String gameId,
                                       @RequestParam String playerId,
                                       @RequestParam String playerGameToken) {
        log.info("game version. gameId: " + gameId + ", playerId: " + playerId + ", token: " + playerGameToken);
        UUID uuidGameId = UUID.fromString(gameId);
        GameData gameData = gameManager.getGame(uuidGameId);

        if (Objects.isNull(gameData)) {
            log.info("cancel game version, no game data. gameId: " + gameId + ", playerId: " + playerId + ", token: " + playerGameToken);
            return null;
        }

        UUID uuidPlayerId = UUID.fromString(playerId);
        PlayerData playerData = playerManager.getPlayer(uuidPlayerId);

        if (Objects.isNull(playerData)) {
            log.info("cancel game version, no player data. gameId: " + gameId + ", playerId: " + playerId + ", token: " + playerGameToken);
            return null;
        }

        UUID uuidPlayerToken = UUID.fromString(playerGameToken);
        if (!gameData.isValidPlayer(uuidPlayerId, uuidPlayerToken)) {
            log.info("cancel game version, invalid playerId. gameId: " + gameId + ", playerId: " + playerId + ", token: " + playerGameToken);
            return null;
        }

        String version = gameData.getVersionId();

        log.info("game version. gameId: " + gameId + ", playerId: " + playerId + ", token: " + playerGameToken + ", version: " + version);

        return Lists.newArrayList(version);
    }

    @GetMapping(value = "/game/start", produces = "application/json")
    @ResponseBody
    public void startGame(@RequestParam String gameId) {
        UUID uuidGameId = UUID.fromString(gameId);
        GameData gameData = gameManager.getGame(uuidGameId);

        if (Objects.isNull(gameData)) {
            return;
        }

        gameData.start();
    }

    @GetMapping(value = "/game/state", produces = "application/json")
    @ResponseBody
    public GameState getGameState(@RequestParam String gameId,
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

        GameState gameState = gameData.getGameState();

        return gameState;
    }

    @GetMapping(value = "/player/action/hit", produces = "application/json")
    @ResponseBody
    public void playerHit(@RequestParam String gameId,
                          @RequestParam String playerId,
                          @RequestParam String playerGameToken) {

        GameData gameData = gameManager.getGame(UUID.fromString(gameId));

        if (Objects.nonNull(gameData)) {
            gameData.playerHit(UUID.fromString(playerId), UUID.fromString(playerGameToken));
        }
    }

    @GetMapping(value = "/player/action/stick", produces = "application/json")
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
