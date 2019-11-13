package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Event.LikeCommentEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin.Retrofit.Request.LikeSocialCommentRequest;
import sg.edu.nus.imovin.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_LIKE_SOCIAL_COMMENT;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class SocialContentActivity extends BaseActivity implements View.OnClickListener {

    private View customActionBar;
    @BindView(R.id.navigator_middle_title)
    TextView navigator_middle_title;
    @BindView(R.id.navigator_left)
    LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image)
    ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;
    @BindView(R.id.navigator_right)
    RelativeLayout navigator_right;
    @BindView(R.id.navigator_right_image) ImageView navigator_right_image;

    @BindView(R.id.username) TextView username;
    @BindView(R.id.post_since) TextView post_since;
    @BindView(R.id.feedContentText) TextView feed_content_text;
    @BindView(R.id.feedImage) ImageView feed_image;
    @BindView(R.id.comment_list) RecyclerView comment_list;

    private SocialFeedData socialFeedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_content);

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
        socialFeedData = (SocialFeedData) getIntent().getSerializableExtra(IntentConstants.SOCIAL_POST_DATA);

//        username.setText(socialFeedData.getOwnerName());
//        post_since.setText(ConvertDateString2DisplayFormat(socialFeedData.getCreatedAt()));
//        feed_content_text.setText(socialFeedData.getMessage());
//        byte[] decodedString = Base64.decode(socialFeedData.getImageString(), Base64.DEFAULT);
//        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        feed_image.setImageBitmap(decodedByte);
    }

    private void SetFunction(){
        navigator_left_text.setText(getString(R.string.social_feed));
        navigator_left_text.setVisibility(View.VISIBLE);
        navigator_left_image.setVisibility(View.VISIBLE);

        navigator_right_image.setVisibility(View.VISIBLE);
        navigator_right_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_speech_small_white));

        navigator_left.setOnClickListener(this);
        navigator_right.setOnClickListener(this);
    }

    private void Init(){
//        CommentAdapter commentAdapter = new CommentAdapter(socialFeedData.getComments());
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
                intent.setClass(this, SocialNewCommentActivity.class);
//                intent.putExtra(IntentConstants.THREAD_ID, socialFeedData.getId());
                startActivityForResult(intent, IntentConstants.FORUM_NEW_COMMENT);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LikeCommentEvent event) {
//        likeComment(event.getComment_id(), new LikeSocialCommentRequest(socialFeedData.getId(), event.getIs_like()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case IntentConstants.FORUM_NEW_COMMENT:
                if(resultCode == Activity.RESULT_OK){
//                    CommentData commentData = (CommentData) data.getSerializableExtra(IntentConstants.COMMENT_DATA);
//                    List<CommentData> commentDataList = socialFeedData.getComments();
//                    commentDataList.add(commentData);
//                    socialFeedData.setComments(commentDataList);
//                    Init();
                }
                break;
        }
    }

    public void likeComment(String comment_id, LikeSocialCommentRequest likeSocialCommentRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_LIKE_SOCIAL_COMMENT, comment_id);

        Call<CommentResponse> call = service.likeComment(url, likeSocialCommentRequest);

        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                try {
//                    CommentResponse commentResponse = response.body();
//                    CommentData resultData = commentResponse.getData();
//                    List<CommentData> commentDataList = socialFeedData.getComments();
//                    for(int i=0; i<commentDataList.size(); i++){
//                        CommentData commentData = commentDataList.get(i);
//                        if(commentData.get_id().equals(resultData.get_id())){
//                            commentDataList.set(i, resultData);
//                        }
//                    }
//                    socialFeedData.setComments(commentDataList);
//                    Init();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception LikeSocialComment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), ImovinApplication.getInstance().getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure LikeSocialComment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), ImovinApplication.getInstance().getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
