package utils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClientUtilAllies {

    public final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .build();


    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtilAllies.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }




}
