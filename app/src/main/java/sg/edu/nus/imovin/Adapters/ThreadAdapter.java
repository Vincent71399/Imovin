package sg.edu.nus.imovin.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.ThreadData;

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

        public ThreadData_ViewHolder(View itemView){
            super(itemView);
            container = itemView.findViewById(R.id.container);
            title_text = itemView.findViewById(R.id.title_text);
            owner_text = itemView.findViewById(R.id.owner_text);
            comment_count = itemView.findViewById(R.id.comment_count);
            post_time = itemView.findViewById(R.id.post_time);
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
        ThreadData threadData = threadDataList.get(position);

        holder.title_text.setText(threadData.getTitle());
        holder.owner_text.setText(threadData.getOwnerName());
        holder.comment_count.setText(String.valueOf(threadData.getComments().size()));
        holder.post_time.setText(ConvertDateString2DisplayFormat(threadData.getCreatedAt()));

        holder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return threadDataList.size();
    }
}
