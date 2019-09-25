package sg.edu.nus.imovin.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Request.CreateCommentRequest;
import sg.edu.nus.imovin.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseSimpleActivity;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class ForumNewCommentActivity extends BaseSimpleActivity implements View.OnClickListener {

    @BindView(R.id.comment_input) TextView comment_input;
    @BindView(R.id.button_post) Button button_post;
    @BindView(R.id.button_cancel) Button button_cancel;

    protected String thread_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_new_comment);

        LinkUIById();
        SetFunction();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void SetFunction(){
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        button_post.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        thread_id = getIntent().getStringExtra(IntentConstants.THREAD_ID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_post:
                if(comment_input.getText().toString().equals("")){
                    Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    PostComment(new CreateCommentRequest(thread_id, comment_input.getText().toString()));
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void PostComment(CreateCommentRequest createCommentRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<CommentResponse> call = service.createComment(createCommentRequest);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                try {
                    CommentResponse commentResponse = response.body();
                    Log.d(LogConstants.LogTag, "ForumNewCommentActivity : " + commentResponse.getMessage());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(IntentConstants.COMMENT_DATA, commentResponse.getData());
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumNewCommentActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumNewCommentActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
