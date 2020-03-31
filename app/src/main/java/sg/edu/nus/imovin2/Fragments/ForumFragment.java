package sg.edu.nus.imovin2.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import sg.edu.nus.imovin2.Activities.ForumCommentActivity;
import sg.edu.nus.imovin2.Activities.ForumNewPostActivity;
import sg.edu.nus.imovin2.Adapters.ThreadAdapter;
import sg.edu.nus.imovin2.Event.ForumEvent;
import sg.edu.nus.imovin2.Event.LaunchThreadDetailEvent;
import sg.edu.nus.imovin2.Event.LikeThreadEvent;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.LikeData;
import sg.edu.nus.imovin2.Retrofit.Object.ThreadData;
import sg.edu.nus.imovin2.Retrofit.Request.LikeRequest;
import sg.edu.nus.imovin2.Retrofit.Response.LikeResponse;
import sg.edu.nus.imovin2.Retrofit.Response.ThreadMultiResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseFragment;
import sg.edu.nus.imovin2.System.EventConstants;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.IntentConstants;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.REQUEST_LIKE_THREAD;
import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;

public class ForumFragment extends BaseFragment implements View.OnClickListener{
    private View rootView;
    private List<ThreadData> threadDataList;

    private int request_page;
    private int total_page;

    @BindView(R.id.mainView) RelativeLayout mainView;
    @BindView(R.id.newPostBtn) Button newPostBtn;
    @BindView(R.id.thread_list) RecyclerView thread_list;

    @BindView(R.id.room_name) TextView room_name;

    public static ForumFragment getInstance() {
        ForumFragment forumFragment = new ForumFragment();
        return forumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forum, null);

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

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetupData(){
        if(threadDataList == null){
            threadDataList = new ArrayList<>();
        }else{
            threadDataList.clear();
        }
        ThreadAdapter threadAdapter = new ThreadAdapter(threadDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        thread_list.setLayoutManager(layoutManager);
        thread_list.setAdapter(threadAdapter);
    }

    private void SetFunction(){
        SetMainView(mainView);
        newPostBtn.setOnClickListener(this);

        thread_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        if(threadDataList != null) {
            threadDataList.clear();
        }else{
            threadDataList = new ArrayList<>();
        }
        request_page = 1;
        loadPageData(request_page);
    }

    private void loadPageData(int page){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<ThreadMultiResponse> call = service.getAllThreads(page);

        ShowConnectIndicator();

        call.enqueue(new Callback<ThreadMultiResponse>() {
            @Override
            public void onResponse(Call<ThreadMultiResponse> call, Response<ThreadMultiResponse> response) {
                try {
                    ThreadMultiResponse threadMultiResponse = response.body();
                    room_name.setText(threadMultiResponse.getRoom());
                    request_page = threadMultiResponse.getPage();
                    total_page = threadMultiResponse.getTotal();
                    threadDataList.addAll(threadMultiResponse.getData());
                    thread_list.getAdapter().notifyDataSetChanged();
                    HideConnectIndicator();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                    HideConnectIndicator();
                }
            }

            @Override
            public void onFailure(Call<ThreadMultiResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                HideConnectIndicator();
            }
        });
    }

    private void LikeThread(final LikeThreadEvent likeThreadEvent){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_LIKE_THREAD, likeThreadEvent.getThread_id());

        Call<LikeResponse> call = service.likeThread(url, new LikeRequest(likeThreadEvent.getIs_like()));

        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                try {
                    LikeResponse likeResponse = response.body();
                    if(likeResponse.getMessage().equals(getString(R.string.operation_success))){
                        updateLikeFlag(likeThreadEvent.getThread_id(), likeResponse.getData());
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
        for(ThreadData threadData : threadDataList){
            if(threadData.get_id().equals(id)){
                threadDataList.get(index).setLiked_by_me(likeData.getLiked_by_me());
                threadDataList.get(index).setLikes(likeData.getLikes());
            }
            index++;
        }

        thread_list.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newPostBtn:
                Intent intentForum = new Intent();
                intentForum.setClass(getActivity(), ForumNewPostActivity.class);
                startActivityForResult(intentForum, IntentConstants.FORUM_NEW_POST);
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
    public void onEvent(LikeThreadEvent event) {
        LikeThread(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(LaunchThreadDetailEvent event) {
        Log.d("forum_event", String.valueOf(event.getThread_id()));
        for(ThreadData threadData : threadDataList){
            if(event.getThread_id().equals(threadData.get_id())){
                Log.d("forum_event", String.valueOf(event.getThread_id()));

                Intent intent = new Intent();
                intent.setClass(getActivity(), ForumCommentActivity.class);
                intent.putExtra(IntentConstants.THREAD_DATA, threadData);
                startActivityForResult(intent, IntentConstants.FORUM_COMMENT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IntentConstants.FORUM_NEW_POST:
                if(resultCode == Activity.RESULT_OK){
                    Init();
                }
                break;
            case IntentConstants.FORUM_COMMENT:
                if(resultCode == Activity.RESULT_OK){
                    Init();
                }
                break;
        }
    }
}
