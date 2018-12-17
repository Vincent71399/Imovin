package sg.edu.nus.imovin.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Event.LikeCommentEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.CommentData;
import sg.edu.nus.imovin.Retrofit.Request.LikeCommentRequest;
import sg.edu.nus.imovin.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin.Retrofit.Response.ThreadMultiResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.Common.CommonFunc.ConvertDateString2DisplayFormat;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_LIKE_COMMENT;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentData_ViewHolder>{
    private List<CommentData> commentDataList;

    public CommentAdapter(List<CommentData> commentDataList){
        this.commentDataList = commentDataList;
    }

    public final static class CommentData_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView usernameText;
        TextView postTimeText;
        TextView commentContentText;
        TextView numberLikesText;
        ImageView thumbsUpImage;
        LinearLayout thumbs_up_container;
        public CommentData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            usernameText = itemView.findViewById(R.id.poster_text);
            postTimeText = itemView.findViewById(R.id.post_time_text);
            commentContentText = itemView.findViewById(R.id.comment_text);
            numberLikesText = itemView.findViewById(R.id.likes_text);
            thumbsUpImage = itemView.findViewById(R.id.thumbs_up_image);
            thumbs_up_container = itemView.findViewById(R.id.thumbs_up_container);
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

        holder.usernameText.setText(commentData.getOwnerName());
        holder.postTimeText.setText(ConvertDateString2DisplayFormat(commentData.getCreatedAt()));
        holder.commentContentText.setText(commentData.getMessage());
        holder.numberLikesText.setText(String.valueOf(commentData.getLikes().size()));
        boolean hasLiked = commentData.getLikes().contains(ImovinApplication.getUserData().getUid());
        if(hasLiked){
            holder.thumbsUpImage.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_colored_small));
        }else {
            holder.thumbsUpImage.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_small));
        }
        holder.thumbs_up_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean like = !commentData.getLikes().contains(ImovinApplication.getUserData().getUid());
                EventBus.getDefault().post(new LikeCommentEvent(commentData.getId(), like));
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentDataList.size();
    }
}

