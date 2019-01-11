package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.Objects.ChallengeStar;
import sg.edu.nus.imovin.R;

public class StarAdapter extends RecyclerView.Adapter<StarAdapter.StarData_ViewHolder>{
    private List<ChallengeStar> challengeStarList;

    public StarAdapter(List<ChallengeStar> challengeStarList){
        this.challengeStarList = challengeStarList;
    }

    public final static class StarData_ViewHolder extends RecyclerView.ViewHolder {
        ImageView star_img;
        TextView points;
        TextView steps;
        public StarData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            star_img = itemView.findViewById(R.id.star_img);
            points = itemView.findViewById(R.id.points);
            steps = itemView.findViewById(R.id.steps);
        }
    }

    @NonNull
    @Override
    public StarData_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_star, viewGroup, false);

        return new StarAdapter.StarData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StarData_ViewHolder holder, int i) {
        ChallengeStar challengeStar = challengeStarList.get(i);

        switch (challengeStar.getColor()){
            case Gold:
                holder.star_img.setImageResource(R.drawable.icon_star_gold);
                break;
            case Silver:
                holder.star_img.setImageResource(R.drawable.icon_star_silver);
                break;
            case Bronze:
                holder.star_img.setImageResource(R.drawable.icon_star_bronze);
                break;
            default:
                holder.star_img.setImageResource(R.drawable.icon_star_gold);
                break;
        }

        holder.points.setText(String.valueOf(challengeStar.getPoints()));
        holder.steps.setText(String.valueOf(challengeStar.getSteps()));

    }

    @Override
    public int getItemCount() {
        return challengeStarList.size();
    }
}
