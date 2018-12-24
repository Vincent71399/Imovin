package sg.edu.nus.imovin.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;

import static sg.edu.nus.imovin.Common.CommonFunc.ConvertDateString2DisplayFormat;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.SocialFeedData_ViewHolder> {
    private List<SocialFeedData> socialFeedDataList;

    public SocialFeedAdapter(Activity context, List<SocialFeedData> socialFeedDataList){
        this.socialFeedDataList = socialFeedDataList;
    }

    @NonNull
    @Override
    public SocialFeedData_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_social_feed, viewGroup, false);

        return new SocialFeedAdapter.SocialFeedData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialFeedData_ViewHolder holder, int i) {
        final SocialFeedData socialFeedData = socialFeedDataList.get(i);

        holder.usernameText.setText(socialFeedData.getOwnerName());
        holder.postTimeText.setText(ConvertDateString2DisplayFormat(socialFeedData.getCreatedAt()));
        holder.feedContentText.setText(socialFeedData.getMessage());
        holder.imageView.setImageResource(R.drawable.imovin_big);
        //ImovinApplication.getImageLoader().displayImage(socialFeedData.getImageUrl(), holder.imageView);
        holder.commentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.numberCommentsText.setText(String.valueOf(socialFeedData.getComments().size()));
    }

    @Override
    public int getItemCount() {
        return socialFeedDataList.size();
    }

    public final static class SocialFeedData_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView usernameText;
        TextView postTimeText;
        TextView feedContentText;
        ImageView imageView;
        ImageView commentsImageView;
        TextView numberCommentsText;
        public SocialFeedData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            usernameText = itemView.findViewById(R.id.username);
            postTimeText = itemView.findViewById(R.id.post_since);
            feedContentText = itemView.findViewById(R.id.feedContentText);
            imageView = itemView.findViewById(R.id.feedImage);
            commentsImageView = itemView.findViewById(R.id.commentIcon);
            numberCommentsText = itemView.findViewById(R.id.numberCommentsText);
        }
    }
}

