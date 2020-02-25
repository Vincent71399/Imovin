package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.RewardsAvailableItemData;
import sg.edu.nus.imovin.System.ImovinApplication;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardAdapter_ViewHolder>{
    private List<RewardsAvailableItemData> rewardsAvailableItemDataList;

    public RewardAdapter(List<RewardsAvailableItemData> rewardsAvailableItemDataList){
        this.rewardsAvailableItemDataList = rewardsAvailableItemDataList;
    }

    public final static class RewardAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView reward_name;
        TextView reward_points;
        TextView quantity;

        public RewardAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            reward_name = itemView.findViewById(R.id.reward_name);
            reward_points = itemView.findViewById(R.id.reward_points);
            quantity = itemView.findViewById(R.id.quantity);
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
        RewardsAvailableItemData rewardsAvailableItemData = rewardsAvailableItemDataList.get(position);
        holder.reward_name.setText(rewardsAvailableItemData.getName());
        holder.reward_points.setText(CommonFunc.Integer2String(rewardsAvailableItemData.getPoints()) + " " + ImovinApplication.getInstance().getString(R.string.points));
        holder.quantity.setText(rewardsAvailableItemData.getQuantity() + " " + ImovinApplication.getInstance().getString(R.string.left));
    }

    @Override
    public int getItemCount() {
        return rewardsAvailableItemDataList.size();
    }
}
