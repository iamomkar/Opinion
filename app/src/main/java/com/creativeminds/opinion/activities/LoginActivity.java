package com.creativeminds.opinion.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeminds.opinion.R;
import com.creativeminds.opinion.models.LoginResponse;
import com.creativeminds.opinion.models.User;
import com.creativeminds.opinion.retrofit.APIClient;
import com.creativeminds.opinion.retrofit.APIInterface;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    TextView registerTextView,forgotTextView;
    Button login;
    APIInterface apiInterface;
    ProgressDialog p;
    int success;
    @NotEmpty
    @Email
    EditText mEmailId;
    @Password(min = 8, scheme = Password.Scheme.ANY)
    EditText mPassword;
    String message,email,password;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        registerTextView = (TextView)findViewById(R.id.register_text);
        forgotTextView = (TextView)findViewById(R.id.forgot_text);
        login = (Button) findViewById(R.id.login);
        mEmailId = (EditText)findViewById(R.id.email);
        mPassword = (EditText)findViewById(R.id.password);

        validator = new Validator(this);
        validator.setValidationListener(this);

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
                //validator.validate();
                // TODO: 06-04-2019 add validator while building release build
            }
        });

        forgotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Not Implimented", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        email = mEmailId.getText().toString();
        password = mPassword.getText().toString();

        p = ProgressDialog.show(LoginActivity.this, "Logging in..", "Verifying details...", true, false);
        checkLogin();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void checkLogin() {
        apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<LoginResponse> call = apiInterface.checkLogin(email,password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("URL", call.request().url().toString());
                if (response.isSuccessful()) {
                    success = response.body().getSuccess();
                    message = response.body().getMessage();
                    p.cancel();
                    if (success == 1) {
                        User user = response.body().getUser();
                        SharedPreferences.Editor sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE).edit();
                        //Log.d("USER", "onResponse: "+user.getUid());
                        sharedPreferences.putString("uid",user.getUid());
                        sharedPreferences.putString("name",user.getName());
                        sharedPreferences.putString("email",user.getEmail());
                        sharedPreferences.putString("phone",user.getPhone());
                        sharedPreferences.putString("uidno",user.getUidno());
                        sharedPreferences.putString("password",user.getPass());
                        sharedPreferences.putString("gender",user.getGender());
                        sharedPreferences.putString("dob",user.getDob());
                        sharedPreferences.putString("occupation",user.getOccupation());
                        sharedPreferences.putString("state",user.getState());
                        sharedPreferences.putString("city",user.getCity());
                        sharedPreferences.putString("pinCode",user.getPincode());
                        sharedPreferences.putString("secQuestion",user.getSecQuestion());
                        sharedPreferences.putString("secAnswer",user.getSecAnswer());
                        sharedPreferences.apply();
                        finish();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    p.cancel();
                    Toast.makeText(LoginActivity.this, "Error Logging in Try Again\n\n"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("URL", call.request().url().toString());
                Toast.makeText(LoginActivity.this, "Error Logging in Try Again(Device Error)", Toast.LENGTH_SHORT).show();
                p.cancel();
                Log.e("RegisterActivity", t.toString());
            }
        });
    }
}
