package com.blackjack.game.GameModels;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class GameState implements Serializable {
    private final UUID gameId;
    private final UUID currentPlayerId;
    private final int deckSize;
    private final PlayerData dealer;
    private final List<PlayerData> playerData;
    private final GamePhase gamePhase;

    private String versionId;

    @PostConstruct
    void init() {
        try {
            this.versionId = getChecksum(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getChecksum(Serializable object) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(baos.toByteArray());
            return DatatypeConverter.printHexBinary(thedigest);
        } finally {
            oos.close();
            baos.close();
        }
    }
}
