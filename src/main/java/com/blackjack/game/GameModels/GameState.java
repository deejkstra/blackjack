package com.blackjack.game.GameModels;

import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
public class GameState {
    private final UUID gameId;
    private final UUID currentPlayerId;
    private final int deckSize;
    private final PlayerData dealer;
    private final List<PlayerData> playerData;
    private final GamePhase gamePhase;

    public String getVersionId() {
        return Hashing
                .md5()
                .hashString(this.toString(), StandardCharsets.UTF_8)
                .toString();
    }
}
