package sg.edu.nus.imovin.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Activities.AddPlanActivity;
import sg.edu.nus.imovin.Adapters.PlanAdapter;
import sg.edu.nus.imovin.Event.ChangePlanEvent;
import sg.edu.nus.imovin.Event.PlanEvent;
import sg.edu.nus.imovin.Objects.PlanDataCategory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.PlanData;
import sg.edu.nus.imovin.Retrofit.Response.CommonMessageResponse;
import sg.edu.nus.imovin.Retrofit.Response.PlanMultiResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_DELETE_PLAN;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_SELECT_PLAN;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin.System.ValueConstants.DefaultPlanType;

public class GoalFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private List<PlanData> planDataDefaultList;
    private List<PlanData> planDataCustomList;

    @BindView(R.id.newCustomBtn) Button newCustomBtn;
    @BindView(R.id.plan_list) RecyclerView plan_list;

    public static GoalFragment getInstance() {
        GoalFragment goalFragment = new GoalFragment();
        return goalFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_goal, null);

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

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        newCustomBtn.setOnClickListener(this);
    }

    private void Init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<PlanMultiResponse> call = service.getAllPlans();

        call.enqueue(new Callback<PlanMultiResponse>() {
            @Override
            public void onResponse(Call<PlanMultiResponse> call, Response<PlanMultiResponse> response) {
                try {
                    PlanMultiResponse planMultiResponse = response.body();
                    SetupData(planMultiResponse.get_items());

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception GoalFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanMultiResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure GoalFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newCustomBtn:
                Intent intentGoal = new Intent();
                intentGoal.setClass(getActivity(), AddPlanActivity.class);
                startActivityForResult(intentGoal, IntentConstants.GOAL_NEW_PLAN);
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(PlanEvent event) {
        if(event.getModule() == PlanEvent.MODULE_GOAL) {
            switch (event.getMessage()) {
                case EventConstants.REFRESH:
                    Init();
                    break;
                case EventConstants.DELETE:
                    if (event.getId() != null) {
                        DeletePlan(event.getId());
                    }
                    break;
                case EventConstants.SELECT:
                    if (event.getId() != null) {
                        SelectPlan(event.getId());
                    }
                    break;
                case EventConstants.UPDATE:
                    if (event.getId() != null) {
                        Intent intentGoal = new Intent();
                        intentGoal.setClass(getActivity(), AddPlanActivity.class);
                        intentGoal.putExtra(AddPlanActivity.Update_Plan_ID, event.getId());
                        PlanData pendingEditPlan = null;
                        for(PlanData planData : planDataCustomList){
                            if(planData.get_id().equals(event.getId())){
                                pendingEditPlan = planData;
                            }
                        }
                        if(pendingEditPlan != null){
                            intentGoal.putExtra(AddPlanActivity.Default_Plan_Name, pendingEditPlan.getName());
                            intentGoal.putExtra(AddPlanActivity.Default_Plan_Target, pendingEditPlan.getTarget());
                        }
                        startActivityForResult(intentGoal, IntentConstants.GOAL_EDIT_PLAN);
                    }
                    break;
            }
        }
    }

    private void SetupData(List<PlanData> planDataList){
        planDataDefaultList = new ArrayList<>();
        planDataCustomList = new ArrayList<>();

        for(PlanData planData : planDataList){
            if(planData.getPlanType().equals(DefaultPlanType)){
                planDataDefaultList.add(planData);
            }else {
                planDataCustomList.add(planData);
            }

            if(planData.getIs_selected()){
                ImovinApplication.setPlanData(planData);
                EventBus.getDefault().post(new ChangePlanEvent());
            }
        }

        PlanDataCategory defaultPlanCategory = new PlanDataCategory(getString(R.string.default_plans), planDataDefaultList);
        PlanDataCategory customPlanCategory = new PlanDataCategory(getString(R.string.custom_plans), planDataCustomList);

        PlanAdapter adapter = new PlanAdapter(getActivity(), Arrays.asList(defaultPlanCategory, customPlanCategory));

        plan_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        plan_list.setAdapter(adapter);

    }

    private void SelectPlan(final String plan_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_SELECT_PLAN, plan_id);

        Call<CommonMessageResponse> call = service.selectPlan(url);

        call.enqueue(new Callback<CommonMessageResponse>() {
            @Override
            public void onResponse(Call<CommonMessageResponse> call, Response<CommonMessageResponse> response) {
                try {
                    CommonMessageResponse commonMessageResponse = response.body();
                    if(commonMessageResponse != null && commonMessageResponse.getMessage().equals(getString(R.string.operation_success))) {
                        Init();
                    }else{
                        Gson g = new Gson();
                        commonMessageResponse = g.fromJson(response.errorBody().string(), CommonMessageResponse.class);
                        Toast.makeText(getActivity(), commonMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception GoalFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure GoalFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeletePlan(String plan_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_DELETE_PLAN, plan_id);

        Call<CommonMessageResponse> call = service.deletePlan(url);

        call.enqueue(new Callback<CommonMessageResponse>() {
            @Override
            public void onResponse(Call<CommonMessageResponse> call, Response<CommonMessageResponse> response) {
                try {
                    CommonMessageResponse commonMessageResponse = response.body();
                    if(commonMessageResponse != null && commonMessageResponse.getMessage().equals(getString(R.string.operation_success))) {
                        Init();
                    }else{
                        Gson g = new Gson();
                        commonMessageResponse = g.fromJson(response.errorBody().string(), CommonMessageResponse.class);
                        Toast.makeText(getActivity(), commonMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception GoalFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure GoalFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IntentConstants.GOAL_NEW_PLAN:
                if(resultCode == Activity.RESULT_OK) {
                    Init();
                }
                break;
            case IntentConstants.GOAL_EDIT_PLAN:
                if(resultCode == Activity.RESULT_OK) {
                    Init();
                }
                break;
        }
    }
}
