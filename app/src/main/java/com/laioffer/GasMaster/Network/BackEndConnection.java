package com.laioffer.GasMaster.Network;

import com.laioffer.GasMaster.Config.Config;
import com.laioffer.GasMaster.Utility;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackEndConnection {
  private final String BASE_URL = Config.GAS_MASTER_URL;
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