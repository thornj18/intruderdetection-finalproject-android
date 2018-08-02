package com.finalproject.intruderdetection;

import com.finalproject.intruderdetection.models.RegisterModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by TKPC on 1/17/2018.
 */

public class Api {

    public static final String BASE_URL = "http://192.168.8.102:5000/";
    public static final String FILE_SERVER_GET_URL = BASE_URL+"photo/";

    public static final String TAG = "OpenFace";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface OpenfaceApi {

        @FormUrlEncoded
        @POST("/user/login")
        Call<ResponseBody> login(@Field("phone") String phone, @Field("password") String password);

        @POST("/user/create")
        Call<ResponseBody> register(@Body RegisterModel registerModel);

        @GET()
        Call<ResponseBody> blacklist(@Url String url, @Query("state") boolean state);

        @GET()
        Call<ResponseBody> delete(@Url String url);

        @GET("users")
        Call<ResponseBody> get();

        @GET()
        Call<ResponseBody> getUser(@Url String url);

        @GET()
        Call<ResponseBody> getUserEvents(@Url String url);

        @GET("lock_door")
        Call<ResponseBody> lock();

        @GET("unlock_door")
        Call<ResponseBody> unlock();

        @GET("events")
        Call<ResponseBody> getEvents();

        @Multipart
        @POST("upload")
        Call<ResponseBody> uploadAvatar(@Part("user_id") RequestBody id, @Part("username") RequestBody username, @Part MultipartBody.Part photo);


    }
}
