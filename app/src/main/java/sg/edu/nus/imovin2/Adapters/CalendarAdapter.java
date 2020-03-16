package sg.edu.nus.imovin2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin2.Objects.Goal;
import sg.edu.nus.imovin2.R;

public class CalendarAdapter extends ArrayAdapter<Goal> {

    private Context mContext;

    public CalendarAdapter(Context context, List<Goal> indexList) {
        super(context, R.layout.item_calendar_cell, indexList);
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CalendarCellViewHolder holder;
        final Goal goal = getItem(position);

        if(convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_calendar_cell, null);
            holder = createCalendarCellViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (CalendarCellViewHolder) convertView.getTag();
        }
        holder = SetupCellItem(goal, holder);

        return convertView;
    }

    private CalendarCellViewHolder SetupCellItem(final Goal goal, final CalendarCellViewHolder holder){
        if(goal.getTitle()){
            holder.progress_bar.setVisibility(View.GONE);
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(goal.getDate());
        }else {
            if (goal.getShown()) {
                holder.progress_bar.setVisibility(View.VISIBLE);
                holder.date.setVisibility(View.VISIBLE);
                holder.progress_bar.setMax(goal.getGoal());
                holder.progress_bar.setProgress(goal.getSteps());
                holder.date.setText(goal.getDate());
            } else {
                holder.progress_bar.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);
            }
        }
        return holder;
    }

    private CalendarCellViewHolder createCalendarCellViewHolder(View cellItem) {
        CalendarCellViewHolder calendarCellViewHolder = new CalendarCellViewHolder();
        calendarCellViewHolder.progress_bar = cellItem.findViewById(R.id.progress_bar);
        calendarCellViewHolder.date = cellItem.findViewById(R.id.date);
        return calendarCellViewHolder;
    }

    private static class CalendarCellViewHolder {
        private ProgressBar progress_bar;
        private TextView date;
    }
}
