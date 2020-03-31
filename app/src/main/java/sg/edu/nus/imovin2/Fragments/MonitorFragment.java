package sg.edu.nus.imovin2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.Activities.MonitorChangePlanActivity;
import sg.edu.nus.imovin2.Activities.MonitorDetailActivity;
import sg.edu.nus.imovin2.Adapters.CalendarAdapter;
import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.Event.ChangePlanEvent;
import sg.edu.nus.imovin2.HttpConnection.ConnectionURL;
import sg.edu.nus.imovin2.Objects.Goal;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.DailySummaryData;
import sg.edu.nus.imovin2.Retrofit.Object.PlanData;
import sg.edu.nus.imovin2.Retrofit.Response.MonitorDailySymmaryResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseFragment;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.Common.CommonFunc.GetCurrentMonthString;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;

public class MonitorFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;

    @BindView(R.id.date_text) TextView date_text;
    @BindView(R.id.calendar_gridview) GridView calendar_gridview;
    @BindView(R.id.change_plan_btn) TextView change_plan_btn;
    @BindView(R.id.warning) TextView warning;

    @BindView(R.id.calendar_prev_arrow) ImageView calendar_prev_arrow;
    @BindView(R.id.calendar_next_arrow) ImageView calendar_next_arrow;

    private List<Goal> goalList;
    private List<DailySummaryData> dailySummaryDataList;
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        change_plan_btn.setOnClickListener(this);
        calendar_prev_arrow.setOnClickListener(this);
        calendar_next_arrow.setOnClickListener(this);
        date_text.setOnClickListener(this);
    }

    private void Init(){
        calendarShownDay = Calendar.getInstance();
        calendarShownDay.set(Calendar.MILLISECOND, 0);
        calendarShownDay.set(Calendar.MINUTE, 0);
        calendarShownDay.set(Calendar.HOUR, 0);
        calendarShownDay.set(Calendar.HOUR_OF_DAY, 0);

        getDataAndDisplay();
    }

    private void getDataAndDisplay(){
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

        String startDateStr = CommonFunc.GetQueryDateStringRevert(calendarStartDay);
        String endDateStr = CommonFunc.GetQueryDateStringRevert(calendarEndDay);
        getDailySummary(startDateStr, endDateStr);

        if(ImovinApplication.getUserInfoResponse().getRiskLapse() != 0){
            warning.setVisibility(View.VISIBLE);
        }else {
            warning.setVisibility(View.INVISIBLE);
        }
    }

    private void getDailySummary(String startDateStr, String endDateStr){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String queryFormat = String.format(
                Locale.ENGLISH, ConnectionURL.PARAMETER_DAILY_SUMMARY, startDateStr, endDateStr);

        Call<MonitorDailySymmaryResponse> call = service.getMonitorDailySummary(queryFormat);

        call.enqueue(new Callback<MonitorDailySymmaryResponse>() {
            @Override
            public void onResponse(Call<MonitorDailySymmaryResponse> call, Response<MonitorDailySymmaryResponse> response) {
                try {
                    MonitorDailySymmaryResponse monitorDailySymmaryResponse = response.body();
                    dailySummaryDataList = monitorDailySymmaryResponse.get_items();
                    DisplayCalendar();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception HomeFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MonitorDailySymmaryResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure HomeFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DisplayCalendar(){
        date_text.setText(GetCurrentMonthString(calendarShownDay));

        SetupData();

        calendar_prev_arrow.setVisibility(View.VISIBLE);
        calendar_prev_arrow.setEnabled(true);

        if(CommonFunc.isSameMonth(calendarShownDay, Calendar.getInstance())){
            calendar_next_arrow.setVisibility(View.INVISIBLE);
            calendar_next_arrow.setEnabled(false);
        }else{
            calendar_next_arrow.setVisibility(View.VISIBLE);
            calendar_next_arrow.setEnabled(true);
        }

    }

    private boolean getPrevMonthCalendar(){
        calendarShownDay.set(Calendar.DAY_OF_MONTH, 1);
        calendarShownDay.add(Calendar.DAY_OF_MONTH, -1);
        return true;
    }

    private boolean getNextMonthCalendar(){
        if(!CommonFunc.isSameMonth(calendarShownDay, Calendar.getInstance())){
            calendarShownDay.add(Calendar.MONTH, 1);
            return true;
        }
        return false;
    }

    private void SetupData(){
        goalList = generateCalendar(dailySummaryDataList);
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
                    getDataAndDisplay();
                }
                break;
            case R.id.calendar_next_arrow:
                if(getNextMonthCalendar()){
                    getDataAndDisplay();
                }
                break;
            case R.id.date_text:
                getDataAndDisplay();
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ChangePlanEvent event) {
        getDataAndDisplay();
    }

    private List<Goal> generateCalendar(List<DailySummaryData> dailySummaryDataList){
        List<Goal> goalList = new ArrayList<>();

        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);
        firstDay.set(Calendar.MONTH, calendarShownDay.get(Calendar.MONTH));
        firstDay.set(Calendar.YEAR, calendarShownDay.get(Calendar.YEAR));

        int numDays = firstDay.getActualMaximum(Calendar.DATE);

        int day = firstDay.get(Calendar.DAY_OF_WEEK);

        goalList.add(new Goal(getString(R.string.sun)));
        goalList.add(new Goal(getString(R.string.mon)));
        goalList.add(new Goal(getString(R.string.tue)));
        goalList.add(new Goal(getString(R.string.wed)));
        goalList.add(new Goal(getString(R.string.thu)));
        goalList.add(new Goal(getString(R.string.fri)));
        goalList.add(new Goal(getString(R.string.sat)));

        for(int i=0; i<day-1; i++){
            goalList.add(new Goal(false));
        }

        int step_threshold = 0;
        PlanData planData = ImovinApplication.getPlanData();
        if(planData != null) {
            step_threshold = planData.getTarget();
        }else{
            step_threshold = ImovinApplication.getTarget();
        }

        int dailySummaryIndex = 0;

        for(int i=1; i<=numDays; i++){
            if(dailySummaryIndex < dailySummaryDataList.size()) {
                DailySummaryData dailySummaryData = dailySummaryDataList.get(dailySummaryIndex);
                Calendar calendar = CommonFunc.RevertFullDateStringRevert(dailySummaryData.getDate());
                if (i == calendar.get(Calendar.DAY_OF_MONTH)) {
                    int step = dailySummaryData.getSteps();
                    if (step >= step_threshold) {
                        goalList.add(new Goal(step_threshold, step_threshold, String.valueOf(i)));
                    } else {
                        goalList.add(new Goal(step, step_threshold, String.valueOf(i)));
                    }
                    dailySummaryIndex++;
                } else if (i < calendar.get(Calendar.DAY_OF_MONTH)) {
                    goalList.add(new Goal(0, step_threshold, String.valueOf(i)));
                }
            }else{
                goalList.add(new Goal(0, step_threshold, String.valueOf(i)));
            }
        }

        return goalList;
    }

}
