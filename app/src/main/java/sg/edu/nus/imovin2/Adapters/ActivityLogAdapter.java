package sg.edu.nus.imovin2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.Objects.SubLogData;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.ActivityLogData;
import sg.edu.nus.imovin2.System.ImovinApplication;

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ActivityLogData_ViewHolder> {
    private List<ActivityLogData> activityLogDataList;

    public ActivityLogAdapter(List<ActivityLogData> activityLogDataList) {
        this.activityLogDataList = activityLogDataList;
    }

    public final static class ActivityLogData_ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        LinearLayout divider;
        ImageView log_icon;
        TextView name;
        TextView time;
        RecyclerView sub_log;

        public ActivityLogData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            divider = itemView.findViewById(R.id.divider);
            log_icon = itemView.findViewById(R.id.log_icon);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            sub_log = itemView.findViewById(R.id.sub_log);
        }
    }

    @NonNull
    @Override
    public ActivityLogData_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_activity_log, viewGroup, false);

        return new ActivityLogAdapter.ActivityLogData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityLogData_ViewHolder activityLogData_viewHolder, int position) {
        ActivityLogData activityLogData = activityLogDataList.get(position);
        activityLogData_viewHolder.date.setText(CommonFunc.GetDisplayDateDetail(activityLogData.getDate()));
        activityLogData_viewHolder.name.setText(activityLogData.getName());
        activityLogData_viewHolder.time.setText(CommonFunc.GetTimeDetail(activityLogData.getDate()));

        if(activityLogData.getDisplay_title()){
            activityLogData_viewHolder.date.setVisibility(View.VISIBLE);
            activityLogData_viewHolder.divider.setVisibility(View.GONE);
        }else{
            activityLogData_viewHolder.date.setVisibility(View.GONE);
            activityLogData_viewHolder.divider.setVisibility(View.VISIBLE);
        }

        if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_bike))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_bike));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_elliptical))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_elliptical));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_interval))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_interval));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_martialarts))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_martialarts));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_pilates))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_pilates));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_run))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_run));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_stairs))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_stairs));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_swim))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_swim));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_threadmill))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_threadmill));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_walk))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_walk));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_weights))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_weights));
        }else if(activityLogData.getName().toLowerCase().equals(ImovinApplication.getInstance().getString(R.string.log_yoga))){
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_yoga));
        }else{
            activityLogData_viewHolder.log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_workout));
        }

        List<SubLogData> subLogDataList = new ArrayList<>();
        if(activityLogData.getSteps() != null){
            subLogDataList.add(new SubLogData(ImovinApplication.getInstance().getString(R.string.log_steps), activityLogData.getSteps()));
        }
        if(activityLogData.getCalories() != null){
            subLogDataList.add(new SubLogData(ImovinApplication.getInstance().getString(R.string.log_calories), activityLogData.getCalories()));
        }
        if(activityLogData.getDuration() != null){
            subLogDataList.add(new SubLogData(ImovinApplication.getInstance().getString(R.string.log_duration), activityLogData.getDuration()));
        }
        if(activityLogData.getDistance() != null){
            subLogDataList.add(new SubLogData(ImovinApplication.getInstance().getString(R.string.log_distance), activityLogData.getDistance()));
        }
        if(activityLogData.getAverage_hr() != null){
            subLogDataList.add(new SubLogData(ImovinApplication.getInstance().getString(R.string.log_average_hr), activityLogData.getAverage_hr()));
        }

        SubActivityLogAdapter subActivityLogAdapter = new SubActivityLogAdapter(subLogDataList);

        activityLogData_viewHolder.sub_log.setLayoutManager(new GridLayoutManager(ImovinApplication.getInstance(), 2));
        activityLogData_viewHolder.sub_log.setAdapter(subActivityLogAdapter);
    }

    @Override
    public int getItemCount() {
        return activityLogDataList.size();
    }
}