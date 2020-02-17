package db;

public interface DBConnection {
	public void close();
	public String getFullname(String userId);
	public boolean verifyLogin(String userId, String password);
	public boolean registerUser(String userId, String password, String firstname, String lastname
			, String phone_number, String email, String car_model, String trip_completed, String promotion);
}
