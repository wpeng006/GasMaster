package com.laioffer.GasMaster.Network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.laioffer.GasMaster.Config.Config.GAS_MASTER_URL;

public class BackEndConnection {
  private final String BASE_URL = GAS_MASTER_URL;
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