package sg.edu.nus.imovin2.System;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import sg.edu.nus.imovin2.R;

public class BaseFragment extends Fragment {

    private RelativeLayout mainView;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams progressParams;
    private LinearLayout coverLayout;
    private LinearLayout.LayoutParams coverParams;

    private boolean hasCover = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SetupViews();
    }

    private void SetupViews(){
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        progressParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        coverLayout = new LinearLayout(getActivity());
        coverLayout.setBackgroundResource(R.color.grey_transparent);
        coverParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        coverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });
    }

    protected void SetMainView(RelativeLayout mainView){
        this.mainView = mainView;
    }

    protected void ShowConnectIndicator(){
        if(mainView != null) {
            if (!hasCover) {
                mainView.addView(coverLayout, coverParams);
                mainView.addView(progressBar, progressParams);
                hasCover = true;
            }
        }
    }

    protected void HideConnectIndicator(){
        if(mainView != null) {
            if (hasCover) {
                mainView.removeView(progressBar);
                mainView.removeView(coverLayout);
                hasCover = false;
            }
        }
    }
}
