package com.zhujj.vrplayer.http;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHelper {
    String mUrl;

    public HttpHelper(String url) {
        mUrl = url;
    }

    public void execute(ResponseCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.e("zhujj", mUrl);
        Request build = new Request.Builder().get().url(mUrl).build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                MainThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(string);
                    }
                });
            }
        });
    }

}
