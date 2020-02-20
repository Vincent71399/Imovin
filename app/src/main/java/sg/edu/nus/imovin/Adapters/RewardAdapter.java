package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.R;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardAdapter_ViewHolder>{
    private List<String> rewardsDataList;

    public RewardAdapter(List<String> rewardsDataList){
        this.rewardsDataList = rewardsDataList;
    }

    public final static class RewardAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView reward_name;
        TextView reward_points;

        public RewardAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            reward_name = itemView.findViewById(R.id.reward_name);
            reward_points = itemView.findViewById(R.id.reward_points);
        }
    }

    @NonNull
    @Override
    public RewardAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_reward, viewGroup, false);

        return new RewardAdapter.RewardAdapter_ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RewardAdapter_ViewHolder holder, int position) {
        String reward_name = rewardsDataList.get(position);
        holder.reward_name.setText(reward_name);
    }

    @Override
    public int getItemCount() {
        return rewardsDataList.size();
    }
}
