package sg.edu.nus.imovin2.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.System.IntentConstants;

public class ConsentDocActivity extends AppCompatActivity implements View.OnClickListener, ViewTreeObserver.OnScrollChangedListener{

    @BindView(R.id.consent_form) ScrollView consent_form;
    @BindView(R.id.agreeBtn) Button agreeBtn;
    @BindView(R.id.disagreeBtn) Button disagreeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_doc);

        LinkUIById();
        SetFunction();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void SetFunction(){
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(getDrawable(R.color.theme_purple));
        ab.setTitle(getString(R.string.cf));

        agreeBtn.setOnClickListener(this);
        agreeBtn.setEnabled(false);
        disagreeBtn.setOnClickListener(this);
        consent_form.getViewTreeObserver().addOnScrollChangedListener(this);

        ViewTreeObserver observer = consent_form.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int viewHeight = consent_form.getMeasuredHeight();
                int contentHeight = consent_form.getChildAt(0).getHeight();
                if(viewHeight - contentHeight >= 0) {
                    // not scrollable
                    agreeBtn.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.agreeBtn:
                Intent intent = new Intent();
                intent.setClass(this, ConsentSignActivity.class);
                startActivityForResult(intent, IntentConstants.CONSENT);
                break;
            case R.id.disagreeBtn:
                finish();
                break;
        }
    }

    @Override
    public void onScrollChanged() {
        if (consent_form.getChildAt(0).getBottom()
                <= (consent_form.getHeight() + consent_form.getScrollY())) {
            //scroll view is at bottom
            agreeBtn.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IntentConstants.CONSENT:
                if(resultCode == RESULT_OK){
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                break;
        }
    }
}
