package sg.edu.nus.imovin.Services;

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

import sg.edu.nus.imovin.Common.OtherFunc;
import sg.edu.nus.imovin.GreenDAO.LogFuncClick;
import sg.edu.nus.imovin.HttpConnection.UploadRequests;

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
                        if(!HavePendingLogs()){
                            Log.d(TAG, "No Pending Record Found, Stop Service");
                            keepRunning = false;
                        }
                    }else{
                        keepRunning = false;
                    }
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    keepRunning = false;
                }
            }
//            stopSelf();
        }
    }

    private void UploadPendingLogs(){
        List<LogFuncClick> pendingUploadLogs = OtherFunc.GetDBFunction().queryLogFuncClick_need_upload();
        for(LogFuncClick logFuncClick : pendingUploadLogs){
            Log.d("Monitoring", String.valueOf(logFuncClick.getId()));

//            String return_id = UploadRequests.UploadVasCogTestResult(vasCogRecord.getPostData());
//            if(return_id != null){
//                vasCogRecord.setIsPendingUpload(false);
//                OtherFunc.GetDBFunction().updateVasCogRecordStatus(vasCogRecord);
//                OtherFunc.GetDBFunction().updateOnlineRecordId_by_Id(vasCogRecord.getId(), return_id);
//            }
        }
//        OtherFunc.GetDBFunction().removeUpdatedVasCogRecord();
//        UploadRequests.UploadVasEditResult();
    }

    private boolean HavePendingLogs(){
        List<LogFuncClick> logFuncClickList = OtherFunc.GetDBFunction().queryLogFuncClick_need_upload();
        return logFuncClickList.size() != 0;
    }

}
