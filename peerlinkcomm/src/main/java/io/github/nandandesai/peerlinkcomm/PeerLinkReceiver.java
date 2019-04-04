package io.github.nandandesai.peerlinkcomm;

import android.util.Log;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class PeerLinkReceiver extends NanoHTTPD {

    private static final String TAG = "PeerLinkReceiver";

    public PeerLinkReceiver(int port) throws IOException {
        super(port);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        Log.d(TAG, "PeerLinkReceiver: Server started in port :"+port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "{\"id\":\"abcd\", \"message\":\"1234\"}";
        return newFixedLengthResponse(Response.Status.OK, "application/json", msg);
    }

    public void stop(){
        super.stop();
    }
}
