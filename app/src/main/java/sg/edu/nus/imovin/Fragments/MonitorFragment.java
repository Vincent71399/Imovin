package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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
import sg.edu.nus.imovin.Adapters.CalendarAdapter;
import sg.edu.nus.imovin.Objects.Goal;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.PlanData;
import sg.edu.nus.imovin.Retrofit.Object.StatisticsData;
import sg.edu.nus.imovin.Retrofit.Response.StatisticsResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.Common.CommonFunc.GetCurrentMonthString;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class MonitorFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @BindView(R.id.date_text) TextView date_text;
    @BindView(R.id.calendar_gridview) GridView calendar_gridview;
    @BindView(R.id.change_plan_btn) TextView change_plan_btn;
    @BindView(R.id.warning) TextView warning;

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

        date_text.setText(GetCurrentMonthString());
    }

    private void Init(){
        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        getStatistics(dayOfMonth);

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

    private void SetupData(List<StatisticsData> statisticsDataList){
        CalendarAdapter calendarAdapter = new CalendarAdapter(ImovinApplication.getInstance(),  generateCalendar(Lists.reverse(statisticsDataList)));
        calendar_gridview.setAdapter(calendarAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_plan_btn:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MonitorChangePlanActivity.class);
                startActivity(intent);
                break;
        }
    }

    private List<Goal> generateCalendar(List<StatisticsData> statisticsDataList){
        List<Goal> goalList = new ArrayList<>();

        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);

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
