package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Activities.MonitorChangePlanActivity;
import sg.edu.nus.imovin.Activities.MonitorDetailActivity;
import sg.edu.nus.imovin.Adapters.CalendarAdapter;
import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.Objects.Goal;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.PlanData;
import sg.edu.nus.imovin.Retrofit.Object.StatisticsData;
import sg.edu.nus.imovin.Retrofit.Response.StatisticsResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.Common.CommonFunc.GetCurrentMonthString;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class MonitorFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;

    @BindView(R.id.date_text) TextView date_text;
    @BindView(R.id.calendar_gridview) GridView calendar_gridview;
    @BindView(R.id.change_plan_btn) TextView change_plan_btn;
    @BindView(R.id.warning) TextView warning;

    @BindView(R.id.calendar_prev_arrow) ImageView calendar_prev_arrow;
    @BindView(R.id.calendar_next_arrow) ImageView calendar_next_arrow;

    private List<Goal> goalList;
    private List<StatisticsData> statisticsDataList;
    private Calendar calendarShownDay, calendarStartDay, calendarEndDay;

    public static MonitorFragment getInstance() {
        MonitorFragment monitorFragment = new MonitorFragment();
        return monitorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_monitor, null);

        LinkUIById();
        SetFunction();
        Init();

        return rootView;
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        change_plan_btn.setOnClickListener(this);
        calendar_prev_arrow.setOnClickListener(this);
        calendar_next_arrow.setOnClickListener(this);
    }

    private void Init(){
        calendarShownDay = Calendar.getInstance();
        calendarShownDay.set(Calendar.MILLISECOND, 0);
        calendarShownDay.set(Calendar.MINUTE, 0);
        calendarShownDay.set(Calendar.HOUR, 0);
        calendarShownDay.set(Calendar.HOUR_OF_DAY, 0);

        calendarEndDay = Calendar.getInstance();
        calendarEndDay.set(Calendar.MILLISECOND, 0);
        calendarEndDay.set(Calendar.MINUTE, 0);
        calendarEndDay.set(Calendar.HOUR, 0);
        calendarEndDay.set(Calendar.HOUR_OF_DAY, 0);

        calendarStartDay = Calendar.getInstance();
        calendarStartDay.set(Calendar.MILLISECOND, 0);
        calendarStartDay.set(Calendar.MINUTE, 0);
        calendarStartDay.set(Calendar.HOUR, 0);
        calendarStartDay.set(Calendar.HOUR_OF_DAY, 0);
        calendarStartDay.set(Calendar.DAY_OF_MONTH, 1);
        calendarStartDay.add(Calendar.YEAR, -1);

        int dayDiff = CommonFunc.dayDiffBetweenCalendar(calendarStartDay, calendarEndDay);

        getStatistics(dayDiff + 1);

        if(ImovinApplication.getShowWarning()){
            warning.setVisibility(View.VISIBLE);
        }else {
            warning.setVisibility(View.INVISIBLE);
        }
    }

    private void getStatistics(int numOfDays){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<StatisticsResponse> call = service.getStatistics(numOfDays);

        call.enqueue(new Callback<StatisticsResponse>() {
            @Override
            public void onResponse(Call<StatisticsResponse> call, Response<StatisticsResponse> response) {
                try {
                    StatisticsResponse statisticsResponse = response.body();
                    statisticsDataList = statisticsResponse.getData();
                    DisplayCalendar();
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

    private void DisplayCalendar(){
        date_text.setText(GetCurrentMonthString(calendarShownDay));

        int dayOfMonth = calendarShownDay.get(Calendar.DAY_OF_MONTH);

        int index = CommonFunc.dayDiffBetweenCalendar(calendarShownDay, calendarEndDay);

        SetupData(statisticsDataList.subList(index, dayOfMonth + index));

        if(CommonFunc.isSameMonth(calendarShownDay, calendarStartDay)){
            calendar_prev_arrow.setVisibility(View.INVISIBLE);
            calendar_prev_arrow.setEnabled(false);
        }else {
            calendar_prev_arrow.setVisibility(View.VISIBLE);
            calendar_prev_arrow.setEnabled(true);
        }

        if(CommonFunc.isSameMonth(calendarShownDay, calendarEndDay)){
            calendar_next_arrow.setVisibility(View.INVISIBLE);
            calendar_next_arrow.setEnabled(false);
        }else{
            calendar_next_arrow.setVisibility(View.VISIBLE);
            calendar_next_arrow.setEnabled(true);
        }

    }

    public boolean getPrevMonthCalendar(){
        if(!CommonFunc.isSameMonth(calendarShownDay, calendarStartDay)){
            calendarShownDay.set(Calendar.DAY_OF_MONTH, 1);
            calendarShownDay.add(Calendar.DAY_OF_MONTH, -1);
            return true;
        }
        return false;
    }

    public boolean getNextMonthCalendar(){
        if(!CommonFunc.isSameMonth(calendarShownDay, calendarEndDay)){
            calendarShownDay.add(Calendar.MONTH, 1);
            return true;
        }
        return false;
    }

    private void SetupData(List<StatisticsData> statisticsDataList){
        goalList = generateCalendar(Lists.reverse(statisticsDataList));
        CalendarAdapter calendarAdapter = new CalendarAdapter(ImovinApplication.getInstance(),  goalList);
        calendar_gridview.setAdapter(calendarAdapter);
        calendar_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(goalList.get(i).getShown()){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MonitorDetailActivity.class);
                    intent.putExtra(MonitorDetailActivity.MONTH_YEAR, date_text.getText().toString());
                    intent.putExtra(MonitorDetailActivity.GOAL, goalList.get(i));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_plan_btn:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MonitorChangePlanActivity.class);
                startActivity(intent);
                break;
            case R.id.calendar_prev_arrow:
                if(getPrevMonthCalendar()){
                    DisplayCalendar();
                }
                break;
            case R.id.calendar_next_arrow:
                if(getNextMonthCalendar()){
                    DisplayCalendar();
                }
                break;
        }
    }

    private List<Goal> generateCalendar(List<StatisticsData> statisticsDataList){
        List<Goal> goalList = new ArrayList<>();

        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        firstDay.set(Calendar.MONTH, calendarShownDay.get(Calendar.MONTH));
        firstDay.set(Calendar.YEAR, calendarShownDay.get(Calendar.YEAR));

        int numDays = firstDay.getActualMaximum(Calendar.DATE);

        int day = firstDay.get(Calendar.DAY_OF_WEEK);

        goalList.add(new Goal("SUN"));
        goalList.add(new Goal("MON"));
        goalList.add(new Goal("TUE"));
        goalList.add(new Goal("WED"));
        goalList.add(new Goal("THU"));
        goalList.add(new Goal("FRI"));
        goalList.add(new Goal("SAT"));

        for(int i=0; i<day-1; i++){
            goalList.add(new Goal(false));
        }

        int step_threshold = 7500;
        PlanData planData = ImovinApplication.getPlanData();
        if(planData != null){
            step_threshold = planData.getTarget();
        }

        for(int i=0; i<numDays; i++){
            if(i < statisticsDataList.size()){
                StatisticsData statisticsData = statisticsDataList.get(i);
                int step = statisticsData.getSteps();
                if(step >= step_threshold){
                    goalList.add(new Goal(step_threshold, step_threshold, String.valueOf(i + 1)));
                }else {
                    goalList.add(new Goal(step, step_threshold, String.valueOf(i + 1)));
                }
            }else {
                goalList.add(new Goal(0, step_threshold, String.valueOf(i + 1)));
            }
        }

        return goalList;
    }

}
