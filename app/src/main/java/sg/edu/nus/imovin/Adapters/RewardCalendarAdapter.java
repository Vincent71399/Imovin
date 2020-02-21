package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.RewardsPointHistoryData;

public class RewardCalendarAdapter extends RecyclerView.Adapter<RewardCalendarAdapter.RewardCalendarAdapter_ViewHolder>{
    List<String> rewardsPointHistoryDataList;

    public RewardCalendarAdapter(List<String> rewardsPointHistoryDataList){
        this.rewardsPointHistoryDataList = rewardsPointHistoryDataList;
    }

    public final static class RewardCalendarAdapter_ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progress_bar;
        TextView date;

        public RewardCalendarAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            progress_bar = itemView.findViewById(R.id.progress_bar);
            date = itemView.findViewById(R.id.date);
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
        String rewardsPointHistoryData = rewardsPointHistoryDataList.get(position);

        if (rewardsPointHistoryData != null) {
            holder.progress_bar.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(rewardsPointHistoryData);
        } else {
            holder.progress_bar.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return rewardsPointHistoryDataList.size();
    }
}
