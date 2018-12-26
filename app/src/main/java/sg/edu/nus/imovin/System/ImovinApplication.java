package sg.edu.nus.imovin.System;

import android.app.Application;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import sg.edu.nus.imovin.Retrofit.Object.UserData;

public class ImovinApplication extends Application {
    private static ImovinApplication instance;
    private static UserData userData;
    private static ImageLoader imageLoader;
    private static OkHttpClient.Builder httpClient;

    private static boolean NeedRefreshForum = false;
    private static boolean NeedRefreshSocialFeed = false;

    public static ImovinApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(instance));
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setUserData(UserData userData) {
        ImovinApplication.userData = userData;
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
        if(userData != null){
            httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer " + ImovinApplication.getUserData().getToken()).build();
                    return chain.proceed(request);
                }
            });
        }else{
            httpClient = null;
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
}
