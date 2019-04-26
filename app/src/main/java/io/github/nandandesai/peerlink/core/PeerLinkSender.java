package io.github.nandandesai.peerlink.core;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PeerLinkSender extends Worker {
    private static final String TAG = "PeerLinkSender";
    public static final String SENDER_ADDR="sender_addr";
    public static final String MSG_TO_SEND="msg_to_send";
    private Proxy proxy;

    public PeerLinkSender(@NonNull Context context, @NonNull WorkerParameters workerParams){
        super(context, workerParams);
        proxy=new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 9050));
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: Trying to send a message");
        String address="http://"+getInputData().getString(SENDER_ADDR)+":9000";
        String message=getInputData().getString(MSG_TO_SEND);
        Log.d(TAG, "doWork: Address: "+address+", message: "+message);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .proxy(proxy)
                .build();


        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", message)
                .build();

        Request.Builder builder = new Request.Builder();
        builder.url(address);
        builder.post(requestBody);

        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            Log.d(TAG, "doWork: response :"+response.body().string());
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.retry();
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
