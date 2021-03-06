package sg.edu.nus.imovin2.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Request.CreatePlanRequest;
import sg.edu.nus.imovin2.Retrofit.Request.UpdatePlanRequest;
import sg.edu.nus.imovin2.Retrofit.Response.CommonMessageResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseSimpleActivity;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_UPDATE_PLAN;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;

public class AddPlanActivity extends BaseSimpleActivity implements View.OnClickListener {

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.plan_title_input) EditText plan_title_input;
    @BindView(R.id.active_minutes_value) TextView active_minutes_value;
    @BindView(R.id.planStepsBar) IndicatorSeekBar planStepsBar;
    @BindView(R.id.button_add_plan) Button button_add_plan;
    @BindView(R.id.button_cancel) Button button_cancel;

    public static final String Update_Plan_ID = "Update_Plan_ID";
    public static final String Default_Plan_Name = "Default_Plan_Name";
    public static final String Default_Plan_Target = "Default_Plan_Target";

    private String update_plan_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        LinkUIById();
        SetFunction();
        Init();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void SetFunction(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mainView.setOnClickListener(this);
        button_add_plan.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        planStepsBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                int steps = seekParams.progress;
                active_minutes_value.setText(String.valueOf(steps));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }

    private void Init(){
        String default_plan_name = getIntent().getStringExtra(Default_Plan_Name);
        update_plan_id = getIntent().getStringExtra(Update_Plan_ID);
        int default_plan_target = getIntent().getIntExtra(Default_Plan_Target, 100);

        if(update_plan_id != null){
            button_add_plan.setText(getString(R.string.edit));
            plan_title_input.setText(default_plan_name);
            planStepsBar.setProgress(default_plan_target);
        }else{
            planStepsBar.setProgress(default_plan_target);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_add_plan:
                if(plan_title_input.getText().toString().equals("")){
                    Toast.makeText(this, "Plan Name cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    if(update_plan_id == null) {
                        AddPlan(new CreatePlanRequest(plan_title_input.getText().toString(), Integer.parseInt(active_minutes_value.getText().toString())));
                    }else{
                        UpdatePlan(update_plan_id, new UpdatePlanRequest(plan_title_input.getText().toString(), Integer.parseInt(active_minutes_value.getText().toString())));
                    }
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
            case R.id.mainView:
                finish();
                break;
        }
    }

    private void AddPlan(CreatePlanRequest createPlanRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<CommonMessageResponse> call = service.createPlan(createPlanRequest);

        call.enqueue(new Callback<CommonMessageResponse>() {
            @Override
            public void onResponse(Call<CommonMessageResponse> call, Response<CommonMessageResponse> response) {
                try {
                    CommonMessageResponse commonMessageResponse = response.body();
                    if(commonMessageResponse != null && commonMessageResponse.getMessage().equals(getString(R.string.operation_success))) {
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }else{
                        Gson g = new Gson();
                        commonMessageResponse = g.fromJson(response.errorBody().string(), CommonMessageResponse.class);
                        Toast.makeText(getApplicationContext(), commonMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception NewPlanActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure NewPlanActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void UpdatePlan(String plan_id, UpdatePlanRequest updatePlanRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_UPDATE_PLAN, plan_id);

        Call<CommonMessageResponse> call = service.updatePlan(url, updatePlanRequest);

        call.enqueue(new Callback<CommonMessageResponse>() {
            @Override
            public void onResponse(Call<CommonMessageResponse> call, Response<CommonMessageResponse> response) {
                try {
                    CommonMessageResponse commonMessageResponse = response.body();
                    if(commonMessageResponse != null && commonMessageResponse.getMessage().equals(getString(R.string.operation_success))) {
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }else{
                        Gson g = new Gson();
                        commonMessageResponse = g.fromJson(response.errorBody().string(), CommonMessageResponse.class);
                        Toast.makeText(getApplicationContext(),  commonMessageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception NewPlanActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure NewPlanActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
