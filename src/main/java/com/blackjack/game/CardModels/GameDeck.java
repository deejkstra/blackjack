package com.blackjack.game.CardModels;

import java.util.Arrays;

public class GameDeck extends CardStack {

    public GameDeck() {
        super();

        if (!this.getCardData().isEmpty()) {
            getCardData().clear();
        }

        Arrays.stream(Suit.values()).forEach(s -> {
            Arrays.stream(Value.values()).forEach(v -> {
                this.push(new CardData(s, v));
            });
        });

        shuffle();
    }
}
