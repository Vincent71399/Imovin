package sg.edu.nus.imovin2.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin2.Adapters.CommentAdapter;
import sg.edu.nus.imovin2.Event.DeleteSocialCommentEvent;
import sg.edu.nus.imovin2.Event.EditSocialCommentEvent;
import sg.edu.nus.imovin2.Event.LikeSocialCommentEvent;
import sg.edu.nus.imovin2.Event.LikeSocialPostEvent;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.CommentData;
import sg.edu.nus.imovin2.Retrofit.Object.LikeData;
import sg.edu.nus.imovin2.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin2.Retrofit.Request.LikeRequest;
import sg.edu.nus.imovin2.Retrofit.Response.CommentMultiResponse;
import sg.edu.nus.imovin2.Retrofit.Response.CommonMessageResponse;
import sg.edu.nus.imovin2.Retrofit.Response.LikeResponse;
import sg.edu.nus.imovin2.Retrofit.Response.SocialPostResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseActivity;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.IntentConstants;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.Common.CommonFunc.ConvertDateString2DisplayFormat;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_GET_SOCIAL_COMMENT;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_GET_SOCIAL_POST_IMAGE;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_LIKE_SOCIAL_COMMENT;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_LIKE_SOCIAL_POST;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_SOCIAL_COMMENT_WITH_ID;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_SOCIAL_POST_WITH_ID;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;

public class SocialContentActivity extends BaseActivity implements View.OnClickListener {

    private View customActionBar;

    @BindView(R.id.navigator_left) LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image) ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;
    @BindView(R.id.navigator_right) RelativeLayout navigator_right;
    @BindView(R.id.navigator_right_image) ImageView navigator_right_image;

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.body_text) TextView body_text;
    @BindView(R.id.owner_text) TextView owner_text;
    @BindView(R.id.post_time) TextView post_time;
    @BindView(R.id.image_container) LinearLayout image_container;
    @BindView(R.id.social_image) ImageView social_image;
    @BindView(R.id.thumbs_up_container) LinearLayout thumbs_up_container;
    @BindView(R.id.thumbs_up_image) ImageView thumbs_up_image;
    @BindView(R.id.likes_text) TextView likes_text;
    @BindView(R.id.edit_container) LinearLayout edit_container;
    @BindView(R.id.delete_container) LinearLayout delete_container;
    @BindView(R.id.comment_count) TextView comment_count;
    @BindView(R.id.comment_list) RecyclerView comment_list;

    private SocialFeedData socialFeedData;
    private List<CommentData> commentDataList;

    private boolean hasEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_content);

        SetActionBar();
        LinkUIById();
        SetupData();
        SetFunction();
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

    @Override
    public void onBackPressed() {
        if(hasEdit){
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
        }
        super.onBackPressed();
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

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void SetupData(){
        hasEdit = false;

        socialFeedData = (SocialFeedData) getIntent().getSerializableExtra(IntentConstants.SOCIAL_POST_DATA);

        getSocialPost(socialFeedData.get_id());

        commentDataList = new ArrayList<>();

        CommentAdapter commentAdapter = new CommentAdapter(commentDataList, IntentConstants.SOCIAL_POST_COMMENT);
        comment_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        comment_list.setAdapter(commentAdapter);

        thumbs_up_container.setOnClickListener(this);
        if(socialFeedData.getUser_id().equals(ImovinApplication.getUserInfoResponse().get_id())) {
            edit_container.setVisibility(View.VISIBLE);
            delete_container.setVisibility(View.VISIBLE);
            edit_container.setOnClickListener(this);
            delete_container.setOnClickListener(this);
        }else{
            edit_container.setVisibility(View.GONE);
            delete_container.setVisibility(View.GONE);
        }
    }

    private void SetPostData(){
        body_text.setText(socialFeedData.getMessage());
        owner_text.setText(socialFeedData.getUser_name());
        post_time.setText(ConvertDateString2DisplayFormat(socialFeedData.getUpdated_at()));

        boolean hasLiked = socialFeedData.getLiked_by_me();
        if(hasLiked){
            thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_colored_small));
        }else {
            thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_small));
        }
        likes_text.setText(String.valueOf(socialFeedData.getLikes()));

        comment_count.setText(getString(R.string.comments_topics) + "(" + socialFeedData.getComments() + ")");

        if(socialFeedData.getImage() != null){
            image_container.setVisibility(View.VISIBLE);
            String imageUrl = SERVER + String.format(
                    Locale.ENGLISH,REQUEST_GET_SOCIAL_POST_IMAGE, socialFeedData.get_id());
            ImageLoader.getInstance().displayImage(imageUrl, social_image);
        }else{
            image_container.setVisibility(View.GONE);
        }
    }

    private void SetFunction(){
        SetMainView(mainView);

        navigator_left_text.setText(getString(R.string.social_feed));
        navigator_left_text.setVisibility(View.VISIBLE);
        navigator_left_image.setVisibility(View.VISIBLE);

        navigator_right_image.setVisibility(View.VISIBLE);
        navigator_right_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_speech_small_white));

        navigator_left.setOnClickListener(this);
        navigator_right.setOnClickListener(this);
    }

    private void Init(){
        if(commentDataList == null){
            commentDataList = new ArrayList<>();
        }else{
            commentDataList.clear();
        }

        loadCommentData();
    }

    private void updateLikeFlag(String id, LikeData likeData){
        int index = 0;
        for(CommentData commentData : commentDataList){
            if(commentData.get_id().equals(id)){
                commentDataList.get(index).setLiked_by_me(likeData.getLiked_by_me());
                commentDataList.get(index).setLikes(likeData.getLikes());
            }
            index++;
        }

        comment_list.getAdapter().notifyDataSetChanged();
    }

    private void loadCommentData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_GET_SOCIAL_COMMENT, socialFeedData.get_id());

        Call<CommentMultiResponse> call = service.getAllComment(url);

        ShowConnectIndicator();

        call.enqueue(new Callback<CommentMultiResponse>() {
            @Override
            public void onResponse(Call<CommentMultiResponse> call, Response<CommentMultiResponse> response) {
                try {
                    CommentMultiResponse commentMultiResponse = response.body();

                    commentDataList.addAll(commentMultiResponse.getData());
                    comment_list.getAdapter().notifyDataSetChanged();
                    HideConnectIndicator();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                    HideConnectIndicator();
                }
            }

            @Override
            public void onFailure(Call<CommentMultiResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                HideConnectIndicator();
            }
        });
    }

    private void getSocialPost(String post_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH, REQUEST_SOCIAL_POST_WITH_ID, post_id);

        Call<SocialPostResponse> call = service.getSocialPost(url);

        call.enqueue(new Callback<SocialPostResponse>() {
            @Override
            public void onResponse(Call<SocialPostResponse> call, Response<SocialPostResponse> response) {
                try {
                    SocialPostResponse socialPostResponse = response.body();
                    socialFeedData = socialPostResponse.getData();
                    hasEdit = true;
                    SetPostData();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumFragment Like: " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                    SetPostData();
                }
            }

            @Override
            public void onFailure(Call<SocialPostResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumFragment Like: " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                SetPostData();
            }
        });
    }

    private void LikeSocialPost(final LikeSocialPostEvent likeSocialPostEvent){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH, REQUEST_LIKE_SOCIAL_POST, likeSocialPostEvent.getPost_id());

        Call<LikeResponse> call = service.likeSocialPost(url, new LikeRequest(likeSocialPostEvent.getIs_like()));

        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                try {
                    LikeResponse likeResponse = response.body();
                    if(likeResponse.getMessage().equals(getString(R.string.operation_success))){
                        socialFeedData.setLikes(likeResponse.getData().getLikes());
                        socialFeedData.setLiked_by_me(likeResponse.getData().getLiked_by_me());
                        if(socialFeedData.getLiked_by_me()) {
                            thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_colored_small));
                        }else{
                            thumbs_up_image.setImageDrawable(ContextCompat.getDrawable(ImovinApplication.getInstance(), R.drawable.icon_thumb_small));
                        }
                        likes_text.setText(String.valueOf(socialFeedData.getLikes()));
                        hasEdit = true;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumFragment Like: " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumFragment Like: " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSocialPost(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_SOCIAL_POST_WITH_ID, socialFeedData.get_id());

        Call<CommonMessageResponse> call = service.deleteSocialPost(url);

        ShowConnectIndicator();

        call.enqueue(new Callback<CommonMessageResponse>() {
            @Override
            public void onResponse(Call<CommonMessageResponse> call, Response<CommonMessageResponse> response) {
                try {
                    CommonMessageResponse commonMessageResponse = response.body();
                    if(commonMessageResponse.getMessage().equals(getString(R.string.operation_success))){
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                    HideConnectIndicator();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                    HideConnectIndicator();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                HideConnectIndicator();
            }
        });
    }

    public void likeComment(final String comment_id, LikeRequest likeRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_LIKE_SOCIAL_COMMENT, comment_id);

        Call<LikeResponse> call = service.likeComment(url, likeRequest);

        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                try {
                    LikeResponse likeResponse = response.body();
                    if(likeResponse.getMessage().equals(getString(R.string.operation_success))){
                        updateLikeFlag(comment_id, likeResponse.getData());
                    }

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

    private void deleteComment(final String comment_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH, REQUEST_SOCIAL_COMMENT_WITH_ID, comment_id);

        Call<CommonMessageResponse> call = service.deleteComment(url);

        ShowConnectIndicator();

        call.enqueue(new Callback<CommonMessageResponse>() {
            @Override
            public void onResponse(Call<CommonMessageResponse> call, Response<CommonMessageResponse> response) {
                try {
                    CommonMessageResponse commonMessageResponse = response.body();
                    if(commonMessageResponse.getMessage().equals(getString(R.string.operation_success))){
                        int index = 0;
                        for(CommentData commentData : commentDataList){
                            if(commentData.get_id().equals(comment_id)){
                                break;
                            }
                            index ++;
                        }
                        commentDataList.remove(index);
                        comment_list.getAdapter().notifyDataSetChanged();
                        socialFeedData.setComments(commentDataList.size());
                        comment_count.setText(getString(R.string.comments_topics) + "(" + socialFeedData.getComments() + ")");
                        hasEdit = true;
                    }
                    HideConnectIndicator();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                    HideConnectIndicator();
                }
            }

            @Override
            public void onFailure(Call<CommonMessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                HideConnectIndicator();
            }
        });
    }

    private void EditSocialCommentEvent(String comment_id){
        for(CommentData commentData : commentDataList){
            if(commentData.get_id().equals(comment_id)){
                Intent intent = new Intent();
                intent.setClass(this, NewSocialCommentActivity.class);
                intent.putExtra(IntentConstants.PARENT_ID, socialFeedData.get_id());
                intent.putExtra(IntentConstants.COMMENT_DATA, commentData);
                startActivityForResult(intent, IntentConstants.SOCIAL_EDIT_COMMENT);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigator_left:
                if(hasEdit){
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
                break;
            case R.id.navigator_right:
                Intent intent = new Intent();
                intent.setClass(this, NewSocialCommentActivity.class);
                intent.putExtra(IntentConstants.PARENT_ID, socialFeedData.get_id());
                startActivityForResult(intent, IntentConstants.SOCIAL_NEW_COMMENT);
                break;
            case R.id.thumbs_up_container:
                LikeSocialPost(new LikeSocialPostEvent(socialFeedData.get_id(), !socialFeedData.getLiked_by_me()));
                break;
            case R.id.edit_container:
                Intent intentForum = new Intent();
                intentForum.setClass(this, SocialNewPostActivity.class);
                intentForum.putExtra(IntentConstants.SOCIAL_POST_DATA, socialFeedData);
                startActivityForResult(intentForum, IntentConstants.SOCIAL_EDIT_POST);
                break;
            case R.id.delete_container:
                openThreadDeleteDialogBox();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LikeSocialCommentEvent event) {
        likeComment(event.getComment_id(), new LikeRequest(event.getIs_like()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EditSocialCommentEvent event) {
        EditSocialCommentEvent(event.getComment_id());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DeleteSocialCommentEvent event) {
        openCommentDeleteDialogBox(event.getComment_id());
    }

    private void openThreadDeleteDialogBox(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.thread_delete_title));
        builderSingle.setMessage(getString(R.string.thread_delete_confirmation));
        builderSingle.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteSocialPost();
            }
        });
        builderSingle.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void openCommentDeleteDialogBox(final String comment_id){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(getString(R.string.comment_delete_title));
        builderSingle.setMessage(getString(R.string.comment_delete_confirmation));
        builderSingle.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteComment(comment_id);
            }
        });
        builderSingle.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case IntentConstants.SOCIAL_EDIT_POST:
                if(resultCode == Activity.RESULT_OK){
                    SocialFeedData socialFeedData = (SocialFeedData)data.getSerializableExtra(IntentConstants.SOCIAL_POST_DATA);
                    this.socialFeedData = socialFeedData;
                    body_text.setText(socialFeedData.getMessage());
                    post_time.setText(ConvertDateString2DisplayFormat(socialFeedData.getUpdated_at()));
                    if(socialFeedData.getImage() != null){
                        image_container.setVisibility(View.VISIBLE);
                        String imageUrl = SERVER + String.format(
                                Locale.ENGLISH,REQUEST_GET_SOCIAL_POST_IMAGE, socialFeedData.get_id());
                        ImageLoader.getInstance().displayImage(imageUrl, social_image);
                    }else{
                        image_container.setVisibility(View.GONE);
                    }
                    hasEdit = true;
                }
                break;
            case IntentConstants.SOCIAL_NEW_COMMENT:
                if(resultCode == Activity.RESULT_OK){
                    CommentData commentDataReturn = (CommentData) data.getSerializableExtra(IntentConstants.COMMENT_DATA);
                    commentDataList.add(0, commentDataReturn);

                    comment_list.getAdapter().notifyDataSetChanged();
                    socialFeedData.setComments(commentDataList.size());
                    comment_count.setText(getString(R.string.comments_topics) + "(" + socialFeedData.getComments() + ")");
                    hasEdit = true;
                }
                break;
            case IntentConstants.SOCIAL_EDIT_COMMENT:
                if(resultCode == Activity.RESULT_OK){
                    int index = 0;
                    CommentData commentDataReturn = (CommentData) data.getSerializableExtra(IntentConstants.COMMENT_DATA);
                    for(CommentData commentData : commentDataList){
                        if(commentData.get_id().equals(commentDataReturn.get_id())){
                            commentDataList.set(index, commentDataReturn);
                        }
                        index++;
                    }
                    comment_list.getAdapter().notifyDataSetChanged();
                }
                break;
        }
    }

}
