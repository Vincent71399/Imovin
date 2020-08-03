package sg.edu.nus.imovin2.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.Common.CommonFunc;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.MedalData;
import sg.edu.nus.imovin2.System.ValueConstants;

import static sg.edu.nus.imovin2.System.IntentConstants.MEDAL_DATA;

public class ChallengePopupActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.medal_title) TextView medal_title;
    @BindView(R.id.medal_desc) TextView medal_desc;
    @BindView(R.id.medal_img) ImageView medal_img;
    @BindView(R.id.medal_last_updated) TextView medal_last_updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_popup);

        LinkUIbyId();
        SetFunction();
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void SetFunction(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mainView.setOnClickListener(this);

        MedalData medalData = (MedalData) getIntent().getSerializableExtra(MEDAL_DATA);

        String medal_tier = "";
        switch (medalData.getTier()){
            case ValueConstants.MEDAL_TIER_BRONZE:
                medal_tier = "BRONZE";
                break;
            case ValueConstants.MEDAL_TIER_SILVER:
                medal_tier = "SILVER";
                break;
            case ValueConstants.MEDAL_TIER_GOLD:
                medal_tier = "GOLD";
                break;
            case ValueConstants.MEDAL_TIER_PLATINUM:
                medal_tier = "PLATINUM";
                break;
        }
        String title = medalData.getName() + " - " + medal_tier;
        medal_title.setText(title);

        switch (medalData.getTier()){
            case ValueConstants.MEDAL_TIER_PLATINUM:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        medal_img.setImageResource(R.drawable.daily_steps_3);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        medal_img.setImageResource(R.drawable.active_days_for_the_week_3);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        medal_img.setImageResource(R.drawable.daily_total_distance_3);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        medal_img.setImageResource(R.drawable.weekly_exercise_duration_3);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        medal_img.setImageResource(R.drawable.total_days_with_steps_3);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        medal_img.setImageResource(R.drawable.active_weeks_in_a_row_3);
                        break;
                }
                break;
            case ValueConstants.MEDAL_TIER_GOLD:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        medal_img.setImageResource(R.drawable.daily_steps_2);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        medal_img.setImageResource(R.drawable.active_days_for_the_week_2);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        medal_img.setImageResource(R.drawable.daily_total_distance_2);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        medal_img.setImageResource(R.drawable.weekly_exercise_duration_2);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        medal_img.setImageResource(R.drawable.total_days_with_steps_2);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        medal_img.setImageResource(R.drawable.active_weeks_in_a_row_2);
                        break;
                }
                break;
            case ValueConstants.MEDAL_TIER_SILVER:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        medal_img.setImageResource(R.drawable.daily_steps_1);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        medal_img.setImageResource(R.drawable.active_days_for_the_week_1);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        medal_img.setImageResource(R.drawable.daily_total_distance_1);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        medal_img.setImageResource(R.drawable.weekly_exercise_duration_1);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        medal_img.setImageResource(R.drawable.total_days_with_steps_1);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        medal_img.setImageResource(R.drawable.active_weeks_in_a_row_1);
                        break;
                }
                break;
            default:
                switch (medalData.getCategory()){
                    case ValueConstants.CATEGORY_DAILY_STEP:
                        medal_img.setImageResource(R.drawable.daily_steps_0);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_DAYS_FOR_THE_WEEK:
                        medal_img.setImageResource(R.drawable.active_days_for_the_week_0);
                        break;
                    case ValueConstants.CATEGORY_DAILY_TOTAL_DISTANCE:
                        medal_img.setImageResource(R.drawable.daily_total_distance_0);
                        break;
                    case ValueConstants.CATEGORY_WEEKLY_EXERCISE_DURATION:
                        medal_img.setImageResource(R.drawable.weekly_exercise_duration_0);
                        break;
                    case ValueConstants.CATEGORY_TOTAL_DAYS_WITH_STEPS:
                        medal_img.setImageResource(R.drawable.total_days_with_steps_0);
                        break;
                    case ValueConstants.CATEGORY_ACTIVE_WEEKS_IN_A_ROW:
                        medal_img.setImageResource(R.drawable.active_weeks_in_a_row_0);
                        break;
                }
                break;
        }

        medal_desc.setText(medalData.getDescription() + " - Point : " + medalData.getPoints());
        if(medalData.getLast_achieved_at() != null){
            medal_last_updated.setText("Last Acheived: " + CommonFunc.GetDisplayDate(CommonFunc.RevertFullDateStringRevert(medalData.getLast_achieved_at())));
        }else{
            medal_last_updated.setText("Not Acheived Yet");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.mainView:
                finish();
                break;
        }
    }
}
