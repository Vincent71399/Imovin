package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.Common.IntValueFormatter;
import sg.edu.nus.imovin.Common.WeekdayAxisValueFormatter;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.StatisticsData;
import sg.edu.nus.imovin.Retrofit.Response.StatisticsResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;
import sg.edu.nus.imovin.System.ValueConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;


public class HomeFragment extends Fragment implements View.OnClickListener {
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
    @BindView(R.id.warning) TextView warning;
    @BindView(R.id.chart_by_day) BarChart chart_by_day;
    @BindView(R.id.steps_btn) TextView steps_btn;
    @BindView(R.id.calories_btn) TextView calories_btn;
    @BindView(R.id.duration_btn) TextView duration_btn;

    ArrayList<String> weekdayList;
    HashMap<String, Integer> dailyStepsHashMap;
    HashMap<String, Integer> dailyCaloriesHashMap;
    HashMap<String, Integer> dailyDurationsHashMap;

    public static HomeFragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, null);

        LinkUIById();
        SetFunction();
        Init();

        return rootView;
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
    }

    private void Init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<StatisticsResponse> call = service.getStatistics(ValueConstants.StatisticDaysQuery);

        call.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                try {
                    StatisticsResponse statisticsResponse = response.body();
                    SetupData(statisticsResponse.getData());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception HomeFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StatisticsResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure HomeFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.steps_btn:
                steps_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_background));
                calories_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.grey_background));
                duration_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.grey_background));
                updateBarChart(dailyStepsHashMap);
                break;
            case R.id.calories_btn:
                steps_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.grey_background));
                calories_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_background));
                duration_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.grey_background));
                updateBarChart(dailyCaloriesHashMap);
                break;
            case R.id.duration_btn:
                steps_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.grey_background));
                calories_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.grey_background));
                duration_btn.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.purple_background));
                updateBarChart(dailyDurationsHashMap);
                break;
        }
    }

    private void SetupData(List<StatisticsData> statisticsDataList){
        int sumSteps = 0;
        for(StatisticsData statisticsData : statisticsDataList){
            Log.d(LogConstants.LogTag, "Steps : " + statisticsData.getSteps()
                    + "\nCalories : " + statisticsData.getCalories()
                    + "\nDuration : " + statisticsData.getDuration()
                    + "\nDistance : " + statisticsData.getDistance()
            );
            if(statisticsData.getSteps() == null){
                statisticsData.setSteps(0);
            }
            if(statisticsData.getCalories() == null){
                statisticsData.setCalories(0);
            }
            if(statisticsData.getDuration() == null){
                statisticsData.setDuration(0);
            }
            if(statisticsData.getDistance() == null){
                statisticsData.setDistance(0d);
            }
            sumSteps += statisticsData.getSteps();
        }
        int averageSteps = sumSteps / statisticsDataList.size();
        if(averageSteps < 7500){
            warning.setVisibility(View.VISIBLE);
        }else{
            warning.setVisibility(View.INVISIBLE);
        }

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        switch (day){
            case Calendar.SUNDAY:
                dailyStepsHashMap.put(SUNDAY, statisticsDataList.get(0).getSteps());
                dailyCaloriesHashMap.put(SUNDAY, statisticsDataList.get(0).getCalories());
                dailyDurationsHashMap.put(SUNDAY, statisticsDataList.get(0).getDuration());

                dailyStepsHashMap.put(MONDAY, 0);
                dailyCaloriesHashMap.put(MONDAY, 0);
                dailyDurationsHashMap.put(MONDAY, 0);

                dailyStepsHashMap.put(TUESDAY, 0);
                dailyCaloriesHashMap.put(TUESDAY, 0);
                dailyDurationsHashMap.put(TUESDAY, 0);

                dailyStepsHashMap.put(WEDNESDAY, 0);
                dailyCaloriesHashMap.put(WEDNESDAY, 0);
                dailyDurationsHashMap.put(WEDNESDAY, 0);

                dailyStepsHashMap.put(THURSDAY, 0);
                dailyCaloriesHashMap.put(THURSDAY, 0);
                dailyDurationsHashMap.put(THURSDAY, 0);

                dailyStepsHashMap.put(FRIDAY, 0);
                dailyCaloriesHashMap.put(FRIDAY, 0);
                dailyDurationsHashMap.put(FRIDAY, 0);

                dailyStepsHashMap.put(SATURDAY, 0);
                dailyCaloriesHashMap.put(SATURDAY, 0);
                dailyDurationsHashMap.put(SATURDAY, 0);
                break;
            case Calendar.MONDAY:
                dailyStepsHashMap.put(SUNDAY, statisticsDataList.get(1).getSteps());
                dailyCaloriesHashMap.put(SUNDAY, statisticsDataList.get(1).getCalories());
                dailyDurationsHashMap.put(SUNDAY, statisticsDataList.get(1).getDuration());

                dailyStepsHashMap.put(MONDAY, statisticsDataList.get(0).getSteps());
                dailyCaloriesHashMap.put(MONDAY, statisticsDataList.get(0).getCalories());
                dailyDurationsHashMap.put(MONDAY, statisticsDataList.get(0).getDuration());

                dailyStepsHashMap.put(TUESDAY, 0);
                dailyCaloriesHashMap.put(TUESDAY, 0);
                dailyDurationsHashMap.put(TUESDAY, 0);

                dailyStepsHashMap.put(WEDNESDAY, 0);
                dailyCaloriesHashMap.put(WEDNESDAY, 0);
                dailyDurationsHashMap.put(WEDNESDAY, 0);

                dailyStepsHashMap.put(THURSDAY, 0);
                dailyCaloriesHashMap.put(THURSDAY, 0);
                dailyDurationsHashMap.put(THURSDAY, 0);

                dailyStepsHashMap.put(FRIDAY, 0);
                dailyCaloriesHashMap.put(FRIDAY, 0);
                dailyDurationsHashMap.put(FRIDAY, 0);

                dailyStepsHashMap.put(SATURDAY, 0);
                dailyCaloriesHashMap.put(SATURDAY, 0);
                dailyDurationsHashMap.put(SATURDAY, 0);
                break;
            case Calendar.TUESDAY:
                dailyStepsHashMap.put(SUNDAY, statisticsDataList.get(2).getSteps());
                dailyCaloriesHashMap.put(SUNDAY, statisticsDataList.get(2).getCalories());
                dailyDurationsHashMap.put(SUNDAY, statisticsDataList.get(2).getDuration());

                dailyStepsHashMap.put(MONDAY, statisticsDataList.get(1).getSteps());
                dailyCaloriesHashMap.put(MONDAY, statisticsDataList.get(1).getCalories());
                dailyDurationsHashMap.put(MONDAY, statisticsDataList.get(1).getDuration());

                dailyStepsHashMap.put(TUESDAY, statisticsDataList.get(0).getSteps());
                dailyCaloriesHashMap.put(TUESDAY, statisticsDataList.get(0).getCalories());
                dailyDurationsHashMap.put(TUESDAY, statisticsDataList.get(0).getDuration());

                dailyStepsHashMap.put(WEDNESDAY, 0);
                dailyCaloriesHashMap.put(WEDNESDAY, 0);
                dailyDurationsHashMap.put(WEDNESDAY, 0);

                dailyStepsHashMap.put(THURSDAY, 0);
                dailyCaloriesHashMap.put(THURSDAY, 0);
                dailyDurationsHashMap.put(THURSDAY, 0);

                dailyStepsHashMap.put(FRIDAY, 0);
                dailyCaloriesHashMap.put(FRIDAY, 0);
                dailyDurationsHashMap.put(FRIDAY, 0);

                dailyStepsHashMap.put(SATURDAY, 0);
                dailyCaloriesHashMap.put(SATURDAY, 0);
                dailyDurationsHashMap.put(SATURDAY, 0);
                break;
            case Calendar.WEDNESDAY:
                dailyStepsHashMap.put(SUNDAY, statisticsDataList.get(3).getSteps());
                dailyCaloriesHashMap.put(SUNDAY, statisticsDataList.get(3).getCalories());
                dailyDurationsHashMap.put(SUNDAY, statisticsDataList.get(3).getDuration());

                dailyStepsHashMap.put(MONDAY, statisticsDataList.get(2).getSteps());
                dailyCaloriesHashMap.put(MONDAY, statisticsDataList.get(2).getCalories());
                dailyDurationsHashMap.put(MONDAY, statisticsDataList.get(2).getDuration());

                dailyStepsHashMap.put(TUESDAY, statisticsDataList.get(1).getSteps());
                dailyCaloriesHashMap.put(TUESDAY, statisticsDataList.get(1).getCalories());
                dailyDurationsHashMap.put(TUESDAY, statisticsDataList.get(1).getDuration());

                dailyStepsHashMap.put(WEDNESDAY, statisticsDataList.get(0).getSteps());
                dailyCaloriesHashMap.put(WEDNESDAY, statisticsDataList.get(0).getCalories());
                dailyDurationsHashMap.put(WEDNESDAY, statisticsDataList.get(0).getDuration());

                dailyStepsHashMap.put(THURSDAY, 0);
                dailyCaloriesHashMap.put(THURSDAY, 0);
                dailyDurationsHashMap.put(THURSDAY, 0);

                dailyStepsHashMap.put(FRIDAY, 0);
                dailyCaloriesHashMap.put(FRIDAY, 0);
                dailyDurationsHashMap.put(FRIDAY, 0);

                dailyStepsHashMap.put(SATURDAY, 0);
                dailyCaloriesHashMap.put(SATURDAY, 0);
                dailyDurationsHashMap.put(SATURDAY, 0);
                break;
            case Calendar.THURSDAY:
                dailyStepsHashMap.put(SUNDAY, statisticsDataList.get(4).getSteps());
                dailyCaloriesHashMap.put(SUNDAY, statisticsDataList.get(4).getCalories());
                dailyDurationsHashMap.put(SUNDAY, statisticsDataList.get(4).getDuration());

                dailyStepsHashMap.put(MONDAY, statisticsDataList.get(3).getSteps());
                dailyCaloriesHashMap.put(MONDAY, statisticsDataList.get(3).getCalories());
                dailyDurationsHashMap.put(MONDAY, statisticsDataList.get(3).getDuration());

                dailyStepsHashMap.put(TUESDAY, statisticsDataList.get(2).getSteps());
                dailyCaloriesHashMap.put(TUESDAY, statisticsDataList.get(2).getCalories());
                dailyDurationsHashMap.put(TUESDAY, statisticsDataList.get(2).getDuration());

                dailyStepsHashMap.put(WEDNESDAY, statisticsDataList.get(1).getSteps());
                dailyCaloriesHashMap.put(WEDNESDAY, statisticsDataList.get(1).getCalories());
                dailyDurationsHashMap.put(WEDNESDAY, statisticsDataList.get(1).getDuration());

                dailyStepsHashMap.put(THURSDAY, statisticsDataList.get(0).getSteps());
                dailyCaloriesHashMap.put(THURSDAY, statisticsDataList.get(0).getCalories());
                dailyDurationsHashMap.put(THURSDAY, statisticsDataList.get(0).getDuration());

                dailyStepsHashMap.put(FRIDAY, 0);
                dailyCaloriesHashMap.put(FRIDAY, 0);
                dailyDurationsHashMap.put(FRIDAY, 0);

                dailyStepsHashMap.put(SATURDAY, 0);
                dailyCaloriesHashMap.put(SATURDAY, 0);
                dailyDurationsHashMap.put(SATURDAY, 0);
                break;
            case Calendar.FRIDAY:
                dailyStepsHashMap.put(SUNDAY, statisticsDataList.get(5).getSteps());
                dailyCaloriesHashMap.put(SUNDAY, statisticsDataList.get(5).getCalories());
                dailyDurationsHashMap.put(SUNDAY, statisticsDataList.get(5).getDuration());

                dailyStepsHashMap.put(MONDAY, statisticsDataList.get(4).getSteps());
                dailyCaloriesHashMap.put(MONDAY, statisticsDataList.get(4).getCalories());
                dailyDurationsHashMap.put(MONDAY, statisticsDataList.get(4).getDuration());

                dailyStepsHashMap.put(TUESDAY, statisticsDataList.get(3).getSteps());
                dailyCaloriesHashMap.put(TUESDAY, statisticsDataList.get(3).getCalories());
                dailyDurationsHashMap.put(TUESDAY, statisticsDataList.get(3).getDuration());

                dailyStepsHashMap.put(WEDNESDAY, statisticsDataList.get(2).getSteps());
                dailyCaloriesHashMap.put(WEDNESDAY, statisticsDataList.get(2).getCalories());
                dailyDurationsHashMap.put(WEDNESDAY, statisticsDataList.get(2).getDuration());

                dailyStepsHashMap.put(THURSDAY, statisticsDataList.get(1).getSteps());
                dailyCaloriesHashMap.put(THURSDAY, statisticsDataList.get(1).getCalories());
                dailyDurationsHashMap.put(THURSDAY, statisticsDataList.get(1).getDuration());

                dailyStepsHashMap.put(FRIDAY, statisticsDataList.get(0).getSteps());
                dailyCaloriesHashMap.put(FRIDAY, statisticsDataList.get(0).getCalories());
                dailyDurationsHashMap.put(FRIDAY, statisticsDataList.get(0).getDuration());

                dailyStepsHashMap.put(SATURDAY, 0);
                dailyCaloriesHashMap.put(SATURDAY, 0);
                dailyDurationsHashMap.put(SATURDAY, 0);
                break;
            case Calendar.SATURDAY:
                dailyStepsHashMap.put(SUNDAY, statisticsDataList.get(6).getSteps());
                dailyCaloriesHashMap.put(SUNDAY, statisticsDataList.get(6).getCalories());
                dailyDurationsHashMap.put(SUNDAY, statisticsDataList.get(6).getDuration());

                dailyStepsHashMap.put(MONDAY, statisticsDataList.get(5).getSteps());
                dailyCaloriesHashMap.put(MONDAY, statisticsDataList.get(5).getCalories());
                dailyDurationsHashMap.put(MONDAY, statisticsDataList.get(5).getDuration());

                dailyStepsHashMap.put(TUESDAY, statisticsDataList.get(4).getSteps());
                dailyCaloriesHashMap.put(TUESDAY, statisticsDataList.get(4).getCalories());
                dailyDurationsHashMap.put(TUESDAY, statisticsDataList.get(4).getDuration());

                dailyStepsHashMap.put(WEDNESDAY, statisticsDataList.get(3).getSteps());
                dailyCaloriesHashMap.put(WEDNESDAY, statisticsDataList.get(3).getCalories());
                dailyDurationsHashMap.put(WEDNESDAY, statisticsDataList.get(3).getDuration());

                dailyStepsHashMap.put(THURSDAY, statisticsDataList.get(2).getSteps());
                dailyCaloriesHashMap.put(THURSDAY, statisticsDataList.get(2).getCalories());
                dailyDurationsHashMap.put(THURSDAY, statisticsDataList.get(2).getDuration());

                dailyStepsHashMap.put(FRIDAY, statisticsDataList.get(1).getSteps());
                dailyCaloriesHashMap.put(FRIDAY, statisticsDataList.get(1).getCalories());
                dailyDurationsHashMap.put(FRIDAY, statisticsDataList.get(1).getDuration());

                dailyStepsHashMap.put(SATURDAY, statisticsDataList.get(0).getSteps());
                dailyCaloriesHashMap.put(SATURDAY, statisticsDataList.get(0).getCalories());
                dailyDurationsHashMap.put(SATURDAY, statisticsDataList.get(0).getDuration());
                break;
        }

        StatisticsData dataToday = statisticsDataList.get(0);
        steps_value.setText(String.valueOf(dataToday.getSteps()));
        cal_value.setText(String.valueOf(dataToday.getCalories()));
        time_value.setText(CommonFunc.ConvertDuration2TimeFormat(dataToday.getDuration()));

        createBarChart(dailyStepsHashMap);
        steps_btn.setOnClickListener(this);
        calories_btn.setOnClickListener(this);
        duration_btn.setOnClickListener(this);
    }

    private void createBarChart(HashMap<String, Integer> dataHashMap) {
        chart_by_day.setScaleEnabled(false);

        // Create DataSet using the HashMap
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : dataHashMap.entrySet()){
            int intValue = weekdayList.indexOf(entry.getKey());
            entries.add(new BarEntry(intValue,entry.getValue()));
        }
        BarDataSet dataSet = new BarDataSet(entries,"By Category");
        dataSet.setColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.theme_purple));
        //dataSet.setDrawValues(false);

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
        BarDataSet dataSet = new BarDataSet(entries,"By Category");
        dataSet.setColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.theme_purple));
        //dataSet.setDrawValues(false);

        BarData data = new BarData(dataSet);
        data.setValueFormatter(new IntValueFormatter());
        chart_by_day.setData(data);
        chart_by_day.invalidate();
    }

}