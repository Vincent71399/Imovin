package sg.edu.nus.imovin.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.MedalData;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.ValueConstants;

public class MedalAdapter extends RecyclerView.Adapter<MedalAdapter.MedalData_ViewHolder>{
    private List<MedalData> medalDataList;

    public MedalAdapter(List<MedalData> medalDataList){
        this.medalDataList = medalDataList;
    }

    public final static class MedalData_ViewHolder extends RecyclerView.ViewHolder {
        ImageView medal_img;
        TextView medal_count;

        public MedalData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            medal_img = itemView.findViewById(R.id.medal_img);
            medal_count = itemView.findViewById(R.id.medal_count);
        }
    }

    @NonNull
    @Override
    public MedalData_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_medal, viewGroup, false);

        return new MedalAdapter.MedalData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedalData_ViewHolder holder, int i) {
        MedalData medalData = medalDataList.get(i);

        int colorFilter = Color.argb(200,255,255,255);

        switch (medalData.getTier()){
            case ValueConstants.MEDAL_TIER_PLATINUM:
                holder.medal_count.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.tier3_circle_border_blackground));
                holder.medal_count.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.tier_3));
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_3);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_3);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_3);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_3);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_3);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_3);
                        break;
                }
                break;
            case ValueConstants.MEDAL_TIER_GOLD:
                holder.medal_count.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.tier2_circle_border_blackground));
                holder.medal_count.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.tier_2));
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_2);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_2);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_2);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_2);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_2);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_2);
                        break;
                }
                break;
            case ValueConstants.MEDAL_TIER_SILVER:
                holder.medal_count.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.tier1_circle_border_blackground));
                holder.medal_count.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.tier_1));
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_1);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_1);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_1);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_1);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_1);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_1);
                        break;
                }
                break;
            default:
                holder.medal_count.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.tier0_circle_border_blackground));
                holder.medal_count.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.tier_0));
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_0);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_0);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_0);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_0);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_0);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_0);
                        break;
                }
                break;
        }

        if(medalData.getObtained_count() <= 0){
            holder.medal_count.setVisibility(View.GONE);
            holder.medal_img.setColorFilter(colorFilter);
        }else if(medalData.getObtained_count() == 1){
            holder.medal_count.setVisibility(View.GONE);
        }else{
            holder.medal_count.setVisibility(View.VISIBLE);
            holder.medal_count.setText(String.valueOf(medalData.getObtained_count()));
        }
    }

    @Override
    public int getItemCount() {
        return medalDataList.size();
    }
}
