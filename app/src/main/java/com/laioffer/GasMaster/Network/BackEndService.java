package com.laioffer.GasMaster.Network;

import com.laioffer.GasMaster.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BackEndService {
  @POST("/login")
  Call<User> login(@Body User user);

  @POST("/register")
  Call<User> register(@Body User user);
}
