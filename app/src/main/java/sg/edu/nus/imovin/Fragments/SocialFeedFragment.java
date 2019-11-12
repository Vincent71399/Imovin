package sg.edu.nus.imovin.Fragments;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Activities.SocialNewPostActivity;
import sg.edu.nus.imovin.Adapters.SocialFeedAdapter;
import sg.edu.nus.imovin.Event.ForumEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostMultiResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

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
//        socialFeedListView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), SocialContentActivity.class);
//                intent.putExtra(IntentConstants.SOCIAL_POST_DATA, socialFeedList.get(position));
//                startActivityForResult(intent, IntentConstants.SOCIAL_POST_CONTENT);
//            }
//        }));
    }

    private void Init(){
        request_page = 1;
        loadPageData(request_page);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newPostBtn:
                Intent newSocialIntent = new Intent();
                newSocialIntent.setClass(getActivity(), SocialNewPostActivity.class);
                startActivity(newSocialIntent);
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
}
