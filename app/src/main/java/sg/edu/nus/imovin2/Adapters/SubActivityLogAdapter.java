package sg.edu.nus.imovin2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin2.Objects.SubLogData;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.System.ImovinApplication;


public class SubActivityLogAdapter extends RecyclerView.Adapter<SubActivityLogAdapter.SubActivityLog_ViewHolder>{
    private List<SubLogData> subLogDataList;

    public SubActivityLogAdapter(List<SubLogData> subLogDataList) {
        this.subLogDataList = subLogDataList;
    }

    public final static class SubActivityLog_ViewHolder extends RecyclerView.ViewHolder {
        ImageView sub_log_icon;
        TextView sub_log_text;

        public SubActivityLog_ViewHolder(@NonNull View itemView) {
            super(itemView);
            sub_log_icon = itemView.findViewById(R.id.sub_log_icon);
            sub_log_text = itemView.findViewById(R.id.sub_log_text);
        }
    }

    @NonNull
    @Override
    public SubActivityLog_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_sub_log, viewGroup, false);

        return new SubActivityLogAdapter.SubActivityLog_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubActivityLog_ViewHolder subActivityLog_viewHolder, int position) {
        SubLogData subLogData = subLogDataList.get(position);
        subActivityLog_viewHolder.sub_log_text.setText(subLogData.getValue() + " " + subLogData.getName());

        if(subLogData.getName().equals(ImovinApplication.getInstance().getString(R.string.log_steps))){
            subActivityLog_viewHolder.sub_log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_steps));
        }else if(subLogData.getName().equals(ImovinApplication.getInstance().getString(R.string.log_calories))){
            subActivityLog_viewHolder.sub_log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_calorie));
        }else if(subLogData.getName().equals(ImovinApplication.getInstance().getString(R.string.log_duration))){
            subActivityLog_viewHolder.sub_log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_duration));
        }else if(subLogData.getName().equals(ImovinApplication.getInstance().getString(R.string.log_distance))){
            subActivityLog_viewHolder.sub_log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_distance));
        }else if(subLogData.getName().equals(ImovinApplication.getInstance().getString(R.string.log_average_hr))){
            subActivityLog_viewHolder.sub_log_icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.transparent_heart));
        }
    }

    @Override
    public int getItemCount() {
        return subLogDataList.size();
    }
}
