package sg.edu.nus.imovin2.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.CommentData;
import sg.edu.nus.imovin2.Retrofit.Request.CreateCommentRequest;
import sg.edu.nus.imovin2.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseSimpleActivity;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.IntentConstants;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_COMMENT_WITH_ID;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_CREATE_COMMENT;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;

public class NewCommentActivity extends BaseSimpleActivity implements View.OnClickListener {

    @BindView(R.id.comment_input) EditText comment_input;
    @BindView(R.id.button_post) Button button_post;
    @BindView(R.id.button_cancel) Button button_cancel;

    protected String parent_id;
    private CommentData commentData;
    private boolean isNewComment;

    protected static String CREATE_COMMENT_STRING;
    protected static String EDIT_COMMENT_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_new_comment);

        LinkUIById();
        SetFunction();
        SetData();
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

        parent_id = getIntent().getStringExtra(IntentConstants.PARENT_ID);
        commentData = (CommentData) getIntent().getSerializableExtra(IntentConstants.COMMENT_DATA);
        if(commentData == null){
            isNewComment = true;
        }else{
            isNewComment = false;
            comment_input.setText(commentData.getMessage());
        }
    }

    protected void SetData(){
        CREATE_COMMENT_STRING = REQUEST_CREATE_COMMENT;
        EDIT_COMMENT_STRING = REQUEST_COMMENT_WITH_ID;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_post:
                if(comment_input.getText().toString().equals("")){
                    Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    if(isNewComment) {
                        PostComment(parent_id, new CreateCommentRequest(comment_input.getText().toString()));
                    }else{
                        EditComment(commentData.get_id(), new CreateCommentRequest(comment_input.getText().toString()));
                    }
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void PostComment(String thread_id, CreateCommentRequest createCommentRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,CREATE_COMMENT_STRING, thread_id);

        Call<CommentResponse> call = service.createComment(url, createCommentRequest);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                try {
                    CommentResponse commentResponse = response.body();
                    Log.d(LogConstants.LogTag, "NewCommentActivity : " + commentResponse.getMessage());

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(IntentConstants.COMMENT_DATA, commentResponse.getData());
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception NewCommentActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure NewCommentActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EditComment(String comment_id, CreateCommentRequest createCommentRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,EDIT_COMMENT_STRING, comment_id);

        Call<CommentResponse> call = service.editComment(url, createCommentRequest);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                try {
                    CommentResponse commentResponse = response.body();
                    Log.d(LogConstants.LogTag, "NewCommentActivity : " + commentResponse.getMessage());

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(IntentConstants.COMMENT_DATA, commentResponse.getData());
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception NewCommentActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure NewCommentActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
