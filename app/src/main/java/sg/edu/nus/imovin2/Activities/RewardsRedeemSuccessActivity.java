package sg.edu.nus.imovin2.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.RewardsAvailableItemData;

import static sg.edu.nus.imovin2.System.IntentConstants.REWARD_FIRST_DATA;

public class RewardsRedeemSuccessActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.close_btn) Button close_btn;
    @BindView(R.id.icon) ImageView icon;

    private RewardsAvailableItemData rewardsAvailableItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_redeem_success);

        LinkUIbyId();
        SetData();
        SetFunction();
        Init();
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

    private void SetData(){
        rewardsAvailableItemData = (RewardsAvailableItemData) getIntent().getSerializableExtra(REWARD_FIRST_DATA);
    }

    private void SetFunction() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        close_btn.setOnClickListener(this);
    }

    private void Init(){
        if(!rewardsAvailableItemData.getIcon().equals("")) {
            byte[] imageBytes = Base64.decode(rewardsAvailableItemData.getIcon(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            icon.setImageBitmap(bitmap);
        }
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
