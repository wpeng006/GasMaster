package com.laioffer.GasMaster;


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

import androidx.appcompat.app.AppCompatActivity;

import com.laioffer.GasMaster.Model.User;
import com.laioffer.GasMaster.Network.BackEndConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    // hold onto a GasMaster back end connection
    private BackEndConnection backEndConnection;

    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // login button logic
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Sign up activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        // initialize backendConnection
        backEndConnection = BackEndConnection.getInstance();
    }

    private void login() {
        Log.d(TAG, "Login");

        if (!Validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // Todo: Authentication Implementation

//        User loginUser = new User.UserBuilder()
//          //.email(email)
//          .password(password)
//          .email(email)
//          .build();
//
//        Call<User> loginCall = backEndConnection.createService().login(loginUser);
//        loginCall.enqueue(new Callback<User>() {
//
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.body() == null) {
//                    Log.e(TAG, "Login Failure: unable to get response" + " "+ response.code());
//                    onLoginFailed();
//                }
//                if (response.isSuccessful()) {
//                    Log.e(TAG, "Login Response Successful" + " " + response.body().getName());
//                    onLoginSuccess();
//                    progressDialog.dismiss();
//                } else {
//                    Log.e(TAG, "Login Failure: unable to get response" + response.code());
//                    onLoginFailed();
//                }
//                progressDialog.dismiss();
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Log.e(TAG, "Login Failure: unable to get response" + t.getLocalizedMessage());
//                onLoginFailed();
//                progressDialog.dismiss();
//            }
//        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    // login success
    private void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    // operations after login failed
    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
        _loginButton.setEnabled(true);
    }

    // validate if login successful
    private boolean Validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Please enter a valid email address!");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        // validate password, 4 < length < 20
        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("Password is between 4 and 20 alphanumeric characters!");
            valid = false;
        } else {
            _passwordText
                    .setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Successful sign up logic
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
