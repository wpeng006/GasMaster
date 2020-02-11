package com.laioffer.GasMaster.Network;

import com.laioffer.GasMaster.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackEndService {
  @POST("Login")
  Call<User> login(@Body User user);

  @POST("Register")
  Call<User> register(@Body User user);
}
