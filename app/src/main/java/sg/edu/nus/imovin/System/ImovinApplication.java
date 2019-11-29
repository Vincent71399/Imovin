package sg.edu.nus.imovin.System;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.lang.ref.SoftReference;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import sg.edu.nus.imovin.Common.AuthDownloader;
import sg.edu.nus.imovin.Retrofit.Object.PlanData;
import sg.edu.nus.imovin.Retrofit.Response.UserInfoResponse;
import sg.edu.nus.imovin.Services.MonitorConnectionService;

public class ImovinApplication extends Application {
    private static ImovinApplication instance;
    private static String token;
    private static UserInfoResponse userInfoResponse;
    private static PlanData planData;
    private static Integer target;
    private static OkHttpClient.Builder httpClient;

    private SoftReference<SQLiteDatabase> databaseSoftReference;

    private Intent monitor_connection_service_intent;

    public static ImovinApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("fcm_test", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();

                Log.d("fcm_test", token);
            }
        });

        StartPendingUploadService();
    }

    @Override
    public void onTerminate() {
        stopService(monitor_connection_service_intent);
        super.onTerminate();
    }

    public void StartPendingUploadService(){
        monitor_connection_service_intent = new Intent(getApplicationContext(), MonitorConnectionService.class);
        startService(monitor_connection_service_intent);
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ImovinApplication.token = token;

        initImageLoader();
    }

    public static UserInfoResponse getUserInfoResponse() {
        return userInfoResponse;
    }

    public static void setUserInfoResponse(UserInfoResponse userInfoResponse) {
        ImovinApplication.userInfoResponse = userInfoResponse;
    }

    public static PlanData getPlanData() {
        return planData;
    }

    public static void setPlanData(PlanData planData) {
        ImovinApplication.planData = planData;
    }

    public static Integer getTarget() {
        return target;
    }

    public static void setTarget(Integer target) {
        ImovinApplication.target = target;
    }

    private static void initImageLoader(){
        if(token != null && !token.equals("")) {
            ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(instance);
            config.imageDownloader(new AuthDownloader(instance, "Authorization", "Bearer " + token));

            ImageLoader.getInstance().init(config.build());
        }
    }

    public static OkHttpClient.Builder getHttpClient() {
        if(httpClient == null){
            initHttpClient();
        }
        return httpClient;
    }

    private static void initHttpClient(){
        if(token != null && !token.equals("")){
            httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + ImovinApplication.getToken()).build();
                    return chain.proceed(request);
                }
            });
        }else{
            httpClient = new OkHttpClient.Builder();
        }
    }

    public SQLiteDatabase getDatabase() {
        if(databaseSoftReference != null) {
            return databaseSoftReference.get();
        }else{
            return null;
        }
    }

    public void setDatabase(SQLiteDatabase database) {
        SoftReference<SQLiteDatabase> databaseSoftReference = new SoftReference<>(database);
        this.databaseSoftReference = databaseSoftReference;
    }

}
