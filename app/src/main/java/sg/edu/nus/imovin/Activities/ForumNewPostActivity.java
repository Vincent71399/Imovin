package sg.edu.nus.imovin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.ThreadData;
import sg.edu.nus.imovin.Retrofit.Request.CreateThreadRequest;
import sg.edu.nus.imovin.Retrofit.Response.ThreadResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_THREAD_WITH_ID;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class ForumNewPostActivity extends BaseActivity implements View.OnClickListener {

    private View customActionBar;

    @BindView(R.id.navigator_left) LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image) ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;

    @BindView(R.id.navigator_right) RelativeLayout navigator_right;
    @BindView(R.id.navigator_right_text) TextView navigator_right_text;

    @BindView(R.id.title_input) EditText title_input;
    @BindView(R.id.message_input) EditText message_input;

    private ThreadData threadData;
    private boolean isNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_new_post);

        SetActionBar();
        LinkUIbyId();
        SetFunction();
        SetupData();
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

    private void SetFunction(){
        navigator_left_text.setText(getString(R.string.forum));
        navigator_left_text.setVisibility(View.VISIBLE);
        navigator_left_image.setVisibility(View.VISIBLE);

        navigator_right_text.setText(getString(R.string.post));
        navigator_right_text.setVisibility(View.VISIBLE);

        navigator_left.setOnClickListener(this);
        navigator_right.setOnClickListener(this);
    }

    private void SetupData(){
        threadData = (ThreadData)getIntent().getSerializableExtra(IntentConstants.THREAD_DATA);
        if(threadData == null){
            isNewPost = true;
        }else{
            isNewPost = false;
            title_input.setText(threadData.getTitle());
            message_input.setText(threadData.getMessage());
        }
    }

    private void PostNewThread(CreateThreadRequest createThreadRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<ThreadResponse> call = service.createThread(createThreadRequest);

        call.enqueue(new Callback<ThreadResponse>() {
            @Override
            public void onResponse(Call<ThreadResponse> call, Response<ThreadResponse> response) {
                try {
                    ThreadResponse threadResponse = response.body();
                    Log.d(LogConstants.LogTag, "ForumNewPostActivity : " + threadResponse.getMessage());
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumNewPostActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ThreadResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumNewPostActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EditThread(String thread_id, CreateThreadRequest createThreadRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_THREAD_WITH_ID, thread_id);

        Call<ThreadResponse> call = service.editThread(url, createThreadRequest);

        call.enqueue(new Callback<ThreadResponse>() {
            @Override
            public void onResponse(Call<ThreadResponse> call, Response<ThreadResponse> response) {
                try {
                    ThreadResponse threadResponse = response.body();
                    Log.d(LogConstants.LogTag, "ForumNewPostActivity : " + threadResponse.getMessage());
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    resultIntent.putExtra(IntentConstants.THREAD_DATA, threadResponse.getData());
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumNewPostActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ThreadResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumNewPostActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigator_left:
                finish();
                break;
            case R.id.navigator_right:
                String title = title_input.getText().toString();
                String message = message_input.getText().toString();
                if(title.equals("") || message.equals("")){
                    Toast.makeText(this, "Title and message cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    if(isNewPost) {
                        PostNewThread(new CreateThreadRequest(title, message));
                    }else{
                        EditThread(threadData.get_id(), new CreateThreadRequest(title, message));
                    }
                }
                break;
        }
    }
}
