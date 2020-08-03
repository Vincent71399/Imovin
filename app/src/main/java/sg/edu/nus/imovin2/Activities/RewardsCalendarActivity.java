package sg.edu.nus.imovin2.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.Adapters.RewardCalendarAdapter;
import sg.edu.nus.imovin2.Adapters.RewardCalendarTitleAdapter;
import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.Common.RecyclerItemClickListener;
import sg.edu.nus.imovin2.Common.SpanningLinearLayoutManager;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsData;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsPointHistoryData;

import static sg.edu.nus.imovin2.System.IntentConstants.REWARD_DAILY_DATA;

public class RewardsCalendarActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.reward_calendar_title) RecyclerView reward_calendar_title;
    @BindView(R.id.reward_calendar) RecyclerView reward_calendar;

    @BindView(R.id.calendar_prev_arrow) ImageView calendar_prev_arrow;
    @BindView(R.id.calendar_next_arrow) ImageView calendar_next_arrow;

    @BindView(R.id.total_points) TextView total_points;
    @BindView(R.id.month_point) TextView month_point;
    @BindView(R.id.date_title) TextView date_title;
    @BindView(R.id.date_point_detail) TextView date_point_detail;

    @BindView(R.id.close_btn) Button close_btn;

    private List<RewardsPointHistoryData> displayRewardsPointHistoryDataList;
    private RewardCalendarAdapter rewardCalendarAdapter;
    private RewardsData rewardsData;

    private Calendar calendarShownDay, calendarStartDay, calendarEndDay;
    private Calendar earliestDate, latestDate;

    private HashMap<String, RewardsPointHistoryData> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_calendar);

        LinkUIById();
        SetData();
        SetFunction();
        Init();
    }

    private void LinkUIById() {
        ButterKnife.bind(this);
    }

    private void SetData(){
        rewardsData = (RewardsData) getIntent().getSerializableExtra(REWARD_DAILY_DATA);

        if(rewardsData.getPoints_history() != null && rewardsData.getPoints_history().size() > 0){
            earliestDate = CommonFunc.RevertFullDateStringRevert(rewardsData.getPoints_history().get(0).getDate());
            latestDate = CommonFunc.RevertFullDateStringRevert(rewardsData.getPoints_history().get(rewardsData.getPoints_history().size() - 1).getDate());

            hashMap = new HashMap<>();
            for(RewardsPointHistoryData rewardsPointHistoryData : rewardsData.getPoints_history()){
                hashMap.put(rewardsPointHistoryData.getDate(), rewardsPointHistoryData);
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void SetFunction() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        total_points.setText(getString(R.string.total) + " " + CommonFunc.Integer2String(rewardsData.getPoints()) + " " + getString(R.string.points));

        List<String> rewardCalendarTitleList = new ArrayList<>();
        rewardCalendarTitleList.add(getString(R.string.sun));
        rewardCalendarTitleList.add(getString(R.string.mon));
        rewardCalendarTitleList.add(getString(R.string.tue));
        rewardCalendarTitleList.add(getString(R.string.wed));
        rewardCalendarTitleList.add(getString(R.string.thu));
        rewardCalendarTitleList.add(getString(R.string.fri));
        rewardCalendarTitleList.add(getString(R.string.sat));

        RewardCalendarTitleAdapter rewardCalendarTitleAdapter = new RewardCalendarTitleAdapter(rewardCalendarTitleList);

        reward_calendar_title.setLayoutManager(new SpanningLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        reward_calendar_title.setAdapter(rewardCalendarTitleAdapter);

        if(displayRewardsPointHistoryDataList == null){
            displayRewardsPointHistoryDataList = new ArrayList<>();
        }else{
            displayRewardsPointHistoryDataList.clear();
        }
        rewardCalendarAdapter = new RewardCalendarAdapter(displayRewardsPointHistoryDataList);
        reward_calendar.setLayoutManager(new GridLayoutManager(this, 7));
        reward_calendar.setAdapter(rewardCalendarAdapter);

        reward_calendar.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RewardsPointHistoryData rewardsPointHistoryData = displayRewardsPointHistoryDataList.get(position);
                if(rewardsPointHistoryData != null){
                    calendarShownDay = CommonFunc.RevertFullDateStringRevert(rewardsPointHistoryData.getDate());
                    displayShownDate();
                    rewardCalendarAdapter.setSelectDate(calendarShownDay);
                    rewardCalendarAdapter.notifyDataSetChanged();
                }
            }
        }));

        calendar_prev_arrow.setOnClickListener(this);
        calendar_next_arrow.setOnClickListener(this);
        close_btn.setOnClickListener(this);
    }

    private void Init(){
        calendarShownDay = Calendar.getInstance();
        calendarShownDay.set(Calendar.MILLISECOND, 0);
        calendarShownDay.set(Calendar.SECOND, 0);
        calendarShownDay.set(Calendar.MINUTE, 0);
        calendarShownDay.set(Calendar.HOUR, 0);
        calendarShownDay.set(Calendar.HOUR_OF_DAY, 0);

        if(latestDate.compareTo(calendarShownDay) < 0){
            latestDate = Calendar.getInstance();
            latestDate.set(Calendar.MILLISECOND, 0);
            latestDate.set(Calendar.SECOND, 0);
            latestDate.set(Calendar.MINUTE, 0);
            latestDate.set(Calendar.HOUR, 0);
            latestDate.set(Calendar.HOUR_OF_DAY, 0);
        }

        getDataAndDisplay();
    }

    public void getDataAndDisplay(){
        calendarEndDay = Calendar.getInstance();
        calendarEndDay.set(Calendar.MILLISECOND, 0);
        calendarEndDay.set(Calendar.SECOND, 0);
        calendarEndDay.set(Calendar.MINUTE, 0);
        calendarEndDay.set(Calendar.HOUR, 0);
        calendarEndDay.set(Calendar.HOUR_OF_DAY, 0);
        calendarEndDay.set(Calendar.DAY_OF_MONTH, 1);
        calendarEndDay.set(Calendar.MONTH, calendarShownDay.get(Calendar.MONTH));
        calendarEndDay.set(Calendar.YEAR, calendarShownDay.get(Calendar.YEAR));
        calendarEndDay.add(Calendar.MONTH, 1);
        calendarEndDay.add(Calendar.DAY_OF_MONTH, -1);

        calendarStartDay = Calendar.getInstance();
        calendarStartDay.set(Calendar.MILLISECOND, 0);
        calendarStartDay.set(Calendar.SECOND, 0);
        calendarStartDay.set(Calendar.MINUTE, 0);
        calendarStartDay.set(Calendar.HOUR, 0);
        calendarStartDay.set(Calendar.HOUR_OF_DAY, 0);
        calendarStartDay.set(Calendar.MONTH, calendarShownDay.get(Calendar.MONTH));
        calendarStartDay.set(Calendar.DAY_OF_MONTH, 1);
        calendarStartDay.set(Calendar.YEAR, calendarShownDay.get(Calendar.YEAR));

        if(CommonFunc.isSameMonth(calendarShownDay, earliestDate)){
            calendar_prev_arrow.setVisibility(View.INVISIBLE);
            calendar_prev_arrow.setEnabled(false);
        }else{
            calendar_prev_arrow.setVisibility(View.VISIBLE);
            calendar_prev_arrow.setEnabled(true);
        }

        if(CommonFunc.isSameMonth(calendarShownDay, latestDate)){
            calendar_next_arrow.setVisibility(View.INVISIBLE);
            calendar_next_arrow.setEnabled(false);
        }else{
            calendar_next_arrow.setVisibility(View.VISIBLE);
            calendar_next_arrow.setEnabled(true);
        }
        generateCalendar();
    }

    private void generateCalendar(){
        displayRewardsPointHistoryDataList.clear();

        int day = calendarStartDay.get(Calendar.DAY_OF_WEEK);
        for(int i=0; i<day-1; i++){
            displayRewardsPointHistoryDataList.add(null);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MONTH, calendarStartDay.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendarStartDay.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.YEAR, calendarStartDay.get(Calendar.YEAR));

        int monthPoint = 0;
        while(calendar.compareTo(calendarEndDay) <= 0){
            String dateString = CommonFunc.GetQueryDateStringRevert(calendar);
            if(hashMap.containsKey(dateString)){
                RewardsPointHistoryData rewardsPointHistoryData = hashMap.get(dateString);
                displayRewardsPointHistoryDataList.add(rewardsPointHistoryData);
                monthPoint += rewardsPointHistoryData.getPoints();
            }else{
                RewardsPointHistoryData rewardsPointHistoryData = new RewardsPointHistoryData();
                rewardsPointHistoryData.setDate(dateString);
                rewardsPointHistoryData.setPoints(0);
                rewardsPointHistoryData.setSteps(0);
                displayRewardsPointHistoryDataList.add(rewardsPointHistoryData);
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        displayShownMonth(monthPoint);
        displayShownDate();

        rewardCalendarAdapter.setSelectDate(calendarShownDay);
        rewardCalendarAdapter.notifyDataSetChanged();
    }

    private void displayShownMonth(int points){
        month_point.setText(CommonFunc.GetDisplayMonth(calendarShownDay) + " : " + CommonFunc.Integer2String(points) + " " + getString(R.string.points));
    }

    private void displayShownDate(){
        date_title.setText(CommonFunc.GetDisplayDateDetail(calendarShownDay));
        String dateString = CommonFunc.GetQueryDateStringRevert(calendarShownDay);
        if(hashMap.containsKey(dateString)){
            RewardsPointHistoryData rewardsPointHistoryData = hashMap.get(dateString);
            date_point_detail.setText(CommonFunc.Integer2String(rewardsPointHistoryData.getSteps()) + " " + getString(R.string.steps)
                    + " → " + CommonFunc.Integer2String(rewardsPointHistoryData.getPoints()) + " " + getString(R.string.points));
        }else{
            date_point_detail.setText("0 Steps → 0 Points");
        }
    }

    private boolean getPrevMonthCalendar(){
        if(!CommonFunc.isSameMonth(calendarShownDay, earliestDate)) {
            calendarShownDay.add(Calendar.MONTH, -1);
            return true;
        }
        return false;
    }

    private boolean getNextMonthCalendar(){
        if(!CommonFunc.isSameMonth(calendarShownDay, latestDate)){
            calendarShownDay.add(Calendar.MONTH, 1);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.calendar_prev_arrow:
                if(getPrevMonthCalendar()){
                    getDataAndDisplay();
                }
                break;
            case R.id.calendar_next_arrow:
                if(getNextMonthCalendar()){
                    getDataAndDisplay();
                }
                break;
            case R.id.close_btn:
                finish();
                break;
        }
    }
}
