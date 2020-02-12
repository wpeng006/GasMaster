package com.laioffer.GasMaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.laioffer.GasMaster.Model.User;
import com.laioffer.GasMaster.Network.BackEndConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";


    // hold on to a GasMaster connection
    private BackEndConnection backEndConnection;
    @BindView(R.id.input_firstName) EditText _firstNameText;
    @BindView(R.id.input_lastName) EditText _lastNameText;

    @BindView(R.id.input_car) EditText _carText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        // initialize backendConnection
        backEndConnection = BackEndConnection.getInstance();
    }

    private void signup() {
        Log.d(TAG, "Signup");

        if(!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String car = _carText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // Todo: Signup logic Implementation
        User user = new User.UserBuilder()
          .email(email)
          .fullName(name)
          .phone(mobile)
          .password(password)
          .carModel(car)
          .build();

        Call<User> loginCall = backEndConnection.createService().register(user);
        loginCall.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e(TAG, "Register Response" + response.body());
                if (response.isSuccessful() && response.body().getStatus().equals("OK")) {
                    onSignupSuccess();
                } else {
                    Log.e(TAG, "Register Failed" + response.body().getStatus());
                    Toast.makeText(getBaseContext(), "Register Failed" + " "+ response.body().getStatus(), Toast.LENGTH_LONG).show();
                    onSignupFailed();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Register Failure: unable to get response" + t.getLocalizedMessage());
                onSignupFailed();
                progressDialog.dismiss();
            }
        });

        //////////////////////////////////////what does it do
//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        // On complete call either onSignupSuccess or onSignupFailed
//                        // depending on success
//                        onSignupSuccess();
//                        // onSignupFailed();
//                        progressDialog.dismiss();
//                    }
//                }, 3000
//        );

    }


    private void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();

    }

    private void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login Failed!", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    // check if each field is validate
    private boolean validate() {
        boolean valid = true;

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String car = _carText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // check first name field
        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("Please enter a valid fist name!");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        // check last name field
        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("Please enter a valid last name!");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        // check car model field
        if (car.isEmpty()) {
            _carText.setError("Please enter a valid car model!");
            valid = false;
        } else {
            _carText.setError(null);
        }

        // check email field
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Please enter a valid Email address!");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        // check mobile field
        // phone validation logic can be improved
        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Please enter a valid phone number!");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        // check password field
        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("Password is between 4 and 20 alphanumeric characters!");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        // check Re-enter password field
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 ||
                reEnterPassword.length() > 20 || !reEnterPassword.equals(password)) {
            _reEnterPasswordText.setError("Password is not matched!");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }


}
