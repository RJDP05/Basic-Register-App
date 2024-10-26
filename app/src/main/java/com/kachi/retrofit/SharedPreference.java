package com.kachi.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.kachi.retrofit.ModelResponse.User;

public class SharedPreference {

    private static String SHARED_PREF_NAME = null;
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SharedPreference(Context context) {
        this.context = context;
    }

    public void saveUser(User user){

        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("name",user.getName());
        editor.putString("email",user.getEmail());
        editor.putString("gender",user.getGender());
        editor.putBoolean("logged",true);
        editor.apply();

    }

    public boolean isLoggedIn(){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged",false);
    }

    public User getUser(){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString("name",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("gender",null));
    }

    public void logout(){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
