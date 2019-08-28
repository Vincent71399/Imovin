package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Adapters.StarAdapter;
import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.Objects.ChallengeStar;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.ChallengeData;
import sg.edu.nus.imovin.Retrofit.Response.ChallengeResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class ChallengeFragment extends BaseFragment {
    private View rootView;

    @BindView(R.id.points_title) TextView points_title;
    @BindView(R.id.points_number) TextView points_number;
    @BindView(R.id.rank_text) TextView rank_text;

    @BindView(R.id.daily_steps_title) TextView daily_steps_title;
    @BindView(R.id.daily_distance_title) TextView daily_distance_title;
    @BindView(R.id.step_target_title) TextView step_target_title;
    @BindView(R.id.active_days_title) TextView active_days_titles;
    @BindView(R.id.weekly_ex_title) TextView weekly_ex_title;

    @BindView(R.id.daily_step_list) RecyclerView daily_step_list;
    @BindView(R.id.daily_distance_list) RecyclerView daily_distance_list;
    @BindView(R.id.steps_target_list) RecyclerView steps_target_list;
    @BindView(R.id.active_days_list) RecyclerView active_days_list;
    @BindView(R.id.weekly_ex_list) RecyclerView weekly_ex_list;

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
//        getChallengeData();

//        List<ChallengeStar> challengeStarList1 = new ArrayList<>();
//        challengeStarList1.add(new ChallengeStar(100, 15000, ChallengeStar.starColor.Gold));
//        challengeStarList1.add(new ChallengeStar(50, 7500, ChallengeStar.starColor.Silver));
//        challengeStarList1.add(new ChallengeStar(20, 2500, ChallengeStar.starColor.Bronze));
//
//        StarAdapter starAdapter1 = new StarAdapter(challengeStarList1);
//        daily_step_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        daily_step_list.setAdapter(starAdapter1);
//
//        List<ChallengeStar> challengeStarList2 = new ArrayList<>();
//        challengeStarList2.add(new ChallengeStar(500, 500000, ChallengeStar.starColor.Gold));
//        challengeStarList2.add(new ChallengeStar(400, 400000, ChallengeStar.starColor.Gold));
//        challengeStarList2.add(new ChallengeStar(250, 300000, ChallengeStar.starColor.Silver));
//
//        StarAdapter starAdapter2 = new StarAdapter(challengeStarList2);
//        life_time_total_step_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        life_time_total_step_list.setAdapter(starAdapter2);
    }

    private void SetupData(ChallengeData challengeData){
        points_number.setText(" " + String.valueOf(challengeData.getChallengePoints()));
        rank_text.setText("RANK: " + CommonFunc.ordinal(challengeData.getRank()) + " of " + challengeData.getNumberOfChallenges());

        List<List<Integer>> dailyStepsArray = challengeData.getChallenges().getDailySteps();
        if(dailyStepsArray.size() > 0) {
            setStarList(daily_step_list, dailyStepsArray);
            daily_steps_title.setVisibility(View.VISIBLE);
        }

        List<List<Integer>> dailyDistanceArray = challengeData.getChallenges().getDailyDistance();
        if(dailyDistanceArray.size() > 0){
            setStarList(daily_distance_list, dailyDistanceArray);
            daily_distance_title.setVisibility(View.VISIBLE);
        }

        List<List<Integer>> stepsTargetArray = challengeData.getChallenges().getStepsTarget();
        if(stepsTargetArray.size() > 0){
            setStarList(steps_target_list, stepsTargetArray);
            step_target_title.setVisibility(View.VISIBLE);
        }

        List<List<Integer>> activeDaysArray = challengeData.getChallenges().getActiveDays();
        if(activeDaysArray.size() > 0){
            setStarList(active_days_list, activeDaysArray);
            active_days_titles.setVisibility(View.VISIBLE);
        }

        List<List<Integer>> weeklyExArray = challengeData.getChallenges().getWeeklyEx();
        if(weeklyExArray.size() > 0){
            setStarList(weekly_ex_list, weeklyExArray);
            weekly_ex_title.setVisibility(View.VISIBLE);
        }
    }

    private void setStarList(RecyclerView view, List<List<Integer>> dataArray){
        List<ChallengeStar> challengeStarList = new ArrayList<>();
        for (List<Integer> data : dataArray) {
            if (data.size() == 2) {
                if (challengeStarList.size() == 0) {
                    challengeStarList.add(new ChallengeStar(data.get(1), data.get(0), ChallengeStar.starColor.Bronze));
                } else if (challengeStarList.size() == 1) {
                    challengeStarList.add(new ChallengeStar(data.get(1), data.get(0), ChallengeStar.starColor.Silver));
                } else {
                    challengeStarList.add(new ChallengeStar(data.get(1), data.get(0), ChallengeStar.starColor.Gold));
                }
            }
        }

        StarAdapter dailyStepAdapter = new StarAdapter(challengeStarList);
        view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        view.setAdapter(dailyStepAdapter);
    }

//    private void getChallengeData(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(SERVER)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(ImovinApplication.getHttpClient().build())
//                .build();
//
//        ImovinService service = retrofit.create(ImovinService.class);
//
//        Call<ChallengeResponse> call = service.getChallenge();
//
//        call.enqueue(new Callback<ChallengeResponse>() {
//            @Override
//            public void onResponse(Call<ChallengeResponse> call, Response<ChallengeResponse> response) {
//                try {
//                    ChallengeResponse challengeResponse = response.body();
//                    SetupData(challengeResponse.getData());
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Log.d(LogConstants.LogTag, "Exception ChallengeFragment : " + e.toString());
//                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ChallengeResponse> call, Throwable t) {
//                Log.d(LogConstants.LogTag, "Failure ChallengeFragment : " + t.toString());
//                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
