package sg.edu.nus.imovin.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import sg.edu.nus.imovin.Retrofit.Response.EmailLoginResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.FitbitConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.layout_container) LinearLayout layout_container;
    @BindView(R.id.email_input) EditText email_input;
    @BindView(R.id.password_input) EditText password_input;
    @BindView(R.id.oauthBtn) Button oauthBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HideActionBar();
        LinkUIById();
        SetFunction();

        PushNotifications.start(getApplicationContext(), "b25cdd15-cea2-4078-9394-fff4ef98a3a7");
        PushNotifications.subscribe("imovin");
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
        oauthBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_container:
                HideKeyboardAll();
                break;
            case R.id.oauthBtn:
                EmailLogin();
                break;
        }
    }

    private void EmailLogin(){
        String email = email_input.getText().toString();
        String password = password_input.getText().toString();

        email =
//                "amotivation@gmail.com";
//                "externalregulation@gmail.com";
//                "introjectedregulation@gmail.com";
//                "identifiedregulation@gmail.com";
//                "integratedregulation@gmail.com";
//                "intrinsicregulation@gmail.com";
//                "exerciselapse@gmail.com";
                "notification@gmail.com";
//                        "all@gmail.com";
//
        password = "password";

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

//                        PushNotifications.start(getApplicationContext(), "b25cdd15-cea2-4078-9394-fff4ef98a3a7");
//                        PushNotifications.subscribe(emailLoginResponse.getData().getEmail());

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
