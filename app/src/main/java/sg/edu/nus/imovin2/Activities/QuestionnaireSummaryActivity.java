package sg.edu.nus.imovin2.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.System.BaseActivity;

public class QuestionnaireSummaryActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.continueBtn) Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_summary);

        LinkUIById();
        SetFunction();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void SetFunction() {
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(getDrawable(R.color.theme_purple));
        ab.setTitle(getString(R.string.submission_confirmed));

        continueBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.continueBtn:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                break;
        }
    }
}
