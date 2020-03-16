package sg.edu.nus.imovin2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.MessageItemData;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Message_ViewHolder>{
    private List<MessageItemData> messageList;

    public MessageAdapter(List<MessageItemData> messageList){
        this.messageList = messageList;
    }

    public final static class Message_ViewHolder extends RecyclerView.ViewHolder {
        TextView message_title;
        TextView message_date;
        TextView message_content;

        public Message_ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_title = itemView.findViewById(R.id.message_title);
            message_date = itemView.findViewById(R.id.message_date);
            message_content = itemView.findViewById(R.id.message_content);
        }
    }

    @NonNull
    @Override
    public Message_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_message, viewGroup, false);

        return new MessageAdapter.Message_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Message_ViewHolder holder, int i) {
        MessageItemData messageItemData = messageList.get(i);
        holder.message_title.setText(messageItemData.getTitle());
        holder.message_date.setText(messageItemData.getDate());
        holder.message_content.setText(messageItemData.getContent());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
