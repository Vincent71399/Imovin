package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Array;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Adapters.PlanAdapter;
import sg.edu.nus.imovin.Objects.PlanDataCategory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.PlanData;

public class GoalFragment extends Fragment {
    private View rootView;

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

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        PlanData p1 = new PlanData("Test1");
        PlanData p2 = new PlanData("Test2");
        PlanData p3 = new PlanData("Test3");
        PlanData p4 = new PlanData("Test4");

        PlanDataCategory c1 = new PlanDataCategory("C1", Arrays.asList(p1, p2));
        PlanDataCategory c2 = new PlanDataCategory("C2", Arrays.asList(p3, p4));

        PlanAdapter adapter = new PlanAdapter(getActivity(), Arrays.asList(c1, c2));

        plan_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        plan_list.setAdapter(adapter);
    }
}
