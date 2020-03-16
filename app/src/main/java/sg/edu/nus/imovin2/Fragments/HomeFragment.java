package sg.edu.nus.imovin2.Fragments;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.Common.IntValueFormatter;
import sg.edu.nus.imovin2.Common.WeekdayAxisValueFormatter;
import sg.edu.nus.imovin2.Event.ChangePlanEvent;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.DailySummaryData;
import sg.edu.nus.imovin2.Retrofit.Response.UserStatsResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseFragment;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private static final String SUNDAY = "Sun";
    private static final String MONDAY = "Mon";
    private static final String TUESDAY = "Tue";
    private static final String WEDNESDAY = "Wed";
    private static final String THURSDAY = "Thu";
    private static final String FRIDAY = "Fri";
    private static final String SATURDAY = "Sat";

    private View rootView;

    @BindView(R.id.steps_value) TextView steps_value;
    @BindView(R.id.cal_value) TextView cal_value;
    @BindView(R.id.time_value) TextView time_value;
    @BindView(R.id.distance_value) TextView distance_value;
    @BindView(R.id.warning) TextView warning;
    @BindView(R.id.chart_by_day) BarChart chart_by_day;
    @BindView(R.id.steps_btn) TextView steps_btn;
    @BindView(R.id.calories_btn) TextView calories_btn;
    @BindView(R.id.duration_btn) TextView duration_btn;
    @BindView(R.id.distance_btn) TextView distance_btn;
    @BindView(R.id.daily_goal) TextView daily_goal;

    ArrayList<String> weekdayList;
    HashMap<String, Integer> dailyStepsHashMap;
    HashMap<String, Integer> dailyCaloriesHashMap;
    HashMap<String, Integer> dailyDurationsHashMap;
    HashMap<String, Float> dailyDistanceHashMap;

    private List<Integer> barColors;

    public static HomeFragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);

        LinkUIById();
        SetFunction();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Init();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void LinkUIById() {
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction() {
        String[] weekdays = {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};
        weekdayList = new ArrayList<>(Arrays.asList(weekdays));

        dailyStepsHashMap = new HashMap<>();
        dailyCaloriesHashMap = new HashMap<>();
        dailyDurationsHashMap = new HashMap<>();
        dailyDistanceHashMap = new HashMap<>();

        steps_btn.setOnClickListener(this);
        calories_btn.setOnClickListener(this);
        duration_btn.setOnClickListener(this);
        distance_btn.setOnClickListener(this);
    }

    private void Init(){
        getUserStats();
    }

    private void getUserStats(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<UserStatsResponse> call = service.getUserStatsOverview();

        call.enqueue(new Callback<UserStatsResponse>() {
            @Override
            public void onResponse(Call<UserStatsResponse> call, Response<UserStatsResponse> response) {
                try {
                    UserStatsResponse userStatsResponse = response.body();
                    SetupDataNew(userStatsResponse);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception HomeFragment : " + e.toString());
                    HideConnectIndicator();
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserStatsResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure HomeFragment : " + t.toString());
                HideConnectIndicator();
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.steps_btn:
                steps_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_background));
                calories_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                duration_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                distance_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                updateBarChart(dailyStepsHashMap);
                break;
            case R.id.calories_btn:
                steps_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                calories_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_background));
                duration_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                distance_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                updateBarChart(dailyCaloriesHashMap);
                break;
            case R.id.duration_btn:
                steps_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                calories_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                duration_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_background));
                distance_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                updateBarChart(dailyDurationsHashMap);
                break;
            case R.id.distance_btn:
                steps_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                calories_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                duration_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_lesser_background));
                distance_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_background));
                updateBarChartFloat(dailyDistanceHashMap);
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ChangePlanEvent event) {
        Init();
    }

    private void SetupDataNew(UserStatsResponse userStatsResponse){
        int step_target = userStatsResponse.getTarget();

        ImovinApplication.setTarget(step_target);

        daily_goal.setText(getString(R.string.daily_goal_text) + step_target);

        barColors = new ArrayList<>();

        List<DailySummaryData> dailySummaryDataList = userStatsResponse.getDaily_summaries();

        Calendar today = Calendar.getInstance();
        List<Calendar> dayOfWeekList = generateDayOfWeekList();

        for(Calendar calendar : dayOfWeekList){
            boolean hasCalenderFlag = false;
            for(DailySummaryData dailySummaryData : dailySummaryDataList){
                Calendar dailyDataCalender = CommonFunc.RevertFullDateStringRevert(dailySummaryData.getDate());
                if(dailyDataCalender != null && CommonFunc.isSameDay(dailyDataCalender, calendar)){
                    hasCalenderFlag = true;
                    SetHashMapValueForDay(calendar, dailySummaryData);
                    if(CommonFunc.isSameDay(dailyDataCalender, today)){
                        barColors.add(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.theme_purple));
                    }else{
                        barColors.add(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.lesser_theme_purple));
                    }

                    if(CommonFunc.isSameDay(dailyDataCalender, today)){
                        steps_value.setText(String.valueOf(dailySummaryData.getSteps()));
                        cal_value.setText(String.valueOf(dailySummaryData.getCalories()));
                        time_value.setText(String.valueOf(dailySummaryData.getDuration()));
                        distance_value.setText(String.valueOf(dailySummaryData.getDistance()));
                    }
                }
            }
            if(!hasCalenderFlag){
                SetHashMapValueForDay(calendar);
                barColors.add(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.lesser_theme_purple));
            }
        }

        if(userStatsResponse.getRiskLapse() != 0){
            warning.setVisibility(View.VISIBLE);
        }else{
            warning.setVisibility(View.INVISIBLE);
        }

        createBarChart(dailyStepsHashMap);
    }

    private Comparator<BarEntry> compareByX = new Comparator<BarEntry>() {
        @Override
        public int compare(BarEntry barEntry1, BarEntry barEntry2) {
            return ((Float)barEntry1.getX()).compareTo((Float)barEntry2.getX());
        }
    };

    private void createBarChart(HashMap<String, Integer> dataHashMap) {
        chart_by_day.setScaleEnabled(false);

        // Create DataSet using the HashMap
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : dataHashMap.entrySet()){
            int intValue = weekdayList.indexOf(entry.getKey());
            entries.add(new BarEntry(intValue,entry.getValue()));
        }
        entries.sort(compareByX);
        BarDataSet dataSet = new BarDataSet(entries,"By Category");
        dataSet.setColors(barColors);

        BarData data = new BarData(dataSet);
        data.setValueFormatter(new IntValueFormatter());

        chart_by_day.setData(data);
        chart_by_day.setDrawValueAboveBar(true);
        chart_by_day.getDescription().setEnabled(false);
        chart_by_day.getLegend().setEnabled(false);

        XAxis xAxis = chart_by_day.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(weekdayList.size());
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new WeekdayAxisValueFormatter(weekdayList));
        YAxis rightAxis = chart_by_day.getAxisRight();
        rightAxis.setEnabled(false);
        YAxis leftAxis = chart_by_day.getAxisLeft();
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setAxisMinimum(0);

        chart_by_day.invalidate();
    }

    private void updateBarChart(HashMap<String, Integer> dataHashMap){
        // Create DataSet using the HashMap
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : dataHashMap.entrySet()){
            int intValue = weekdayList.indexOf(entry.getKey());
            entries.add(new BarEntry(intValue,entry.getValue()));
        }
        entries.sort(compareByX);
        BarDataSet dataSet = new BarDataSet(entries,"By Category");
        dataSet.setColors(barColors);

        BarData data = new BarData(dataSet);
        data.setValueFormatter(new IntValueFormatter());
        chart_by_day.setData(data);
        chart_by_day.invalidate();
    }

    private void updateBarChartFloat(HashMap<String, Float> dataHashMap){
        // Create DataSet using the HashMap
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : dataHashMap.entrySet()){
            float floatValue = weekdayList.indexOf(entry.getKey());
            entries.add(new BarEntry(floatValue, entry.getValue()));
        }
        entries.sort(compareByX);
        BarDataSet dataSet = new BarDataSet(entries,"By Category");
        dataSet.setColors(barColors);

        BarData data = new BarData(dataSet);
        chart_by_day.setData(data);
        chart_by_day.invalidate();
    }

    private List<Calendar> generateDayOfWeekList(){
        List<Calendar> dayOfWeekList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);
        //get first day of week (Sunday)
        calendar.add(Calendar.DATE, 1- day_of_week);
        Calendar today = (Calendar) calendar.clone();
        dayOfWeekList.add(today);

        for(int i=1;i<7;i++){
            calendar.add(Calendar.DATE, 1);
            Calendar day = (Calendar) calendar.clone();
            dayOfWeekList.add(day);
        }

        return dayOfWeekList;
    }

    private void SetHashMapValueForDay(Calendar calendar, DailySummaryData dailySummaryData){
        String dayOfWeek = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                dayOfWeek = SUNDAY;
                break;
            case Calendar.MONDAY:
                dayOfWeek = MONDAY;
                break;
            case Calendar.TUESDAY:
                dayOfWeek = TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                dayOfWeek = THURSDAY;
                break;
            case Calendar.FRIDAY:
                dayOfWeek = FRIDAY;
                break;
            case Calendar.SATURDAY:
                dayOfWeek = SATURDAY;
                break;
        }

        dailyStepsHashMap.put(dayOfWeek, dailySummaryData.getSteps());
        dailyCaloriesHashMap.put(dayOfWeek, dailySummaryData.getCalories());
        dailyDurationsHashMap.put(dayOfWeek, dailySummaryData.getDuration());
        dailyDistanceHashMap.put(dayOfWeek, dailySummaryData.getDistance());
    }

    private void SetHashMapValueForDay(Calendar calendar){
        String dayOfWeek = "";
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SUNDAY:
                dayOfWeek = SUNDAY;
                break;
            case Calendar.MONDAY:
                dayOfWeek = MONDAY;
                break;
            case Calendar.TUESDAY:
                dayOfWeek = TUESDAY;
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = WEDNESDAY;
                break;
            case Calendar.THURSDAY:
                dayOfWeek = THURSDAY;
                break;
            case Calendar.FRIDAY:
                dayOfWeek = FRIDAY;
                break;
            case Calendar.SATURDAY:
                dayOfWeek = SATURDAY;
                break;
        }

        dailyStepsHashMap.put(dayOfWeek, 0);
        dailyCaloriesHashMap.put(dayOfWeek, 0);
        dailyDurationsHashMap.put(dayOfWeek, 0);
        dailyDistanceHashMap.put(dayOfWeek, 0f);
    }

}