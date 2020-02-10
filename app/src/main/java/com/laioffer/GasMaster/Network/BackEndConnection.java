package com.laioffer.GasMaster.Network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackEndConnection {
  private final String BASE_URL = "http://192.168.0.0/8080";
  private static BackEndConnection connection;
  private BackEndService service;

  public static BackEndConnection getInstance() {
    if (connection == null) {
      connection = new BackEndConnection();
    }
    return connection;
  }

  public BackEndService createService() {
    if (service == null) {
      Retrofit retrofit = new Retrofit.Builder()
        .client(getClient())
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
      service = retrofit.create(BackEndService.class);
    }
    return service;
  }

  private OkHttpClient getClient() {
    OkHttpClient client = new OkHttpClient.Builder()
      .build();
    return client;
  }

}
