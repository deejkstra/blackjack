package com.blackjack.game.CardModels;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CardData {
    private final Suit suit;
    private final Value value;

    private boolean isVisible = true;

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        return value.name() + " of " + suit.name();
    }
}
