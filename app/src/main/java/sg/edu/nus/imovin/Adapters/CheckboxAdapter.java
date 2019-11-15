package sg.edu.nus.imovin.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import sg.edu.nus.imovin.Event.ChangeCheckEvent;
import sg.edu.nus.imovin.Objects.CheckboxOption;
import sg.edu.nus.imovin.R;

public class CheckboxAdapter extends RecyclerView.Adapter<CheckboxAdapter.Checkbox_ViewHolder>{
    private List<CheckboxOption> checkboxOptionList;

    public CheckboxAdapter(List<CheckboxOption> checkboxOptionList){
        this.checkboxOptionList = checkboxOptionList;
    }

    public final static class Checkbox_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        CheckBox checkbox;
        TextView option_title;

        public Checkbox_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            checkbox = itemView.findViewById(R.id.checkbox);
            option_title = itemView.findViewById(R.id.option_title);
        }
    }

    @NonNull
    @Override
    public Checkbox_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_checkbox, viewGroup, false);

        return new CheckboxAdapter.Checkbox_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Checkbox_ViewHolder holder, final int position) {
        final CheckboxOption checkboxOption = checkboxOptionList.get(position);

        holder.checkbox.setChecked(checkboxOption.getIs_check());
        holder.option_title.setText(checkboxOption.getOption_message());

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                EventBus.getDefault().post(new ChangeCheckEvent(position, isChecked));
            }
        });
    }

    @Override
    public int getItemCount() {
        return checkboxOptionList.size();
    }
}

