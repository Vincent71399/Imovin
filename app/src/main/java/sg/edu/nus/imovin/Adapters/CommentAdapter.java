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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import sg.edu.nus.imovin.Event.DeleteCommentEvent;
import sg.edu.nus.imovin.Event.DeleteSocialCommentEvent;
import sg.edu.nus.imovin.Event.EditCommentEvent;
import sg.edu.nus.imovin.Event.EditSocialCommentEvent;
import sg.edu.nus.imovin.Event.LikeCommentEvent;
import sg.edu.nus.imovin.Event.LikeSocialCommentEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.CommentData;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;

import static sg.edu.nus.imovin.Common.CommonFunc.ConvertDateString2DisplayFormat;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentData_ViewHolder>{
    private List<CommentData> commentDataList;
    private String func_blk;

    public CommentAdapter(List<CommentData> commentDataList, String func_blk){
        this.commentDataList = commentDataList;
        this.func_blk = func_blk;
    }

    public final static class CommentData_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView usernameText;
        TextView postTimeText;
        TextView commentContentText;
        TextView numberLikesText;
        ImageView thumbsUpImage;
        LinearLayout thumbs_up_container;
        LinearLayout edit_container;
        LinearLayout delete_container;

        public CommentData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            usernameText = itemView.findViewById(R.id.poster_text);
            postTimeText = itemView.findViewById(R.id.post_time_text);
            commentContentText = itemView.findViewById(R.id.comment_text);
            numberLikesText = itemView.findViewById(R.id.likes_text);
            thumbsUpImage = itemView.findViewById(R.id.thumbs_up_image);
            thumbs_up_container = itemView.findViewById(R.id.thumbs_up_container);
            edit_container = itemView.findViewById(R.id.edit_container);
            delete_container = itemView.findViewById(R.id.delete_container);
        }
    }

    @NonNull
    @Override
    public CommentData_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_comment, viewGroup, false);

        return new CommentAdapter.CommentData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentData_ViewHolder holder, int position) {
        final CommentData commentData = commentDataList.get(position);

        holder.usernameText.setText(commentData.getUser_name());
        holder.postTimeText.setText(ConvertDateString2DisplayFormat(commentData.getUpdated_at()));
        holder.commentContentText.setText(commentData.getMessage());
        holder.numberLikesText.setText(String.valueOf(commentData.getLikes()));
        boolean hasLiked = commentData.getLiked_by_me();
        if(hasLiked){
            holder.thumbsUpImage.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_colored_small));
        }else {
            holder.thumbsUpImage.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_small));
        }
        holder.thumbs_up_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean like = !commentData.getLiked_by_me();
                switch (func_blk) {
                    case IntentConstants.THREAD_COMMENT:
                        EventBus.getDefault().post(new LikeCommentEvent(commentData.get_id(), like));
                        break;
                    case IntentConstants.SOCIAL_POST_COMMENT:
                        EventBus.getDefault().post(new LikeSocialCommentEvent(commentData.get_id(), like));
                        break;
                }
            }
        });
        if(commentData.getUser_id().equals(ImovinApplication.getUserInfoResponse().get_id())) {
            holder.edit_container.setVisibility(View.VISIBLE);
            holder.delete_container.setVisibility(View.VISIBLE);
            holder.edit_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (func_blk) {
                        case IntentConstants.THREAD_COMMENT:
                            EventBus.getDefault().post(new EditCommentEvent(commentData.get_id()));
                            break;
                        case IntentConstants.SOCIAL_POST_COMMENT:
                            EventBus.getDefault().post(new EditSocialCommentEvent(commentData.get_id()));
                            break;
                    }
                }
            });
            holder.delete_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (func_blk) {
                        case IntentConstants.THREAD_COMMENT:
                            EventBus.getDefault().post(new DeleteCommentEvent(commentData.get_id()));
                            break;
                        case IntentConstants.SOCIAL_POST_COMMENT:
                            EventBus.getDefault().post(new DeleteSocialCommentEvent(commentData.get_id()));
                            break;
                    }
                }
            });
        }else{
            holder.edit_container.setVisibility(View.GONE);
            holder.delete_container.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return commentDataList.size();
    }
}

