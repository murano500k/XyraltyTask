package com.stc.xyralitytask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.stc.xyralitytask.data.Creds;
import com.stc.xyralitytask.data.MyResponce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by artem on 1/25/18.
 */

public class RetrofitHelper {
    private static final String TAG = "RetrofitHelper";
    public static final String BASE_URL = "http://backend1.lordsandknights.com/";
    //XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds?login= android.test@xyrality.com&password=password&deviceType=Pixel 8.1.0&deviceId=1066869565";
    private final Retrofit retrofit;
    private final Gson gson;
    private  MyApiInterface api;

    public RetrofitHelper() {
        gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(LenientGsonConverterFactory.create(gson))
                .build();

        api=retrofit.create(MyApiInterface.class);
    }

    public void getResponce(Creds creds,Callback<MyResponce>callback){
        Call<MyResponce> call = api.login(creds);
        call.enqueue(callback);
    }


    public interface MyApiInterface {
        @POST("XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds")
        Call<MyResponce> login(@Body Creds creds);
        @POST("XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds")
        Call<JsonObject> login2(@Body Creds creds);
    }

}
