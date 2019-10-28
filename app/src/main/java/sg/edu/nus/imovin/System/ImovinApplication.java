package sg.edu.nus.imovin.System;

import android.app.Application;
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
import sg.edu.nus.imovin.Retrofit.Object.PlanData;
import sg.edu.nus.imovin.Retrofit.Object.UserData;
import sg.edu.nus.imovin.Retrofit.Response.UserInfoResponse;

public class ImovinApplication extends Application {
    private static ImovinApplication instance;
    private static String token;
    private static UserData userData;
    private static UserInfoResponse userInfoResponse;
    private static PlanData planData;
    private static Integer target;
    private static ImageLoader imageLoader;
    private static OkHttpClient.Builder httpClient;

    private static boolean NeedRefreshForum = false;
    private static boolean NeedRefreshSocialFeed = false;
    private static boolean NeedRefreshPlanGoal = false;
    private static boolean NeedRefreshPlanMonitor = false;

    private SoftReference<SQLiteDatabase> databaseSoftReference;

    public static ImovinApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(instance));
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
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        ImovinApplication.token = token;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setUserData(UserData userData) {
        ImovinApplication.userData = userData;
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

    public static ImageLoader getImageLoader() {
        return imageLoader;
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

    public static boolean isNeedRefreshForum() {
        return NeedRefreshForum;
    }

    public static void setNeedRefreshForum(boolean needRefreshForum) {
        NeedRefreshForum = needRefreshForum;
    }

    public static boolean isNeedRefreshSocialFeed(){
        return NeedRefreshSocialFeed;
    }

    public static void setNeedRefreshSocialNeed(boolean needRefreshSocialFeed){
        NeedRefreshSocialFeed = needRefreshSocialFeed;
    }

    public static boolean isNeedRefreshPlanGoal() {
        return NeedRefreshPlanGoal;
    }

    public static void setNeedRefreshPlanGoal(boolean needRefreshPlanGoal) {
        NeedRefreshPlanGoal = needRefreshPlanGoal;
    }

    public static boolean isNeedRefreshPlanMonitor() {
        return NeedRefreshPlanMonitor;
    }

    public static void setNeedRefreshPlanMonitor(boolean needRefreshPlanMonitor) {
        NeedRefreshPlanMonitor = needRefreshPlanMonitor;
    }

    public static void setNeedRefreshPlan(boolean needRefreshPlan){
        NeedRefreshPlanGoal = needRefreshPlan;
        NeedRefreshPlanMonitor = needRefreshPlan;
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
