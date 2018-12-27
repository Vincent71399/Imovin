package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Activities.MonitorChangePlanActivity;
import sg.edu.nus.imovin.Adapters.CalendarAdapter;
import sg.edu.nus.imovin.Objects.Goal;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.System.ImovinApplication;

import static sg.edu.nus.imovin.Common.CommonFunc.GetCurrentMonthString;

public class MonitorFragment extends Fragment implements View.OnClickListener {
    private View rootView;

    @BindView(R.id.date_text) TextView date_text;
    @BindView(R.id.calendar_gridview) GridView calendar_gridview;
    @BindView(R.id.change_plan_btn) TextView change_plan_btn;

    public static MonitorFragment getInstance() {
        MonitorFragment monitorFragment = new MonitorFragment();
        return monitorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_monitor, null);

        LinkUIById();
        SetFunction();

        return rootView;
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        change_plan_btn.setOnClickListener(this);

        date_text.setText(GetCurrentMonthString());

        CalendarAdapter calendarAdapter = new CalendarAdapter(ImovinApplication.getInstance(),  generateCalendar());

        calendar_gridview.setAdapter(calendarAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_plan_btn:
                Intent intent = new Intent();
                intent.setClass(getActivity(), MonitorChangePlanActivity.class);
                startActivity(intent);
                break;
        }
    }

    private List<Goal> generateCalendar(){
        List<Goal> goalList = new ArrayList<>();

        Calendar firstDay = Calendar.getInstance();
        firstDay.set(Calendar.DAY_OF_MONTH, 1);

        int numDays = firstDay.getActualMaximum(Calendar.DATE);

        int day = firstDay.get(Calendar.DAY_OF_WEEK);

        goalList.add(new Goal("SUN"));
        goalList.add(new Goal("MON"));
        goalList.add(new Goal("TUE"));
        goalList.add(new Goal("WED"));
        goalList.add(new Goal("THU"));
        goalList.add(new Goal("FRI"));
        goalList.add(new Goal("SAT"));

        for(int i=0; i<day-1; i++){
            goalList.add(new Goal(false));
        }
        for(int i=0; i<numDays; i++){
            goalList.add(new Goal(650, 1000, String.valueOf(i+1)));
        }

        return goalList;
    }

}
