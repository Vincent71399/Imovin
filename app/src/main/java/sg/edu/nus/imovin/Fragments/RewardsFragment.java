package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
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
import sg.edu.nus.imovin.Activities.RewardsCalendarActivity;
import sg.edu.nus.imovin.Activities.RewardsCheckoutActivity;
import sg.edu.nus.imovin.Activities.RewardsRedeemSuccessActivity;
import sg.edu.nus.imovin.Adapters.RewardAdapter;
import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.Common.RecyclerItemClickListener;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.RewardsAvailableItemData;
import sg.edu.nus.imovin.Retrofit.Object.RewardsData;
import sg.edu.nus.imovin.Retrofit.Response.RewardsResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin.System.IntentConstants.REWARD_CHECKOUT_DATA;
import static sg.edu.nus.imovin.System.IntentConstants.REWARD_DAILY_DATA;


public class RewardsFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;

    @BindView(R.id.reward_list) RecyclerView reward_list;
    @BindView(R.id.question_mark) TextView question_mark;
    @BindView(R.id.points) TextView points;
    @BindView(R.id.next_redeem_guide_text) TextView next_redeem_guide_text;
    @BindView(R.id.reward_progress_bar) ProgressBar reward_progress_bar;

    @BindView(R.id.redeem_btn) Button redeem_btn;

    private List<RewardsAvailableItemData> rewardsAvailableItemDataList;
    private RewardAdapter rewardAdapter;
    private RewardsData rewardsData;
    private Integer min_point_for_redemption;

    public static RewardsFragment getInstance() {
        RewardsFragment rewardsFragment = new RewardsFragment();
        return rewardsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rewards, null);

        LinkUIById();
        SetFunction();
        Init();

        return rootView;
    }

    private void LinkUIById() {
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction() {
        if(rewardsAvailableItemDataList == null){
            rewardsAvailableItemDataList = new ArrayList<>();
        }else{
            rewardsAvailableItemDataList.clear();
        }

        rewardAdapter = new RewardAdapter(rewardsAvailableItemDataList);

        reward_list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        reward_list.setAdapter(rewardAdapter);

        reward_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RewardsAvailableItemData rewardsAvailableItemData = rewardsAvailableItemDataList.get(position);
                if(rewardsAvailableItemData != null){
                    RewardSelectFunc(rewardsAvailableItemData);
                }
            }
        }));

        question_mark.setOnClickListener(this);
        redeem_btn.setOnClickListener(this);
    }

    private void Init(){
        getRewardData();
    }

    private void RewardSelectFunc(RewardsAvailableItemData rewardsAvailableItemData){
        if(rewardAdapter.checkItemSelected(rewardsAvailableItemData.getId())){
            rewardsData.setPoints(rewardsData.getPoints() + rewardsAvailableItemData.getPoints());
            rewardAdapter.selectItem(rewardsAvailableItemData.getId());
            SetPointAndProgress();
        }else{
            if(rewardsData.getPoints() < rewardsAvailableItemData.getPoints()){
                Toast.makeText(getActivity(), "Not enough Points!", Toast.LENGTH_SHORT).show();
            }else{
                rewardsData.setPoints(rewardsData.getPoints() - rewardsAvailableItemData.getPoints());
                rewardAdapter.selectItem(rewardsAvailableItemData.getId());
                SetPointAndProgress();
            }
        }

        rewardAdapter.notifyDataSetChanged();
    }

    private void SetupData(RewardsData rewardsData){
        this.rewardsData = rewardsData;

        min_point_for_redemption = null;
        for(RewardsAvailableItemData rewardsAvailableItemData : this.rewardsData.getAvailable_items()){
            if(rewardsAvailableItemData.getQuantity() > 0){
                if(min_point_for_redemption == null){
                    min_point_for_redemption = rewardsAvailableItemData.getPoints();
                }else if(min_point_for_redemption > rewardsAvailableItemData.getPoints()){
                    min_point_for_redemption = rewardsAvailableItemData.getPoints();
                }
                rewardsAvailableItemDataList.add(rewardsAvailableItemData);
            }
        }
        rewardAdapter.notifyDataSetChanged();

        SetPointAndProgress();
    }

    private void SetPointAndProgress(){
        points.setText(CommonFunc.Integer2String(rewardsData.getPoints()));

        if(min_point_for_redemption != null){
            if(min_point_for_redemption < rewardsData.getPoints()){
                reward_progress_bar.setProgress(100);
                next_redeem_guide_text.setText(getString(R.string.to_next_reward) + " " + 0);
            }else{
                reward_progress_bar.setProgress(100 * rewardsData.getPoints() / min_point_for_redemption);
                int next_point = min_point_for_redemption - rewardsData.getPoints();
                next_redeem_guide_text.setText(getString(R.string.to_next_reward) + " " + next_point);
            }
        }
    }

    private void getRewardData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<RewardsResponse> call = service.getRewards();

        call.enqueue(new Callback<RewardsResponse>() {
            @Override
            public void onResponse(Call<RewardsResponse> call, Response<RewardsResponse> response) {
                try {
                    RewardsResponse rewardsResponse = response.body();
                    SetupData(rewardsResponse.getData());
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception RewardsFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RewardsResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure RewardsFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.question_mark:
                Intent intentDetail = new Intent();
                intentDetail.setClass(ImovinApplication.getInstance(), RewardsCalendarActivity.class);
                intentDetail.putExtra(REWARD_DAILY_DATA, rewardsData);
                startActivity(intentDetail);
                break;
            case R.id.redeem_btn:
                RewardsData rewardsData = generateCheckoutList();
                if(rewardsData.getAvailable_items().size() > 0) {
                    Intent intentCheckout = new Intent();
                    intentCheckout.setClass(ImovinApplication.getInstance(), RewardsCheckoutActivity.class);
                    intentCheckout.putExtra(REWARD_CHECKOUT_DATA, rewardsData);
                    startActivity(intentCheckout);
                }else{
                    Toast.makeText(getActivity(), ImovinApplication.getInstance().getString(R.string.no_item_selected_message), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private RewardsData generateCheckoutList(){
        List<RewardsAvailableItemData> rewardsAvailableItemDataList = new ArrayList<>();
        for(RewardsAvailableItemData rewardsAvailableItemData : this.rewardsAvailableItemDataList){
            if(rewardAdapter.checkItemSelected(rewardsAvailableItemData.getId())){
                rewardsAvailableItemDataList.add(rewardsAvailableItemData);
            }
        }

        RewardsData rewardsData = new RewardsData();
        rewardsData.setAvailable_items(rewardsAvailableItemDataList);
        return rewardsData;
    }
}