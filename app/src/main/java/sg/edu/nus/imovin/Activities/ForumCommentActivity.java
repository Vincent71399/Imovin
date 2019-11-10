package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Adapters.CommentAdapter;
import sg.edu.nus.imovin.Event.LikeCommentEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.CommentData;
import sg.edu.nus.imovin.Retrofit.Object.ThreadData;
import sg.edu.nus.imovin.Retrofit.Request.LikeRequest;
import sg.edu.nus.imovin.Retrofit.Response.LikeResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_LIKE_COMMENT;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class ForumCommentActivity extends BaseActivity implements View.OnClickListener {

    private View customActionBar;
    @BindView(R.id.navigator_middle_title) TextView navigator_middle_title;
    @BindView(R.id.navigator_left) LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image) ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;
    @BindView(R.id.navigator_right) RelativeLayout navigator_right;
    @BindView(R.id.navigator_right_image) ImageView navigator_right_image;

    @BindView(R.id.title_text) TextView title_text;
    @BindView(R.id.body_text) TextView body_text;
    @BindView(R.id.owner_text) TextView owner_text;
    @BindView(R.id.thumbs_up_container) LinearLayout thumbs_up_container;
    @BindView(R.id.thumbs_up_image) ImageView thumbs_up_image;
    @BindView(R.id.likes_text) TextView likes_text;
    @BindView(R.id.edit_container) LinearLayout edit_container;
    @BindView(R.id.delete_container) LinearLayout delete_container;

    @BindView(R.id.comment_count) TextView comment_count;
    @BindView(R.id.comment_list) RecyclerView comment_list;

    private ThreadData threadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_comment);

        SetActionBar();
        LinkUIById();
        SetFunction();
        SetupData();
        Init();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void SetActionBar(){
        ActionBar actionBar = getSupportActionBar();
        customActionBar = getLayoutInflater().inflate(R.layout.main_navigator, null);

        if(actionBar != null){
            actionBar.show();
            actionBar.setCustomView(customActionBar);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Toolbar parent =(Toolbar) customActionBar.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void SetupData(){
        threadData = (ThreadData) getIntent().getSerializableExtra(IntentConstants.THREAD_DATA);
        title_text.setText(threadData.getTitle());
        body_text.setText(threadData.getMessage());
        owner_text.setText(threadData.getUser_name());

        if(threadData.getLiked_by_me()) {
            thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_colored_small));
        }else{
            thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_small));
        }
        likes_text.setText(String.valueOf(threadData.getLikes()));

        comment_count.setText(getString(R.string.comments_topics) + "(" + threadData.getComments() + ")");
    }

    private void SetFunction(){
        navigator_middle_title.setText("");

        navigator_left_text.setText(getString(R.string.forum));
        navigator_left_text.setVisibility(View.VISIBLE);
        navigator_left_image.setVisibility(View.VISIBLE);

        navigator_right_image.setVisibility(View.VISIBLE);
        navigator_right_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_speech_small_white));

        navigator_left.setOnClickListener(this);
        navigator_right.setOnClickListener(this);
    }

    private void Init(){
        //todo
//        CommentAdapter commentAdapter = new CommentAdapter(threadData.getComments());
//        comment_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        comment_list.setAdapter(commentAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigator_left:
                finish();
                break;
            case R.id.navigator_right:
                Intent intent = new Intent();
                intent.setClass(this, ForumNewCommentActivity.class);
                intent.putExtra(IntentConstants.THREAD_ID, threadData.get_id());
                startActivityForResult(intent, IntentConstants.FORUM_NEW_COMMENT);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LikeCommentEvent event) {
        likeComment(event.getComment_id(), new LikeRequest(event.getIs_like()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case IntentConstants.FORUM_NEW_COMMENT:
                if(resultCode == Activity.RESULT_OK){
                    //todo
//                    CommentData commentData = (CommentData) data.getSerializableExtra(IntentConstants.COMMENT_DATA);
//                    List<CommentData> commentDataList = threadData.getComments();
//                    commentDataList.add(commentData);
//                    threadData.setComments(commentDataList);
//                    Init();
//                    ImovinApplication.setNeedRefreshForum(true);
                }
                break;
        }
    }

    public void likeComment(String comment_id, LikeRequest likeRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_LIKE_COMMENT, comment_id);

        Call<LikeResponse> call = service.likeComment(url, likeRequest);

        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                try {
                    LikeResponse likeResponse = response.body();
                    //todo
//                    CommentData resultData = commentResponse.getData();
//                    List<CommentData> commentDataList = threadData.getComments();
//                    for(int i=0; i<commentDataList.size(); i++){
//                        CommentData commentData = commentDataList.get(i);
//                        if(commentData.get_id().equals(resultData.get_id())){
//                            commentDataList.set(i, resultData);
//                        }
//                    }
//                    threadData.setComments(commentDataList);
//                    Init();
//                    ImovinApplication.setNeedRefreshForum(true);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumComment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), ImovinApplication.getInstance().getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumComment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), ImovinApplication.getInstance().getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
