package sg.edu.nus.imovin2.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.R;

public class RewardsRedeemSuccessActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.close_btn) Button close_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_redeem_success);

        LinkUIbyId();
        SetFunction();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this);
    }

    private void SetFunction() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        close_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close_btn:
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

}
