package sg.edu.nus.imovin2.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsAvailableItemData;
import sg.edu.nus.imovin2.System.ImovinApplication;

public class RewardCheckoutAdapter extends RecyclerView.Adapter<RewardCheckoutAdapter.RewardCheckoutAdapter_ViewHolder>{
    private List<RewardsAvailableItemData> rewardsAvailableItemDataList;

    public RewardCheckoutAdapter(List<RewardsAvailableItemData> rewardsAvailableItemDataList){
        this.rewardsAvailableItemDataList = rewardsAvailableItemDataList;
    }

    public final static class RewardCheckoutAdapter_ViewHolder extends RecyclerView.ViewHolder {
        TextView reward_name;
        TextView reward_points;
        ImageView icon;

        public RewardCheckoutAdapter_ViewHolder(@NonNull View itemView) {
            super(itemView);
            reward_name = itemView.findViewById(R.id.reward_name);
            reward_points = itemView.findViewById(R.id.reward_points);
            icon = itemView.findViewById(R.id.icon);
        }
    }

    @NonNull
    @Override
    public RewardCheckoutAdapter_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_reward_checkout, viewGroup, false);

        return new RewardCheckoutAdapter.RewardCheckoutAdapter_ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RewardCheckoutAdapter_ViewHolder holder, int position) {
        RewardsAvailableItemData rewardsAvailableItemData = rewardsAvailableItemDataList.get(position);
        holder.reward_name.setText(rewardsAvailableItemData.getName());
        holder.reward_points.setText(CommonFunc.Integer2String(rewardsAvailableItemData.getPoints()) + " " + ImovinApplication.getInstance().getString(R.string.points));

        if(!rewardsAvailableItemData.getIcon().equals("")){
            byte[] imageBytes = Base64.decode(rewardsAvailableItemData.getIcon(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            holder.icon.setImageBitmap(bitmap);
        }else{
            holder.icon.setImageDrawable(ImovinApplication.getInstance().getDrawable(R.drawable.gift_redeemed));
        }
    }

    @Override
    public int getItemCount() {
        return rewardsAvailableItemDataList.size();
    }
}
