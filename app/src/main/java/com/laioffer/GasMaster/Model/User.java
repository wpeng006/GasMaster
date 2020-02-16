package com.laioffer.GasMaster.Model;

import com.google.gson.annotations.SerializedName;

public class User {
  @SerializedName(value = "name")
  private String name;
//  @SerializedName (value = "trip")
//  private int trips;
//  @SerializedName(value = "email", alternate = "user_id")
  private String email;
  @SerializedName(value = "password")
  private String password;
  @SerializedName(value = "first_name")
  private String firstName;
  @SerializedName(value = "last_name")
  private String lastName;
  @SerializedName(value = "car_model")
  private String carModel;
  @SerializedName(value = "phone_number")
  private String phone;
  @SerializedName(value = "user_id")
  private String userId;

  public void setStatus(String status) {
    this.status = status;
  }

  private String status;

  private User(UserBuilder builder) {
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
//    this.trips = builder.trips;
    this.email = builder.email;
    this.password = builder.password;
    this.carModel = builder.carModel;
    this.phone = builder.phone;
    this.userId = builder.userId;

  }
// Don't want outside to access first Name and Last Name
//  public String getFirstName() {
//    return firstName;
//  }
//
//  public String getLastName() {
//    return lastName;
//  }

  public String getName() {
    if (name == "") {
      return firstName + lastName;
    }
    return name;
  }

//  public int getTrips() {
//    return trips;
//  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getCarModel() {
    return carModel;
  }

  public String getPhone() {
    return phone;
  }

  public String getUserId() {
    return userId;
  }

  public String getStatus() {
    return status;
  }

  public static class UserBuilder {
    private String firstName = "Unknown";
    private String lastName = "Unknown";
    private String name = "Unknown";
//    private int trips = 0;
    private String email = "Unknown";
    private String password = "Unknown";
    private String carModel = "Unknown";
    private String phone = "Unknown";
    private String userId = "Unknown";

    public UserBuilder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public UserBuilder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public UserBuilder fullName(String name) {
      String[] nameArray = name.split(" ");
      this.name = name;
      this.firstName = nameArray[0];
      this.lastName = nameArray.length > 1 ? nameArray[1] : "Unknown";
      return this;
    }

//    public UserBuilder trips(int trips) {
//      this.trips = trips;
//      return this;
//    }

    public UserBuilder email(String email) {
      this.email = email;
      return this.userId(email);
    }

    public UserBuilder password(String password) {
      this.password = password;
      return this;
    }

    public UserBuilder carModel(String carModel) {
      this.carModel = carModel;
      return this;
    }

    public UserBuilder phone(String phone) {
      this.phone = phone;
      return this;
    }

    private UserBuilder userId(String userId) {
      this.userId = userId;
      return this;
    }
    public User build() {
      return new User(this);
    }

  }
}
