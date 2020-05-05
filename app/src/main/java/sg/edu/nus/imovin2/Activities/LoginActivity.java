package sg.edu.nus.imovin2.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Request.EmailLoginRequest;
import sg.edu.nus.imovin2.Retrofit.Request.ResetPasswordRequest;
import sg.edu.nus.imovin2.Retrofit.Response.EmailLoginResponse;
import sg.edu.nus.imovin2.Retrofit.Response.QuestionnaireResponse;
import sg.edu.nus.imovin2.Retrofit.Response.ResetPasswordResponse;
import sg.edu.nus.imovin2.Retrofit.Response.UserInfoResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.Services.MonitorConnectionService;
import sg.edu.nus.imovin2.System.BaseActivity;
import sg.edu.nus.imovin2.System.Config;
import sg.edu.nus.imovin2.System.FitbitConstants;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.IntentConstants;
import sg.edu.nus.imovin2.System.LogConstants;
import sg.edu.nus.imovin2.System.SystemConstant;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;
import static sg.edu.nus.imovin2.System.Config.AUTO_LOGIN;


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

    private EmailLoginResponse emailLoginResponse;

    private int redirect = -1;
    private boolean isAutoLogin = false;

    private Intent monitor_connection_service_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("redirect")) {
            redirect = Integer.parseInt(getIntent().getExtras().getString("redirect"));
            Log.d("fcm_notification", "login redirect : " + redirect);
        }

        HideActionBar();
        LinkUIById();
        SetFunction();
        Init();
        StartPendingUploadService();
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

        if(AUTO_LOGIN) {
            SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);

            String username = preferences.getString(SystemConstant.USERNAME, "");
            String password = preferences.getString(SystemConstant.PASSWORD, "");

            Log.d("lutarez", "username : " + username + " ---  password : " + password);

            if (!username.equals("") && !password.equals("")) {
                isAutoLogin = true;
                EmailLogin(username, password);
            }
        }
    }

    public void StartPendingUploadService(){
        monitor_connection_service_intent = new Intent(getApplicationContext(), MonitorConnectionService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(monitor_connection_service_intent);
//        } else {
            startService(monitor_connection_service_intent);
//        }
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
                        emailLoginResponse = response.body();
                        Log.d(LogConstants.LogTag, emailLoginResponse.getMessage());
                        Log.d("token_value", emailLoginResponse.getData().getToken());

                        ImovinApplication.setToken(emailLoginResponse.getData().getToken());

                        SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SystemConstant.USERNAME, email);
                        editor.putString(SystemConstant.PASSWORD, password);
                        editor.apply();

                        Log.d("lutarez", "save username : " + email + " ---  password : " + password);

                        if(!emailLoginResponse.getData().getHas_consented()) {
                            StartConsent();
                        }else {
                            GetQuestionNaire();
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

    private void StartConsent(){
        Intent intent = new Intent();
        intent.setClass(this, ConsentDocActivity.class);
        startActivityForResult(intent, IntentConstants.CONSENT);
    }

    private void GetQuestionNaire(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<QuestionnaireResponse> call = service.getQuestionnaire();

        call.enqueue(new Callback<QuestionnaireResponse>() {
            @Override
            public void onResponse(Call<QuestionnaireResponse> call, Response<QuestionnaireResponse> response) {
                QuestionnaireResponse questionnaireResponse = response.body();
                if(questionnaireResponse.getSections().size() > 0) {
                    StartQuestionNaire(questionnaireResponse);
                }else{
                    CheckNeedOauth();
                }
            }
            @Override
            public void onFailure(Call<QuestionnaireResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure getQuestionnaire : " + t.toString());
            }
        });
    }

    private void StartQuestionNaire(QuestionnaireResponse questionnaireResponse){
        Intent intent = new Intent();
        intent.setClass(this, QuestionnaireActivity.class);
        intent.putExtra(IntentConstants.QUESTION_DATA, questionnaireResponse);
        startActivityForResult(intent, IntentConstants.QUESTIONNAIRE);
    }

    private void CheckNeedOauth(){
        if(emailLoginResponse.getData().getFitbitAuthenticated()){
            GetUserInfo();
        }else {
            if(isAutoLogin){
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SystemConstant.USERNAME, "");
                editor.putString(SystemConstant.PASSWORD, "");
                editor.apply();
            }else {
                StartOauth();
            }
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
                        openDialogBox();
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
                Toast.makeText(getApplicationContext(), "Reset password fail, please check internet connection", Toast.LENGTH_SHORT).show();

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

    private void GetUserInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<UserInfoResponse> call = service.getUserInfo();
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                try {
                    UserInfoResponse userInfoResponse = response.body();
                    ImovinApplication.setUserInfoResponse(userInfoResponse);
                    Log.d(LogConstants.LogTag, "success get userinfo");

                    PushNotifications.start(getApplicationContext(), SystemConstant.PUSH_NOTIFICATION_TOKEN);
                    PushNotifications.subscribe(userInfoResponse.getEmail());

                    LaunchDashboard();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception get userinfo : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure get userinfo: " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LaunchDashboard(){
        Intent intent = new Intent();
        intent.setClass(this, DashBoardActivity.class);
        intent.putExtra(IntentConstants.REDIRECT, redirect);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void HideKeyboardAll(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(email_input.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(password_input.getWindowToken(), 0);
    }

    private void openDialogBox(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.reset_pwd_title));
        builderSingle.setMessage(getString(R.string.reset_pwd_text));
        builderSingle.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IntentConstants.CONSENT:
                if(resultCode == RESULT_OK){
                    GetQuestionNaire();
                }
                break;
            case IntentConstants.QUESTIONNAIRE:
                if(resultCode == RESULT_OK) {
                    CheckNeedOauth();
                }
                break;
        }
    }
}
