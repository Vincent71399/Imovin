package sg.edu.nus.imovin.System;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


public class BaseActivity extends AppCompatActivity {
    private RelativeLayout mainView;
    private ProgressBar progressBar;
    private RelativeLayout.LayoutParams progressParams;
    private LinearLayout coverLayout;
    private LinearLayout.LayoutParams coverParams;

    private ProgressDialog mProgressDialog;

    private boolean hasCover = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SetupViews();
    }

    private void SetupViews(){
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        progressParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        coverLayout = new LinearLayout(this);
        coverParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        coverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Message");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(true);
    }

    protected void SetMainView(RelativeLayout mainView){
        this.mainView = mainView;
    }

    protected void HideActionBar(){
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
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
