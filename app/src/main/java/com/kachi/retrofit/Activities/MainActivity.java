package com.kachi.retrofit.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kachi.retrofit.ModelResponse.PassResponse;
import com.kachi.retrofit.NavigationFragments.HomeFragment;
import com.kachi.retrofit.NavigationFragments.ProfileFragment;
import com.kachi.retrofit.NavigationFragments.UsersFragment;
import com.kachi.retrofit.R;
import com.kachi.retrofit.RetrofitClient;
import com.kachi.retrofit.SharedPreference;

import java.util.Objects;

import okhttp3.Cache;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    Intent intent;
    SharedPreference sharedPreference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        intent = getIntent();

        loadFragment(new HomeFragment());

        sharedPreference = new SharedPreference(getApplicationContext());

        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            //Back
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Welcome");
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Fragment fragment = null;

        if(id == R.id.home){
            fragment = new HomeFragment();
        }
        else if (id == R.id.users) {
            fragment = new UsersFragment();
        }
        else if (id == R.id.profile){
            fragment = new ProfileFragment();
        }

        if (fragment!=null){
            loadFragment(fragment);
        }

        return true;
    }

    public void loadFragment(Fragment fragment){
        //Attach Fragment to Container
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.logout){
            logoutUser();
        }
        else if (id == R.id.delete) {
            deleteacc();
        }
        else {
            super.getOnBackPressedDispatcher();
        }
        return super.onOptionsItemSelected(item);
    }


    private void logoutUser() {

        sharedPreference.logout();
        Intent intent = new Intent(MainActivity.this,Register.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        Toast.makeText(this, "You have Been Logged Out", Toast.LENGTH_SHORT).show();

    }

    private void deleteacc() {
        Call<PassResponse> call = RetrofitClient.getInstance().getAPI().deleteacc(sharedPreference.getUser().getEmail());

        call.enqueue(new Callback<PassResponse>() {
            @Override
            public void onResponse(Call<PassResponse> call, Response<PassResponse> response) {
                PassResponse delete = response.body();
                if(response.isSuccessful()){

                    if (delete.getError().equals("000")){
                        logoutUser();
                        Toast.makeText(MainActivity.this, delete.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, delete.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PassResponse> call, Throwable throwable) {

                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}