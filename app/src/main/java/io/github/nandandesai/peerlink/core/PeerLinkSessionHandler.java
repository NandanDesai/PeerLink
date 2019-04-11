package io.github.nandandesai.peerlink.core;

import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.SessionBuilder;
import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.UntrustedIdentityException;
import org.whispersystems.libsignal.state.PreKeyBundle;
import org.whispersystems.libsignal.state.SignalProtocolStore;

public class PeerLinkSessionHandler {
    private SignalProtocolStore store;
    //all the Android mobile clients of PeerLink will have deviceId as 1
    private static final int peerDeviceId=1;
    public PeerLinkSessionHandler(SignalProtocolStore store) {
        this.store = store;
    }

    //when chatting for the first time,
    public SessionCipher buildNewSession(String peerAddress, PreKeyBundle peerKeyBundle) throws UntrustedIdentityException, InvalidKeyException {
        SignalProtocolAddress protocolAddress=new SignalProtocolAddress(peerAddress, peerKeyBundle.getDeviceId());
        SessionBuilder builder = new SessionBuilder(store, protocolAddress);
        builder.process(peerKeyBundle);
        return new SessionCipher(store, protocolAddress);
    }

    //for subsequent messages
    public SessionCipher getSessionCipher(String peerAddress){
        SignalProtocolAddress protocolAddress=new SignalProtocolAddress(peerAddress,peerDeviceId);
        return new SessionCipher(store, protocolAddress);
    }

}
