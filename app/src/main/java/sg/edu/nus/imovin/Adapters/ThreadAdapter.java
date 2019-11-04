package sg.edu.nus.imovin.Adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import sg.edu.nus.imovin.Event.LikeCommentEvent;
import sg.edu.nus.imovin.Event.LikeThreadEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.ThreadData;
import sg.edu.nus.imovin.System.ImovinApplication;

import static sg.edu.nus.imovin.Common.CommonFunc.ConvertDateString2DisplayFormat;

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ThreadData_ViewHolder>{
    private List<ThreadData> threadDataList;
    private SparseBooleanArray selectedItems;

    public ThreadAdapter(List<ThreadData> threadDataList){
        this.threadDataList = threadDataList;
        selectedItems = new SparseBooleanArray();
    }

    public final static class ThreadData_ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout container;
        TextView title_text;
        TextView owner_text;
        TextView comment_count;
        TextView post_time;
        TextView likes_text;
        ImageView thumbs_up_image;
        LinearLayout thumbs_up_container;

        public ThreadData_ViewHolder(View itemView){
            super(itemView);
            container = itemView.findViewById(R.id.container);
            title_text = itemView.findViewById(R.id.title_text);
            owner_text = itemView.findViewById(R.id.owner_text);
            comment_count = itemView.findViewById(R.id.comment_count);
            post_time = itemView.findViewById(R.id.post_time);
            likes_text = itemView.findViewById(R.id.likes_text);
            thumbs_up_image = itemView.findViewById(R.id.thumbs_up_image);
            thumbs_up_container = itemView.findViewById(R.id.thumbs_up_container);
        }
    }

    @Override
    public ThreadData_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_thread, viewGroup, false);

        return new ThreadData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ThreadData_ViewHolder holder, int position) {
        final ThreadData threadData = threadDataList.get(position);

        holder.title_text.setText(threadData.getTitle());
        holder.owner_text.setText(threadData.getUser_name());
        holder.comment_count.setText(String.valueOf(threadData.getComments().size()));
        holder.post_time.setText(ConvertDateString2DisplayFormat(threadData.getCreated_at()));

        holder.itemView.setActivated(selectedItems.get(position, false));

        boolean hasLiked = threadData.getLiked_by_me();
        if(hasLiked){
            holder.thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_colored_small));
        }else {
            holder.thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_small));
        }
        holder.thumbs_up_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean like = !threadData.getLiked_by_me();
                EventBus.getDefault().post(new LikeThreadEvent(threadData.get_id(), like));
            }
        });
    }

    @Override
    public int getItemCount() {
        return threadDataList.size();
    }
}
