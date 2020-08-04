package sg.edu.nus.imovin2.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.Adapters.ActivityLogAdapter;
import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.Objects.DisplayLog;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.ActivityLogData;

import static sg.edu.nus.imovin2.System.IntentConstants.ACTIVITY_LOG_DATA;

public class HomePopActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.activity_log_list) RecyclerView activity_log_list;
    @BindView(R.id.close_btn) Button close_btn;

    private List<ActivityLogData> activityLogDataList;
    private ActivityLogAdapter activityLogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pop);

        LinkUIbyId();
        SetFunction();
        SetData();
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void SetFunction(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mainView.setOnClickListener(this);
        close_btn.setOnClickListener(this);

        if(activityLogDataList == null){
            activityLogDataList = new ArrayList<>();
        }

        activityLogAdapter = new ActivityLogAdapter(activityLogDataList);
        activity_log_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activity_log_list.setAdapter(activityLogAdapter);
    }

    private void SetData(){
        activityLogDataList.clear();
        DisplayLog displayLog = (DisplayLog) getIntent().getSerializableExtra(ACTIVITY_LOG_DATA);
        activityLogDataList.addAll(displayLog.getActivityLogDataList());

        String previousDateString = null;
        for(ActivityLogData activityLogData : activityLogDataList){
            if(previousDateString != null && CommonFunc.isSameDay(previousDateString, activityLogData.getDate())){
                activityLogData.setDisplay_title(false);
            }
            previousDateString = activityLogData.getDate();
        }

        activityLogAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mainView:
                finish();
                break;
            case R.id.close_btn:
                finish();
                break;
        }
    }
}
