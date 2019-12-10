package sg.edu.nus.imovin.Adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.MedalData;
import sg.edu.nus.imovin.System.ValueConstants;

public class MedalAdapter extends RecyclerView.Adapter<MedalAdapter.MedalData_ViewHolder>{
    private List<MedalData> medalDataList;

    public MedalAdapter(List<MedalData> medalDataList){
        this.medalDataList = medalDataList;
    }

    public final static class MedalData_ViewHolder extends RecyclerView.ViewHolder {
        ImageView medal_img;
        TextView earned_count;

        public MedalData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            medal_img = itemView.findViewById(R.id.medal_img);
            earned_count = itemView.findViewById(R.id.earned_count);
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

        int colorFilter = Color.argb(160,255,255,255);

        switch (medalData.getTier()){
            case ValueConstants.MEDAL_TIER_PLATINUM:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_3_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_3_sub);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_3_sub);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_3_sub);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_3_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_3_sub);
                        break;
                }
                break;
            case ValueConstants.MEDAL_TIER_GOLD:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_2_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_2_sub);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_2_sub);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_2_sub);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_2_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_2_sub);
                        break;
                }
                break;
            case ValueConstants.MEDAL_TIER_SILVER:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_1_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_1_sub);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_1_sub);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_1_sub);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_1_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_1_sub);
                        break;
                }
                break;
            default:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        holder.medal_img.setImageResource(R.drawable.daily_steps_0_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        holder.medal_img.setImageResource(R.drawable.active_days_for_the_week_0_sub);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        holder.medal_img.setImageResource(R.drawable.daily_total_distance_0_sub);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        holder.medal_img.setImageResource(R.drawable.weekly_exercise_duration_0_sub);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        holder.medal_img.setImageResource(R.drawable.total_days_with_steps_0_sub);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        holder.medal_img.setImageResource(R.drawable.active_weeks_in_a_row_0_sub);
                        break;
                }
                break;
        }

        if(medalData.getObtained_count() <= 0){
            holder.medal_img.setColorFilter(colorFilter);
        }

        if(medalData.getObtained_count() <= 1) {
            holder.earned_count.setText(medalData.getObtained_count() + " time");
        }else{
            holder.earned_count.setText(medalData.getObtained_count() + " times");
        }
    }

    @Override
    public int getItemCount() {
        return medalDataList.size();
    }
}
