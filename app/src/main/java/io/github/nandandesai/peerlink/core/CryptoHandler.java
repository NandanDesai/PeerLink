package io.github.nandandesai.peerlink.core;

import org.whispersystems.libsignal.DuplicateMessageException;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.InvalidMessageException;
import org.whispersystems.libsignal.InvalidVersionException;
import org.whispersystems.libsignal.LegacyMessageException;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.protocol.PreKeySignalMessage;

import java.io.IOException;

import io.github.nandandesai.peerlink.utils.Base64;


public class CryptoHandler {
    private SessionCipher sessionCipher;

    public CryptoHandler(SessionCipher sessionCipher){
        this.sessionCipher=sessionCipher;
    }

    //returns the cipher text in Base64 encoded text
    public String encrypt(String plainText) throws UntrustedIdentityException, InvalidVersionException, InvalidMessageException {
        //the encrypt() method expects "paddedMessage". I don't know what the hell that is. So, I'm just gonna pass the bytes that I have.
        //figure out what Padded Message is.
        //for reference, go to this link:
        // https://github.com/signalapp/libsignal-service-java/blob/master/java/src/main/java/org/whispersystems/signalservice/internal/push/PushTransportDetails.java
        PreKeySignalMessage preKeySignalMessage = new PreKeySignalMessage(sessionCipher.encrypt(plainText.getBytes()).serialize());
        return Base64.encodeBytes(preKeySignalMessage.serialize());
    }

    //returns plain text in text based messages
    public String decrypt(String cipherText) throws InvalidKeyException, LegacyMessageException, InvalidMessageException, DuplicateMessageException, InvalidKeyIdException, UntrustedIdentityException, IOException, InvalidVersionException {
        //the received text will be Base64 encoded.So, we'll decode it and feed the bytes to PreKeySignalMessage
        PreKeySignalMessage preKeySignalMessage=new PreKeySignalMessage(Base64.decode(cipherText));

        //not sure if this is the right way to do it, i.e. using toString() here.
        return sessionCipher.decrypt(preKeySignalMessage).toString();
    }
}
