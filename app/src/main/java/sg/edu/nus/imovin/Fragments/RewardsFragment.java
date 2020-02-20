package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Activities.RewardsCheckoutActivity;
import sg.edu.nus.imovin.Adapters.RewardAdapter;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.System.BaseFragment;


public class RewardsFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;

    @BindView(R.id.reward_list)
    RecyclerView reward_list;
    @BindView(R.id.question_mark)
    TextView question_mark;
    @BindView(R.id.redeem_btn)
    Button redeem_btn;

    public static RewardsFragment getInstance() {
        RewardsFragment rewardsFragment = new RewardsFragment();
        return rewardsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rewards, null);

        LinkUIById();
        SetFunction();

        return rootView;
    }

    private void LinkUIById() {
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction() {
        List<String> testList = new ArrayList<>();
        testList.add("reward_1");
        testList.add("reward_2");
        testList.add("reward_3");

        RewardAdapter rewardAdapter = new RewardAdapter(testList);

        reward_list.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        reward_list.setAdapter(rewardAdapter);

        question_mark.setOnClickListener(this);
        redeem_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.redeem_btn:
                Intent intent = new Intent();
                intent.setClass(getActivity(), RewardsCheckoutActivity.class);
                startActivity(intent);
                break;
        }
    }
}