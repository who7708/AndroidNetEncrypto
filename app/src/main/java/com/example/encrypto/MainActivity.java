package com.example.encrypto;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encrypto.http.HttpRequest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * 服务端url地址
     */
    private static final String URL = "http://192.168.31.10:7001";
    private static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final HttpRequest request = new HttpRequest(URL);
        findViewById(R.id.send_request_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request.request(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Log.e(TAG, "GET请求失败", e);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Log.i(TAG, "GET请求成功， result -> " + Objects.requireNonNull(response.body()).string());
                    }
                });
            }
        });
    }
}