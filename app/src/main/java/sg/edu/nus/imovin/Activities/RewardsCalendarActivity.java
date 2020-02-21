package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Adapters.RewardCalendarAdapter;
import sg.edu.nus.imovin.Adapters.RewardCalendarTitleAdapter;
import sg.edu.nus.imovin.Common.SpanningLinearLayoutManager;
import sg.edu.nus.imovin.R;

public class RewardsCalendarActivity extends Activity {

    @BindView(R.id.reward_calendar_title) RecyclerView reward_calendar_title;
    @BindView(R.id.reward_calendar) RecyclerView reward_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_calendar);

        LinkUIById();
        SetFunction();
    }

    private void LinkUIById() {
        ButterKnife.bind(this);
    }

    private void SetFunction() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

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

        generateCalendar();
    }

    public void generateCalendar(){
        List<String> dateList = new ArrayList<>();
        for(int i=0;i<5;i++) {
            dateList.add(null);
        }

        for(int i=1;i<=29;i++){
            dateList.add(String.valueOf(i));
        }

        RewardCalendarAdapter rewardCalendarAdapter = new RewardCalendarAdapter(dateList);

        reward_calendar.setLayoutManager(new GridLayoutManager(this, 7));
        reward_calendar.setAdapter(rewardCalendarAdapter);
    }
}
