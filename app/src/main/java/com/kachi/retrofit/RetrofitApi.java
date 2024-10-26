package com.kachi.retrofit;

import com.kachi.retrofit.ModelResponse.LoginResponse;
import com.kachi.retrofit.ModelResponse.PassResponse;
import com.kachi.retrofit.ModelResponse.RegisterResponse;
import com.kachi.retrofit.ModelResponse.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitApi {

    @FormUrlEncoded
    @POST("register")
    Call<RegisterResponse> register(
            @Field("key_name") String name,
            @Field("key_email") String email,
            @Field("key_pass") String pass,
            @Field("key_gender") String gender

    );

    @FormUrlEncoded
    @POST("login")
    Call<RegisterResponse> login(
            @Field("key_email") String email,
            @Field("key_pass") String pass
    );

    @GET("alluser")
    Call<UserResponse> alluser();

    @FormUrlEncoded
    @POST("userupdate")
    Call<LoginResponse> userupdate(
            @Field("key_name") String name,
            @Field("key_email") String email,
            @Field("key_gender") String gender
    );

    @FormUrlEncoded
    @POST("changepass")
    Call<PassResponse> changepass(

            @Field("key_current") String currentPass,
            @Field("key_new") String newPass,
            @Field("key_email") String email
    );

    @FormUrlEncoded
    @POST("deleteacc")
    Call<PassResponse> deleteacc(
            @Field("key_email") String email
    );

}
