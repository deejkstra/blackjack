package com.blackjack.game.GameModels;

import com.blackjack.game.CardModels.PlayerHand;
import lombok.Getter;

import java.util.UUID;

@Getter
public class PlayerData {
    private final PlayerHand playerHand;
    private final UUID playerId;

    PlayerData() {
        this.playerHand = new PlayerHand();
        this.playerId = UUID.randomUUID();
    }
}
