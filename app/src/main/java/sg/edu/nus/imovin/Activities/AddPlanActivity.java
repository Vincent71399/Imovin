package sg.edu.nus.imovin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Request.CreatePlanRequest;
import sg.edu.nus.imovin.Retrofit.Request.UpdatePlanRequest;
import sg.edu.nus.imovin.Retrofit.Response.PlanResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseSimpleActivity;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_UPDATE_PLAN;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class AddPlanActivity extends BaseSimpleActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.plan_title_input) EditText plan_title_input;
    @BindView(R.id.steps_value) TextView steps_value;
    @BindView(R.id.planStepsBar) SeekBar planStepsBar;
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

    private void SetFunction(){
        button_add_plan.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        planStepsBar.setOnSeekBarChangeListener(this);
    }

    private void Init(){
        String default_plan_name = getIntent().getStringExtra(Default_Plan_Name);
        update_plan_id = getIntent().getStringExtra(Update_Plan_ID);
        int default_plan_target = getIntent().getIntExtra(Default_Plan_Target, 10000);

        if(update_plan_id != null){
            button_add_plan.setText(getString(R.string.edit));
            plan_title_input.setText(default_plan_name);
            int progress = 100 * (default_plan_target - 5000) / 10000;
            planStepsBar.setProgress(progress);
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
                        AddPlan(new CreatePlanRequest(plan_title_input.getText().toString(), Integer.parseInt(steps_value.getText().toString())));
                    }else{
                        UpdatePlan(update_plan_id, new UpdatePlanRequest(plan_title_input.getText().toString(), Integer.parseInt(steps_value.getText().toString())));
                    }
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int steps = 5000 + 10000 * progress / 100;
        steps_value.setText(String.valueOf(steps));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void AddPlan(CreatePlanRequest createPlanRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<PlanResponse> call = service.createPlan(createPlanRequest);

        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                try {
                    PlanResponse planResponse = response.body();
                    Log.d(LogConstants.LogTag, "NewPlanActivity : " + planResponse.getMessage());
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception NewPlanActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
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

        Call<PlanResponse> call = service.updatePlan(url, updatePlanRequest);

        call.enqueue(new Callback<PlanResponse>() {
            @Override
            public void onResponse(Call<PlanResponse> call, Response<PlanResponse> response) {
                try {
                    PlanResponse planResponse = response.body();
                    if(planResponse != null) {
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception NewPlanActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PlanResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure NewPlanActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
