package utils;

import okhttp3.*;

public class HttpClientUtil {


    public final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .build();


    public static void runAsync(String finalUrl, Callback callback) {
        Request request = new Request.Builder()
                .url(finalUrl)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }





}
