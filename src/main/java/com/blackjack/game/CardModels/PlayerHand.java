package com.blackjack.game.CardModels;

public class PlayerHand extends CardStack {

    public PlayerHand() {
        super();
    }

    public int getScore() {
        if (this.getCardData().isEmpty()) {
            return 0;
        }

        int score = this.getCardData().stream()
                .mapToInt(c -> c.getValue().getValue()).sum();

        long aceCount = this.getCardData().stream()
                .filter(c -> Value.ACE.equals(c.getValue())).count();

        while (score > 21 && aceCount > 0) {
            score -= 10;
            aceCount--;
        }

        return score;
    }
}
