package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.RewardsPointHistoryData;
import sg.edu.nus.imovin.System.ImovinApplication;

public class RewardCalendarAdapter extends RecyclerView.Adapter<RewardCalendarAdapter.RewardCalendarAdapter_ViewHolder>{

    private Calendar selectDate;
    private List<RewardsPointHistoryData> rewardsPointHistoryDataList;

    public RewardCalendarAdapter(List<RewardsPointHistoryData> rewardsPointHistoryDataList){
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
                inflate(R.layout.item_reward_calendar_cell, viewGroup, false);

        return new RewardCalendarAdapter.RewardCalendarAdapter_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardCalendarAdapter_ViewHolder holder, int position) {
        RewardsPointHistoryData rewardsPointHistoryData = rewardsPointHistoryDataList.get(position);

        if (rewardsPointHistoryData != null) {
            Calendar calendar = CommonFunc.RevertFullDateStringRevert(rewardsPointHistoryData.getDate());
            holder.progress_bar.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            if(calendar != null) {
                holder.date.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            }
            if(selectDate != null) {
                if (calendar.compareTo(selectDate) == 0) {
                    holder.progress_bar.setProgress(100);
                    holder.progress_bar.setVisibility(View.VISIBLE);
                    holder.date.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.white_color));
                } else {
                    holder.progress_bar.setProgress(0);
                    holder.progress_bar.setVisibility(View.INVISIBLE);
                    if(rewardsPointHistoryData.getPoints() == 0) {
                        holder.date.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.lesser_grey_color));
                    }else{
                        holder.date.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.black_color));
                    }
                }
            }

        } else {
            holder.progress_bar.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return rewardsPointHistoryDataList.size();
    }

    public void setSelectDate(Calendar selectDate) {
        this.selectDate = selectDate;
    }
}
