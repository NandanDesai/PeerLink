package io.github.nandandesai.peerlink.core;

import android.os.AsyncTask;

import java.net.InetSocketAddress;
import java.net.Proxy;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PeerLinkSender {
    private Proxy proxy;

    public PeerLinkSender(){
        proxy=new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9050));
    }

    public void TESTsend(){
        new RequestServer(proxy).execute();
    }

    private static class RequestServer extends AsyncTask<String, String, String>{
        private Proxy proxy;

        RequestServer(Proxy proxy) {
            this.proxy = proxy;
        }

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .proxy(proxy)
                    .build();
            Request.Builder builder = new Request.Builder();
            builder.url(strings[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("~~~~MESSAGE RESPONSE~~~~ : "+result);
        }
    }
}
