package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.HttpConnection.UploadRequests;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Request.AuthFitbitRequest;
import sg.edu.nus.imovin.Retrofit.Response.AuthFitbitResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class OauthResponseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth_response);

        Uri uri = getIntent().getData();

        String code = uri.getQueryParameter("code");

        Log.d(LogConstants.LogTag, "Code : " + code);

        RequestTokenTask requestTokenTask = new RequestTokenTask();
        requestTokenTask.execute(code);
    }

    private class RequestTokenTask extends AsyncTask<String, Void, Void> {
        private String user_id = "";
        private String access_token = "";
        private String refresh_token = "";

        @Override
        protected Void doInBackground(String... inputs) {
            String code = inputs[0];
            JSONObject responseObject = UploadRequests.Oauth2Login(code);
            try {
                access_token = responseObject.getString("access_token");
                refresh_token = responseObject.getString("refresh_token");
                user_id = responseObject.getString("user_id");
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!access_token.equals("") && !refresh_token.equals("") && !user_id.equals("")){
                SendTokens(access_token, refresh_token, user_id);
            }
        }
    }

    private void SendTokens(String access_token, String refresh_token, String user_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<AuthFitbitResponse> call = service.authFitbit(new AuthFitbitRequest(access_token, refresh_token, "", user_id));

        call.enqueue(new Callback<AuthFitbitResponse>() {
            @Override
            public void onResponse(Call<AuthFitbitResponse> call, Response<AuthFitbitResponse> response) {

                Log.d(LogConstants.LogTag, "Response : " + response.toString());
                try {
                    AuthFitbitResponse authFitbitResponse = response.body();
                    Log.d(LogConstants.LogTag,  authFitbitResponse.getMessage());
                    LaunchDashboard();

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Fail to login", Toast.LENGTH_SHORT).show();
                    Log.d(LogConstants.LogTag, "Exception UploadFitbitToken : " + e.toString());
                    e.printStackTrace();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AuthFitbitResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure UploadFitbitToken : " + t.toString());
                Toast.makeText(getApplicationContext(), "Fail to login", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void LaunchDashboard(){
        Intent intent = new Intent();
        intent.setClass(this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
