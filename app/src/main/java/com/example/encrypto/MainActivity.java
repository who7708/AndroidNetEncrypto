package com.example.encrypto;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.crypto.AES;
import com.example.crypto.Base64;
import com.example.crypto.DH;
import com.example.crypto.RSA;
import com.example.crypto.constant.Constants;
import com.example.crypto.utils.DataUtils;
import com.example.encrypto.http.HttpRequest;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * 服务端url地址
     */
    private static final String URL = "http://192.168.31.10:7001";
    private static final String TAG = MainActivity.class.getName();

    private byte[] mAesKey;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.show_result_tv);

        final HttpRequest request = new HttpRequest(URL);
        findViewById(R.id.send_request_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DH dh = new DH();
                int publicKey = dh.getPublicKey();

                // 通过判断 aes 密钥是不可用，来确定是不需要握手
                // 如果 aes 密钥不可用，则发送握手请求
                // if (mAesKey == null || mAesKey.length <= 0) {
                    Log.d(TAG, "客户端生成的 DH PUBLIC KEY IS " + publicKey);
                    request.handshake(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e(TAG, "握手请求失败", e);
                            MainActivity.this.showResult("握手请求失败" + e.getMessage());
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            // 返回服务端生成的 dh 公钥
                            byte[] publicKey = response.body().bytes();
                            int result = DataUtils.byte2Int(publicKey);
                            Log.d(TAG, "服务端生成的 DH PUBLIC KEY IS " + result);
                            MainActivity.this.showResult("握手请求成功， result -> " + result);

                            // 根据服务端的 dh 公钥，生成AES的密钥
                            mAesKey = dh.getSecretKey(publicKey);
                            Log.d(TAG, "根据服务端 DH 公钥生成的 AES KEY IS " + Base64.encodeToString(mAesKey, Base64.NO_WRAP));
                        }
                    }, RSA.encrypt(publicKey, Constants.RSA_PUBLIC_KEY));
                // } else {
                //     // 如果已经有 aes key, 则直接发送普通请求
                //     request.request(new Callback() {
                //         @Override
                //         public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //             Log.e(TAG, "GET请求失败", e);
                //             MainActivity.this.showResult("GET请求失败" + e.getMessage());
                //         }
                //
                //         @Override
                //         public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //             // 请求成功，进行数据处理
                //             byte[] responseContent = response.body().bytes();
                //             AES aes = new AES(mAesKey);
                //             String content = new String(aes.decrypt(responseContent));
                //             Log.d(TAG, "GET请求成功， result -> " + content);
                //             MainActivity.this.showResult("GET请求成功， result -> " + content);
                //         }
                //     });
                // }
            }
        });
    }

    private void showResult(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(result);
            }
        });
    }
}