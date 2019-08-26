package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Activities.SocialContentActivity;
import sg.edu.nus.imovin.Adapters.SocialFeedAdapter;
import sg.edu.nus.imovin.Common.RecyclerItemClickListener;
import sg.edu.nus.imovin.Event.ForumEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin.Retrofit.Response.SocialImageResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostMultiResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_GET_SOCIAL_POST_IMAGE;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_SELECT_PLAN;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class SocialFeedFragment extends BaseFragment {
    private View rootView;
    RecyclerView socialFeedListView;
    List<SocialFeedData> socialFeedList;

    public static SocialFeedFragment getInstance() {
        SocialFeedFragment socialFeedFragment = new SocialFeedFragment();
        return socialFeedFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_social_feed, null);

        LinkUIById();
        SetFunction();
        ImovinApplication.setNeedRefreshSocialNeed(true);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if(ImovinApplication.isNeedRefreshSocialFeed()){
            ImovinApplication.setNeedRefreshSocialNeed(false);
            Init();
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void LinkUIById() {
        socialFeedListView = rootView.findViewById(R.id.socialFeedList);

    }

    private void SetFunction() {
        socialFeedListView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SocialContentActivity.class);
                intent.putExtra(IntentConstants.SOCIAL_POST_DATA, socialFeedList.get(position));
                startActivityForResult(intent, IntentConstants.SOCIAL_POST_CONTENT);
            }
        }));
    }

    private void Init(){
        GetSocialData();
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

    private void SetupData(){
        SocialFeedAdapter socialFeedAdapter = new SocialFeedAdapter(getActivity(), socialFeedList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        socialFeedListView.setLayoutManager(layoutManager);
        socialFeedListView.setAdapter(socialFeedAdapter);

        // add divider line between posts
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(socialFeedListView.getContext(),
                layoutManager.getOrientation());
        socialFeedListView.addItemDecoration(dividerItemDecoration);
    }

    private void GetSocialData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<SocialPostMultiResponse> call = service.getAllSocialPosts();

        call.enqueue(new Callback<SocialPostMultiResponse>() {
            @Override
            public void onResponse(Call<SocialPostMultiResponse> call, Response<SocialPostMultiResponse> response) {
                try {
                    SocialPostMultiResponse socialPostMultiResponse = response.body();
                    socialFeedList = socialPostMultiResponse.getData();
                    SetupData();

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
}
