package com.senai.predictguard.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    // ──────────────────────────────────────────────────────────────────────
    // ATENÇÃO: Troque pelo IP correto conforme o ambiente:
    //   → Emulador Android: "http://10.0.2.2:3001/"
    //   → Dispositivo físico na mesma rede Wi-Fi: "http://192.168.X.X:3001/"
    //   → Servidor em nuvem: "https://seu-dominio.com/"0020
    // ──────────────────────────────────────────────────────────────────────
    private static final String BASE_URL = "http://192.168.4.200:3001/";

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            // Logging para debug no Logcat
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}