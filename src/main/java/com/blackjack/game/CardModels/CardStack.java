package com.blackjack.game.CardModels;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public abstract class CardStack {
    private final List<CardData> cardData = new ArrayList<>();

    void shuffle() {
        for (int i = cardData.size() - 1; i > 0; i--) {
            int newIndex = (int)(Math.random() * i);

            // swap
            CardData temp = cardData.get(newIndex);
            cardData.set(newIndex, cardData.get(i));
            cardData.set(i, temp);
        }
    }

    public CardData pop() {
        if (cardData.size() > 0) {
            return cardData.remove(0);
        }
        return null; // throw exception ?
    }

    public void push(CardData cardData) {
        this.cardData.add(0, cardData);
    }
}
