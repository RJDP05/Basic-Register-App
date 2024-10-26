package com.kachi.retrofit.NavigationFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kachi.retrofit.ModelResponse.LoginResponse;
import com.kachi.retrofit.ModelResponse.User;
import com.kachi.retrofit.ModelResponse.UserResponse;
import com.kachi.retrofit.R;
import com.kachi.retrofit.RetrofitApi;
import com.kachi.retrofit.URLPaths;
import com.kachi.retrofit.UserAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    List<User> userList;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Retrofit

        Retrofit retrofit = new  Retrofit.Builder()
                .baseUrl(URLPaths.MAIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        Call<UserResponse> call=retrofitApi.alluser();
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

                if(response.isSuccessful()){
                    userList = response.body().getUserList();
                    recyclerView.setAdapter(new UserAdapter(getActivity(),userList));
                }
                else {
                    Toast.makeText(getActivity(), response.body().getError(), Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {

                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}