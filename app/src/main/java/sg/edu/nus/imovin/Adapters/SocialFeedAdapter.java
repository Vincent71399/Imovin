package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import sg.edu.nus.imovin.Event.LaunchSocialPostDetailEvent;
import sg.edu.nus.imovin.Event.LikeSocialPostEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin.System.ImovinApplication;

import static sg.edu.nus.imovin.Common.CommonFunc.ConvertDateString2DisplayFormat;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_GET_SOCIAL_POST_IMAGE;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;


public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.SocialFeedData_ViewHolder> {
    private List<SocialFeedData> socialFeedDataList;

    public SocialFeedAdapter(List<SocialFeedData> socialFeedDataList){
        this.socialFeedDataList = socialFeedDataList;
    }

    public final static class SocialFeedData_ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        LinearLayout body_container;
        TextView owner_text;
        TextView body_text;
        ImageView social_image;
        LinearLayout thumbs_up_container;
        ImageView thumbs_up_image;
        TextView likes_text;
        TextView comment_count;
        TextView post_time;

        public SocialFeedData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            body_container = itemView.findViewById(R.id.body_container);
            owner_text = itemView.findViewById(R.id.owner_text);
            body_text = itemView.findViewById(R.id.body_text);
            social_image = itemView.findViewById(R.id.social_image);
            thumbs_up_container = itemView.findViewById(R.id.thumbs_up_container);
            thumbs_up_image = itemView.findViewById(R.id.thumbs_up_image);
            likes_text = itemView.findViewById(R.id.likes_text);
            comment_count = itemView.findViewById(R.id.comment_count);
            post_time = itemView.findViewById(R.id.post_time);
        }
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

        holder.body_text.setText(socialFeedData.getMessage());
        holder.owner_text.setText(socialFeedData.getUser_name());

        if(socialFeedData.getComments() <= 1) {
            holder.comment_count.setText(socialFeedData.getComments() + " " + ImovinApplication.getInstance().getString(R.string.comments_topics_single));
        }else {
            holder.comment_count.setText(socialFeedData.getComments() + " " + ImovinApplication.getInstance().getString(R.string.comments_topics));
        }
        holder.post_time.setText(ConvertDateString2DisplayFormat(socialFeedData.getCreated_at()));

        boolean hasLiked = socialFeedData.getLiked_by_me();
        if(hasLiked){
            holder.thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_colored_small));
        }else {
            holder.thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_small));
        }
        holder.likes_text.setText(String.valueOf(socialFeedData.getLikes()));

        if(socialFeedData.getImage() != null){
            String imageUrl = SERVER + String.format(
                    Locale.ENGLISH,REQUEST_GET_SOCIAL_POST_IMAGE, socialFeedData.get_id());
            ImageLoader.getInstance().displayImage(imageUrl, holder.social_image);
        }

        holder.thumbs_up_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean like = !socialFeedData.getLiked_by_me();
                EventBus.getDefault().post(new LikeSocialPostEvent(socialFeedData.get_id(), like));
            }
        });
        holder.body_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LaunchSocialPostDetailEvent(socialFeedData.get_id()));
            }
        });
        holder.comment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new LaunchSocialPostDetailEvent(socialFeedData.get_id()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return socialFeedDataList.size();
    }
}

