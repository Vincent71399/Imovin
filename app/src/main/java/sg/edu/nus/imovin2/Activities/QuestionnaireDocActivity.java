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

public class QuestionnaireDocActivity extends AppCompatActivity implements View.OnClickListener, ViewTreeObserver.OnScrollChangedListener{

    @BindView(R.id.questionnaire_form) ScrollView questionnaire_form;
    @BindView(R.id.agreeBtn) Button agreeBtn;
    @BindView(R.id.disagreeBtn) Button disagreeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_doc);

        LinkUIById();
        SetFunction();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void SetFunction(){
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(getDrawable(R.color.theme_purple));
        ab.setTitle(getString(R.string.qn));

        agreeBtn.setOnClickListener(this);
        agreeBtn.setEnabled(false);
        disagreeBtn.setOnClickListener(this);
        questionnaire_form.getViewTreeObserver().addOnScrollChangedListener(this);

        ViewTreeObserver observer = questionnaire_form.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int viewHeight = questionnaire_form.getMeasuredHeight();
                int contentHeight = questionnaire_form.getChildAt(0).getHeight();
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
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
            case R.id.disagreeBtn:
                finish();
                break;
        }
    }

    @Override
    public void onScrollChanged() {
        if (questionnaire_form.getChildAt(0).getBottom()
                <= (questionnaire_form.getHeight() + questionnaire_form.getScrollY())) {
            //scroll view is at bottom
            agreeBtn.setEnabled(true);
        }
    }
}
