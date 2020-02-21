package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.RewardsPointHistoryData;

public class RewardCalendarAdapter extends RecyclerView.Adapter<RewardCalendarAdapter.RewardCalendarAdapter_ViewHolder>{
    List<RewardsPointHistoryData> rewardsPointHistoryDataList;

    public RewardCalendarAdapter(List<RewardsPointHistoryData> rewardsPointHistoryDataList){
        this.rewardsPointHistoryDataList = rewardsPointHistoryDataList;
    }

    public final static class RewardCalendarAdapter_ViewHolder extends RecyclerView.ViewHolder {

        public RewardCalendarAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public RewardCalendarAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_calendar_cell, viewGroup, false);

        return new RewardCalendarAdapter.RewardCalendarAdapter_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardCalendarAdapter_ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return rewardsPointHistoryDataList.size();
    }
}
