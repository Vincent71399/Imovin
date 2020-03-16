package sg.edu.nus.imovin2.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.Adapters.RewardCheckoutAdapter;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsAvailableItemData;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsData;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsSlotData;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsSlotItemData;
import sg.edu.nus.imovin2.Retrofit.Request.RewardsPostCheckoutRedemptionRequest;
import sg.edu.nus.imovin2.Retrofit.Response.CommonMessageResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin2.System.IntentConstants.REWARD_CHECKOUT_DATA;
import static sg.edu.nus.imovin2.System.IntentConstants.REWARD_TIME;
import static sg.edu.nus.imovin2.System.IntentConstants.REWARD_VENUE;

public class RewardsCheckoutConfirmActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.reward_checkout_list) RecyclerView reward_checkout_list;
    @BindView(R.id.collection_time) TextView collection_time;
    @BindView(R.id.collection_venue) TextView collection_venue;
    @BindView(R.id.confirm_btn) Button confirm_btn;
    @BindView(R.id.close_btn) Button close_btn;

    private RewardsData rewardsData;
    private RewardsSlotData selectedSlotData;
    private RewardsSlotItemData selectedSlotTimeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_checkout_confirm);

        LinkUIById();
        SetData();
        SetFunction();
    }

    private void LinkUIById() {
        ButterKnife.bind(this);
    }

    private void SetData(){
        rewardsData = (RewardsData) getIntent().getSerializableExtra(REWARD_CHECKOUT_DATA);
        selectedSlotData = (RewardsSlotData) getIntent().getSerializableExtra(REWARD_VENUE);
        selectedSlotTimeData = (RewardsSlotItemData) getIntent().getSerializableExtra(REWARD_TIME);

        RewardCheckoutAdapter rewardCheckoutAdapter = new RewardCheckoutAdapter(rewardsData.getAvailable_items());
        reward_checkout_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reward_checkout_list.setAdapter(rewardCheckoutAdapter);

        collection_venue.setText(selectedSlotData.getVenue());
        collection_time.setText(selectedSlotTimeData.getSlot_time());
    }

    private void SetFunction() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        confirm_btn.setOnClickListener(this);
        close_btn.setOnClickListener(this);
    }

    private void postRewardCheckout(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        RewardsPostCheckoutRedemptionRequest rewardsPostCheckoutRedemptionRequest = new RewardsPostCheckoutRedemptionRequest();
        rewardsPostCheckoutRedemptionRequest.setSlot(selectedSlotTimeData.getSlot_id());
        List<String> itemList = new ArrayList<>();
        for(RewardsAvailableItemData rewardsAvailableItemData : rewardsData.getAvailable_items()){
            itemList.add(rewardsAvailableItemData.getId());
        }
        rewardsPostCheckoutRedemptionRequest.setItems(itemList);

        Call<CommonMessageResponse> call = service.postRewardsCheckoutRedemption(rewardsPostCheckoutRedemptionRequest);

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
                    Log.d(LogConstants.LogTag, "Exception RewardsCheckoutConfirmActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure RewardsCheckoutConfirmActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.confirm_btn:
                postRewardCheckout();
                break;
            case R.id.close_btn:
                finish();
                break;
        }
    }
}
