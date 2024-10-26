package com.kachi.retrofit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kachi.retrofit.ModelResponse.LoginResponse;
import com.kachi.retrofit.ModelResponse.RegisterResponse;
import com.kachi.retrofit.R;
import com.kachi.retrofit.RetrofitApi;
import com.kachi.retrofit.SharedPreference;
import com.kachi.retrofit.URLPaths;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText et_email,et_pass;
    Button login_btn;
    TextView register_link;
    Intent intent;
    SharedPreference sharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_email = (EditText) findViewById(R.id.email);
        et_pass = (EditText) findViewById(R.id.pass);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(Login.this);

        register_link = (TextView) findViewById(R.id.register_link);
        register_link.setOnClickListener(Login.this);

        intent = getIntent();

        sharedPreference = new SharedPreference(getApplicationContext());

    }

    public void onClick(View v) {

        int id = v.getId();

        if(id == R.id.login_btn){
            login();
        }
        else if (id == R.id.register_link) {
            Intent intent = new Intent(Login.this,Register.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }

    public void login(){

        String email = et_email.getText().toString().toLowerCase();
        String pass = et_pass.getText().toString();

        if(email.isEmpty()){
            et_email.requestFocus();
            et_email.setError("Please Enter Your Email");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.requestFocus();
            et_email.setError("Please Enter A Valid Email");
            return;
        }
        if(pass.isEmpty()){
            et_pass.requestFocus();
            et_pass.setError("Please Enter Your Password");
            return;
        }
        if(pass.length()<8){
            et_pass.requestFocus();
            et_pass.setError("Please Enter a 8 Digit Password");
            return;
        }

        //Retrofit
        Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl(URLPaths.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        Call<RegisterResponse> call=retrofitApi.login(email,pass);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                RegisterResponse loginResponse = response.body();

                if (response.isSuccessful()) {

                    if(Objects.requireNonNull(loginResponse).getError().equals("000")){

                        sharedPreference.saveUser(loginResponse.getUser());
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }


                    Toast.makeText(Login.this, Objects.requireNonNull(loginResponse).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(Login.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sharedPreference.isLoggedIn()){
            Intent intent = new Intent(Login.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}