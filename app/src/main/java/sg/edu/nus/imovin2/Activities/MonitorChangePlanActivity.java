package sg.edu.nus.imovin2.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import sg.edu.nus.imovin2.Adapters.PlanAdapter;
import sg.edu.nus.imovin2.Event.ChangePlanEvent;
import sg.edu.nus.imovin2.Event.PlanEvent;
import sg.edu.nus.imovin2.Objects.PlanDataCategory;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.PlanData;
import sg.edu.nus.imovin2.Retrofit.Response.CommonMessageResponse;
import sg.edu.nus.imovin2.Retrofit.Response.PlanMultiResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseSimpleActivity;
import sg.edu.nus.imovin2.System.EventConstants;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.IntentConstants;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_DELETE_PLAN;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_SELECT_PLAN;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin2.System.ValueConstants.DefaultPlanType;

public class MonitorChangePlanActivity extends BaseSimpleActivity implements View.OnClickListener {

    private List<PlanData> planDataDefaultList;
    private List<PlanData> planDataCustomList;

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.plan_list) RecyclerView plan_list;
    @BindView(R.id.add_plan_btn) TextView add_plan_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_change_plan);

        LinkUIById();
        SetFunction();
        Init();
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
        ButterKnife.bind(this);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void SetFunction(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        add_plan_btn.setOnClickListener(this);
        mainView.setOnClickListener(this);
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
                    Log.d(LogConstants.LogTag, "Exception MonitorChangePlanAcitivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanMultiResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure MonitorChangePlanAcitivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_plan_btn:
                Intent intentGoal = new Intent();
                intentGoal.setClass(this, AddPlanActivity.class);
                startActivityForResult(intentGoal, IntentConstants.MONITOR_NEW_PLAN);
                break;
            case R.id.mainView:
                finish();
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(PlanEvent event) {
        if(event.getModule() == PlanEvent.MODULE_MONITOR_CHANGE) {
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
                        intentGoal.setClass(this, AddPlanActivity.class);
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

        PlanAdapter adapter = new PlanAdapter(this, Arrays.asList(defaultPlanCategory, customPlanCategory), PlanEvent.MODULE_MONITOR_CHANGE);

        plan_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
                        EventBus.getDefault().post(new PlanEvent(EventConstants.REFRESH));
                        Init();
                    }else{
                        Gson g = new Gson();
                        commonMessageResponse = g.fromJson(response.errorBody().string(), CommonMessageResponse.class);
                        Toast.makeText(getApplicationContext(), commonMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception MonitorChangePlanActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure MonitorChangePlanActivity : " + t.toString());
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
                        EventBus.getDefault().post(new PlanEvent(EventConstants.REFRESH));
                        Init();
                    }else{
                        Gson g = new Gson();
                        commonMessageResponse = g.fromJson(response.errorBody().string(), CommonMessageResponse.class);
                        Toast.makeText(getApplicationContext(), commonMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception MonitorChangePlanActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure MonitorChangePlanActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case IntentConstants.MONITOR_NEW_PLAN:
                if(resultCode == Activity.RESULT_OK){
                    Init();
                    EventBus.getDefault().post(new PlanEvent(EventConstants.REFRESH));
                }
                break;
            case IntentConstants.GOAL_EDIT_PLAN:
                if(resultCode == Activity.RESULT_OK){
                    Init();
                    EventBus.getDefault().post(new PlanEvent(EventConstants.REFRESH));
                }
                break;
        }
    }
}
