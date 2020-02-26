package sg.edu.nus.imovin.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import sg.edu.nus.imovin.Adapters.RewardCheckoutAdapter;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.RewardsAvailableItemData;
import sg.edu.nus.imovin.Retrofit.Object.RewardsData;
import sg.edu.nus.imovin.Retrofit.Object.RewardsSlotData;
import sg.edu.nus.imovin.Retrofit.Object.RewardsSlotItemData;
import sg.edu.nus.imovin.Retrofit.Response.RewardsSlotsResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin.System.IntentConstants.REWARD_CHECKOUT_DATA;

public class RewardsCheckoutActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private View customActionBar;

    @BindView(R.id.navigator_left) LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image) ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;

    @BindView(R.id.reward_checkout_list) RecyclerView reward_checkout_list;

    @BindView(R.id.collection_venue_choose_box) RadioGroup collection_venue_choose_box;

    @BindView(R.id.checkout_btn) Button checkout_btn;
    @BindView(R.id.return_btn) Button return_btn;

    @BindView(R.id.collection_time_choose_box) RadioGroup collection_time_choose_box;

    private List<RewardsAvailableItemData> rewardsAvailableItemDataList;
    private RewardCheckoutAdapter rewardCheckoutAdapter;

    private List<RewardsSlotData> rewardsSlotDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_checkout);

        SetActionBar();
        LinkUIbyId();
        SetData();
        SetFunction();
        Init();
    }

    private void SetActionBar(){
        ActionBar actionBar = getSupportActionBar();
        customActionBar = getLayoutInflater().inflate(R.layout.sub_navigator, null);

        if(actionBar != null){
            actionBar.show();
            actionBar.setCustomView(customActionBar);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Toolbar parent =(Toolbar) customActionBar.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this);
    }

    private void SetData() {
        RewardsData rewardsData = (RewardsData) getIntent().getSerializableExtra(REWARD_CHECKOUT_DATA);
        rewardsAvailableItemDataList = rewardsData.getAvailable_items();

        rewardCheckoutAdapter = new RewardCheckoutAdapter(rewardsAvailableItemDataList);
        reward_checkout_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reward_checkout_list.setAdapter(rewardCheckoutAdapter);
    }

    private void SetFunction() {
        navigator_left_text.setText(getString(R.string.return_text));
        navigator_left_text.setVisibility(View.VISIBLE);
        navigator_left_image.setVisibility(View.VISIBLE);

        navigator_left.setOnClickListener(this);
        checkout_btn.setOnClickListener(this);
        return_btn.setOnClickListener(this);

        collection_venue_choose_box.setOnCheckedChangeListener(this);
    }

    private void Init(){
        getRewardSlotData();
    }

    private void SetupData(){
        for(RewardsSlotData rewardsSlotData : rewardsSlotDataList){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(rewardsSlotData.getVenue());
            radioButton.setTextSize(16);
            radioButton.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.black_color));
            collection_venue_choose_box.addView(radioButton);
        }
    }

    private void SetupTimeData(int selectVenue){
        for (int i=0; i< collection_time_choose_box.getChildCount(); i++){
            collection_time_choose_box.removeViewAt(i);
        }

        for(RewardsSlotItemData rewardsSlotItemData : rewardsSlotDataList.get(selectVenue).getSlots()){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(rewardsSlotItemData.getSlot_time());
            radioButton.setTextSize(16);
            radioButton.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.black_color));
            collection_time_choose_box.addView(radioButton);
        }
    }

    private void getRewardSlotData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<RewardsSlotsResponse> call = service.getRewardsSlots();

        call.enqueue(new Callback<RewardsSlotsResponse>() {
            @Override
            public void onResponse(Call<RewardsSlotsResponse> call, Response<RewardsSlotsResponse> response) {
                try {
                    RewardsSlotsResponse rewardsSlotsResponse = response.body();
                    rewardsSlotDataList = rewardsSlotsResponse.getData();
                    SetupData();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception RewardsCheckoutActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RewardsSlotsResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure RewardsCheckoutActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navigator_left:
                finish();
                break;
            case R.id.return_btn:
                finish();
                break;
            case R.id.checkout_btn:
                Intent intent = new Intent();
                intent.setClass(this, RewardsCheckoutConfirmActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = findViewById(checkedId);
        for(int i=0; i<rewardsSlotDataList.size(); i++){
            if(rb.getText().equals(rewardsSlotDataList.get(i).getVenue())){
                SetupTimeData(i);
            }
        }
    }
}
