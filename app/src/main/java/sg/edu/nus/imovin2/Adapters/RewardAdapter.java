package sg.edu.nus.imovin2.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsAvailableItemData;
import sg.edu.nus.imovin2.System.ImovinApplication;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardAdapter_ViewHolder>{
    private List<RewardsAvailableItemData> rewardsAvailableItemDataList;
    private HashMap<String, Boolean> hashMap;

    public RewardAdapter(List<RewardsAvailableItemData> rewardsAvailableItemDataList){
        this.rewardsAvailableItemDataList = rewardsAvailableItemDataList;
        hashMap = new HashMap<>();
    }

    public final static class RewardAdapter_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView reward_name;
        TextView reward_points;
        TextView quantity;

        public RewardAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
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

        if(hashMap.containsKey(rewardsAvailableItemData.getId()) && hashMap.get(rewardsAvailableItemData.getId())){
            holder.container.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.rounded_corner_with_theme_border_bg));
            holder.reward_name.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.theme_purple));
            holder.reward_points.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.theme_purple));
            holder.quantity.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.theme_purple));
        }else{
            holder.container.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.rounded_corner_with_border_bg));
            holder.reward_name.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.black_color));
            holder.reward_points.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.black_color));
            holder.quantity.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.black_color));
        }
    }

    @Override
    public int getItemCount() {
        return rewardsAvailableItemDataList.size();
    }

    public boolean checkItemSelected(String id){
        if(hashMap.containsKey(id) && hashMap.get(id)){
            return true;
        }
        return false;
    }

    public void selectItem(String id){
        if(hashMap.containsKey(id) && hashMap.get(id)){
            hashMap.put(id, false);
        }else{
            hashMap.put(id, true);
        }
    }
}
