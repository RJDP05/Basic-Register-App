package com.kachi.retrofit.NavigationFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kachi.retrofit.ModelResponse.LoginResponse;
import com.kachi.retrofit.ModelResponse.PassResponse;
import com.kachi.retrofit.R;
import com.kachi.retrofit.RetrofitApi;
import com.kachi.retrofit.RetrofitClient;
import com.kachi.retrofit.SharedPreference;
import com.kachi.retrofit.URLPaths;

import java.io.StringReader;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    EditText updateName,updateGender,updateCurrent,updateNew;
    SharedPreference sharedPreference;
    Button updateProfile,updatePass;
    String email;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        updateName = view.findViewById(R.id.updateName);
        updateGender = view.findViewById(R.id.updateGender);
        updateProfile = view.findViewById(R.id.updateProfile);
        updateProfile.setOnClickListener(this);
        updateCurrent = view.findViewById(R.id.updateCurrent);
        updateNew = view.findViewById(R.id.updateNew);
        updatePass = view.findViewById(R.id.updatePass);
        updatePass.setOnClickListener(this);

        sharedPreference = new SharedPreference(getActivity());
        email = sharedPreference.getUser().getEmail();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id==R.id.updateProfile){
            updateProfile();
        }
        else if (id == R.id.updatePass) {
            updatePass();
        }

    }

    private void updatePass() {

        String currentPass = updateCurrent.getText().toString();
        String newPass = updateNew.getText().toString();

        if(currentPass.isEmpty()){
            updateCurrent.requestFocus();
            updateCurrent.setError("Please Enter Your Password");
            return;
        }
        if(currentPass.length()<8){
            updateCurrent.requestFocus();
            updateCurrent.setError("Please Enter a 8 Digit Password");
            return;
        }
        if(newPass.isEmpty()){
            updateNew.requestFocus();
            updateNew.setError("Please Enter Your Password");
            return;
        }
        if(newPass.length()<8){
            updateNew.requestFocus();
            updateNew.setError("Please Enter a 8 Digit Password");
            return;
        }

        Call<PassResponse> call = RetrofitClient.getInstance().getAPI().changepass(currentPass,newPass,email);

        call.enqueue(new Callback<PassResponse>() {
            @Override
            public void onResponse(Call<PassResponse> call, Response<PassResponse> response) {
                PassResponse passResponse = response.body();
                if (response.isSuccessful()) {
                    if (passResponse.getError().equals("200")){
                        Toast.makeText(getActivity(), passResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PassResponse> call, Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateProfile() {

        String name = updateName.getText().toString();
        String gender = updateGender.getText().toString();

        if(name.isEmpty()){
            updateName.requestFocus();
            updateName.setError("Please Enter Your Name");
            return;
        }
        if(gender.isEmpty()){
            updateGender.requestFocus();
            updateGender.setError("Please Enter Gender");
            return;
        }

        //Retrofit
        Call<LoginResponse> call= RetrofitClient.getInstance().getAPI().userupdate(name,email,gender);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse updateResponse = response.body();

                if (response.isSuccessful()){
                    if (updateResponse.getError().equals("000")){
                        sharedPreference.saveUser(updateResponse.getUser());
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), updateResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable throwable) {

                View layout = getLayoutInflater().inflate(R.layout.toast,getActivity().findViewById(R.id.custom_text_layout));
                TextView tv = layout.findViewById(R.id.toast_text);
                tv.setText(throwable.getMessage());

                Toast toast = new Toast(getActivity());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();

                //Toast.makeText(getActivity(),throwable.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });


    }
}