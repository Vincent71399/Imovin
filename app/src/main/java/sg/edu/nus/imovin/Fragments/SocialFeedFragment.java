package sg.edu.nus.imovin.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import sg.edu.nus.imovin.Activities.SocialContentActivity;
import sg.edu.nus.imovin.Activities.SocialNewPostActivity;
import sg.edu.nus.imovin.Adapters.SocialFeedAdapter;
import sg.edu.nus.imovin.Event.ForumEvent;
import sg.edu.nus.imovin.Event.LaunchSocialPostDetailEvent;
import sg.edu.nus.imovin.Event.LikeSocialPostEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.LikeData;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin.Retrofit.Request.LikeRequest;
import sg.edu.nus.imovin.Retrofit.Response.LikeResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostMultiResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_LIKE_SOCIAL_POST;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class SocialFeedFragment extends BaseFragment implements View.OnClickListener {
    private View rootView;
    List<SocialFeedData> socialFeedList;

    private int request_page;
    private int total_page;

    @BindView(R.id.newPostBtn) Button newPostBtn;
    @BindView(R.id.socialFeedList) RecyclerView socialFeedListView;

    public static SocialFeedFragment getInstance() {
        SocialFeedFragment socialFeedFragment = new SocialFeedFragment();
        return socialFeedFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_social_feed, null);

        LinkUIById();
        SetupData();
        SetFunction();
        Init();

        return rootView;
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

    private void LinkUIById() {
        ButterKnife.bind(this, rootView);
    }

    private void SetupData(){
        if(socialFeedList == null){
            socialFeedList = new ArrayList<>();
        }else{
            socialFeedList.clear();
        }
        SocialFeedAdapter socialFeedAdapter = new SocialFeedAdapter(socialFeedList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        socialFeedListView.setLayoutManager(layoutManager);
        socialFeedListView.setAdapter(socialFeedAdapter);

        // add divider line between posts
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(socialFeedListView.getContext(),
                layoutManager.getOrientation());
        socialFeedListView.addItemDecoration(dividerItemDecoration);
    }

    private void SetFunction() {
        newPostBtn.setOnClickListener(this);

        socialFeedListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("Scroll", "Scroll to Bottom");
                    if(request_page < total_page){
                        request_page ++;
                        loadPageData(request_page);
                    }
                }
            }
        });

    }

    private void Init(){
        socialFeedList.clear();
        request_page = 1;
        loadPageData(request_page);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newPostBtn:
                Intent newSocialIntent = new Intent();
                newSocialIntent.setClass(getActivity(), SocialNewPostActivity.class);
                startActivityForResult(newSocialIntent, IntentConstants.SOCIAL_NEW_POST);
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ForumEvent event) {
        Toast.makeText(ImovinApplication.getInstance(), event.getMessage(), Toast.LENGTH_SHORT).show();
        switch (event.getMessage()){
            case EventConstants.REFRESH:
                Init();
                break;
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LikeSocialPostEvent event) {
        LikeSocialPost(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LaunchSocialPostDetailEvent event) {
        for(SocialFeedData socialFeedData : socialFeedList){
            if(event.getPost_id().equals(socialFeedData.get_id())){
                Intent intent = new Intent();
                intent.setClass(getActivity(), SocialContentActivity.class);
                intent.putExtra(IntentConstants.SOCIAL_POST_DATA, socialFeedData);
                startActivityForResult(intent, IntentConstants.SOCIAL_POST_CONTENT);
            }
        }
    }

    private void loadPageData(int page){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<SocialPostMultiResponse> call = service.getAllSocialPosts(page);

        call.enqueue(new Callback<SocialPostMultiResponse>() {
            @Override
            public void onResponse(Call<SocialPostMultiResponse> call, Response<SocialPostMultiResponse> response) {
                try {
                    SocialPostMultiResponse socialPostMultiResponse = response.body();
                    request_page = socialPostMultiResponse.getPage();
                    total_page = socialPostMultiResponse.getTotal();
                    socialFeedList.addAll(socialPostMultiResponse.getData());
                    socialFeedListView.getAdapter().notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception SocialFeedFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialPostMultiResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure SocialFeedFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
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
                        updateLikeFlag(likeSocialPostEvent.getPost_id(), likeResponse.getData());
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

    private void updateLikeFlag(String id, LikeData likeData){
        int index = 0;
        for(SocialFeedData socialFeedData : socialFeedList){
            if(socialFeedData.get_id().equals(id)){
                socialFeedList.get(index).setLiked_by_me(likeData.getLiked_by_me());
                socialFeedList.get(index).setLikes(likeData.getLikes());
            }
            index++;
        }

        socialFeedListView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IntentConstants.SOCIAL_NEW_POST:
                if(resultCode == Activity.RESULT_OK){
                    Init();
                }
                break;
        }
    }
}
