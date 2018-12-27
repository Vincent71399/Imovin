package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import sg.edu.nus.imovin.Adapters.PlanAdapter;
import sg.edu.nus.imovin.Event.PlanEvent;
import sg.edu.nus.imovin.Objects.PlanDataCategory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.PlanData;
import sg.edu.nus.imovin.Retrofit.Response.PlanMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.PlanResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_DELETE_PLAN;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin.System.ValueConstants.DefaultPlanType;

public class MonitorChangePlanActivity extends Activity {

    private List<PlanData> planDataDefaultList;
    private List<PlanData> planDataCustomList;

    @BindView(R.id.plan_list) RecyclerView plan_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_change_plan);

        LinkUIById();
        Init();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void Init(){
        ImovinApplication.setNeedRefreshMonitorPlan(false);

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
                    SetupData(planMultiResponse.getData());

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

//    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
//    public void onEvent(PlanEvent event) {
//        Toast.makeText(ImovinApplication.getInstance(), event.getMessage(), Toast.LENGTH_SHORT).show();
//        switch (event.getMessage()){
//            case EventConstants.REFRESH:
//                Init();
//                break;
//            case EventConstants.DELETE:
//                if(event.getId() != null){
//                    DeletePlan(event.getId());
//                }
//                break;
//        }
//    }

    private void SetupData(List<PlanData> planDataList){
        planDataDefaultList = new ArrayList<>();
        planDataCustomList = new ArrayList<>();

        for(PlanData planData : planDataList){
            if(planData.getPlanType().equals(DefaultPlanType)){
                planDataDefaultList.add(planData);
            }else {
                planDataCustomList.add(planData);
            }
        }

        PlanDataCategory defaultPlanCategory = new PlanDataCategory(getString(R.string.default_plans), planDataDefaultList);
        PlanDataCategory customPlanCategory = new PlanDataCategory(getString(R.string.custom_plans), planDataCustomList);

        PlanAdapter adapter = new PlanAdapter(this, Arrays.asList(defaultPlanCategory, customPlanCategory));

        plan_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        plan_list.setAdapter(adapter);
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

        Call<PlanResponse> call = service.deletePlan(url);

        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                try {
                    PlanResponse planResponse = response.body();
                    if(planResponse != null) {
                        Init();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception MonitorChangePlanAcitivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure MonitorChangePlanAcitivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
