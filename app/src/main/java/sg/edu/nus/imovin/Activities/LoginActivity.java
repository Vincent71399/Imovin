package sg.edu.nus.imovin.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pusher.pushnotifications.PushNotifications;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Request.EmailLoginRequest;
import sg.edu.nus.imovin.Retrofit.Request.ResetPasswordRequest;
import sg.edu.nus.imovin.Retrofit.Response.EmailLoginResponse;
import sg.edu.nus.imovin.Retrofit.Response.ResetPasswordResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.Config;
import sg.edu.nus.imovin.System.FitbitConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;
import sg.edu.nus.imovin.System.SystemConstant;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.layout_container) LinearLayout layout_container;
    @BindView(R.id.imovin_image) ImageView imovin_image;
    @BindView(R.id.email_title) TextView email_title;
    @BindView(R.id.email_input) EditText email_input;
    @BindView(R.id.password_title) TextView password_title;
    @BindView(R.id.password_input) EditText password_input;
    @BindView(R.id.oauth_btn) Button oauth_btn;
    @BindView(R.id.forgot_pwd_btn) TextView forgot_pwd_btn;

    private boolean isResetPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HideActionBar();
        LinkUIById();
        SetFunction();
        Init();

    }

    @Override
    protected void onStop() {
        HideKeyboardAll();
        super.onStop();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
        SetMainView(mainView);
    }

    private void SetFunction(){
        layout_container.setOnClickListener(this);
        oauth_btn.setOnClickListener(this);
        forgot_pwd_btn.setOnClickListener(this);
    }

    private void Init(){
        if(Config.ENABLE_SPLASH_ANIMATION){
            Animation moveBottomToTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_from_bottom_to_top);
            moveBottomToTop.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    email_title.setVisibility(View.INVISIBLE);
                    email_input.setVisibility(View.INVISIBLE);
                    password_title.setVisibility(View.INVISIBLE);
                    password_input.setVisibility(View.INVISIBLE);
                    oauth_btn.setVisibility(View.INVISIBLE);
                    forgot_pwd_btn.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    email_title.setVisibility(View.VISIBLE);
                    email_input.setVisibility(View.VISIBLE);
                    password_title.setVisibility(View.VISIBLE);
                    password_input.setVisibility(View.VISIBLE);
                    oauth_btn.setVisibility(View.VISIBLE);
                    forgot_pwd_btn.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            imovin_image.setAnimation(moveBottomToTop);
        }

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);
        String username = preferences.getString(SystemConstant.USERNAME, "");
        String password = preferences.getString(SystemConstant.PASSWORD, "");

        Log.d("lutarez", "username : " + username + " ---  password : " + password);

        if(!username.equals("") && !password.equals("")){
            EmailLogin(username, password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_container:
                HideKeyboardAll();
                break;
            case R.id.oauth_btn:
                if(isResetPwd){
                    ResetPassword(email_input.getText().toString());
                }else {
                    EmailLogin(email_input.getText().toString(), password_input.getText().toString());
                }
                break;
            case R.id.forgot_pwd_btn:
                ToggleLoginReset();
                break;
        }
    }

    private void EmailLogin(final String email, final String password){
        if(email.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
        }else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ImovinService service = retrofit.create(ImovinService.class);

            Call<EmailLoginResponse> call = service.emailLogin(new EmailLoginRequest(email, password));

            call.enqueue(new Callback<EmailLoginResponse>() {
                @Override
                public void onResponse(Call<EmailLoginResponse> call, Response<EmailLoginResponse> response) {

                    Log.d(LogConstants.LogTag, "Response : " + response.toString());
                    try {
                        EmailLoginResponse emailLoginResponse = response.body();
                        Log.d(LogConstants.LogTag, emailLoginResponse.getMessage());
                        Log.d("token_value", emailLoginResponse.getData().getToken());
                        ImovinApplication.setUserData(emailLoginResponse.getData());

                        PushNotifications.start(getApplicationContext(), "b25cdd15-cea2-4078-9394-fff4ef98a3a7");
                        PushNotifications.subscribe(emailLoginResponse.getData().getEmail());

                        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SystemConstant.USERNAME, email);
                        editor.putString(SystemConstant.PASSWORD, password);
                        editor.apply();

                        Log.d("lutarez", "save username : " + email + " ---  password : " + password);

                        if(emailLoginResponse.getData().getFitbitAuthenticated()){
                            LaunchDashboard();
                            finish();
                        }else {
                            StartOauth();
                        }

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Fail to login", Toast.LENGTH_SHORT).show();
                        Log.d(LogConstants.LogTag, "Exception EmailLogin : " + e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<EmailLoginResponse> call, Throwable t) {
                    Log.d(LogConstants.LogTag, "Failure EmailLogin : " + t.toString());
                    Toast.makeText(getApplicationContext(), "Fail to login", Toast.LENGTH_SHORT).show();

                    SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(SystemConstant.USERNAME, "");
                    editor.putString(SystemConstant.PASSWORD, "");
                    editor.apply();
                }
            });
        }
    }

    private void StartOauth(){
        String url = String.format(
                Locale.ENGLISH,
                FitbitConstants.AuthorizationURI + FitbitConstants.FitbitAuthorizationFormat,
                FitbitConstants.ClientID,
                FitbitConstants.RedirectURI,
                FitbitConstants.Scopes,
                FitbitConstants.ExpiresIn,
                FitbitConstants.AuthorizationRequestState);

        Log.d(LogConstants.LogTag, "url");

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        finish();
    }

    private void ResetPassword(String email){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<ResetPasswordResponse> call = service.resetPassword(new ResetPasswordRequest(email));

        call.enqueue(new Callback<ResetPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {

                Log.d(LogConstants.LogTag, "Response : " + response.toString());
                try {
                    ResetPasswordResponse resetPasswordResponse = response.body();
                    Log.d(LogConstants.LogTag, String.valueOf(resetPasswordResponse.getMeta().getCode()));

                    if(resetPasswordResponse.getMeta().getCode().equals(200)){
                        Toast.makeText(getApplicationContext(), "Reset password requested, please wait for email for reset link", Toast.LENGTH_SHORT).show();
                        ToggleLoginReset();
                    }else {
                        Toast.makeText(getApplicationContext(), "Reset password fail, please check internet connection", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Reset password fail, please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.d(LogConstants.LogTag, "Exception EmailLogin : " + e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure EmailLogin : " + t.toString());
                Toast.makeText(getApplicationContext(), "Fail to login", Toast.LENGTH_SHORT).show();

                SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SystemConstant.USERNAME, "");
                editor.putString(SystemConstant.PASSWORD, "");
                editor.apply();
            }
        });

    }

    private void ToggleLoginReset(){
        if(isResetPwd){
            isResetPwd = false;
            password_title.setVisibility(View.VISIBLE);
            password_input.setVisibility(View.VISIBLE);
            oauth_btn.setText(getString(R.string.login_btn_title));
            forgot_pwd_btn.setText(getString(R.string.forgot_password_title));
        }else {
            isResetPwd = true;
            password_title.setVisibility(View.GONE);
            password_input.setVisibility(View.GONE);
            oauth_btn.setText(getString(R.string.reset_password_btn_title));
            forgot_pwd_btn.setText(getString(R.string.return_to_login_title));
        }
    }

    private void LaunchDashboard(){
        Intent intent = new Intent();
        intent.setClass(this, DashBoardActivity.class);
        startActivity(intent);
    }

    private void HideKeyboardAll(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(email_input.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(password_input.getWindowToken(), 0);
    }
}
