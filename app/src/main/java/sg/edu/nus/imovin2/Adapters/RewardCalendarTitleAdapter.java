package sg.edu.nus.imovin2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin2.R;

public class RewardCalendarTitleAdapter extends RecyclerView.Adapter<RewardCalendarTitleAdapter.RewardCalendarAdapter_ViewHolder>{
    List<String> calendarTitleList;

    public RewardCalendarTitleAdapter(List<String> calendarTitleList){
        this.calendarTitleList = calendarTitleList;
    }

    public final static class RewardCalendarAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public RewardCalendarAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }

    @NonNull
    @Override
    public RewardCalendarAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_calendar_title, viewGroup, false);

        return new RewardCalendarTitleAdapter.RewardCalendarAdapter_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardCalendarAdapter_ViewHolder holder, int position) {
        String title = calendarTitleList.get(position);
        holder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return calendarTitleList.size();
    }
}
