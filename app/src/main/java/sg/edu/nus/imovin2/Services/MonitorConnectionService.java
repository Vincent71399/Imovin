package sg.edu.nus.imovin2.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.Common.OtherFunc;
import sg.edu.nus.imovin2.GreenDAO.LogFuncClick;
import sg.edu.nus.imovin2.Retrofit.Request.DailyLogRequest;
import sg.edu.nus.imovin2.Retrofit.Response.DailyLogResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;

public class MonitorConnectionService extends Service {
    private static final String TAG = "Monitoring";
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isAvailable()){
                    Log.d(TAG, networkInfo.getTypeName() + "-" + networkInfo.getSubtypeName());
                    new Thread(new LogUploadThread()).start();
                }else{
                    Log.d(TAG, "No Connection");
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service Start");
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(connectionReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class LogUploadThread implements Runnable {

        boolean keepRunning = true;
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (keepRunning) {
                try {
                    if(getApplicationContext() != null){
                        UploadPendingLogs();
                    }
                    Thread.sleep(3600000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    keepRunning = false;
                }
            }
        }
    }

    private void UploadPendingLogs(){
        if(OtherFunc.GetDBFunction() != null) {
            List<LogFuncClick> pendingUploadLogs = OtherFunc.GetDBFunction().queryLogFuncClick_need_upload();
            for (LogFuncClick logFuncClick : pendingUploadLogs) {
                Log.d("Monitoring", String.valueOf(logFuncClick.getId()));
                UploadSinglePendingLog(logFuncClick);
            }
        }
    }

    private void UploadSinglePendingLog(LogFuncClick logFuncClick){
        String dateString = CommonFunc.GetDisplayDate(logFuncClick.getRecordDateYear(), logFuncClick.getRecordDateMonth(), logFuncClick.getRecordDateDay()) ;

        DailyLogRequest dailyLogRequest = new DailyLogRequest(
                dateString,
                logFuncClick.getHomeCount(),
                logFuncClick.getChallengeCount(),
                logFuncClick.getLibraryCount(),
                logFuncClick.getSocialCount(),
                logFuncClick.getForumCount(),
                logFuncClick.getMonitorCount(),
                logFuncClick.getGoalCount()
        );

        Log.d(LogConstants.LogTag, "Home Count : " + logFuncClick.getHomeCount());
        Log.d(LogConstants.LogTag, "Challenge Count : " + logFuncClick.getChallengeCount());
        Log.d(LogConstants.LogTag, "Library Count : " + logFuncClick.getLibraryCount());
        Log.d(LogConstants.LogTag, "Social Count : " + logFuncClick.getSocialCount());
        Log.d(LogConstants.LogTag, "Forum Count : " + logFuncClick.getForumCount());
        Log.d(LogConstants.LogTag, "Monitor Count : " + logFuncClick.getMonitorCount());
        Log.d(LogConstants.LogTag, "Goal Count : " + logFuncClick.getGoalCount());
        UploadDailyLogRequest(dailyLogRequest, logFuncClick);
    }

    private void UploadDailyLogRequest(DailyLogRequest dailyLogRequest, final LogFuncClick logFuncClick){
        OtherFunc.GetDBFunction().updateLogFuncClickFlag_to_IsUploading(logFuncClick);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<DailyLogResponse> call = service.postDailyLog(dailyLogRequest);
        call.enqueue(new Callback<DailyLogResponse>() {
            @Override
            public void onResponse(Call<DailyLogResponse> call, Response<DailyLogResponse> response) {
                try {
                    DailyLogResponse dailyLogResponse = response.body();
                    String result = dailyLogResponse.get_status();
                    Log.d(LogConstants.LogTag, result);
                    if(result.equals("OK")){
                        OtherFunc.GetDBFunction().updateLogFuncClickFlag_to_UploadFinished(logFuncClick);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception upload daily log : " + e.toString());
                    OtherFunc.GetDBFunction().updateLogFuncClickFlag_to_PendingUpdate(logFuncClick);
                }
            }

            @Override
            public void onFailure(Call<DailyLogResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure upload daily log : " + t.toString());
                OtherFunc.GetDBFunction().updateLogFuncClickFlag_to_PendingUpdate(logFuncClick);
            }
        });
    }

}
