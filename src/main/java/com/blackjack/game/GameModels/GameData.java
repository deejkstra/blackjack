package com.blackjack.game.GameModels;

import com.blackjack.game.CardModels.CardData;
import com.blackjack.game.CardModels.GameDeck;
import lombok.Getter;

import java.util.*;

@Getter
public class GameData {
    private static final int PLAYER_LIMIT = 6;

    private final UUID gameId;
    private final List<PlayerData> playerData;
    private final Map<UUID, UUID> playerTokenMap;

    private GamePhase gamePhase;

    private GameDeck gameDeck;
    private PlayerData dealer;

    private int currentPlayerTurnIndex = 0;

    private String versionId;

    /**
     * initialize
     *      deck
     *      players - 1-6, player order
     *      deal cards
     *
     * player turns
     *      for each player
     *      hit, stick, evaluate, repeat
     *
     * dealer turn
     *
     * post game
     *      winner
     */
    GameData() {
        this.gameId = UUID.randomUUID();
        playerData = new ArrayList<>();
        playerTokenMap = new HashMap<>();
        gamePhase = GamePhase.START;
    }

    public UUID addPlayer(PlayerData playerData) {
        if (this.playerData.contains(playerData)) {
            return playerTokenMap.get(playerData.getPlayerId());
        }

        if (this.playerData.size() < PLAYER_LIMIT) {
            this.playerData.add(playerData);
            UUID token = UUID.randomUUID();
            playerTokenMap.put(playerData.getPlayerId(), token);
            return token;
        }

        return null;
    }

    public void start() {
        if (!GamePhase.START.equals(gamePhase)) {
            return;
        }

        if (playerData.size() == 0) {
            return;
        }

        gameDeck = new GameDeck();
        dealer = new PlayerData();

        playerData.forEach(playerData ->
            playerData.getPlayerHand().push(gameDeck.pop()));

        CardData firstDealerCard = gameDeck.pop();
        firstDealerCard.setVisible(false);
        dealer.getPlayerHand().push(firstDealerCard);

        playerData.forEach(playerData ->
            playerData.getPlayerHand().push(gameDeck.pop()));
        dealer.getPlayerHand().push(gameDeck.pop());

        gamePhase = GamePhase.PLAYER_TURN;
    }

    public UUID getCurrentPlayerToken() {
        PlayerData playerData = this.playerData.get(currentPlayerTurnIndex);
        if (Objects.isNull(playerData)) {
            return null;
        }

        UUID playerId = playerData.getPlayerId();
        if (Objects.isNull(playerId) || !playerTokenMap.containsKey(playerId)) {
            return null;
        }

        return playerTokenMap.get(playerId);
    }

    public boolean isPlayersTurn(UUID playerId, UUID playerToken) {
        PlayerData currentPlayerData = playerData.get(currentPlayerTurnIndex);
        if (currentPlayerData.getPlayerId().equals(playerId)) {
            UUID token = playerTokenMap.get(currentPlayerData.getPlayerId());
            return token.equals(playerToken);
        }

        return false;
    }

    public boolean isValidPlayer(UUID playerId, UUID playerToken) {
        UUID token = playerTokenMap.getOrDefault(playerId, null);
        return Objects.nonNull(token) && token.equals(playerToken);
    }

    public void playerHit(UUID playerId, UUID playerToken) {
        if (!isPlayersTurn(playerId, playerToken)) {
            return;
        }

        PlayerData playerData = this.playerData.get(currentPlayerTurnIndex);
        playerData.getPlayerHand().push(gameDeck.pop());
    }

    public void playerStick(UUID playerId, UUID playerToken) {
        if (!isPlayersTurn(playerId, playerToken)) {
            return;
        }

        currentPlayerTurnIndex++;

        if (currentPlayerTurnIndex >= playerData.size()) {
            gamePhase = GamePhase.DEALER_TURN;
        }
    }

    /**
     * gameId: <UUID />,
     * versionId: <UUID />,
     * state: {
     *      currentPlayerTurn: playerId,
     *      deck: { .. }, ? have to be careful with this data not return actual contents of deck.
     *      dealer: { .. }, ? have to be careful not to send data about facedown card.
     *      players: [ .. ],
     *      isActive: bool
     * }
     *
     * note: when isActive goes from true to false, the game will query a "/GameOutcome" API
     * that surfaces the results of the game.
     */
    public GameState getGameState() {
        UUID currentPlayerId = playerData.get(currentPlayerTurnIndex).getPlayerId();
        int deckSize = gameDeck.getCardData().size();
        GameState gameState = new GameState(gameId, currentPlayerId, deckSize, dealer, playerData, gamePhase);
        versionId = gameState.getVersionId();
        return gameState;
    }
}
