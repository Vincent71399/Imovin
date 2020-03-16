package sg.edu.nus.imovin2.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.System.ImovinApplication;

public class MessageTitleAdapter extends RecyclerView.Adapter<MessageTitleAdapter.MessageTitle_ViewHolder> {
    private List<String> messageTitleList;
    private int selectedTab = 0;

    public MessageTitleAdapter(List<String> messageTitleList){
        this.messageTitleList = messageTitleList;
    }

    public final static class MessageTitle_ViewHolder extends RecyclerView.ViewHolder {
        TextView message_title;

        public MessageTitle_ViewHolder(@NonNull View itemView) {
            super(itemView);
            message_title = itemView.findViewById(R.id.message_title);
        }
    }

    @NonNull
    @Override
    public MessageTitle_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_message_title, viewGroup, false);

        return new MessageTitleAdapter.MessageTitle_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageTitle_ViewHolder holder, int i) {
        String message = messageTitleList.get(i);
        holder.message_title.setText(message);
        if(i == selectedTab){
            holder.message_title.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.tab_background_selected));
            holder.message_title.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.theme_purple));
        }else{
            holder.message_title.setBackground(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.tab_background_not_selected));
            holder.message_title.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return messageTitleList.size();
    }

    public void setSelection(int selectedTab){
        this.selectedTab = selectedTab;
    }
}
