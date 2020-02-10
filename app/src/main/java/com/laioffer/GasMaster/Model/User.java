package com.laioffer.GasMaster.Model;

public class User {
  private String firstName;
  private String lastName;
  private int trips;

  private String email;
  private String password;
  private String carModel;
  private String phone;

  private User(UserBuilder builder) {
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.trips = builder.trips;
    this.email = builder.email;
    this.password = builder.password;
    this.carModel = builder.carModel;
    this.phone = builder.phone;

  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getTrips() {
    return trips;
  }

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

  public static class UserBuilder {
    private String firstName = "";
    private String lastName = "";
    private int trips = 0;
    private String email = "";
    private String password = "";
    private String carModel = "";
    private String phone = "";

    public UserBuilder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public UserBuilder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public UserBuilder trips(int trips) {
      this.trips = trips;
      return this;
    }

    public UserBuilder email(String email) {
      this.email = email;
      return this;
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
    public User build() {
      return new User(this);
    }

  }
}
