package sg.edu.nus.imovin.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.R;

public class RewardsCheckoutActivity extends AppCompatActivity implements View.OnClickListener {

    private View customActionBar;

    @BindView(R.id.navigator_left) LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image) ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;

    @BindView(R.id.checkout_btn) Button checkout_btn;
    @BindView(R.id.return_btn) Button return_btn;

    @BindView(R.id.collection_time_choose_box) RadioGroup collection_time_choose_box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_checkout);

        SetActionBar();
        LinkUIbyId();
        SetFunction();
        Init();
    }

    private void SetActionBar(){
        ActionBar actionBar = getSupportActionBar();
        customActionBar = getLayoutInflater().inflate(R.layout.sub_navigator, null);

        if(actionBar != null){
            actionBar.show();
            actionBar.setCustomView(customActionBar);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Toolbar parent =(Toolbar) customActionBar.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this);
    }

    private void SetFunction() {
        navigator_left_text.setText(getString(R.string.return_text));
        navigator_left_text.setVisibility(View.VISIBLE);
        navigator_left_image.setVisibility(View.VISIBLE);

        navigator_left.setOnClickListener(this);
        checkout_btn.setOnClickListener(this);
        return_btn.setOnClickListener(this);
    }

    private void Init(){
        List<String> choices = new ArrayList<>();
        choices.add("4th February 2020, 12pm-1pm");
        choices.add("4th February 2020, 1pm-2pm");
        choices.add("4th February 2020, 2pm-3pm");

        for(String choice : choices){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(choice);
            collection_time_choose_box.addView(radioButton);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navigator_left:
                finish();
                break;
            case R.id.return_btn:
                finish();
                break;
            case R.id.checkout_btn:
                Intent intent = new Intent();
                intent.setClass(this, RewardsCheckoutConfirmActivity.class);
                startActivity(intent);
                break;
        }
    }
}
