package sg.edu.nus.imovin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.System.FuncBlockConstants;
import sg.edu.nus.imovin.System.ImovinApplication;

public class TabAdapter extends ArrayAdapter<String> {
    private int mSelectedIndex = -1;

    public TabAdapter(Context context, List<String> tabList) {
        super(context, 0, tabList);
    }

    public void setSelection(int position) {
        mSelectedIndex =  position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_more_tab, parent, false
            );
        }

        ImageView tab_image = convertView.findViewById(R.id.tab_image);
        TextView tab_text = convertView.findViewById(R.id.tab_text);

        String tab_name = getItem(position);

        if (tab_name != null) {
            tab_text.setText(tab_name);
            if(mSelectedIndex == position) {
                tab_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), FuncBlockConstants.getSelectIcon_by_title(tab_name)));
                tab_text.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.blue_color));
            }else{
                tab_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), FuncBlockConstants.getUnselectIcon_by_title(tab_name)));
                tab_text.setTextColor(ContextCompat.getColor(ImovinApplication.getInstance(), R.color.black_color));
            }
        }

        return convertView;
    }
}
