package com.kachi.retrofit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText et_name,et_email,et_pass;
    RadioButton btn_m,btn_f,btn_o;
    Button btn_register;
    TextView login_link;
    Intent intent;
    SharedPreference sharedPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = (EditText) findViewById(R.id.name);
        et_email = (EditText) findViewById(R.id.email);
        et_pass = (EditText) findViewById(R.id.pass);

        btn_m = (RadioButton) findViewById(R.id.btn_m);
        btn_f = (RadioButton) findViewById(R.id.btn_f);
        btn_o = (RadioButton) findViewById(R.id.btn_o);

        btn_register = (Button) findViewById(R.id.register_btn);
        btn_register.setOnClickListener(this);

        login_link = (TextView) findViewById(R.id.login_link);
        login_link.setOnClickListener(this);

        intent = getIntent();

        sharedPreference = new SharedPreference(getApplicationContext());


    }

    public void onClick(View v) {

        int id=v.getId();

        if(id == R.id.register_btn){
            register();
        }
        else if (id == R.id.login_link) {
            Intent intent = new Intent(Register.this,Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void register() {

        String name = et_name.getText().toString();
        String email = et_email.getText().toString().toLowerCase();
        String pass = et_pass.getText().toString();

        if(name.isEmpty()){
            et_name.requestFocus();
            et_name.setError("Please Enter Your Name");
            return;
        }
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

        String gender = null;
        if(btn_m.isChecked()){
            gender = btn_m.getText().toString();
        }
        else if (btn_f.isChecked()){
            gender = btn_f.getText().toString();
        }
        else if (btn_o.isChecked()){
            gender = btn_o.getText().toString();
        }
        String finalGender = gender;


        //Retrofit
        Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl(URLPaths.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        Call<RegisterResponse> call=retrofitApi.register(name,email,pass,finalGender);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {

                RegisterResponse registerResponse = response.body();

                if (response.isSuccessful()) {

                    if(Objects.requireNonNull(registerResponse).getError().equals("000")){

                        sharedPreference.saveUser(registerResponse.getUser());
                        Intent intent = new Intent(Register.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    Toast.makeText(Register.this, Objects.requireNonNull(registerResponse).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable throwable) {
                Toast.makeText(Register.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onStart() {
        super.onStart();

            if(sharedPreference.isLoggedIn()){
                Intent intent = new Intent(Register.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
    }

}