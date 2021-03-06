package sg.edu.nus.imovin2.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.Activities.ChallengePopupActivity;
import sg.edu.nus.imovin2.Adapters.MedalAdapter;
import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.Common.RecyclerItemClickListener;
import sg.edu.nus.imovin2.Common.SpanningLinearLayoutManager;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.ChallengeData;
import sg.edu.nus.imovin2.Retrofit.Object.MedalData;
import sg.edu.nus.imovin2.Retrofit.Response.ChallengeResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseFragment;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.LogConstants;
import sg.edu.nus.imovin2.System.ValueConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin2.System.IntentConstants.MEDAL_DATA;

public class ChallengeFragment extends BaseFragment {
    private View rootView;

    @BindView(R.id.challenge_points) TextView challenge_points;
    @BindView(R.id.time_period) TextView time_period;
    @BindView(R.id.point_rank) TextView point_rank;
    @BindView(R.id.step_rank) TextView step_rank;

    @BindView(R.id.daily_steps_title) TextView daily_steps_title;
    @BindView(R.id.daily_step_list) RecyclerView daily_step_list;
    @BindView(R.id.active_days_for_the_week_title) TextView active_days_for_the_week_title;
    @BindView(R.id.active_days_for_the_week_list) RecyclerView active_days_for_the_week_list;
    @BindView(R.id.daily_total_distance_title) TextView daily_total_distance_title;
    @BindView(R.id.daily_total_distance_list) RecyclerView daily_total_distance_list;
    @BindView(R.id.weekly_exercise_duration_title) TextView weekly_exercise_duration_title;
    @BindView(R.id.weekly_exercise_duration_list) RecyclerView weekly_exercise_duration_list;
    @BindView(R.id.total_days_with_steps_title) TextView total_days_with_steps_title;
    @BindView(R.id.total_days_with_steps_list) RecyclerView total_days_with_steps_list;
    @BindView(R.id.active_weeks_in_a_row_title) TextView active_weeks_in_a_row_title;
    @BindView(R.id.active_weeks_in_a_row_list) RecyclerView active_weeks_in_a_row_list;

    private List<MedalData> dailyStepList;
    private List<MedalData> activeDaysForTheWeekList;
    private List<MedalData> dailyTotalDistanceList;
    private List<MedalData> weeklyExerciseDurationList;
    private List<MedalData> totalDaysWithStepsList;
    private List<MedalData> activeWeeksInARowList;

    public static ChallengeFragment getInstance() {
        ChallengeFragment challengeFragment = new ChallengeFragment();
        return challengeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_challenge, null);

        LinkUIById();
        SetFunction();
        Init();

        return rootView;
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){

    }

    private void Init(){
        getChallengeData();
    }

    private void SetupData(ChallengeData challengeData){
        challenge_points.setText(CommonFunc.Integer2String(challengeData.getChallenge_points()));

        Calendar calendar = Calendar.getInstance();
        int days = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DAY_OF_MONTH, 2-days);
        String startTimePeriod = CommonFunc.GetDisplayDateDetail(calendar);
        calendar.add(Calendar.DAY_OF_MONTH, 6);
        String endTimePeriod = CommonFunc.GetDisplayDateDetail(calendar);

        time_period.setText(startTimePeriod + " - " + endTimePeriod);
        point_rank.setText(CommonFunc.ordinal(challengeData.getChallenge_rank()));
        step_rank.setText(CommonFunc.ordinal(challengeData.getSteps_rank()));

        List<MedalData> medalDataList = challengeData.getMedals();

        List<MedalData> dailyStepList = new ArrayList<>();
        List<MedalData> activeDaysForTheWeekList = new ArrayList<>();
        List<MedalData> dailyTotalDistanceList = new ArrayList<>();
        List<MedalData> weeklyExerciseDurationList = new ArrayList<>();
        List<MedalData> totalDaysWithStepsList = new ArrayList<>();
        List<MedalData> activeWeeksInARowList = new ArrayList<>();

        for(MedalData medalData : medalDataList){
            switch (medalData.getCategory()){
                case ValueConstants.CATEGORY_DAILY_STEP:
                    dailyStepList.add(medalData);
                    break;
                case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                    activeDaysForTheWeekList.add(medalData);
                    break;
                case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                    dailyTotalDistanceList.add(medalData);
                    break;
                case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                    weeklyExerciseDurationList.add(medalData);
                    break;
                case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                    totalDaysWithStepsList.add(medalData);
                    break;
                case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                    activeWeeksInARowList.add(medalData);
                    break;
            }
        }

        Comparator<MedalData> compareByTier = new Comparator<MedalData>() {
            @Override
            public int compare(MedalData medalData1, MedalData medalData2) {
                return medalData1.getTier().compareTo(medalData2.getTier());
            }
        };

        if(dailyStepList.size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dailyStepList.sort(compareByTier);
            }
            daily_steps_title.setText(dailyStepList.get(0).getName());
            setMedalList(daily_step_list, dailyStepList);

            this.dailyStepList = dailyStepList;
            daily_step_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MedalData medalData = ChallengeFragment.this.dailyStepList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ChallengePopupActivity.class);
                    intent.putExtra(MEDAL_DATA, medalData);
                    startActivity(intent);
                }
            }));
        }

        if(activeDaysForTheWeekList.size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                activeDaysForTheWeekList.sort(compareByTier);
            }
            active_days_for_the_week_title.setText(activeDaysForTheWeekList.get(0).getName());
            setMedalList(active_days_for_the_week_list, activeDaysForTheWeekList);

            this.activeDaysForTheWeekList = activeDaysForTheWeekList;
            active_days_for_the_week_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MedalData medalData = ChallengeFragment.this.activeDaysForTheWeekList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ChallengePopupActivity.class);
                    intent.putExtra(MEDAL_DATA, medalData);
                    startActivity(intent);
                }
            }));
        }

        if(dailyTotalDistanceList.size() > 0){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dailyTotalDistanceList.sort(compareByTier);
            }
            daily_total_distance_title.setText(dailyTotalDistanceList.get(0).getName());
            setMedalList(daily_total_distance_list, dailyTotalDistanceList);

            this.dailyTotalDistanceList = dailyTotalDistanceList;
            daily_total_distance_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MedalData medalData = ChallengeFragment.this.dailyTotalDistanceList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ChallengePopupActivity.class);
                    intent.putExtra(MEDAL_DATA, medalData);
                    startActivity(intent);
                }
            }));
        }

        if(weeklyExerciseDurationList.size() > 0){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                weeklyExerciseDurationList.sort(compareByTier);
            }
            weekly_exercise_duration_title.setText(weeklyExerciseDurationList.get(0).getName());
            setMedalList(weekly_exercise_duration_list, weeklyExerciseDurationList);

            this.weeklyExerciseDurationList = weeklyExerciseDurationList;
            weekly_exercise_duration_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MedalData medalData = ChallengeFragment.this.weeklyExerciseDurationList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ChallengePopupActivity.class);
                    intent.putExtra(MEDAL_DATA, medalData);
                    startActivity(intent);
                }
            }));
        }

        if(totalDaysWithStepsList.size() > 0){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                totalDaysWithStepsList.sort(compareByTier);
            }
            total_days_with_steps_title.setText(totalDaysWithStepsList.get(0).getName());
            setMedalList(total_days_with_steps_list, totalDaysWithStepsList);

            this.totalDaysWithStepsList = totalDaysWithStepsList;
            total_days_with_steps_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MedalData medalData = ChallengeFragment.this.totalDaysWithStepsList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ChallengePopupActivity.class);
                    intent.putExtra(MEDAL_DATA, medalData);
                    startActivity(intent);
                }
            }));
        }

        if(activeWeeksInARowList.size() > 0){
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                activeWeeksInARowList.sort(compareByTier);
            }
            active_weeks_in_a_row_title.setText(activeWeeksInARowList.get(0).getName());
            setMedalList(active_weeks_in_a_row_list, activeWeeksInARowList);

            this.activeWeeksInARowList = activeWeeksInARowList;
            active_weeks_in_a_row_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MedalData medalData = ChallengeFragment.this.activeWeeksInARowList.get(position);
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ChallengePopupActivity.class);
                    intent.putExtra(MEDAL_DATA, medalData);
                    startActivity(intent);
                }
            }));
        }
    }

    private void setMedalList(RecyclerView view, List<MedalData> dataArray){
        MedalAdapter dailyStepAdapter = new MedalAdapter(dataArray);
        view.setLayoutManager(new SpanningLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        view.setAdapter(dailyStepAdapter);
    }

    private void getChallengeData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<ChallengeResponse> call = service.getChallenge();

        call.enqueue(new Callback<ChallengeResponse>() {
            @Override
            public void onResponse(Call<ChallengeResponse> call, Response<ChallengeResponse> response) {
                try {
                    ChallengeResponse challengeResponse = response.body();
                    SetupData(challengeResponse.getData());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ChallengeFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChallengeResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ChallengeFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
