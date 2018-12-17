package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Activities.ForumCommentActivity;
import sg.edu.nus.imovin.Adapters.ThreadAdapter;
import sg.edu.nus.imovin.Common.RecyclerItemClickListener;
import sg.edu.nus.imovin.Event.ForumEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.ThreadData;
import sg.edu.nus.imovin.Retrofit.Response.ThreadMultiResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class ForumFragment extends Fragment {
    private View rootView;
    private List<ThreadData> threadDataList;

    @BindView(R.id.thread_list) RecyclerView thread_list;

    public static ForumFragment getInstance() {
        ForumFragment forumFragment = new ForumFragment();
        return forumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forum, null);

        LinkUIById();
        SetFunction();
        Init();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if(ImovinApplication.isNeedRefreshForum()){
            ImovinApplication.setNeedRefreshForum(false);
            Init();
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        thread_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ForumCommentActivity.class);
                intent.putExtra(IntentConstants.THREAD_DATA, threadDataList.get(position));
                startActivityForResult(intent, IntentConstants.FORUM_COMMENT);
            }
        }));
    }

    private void Init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<ThreadMultiResponse> call = service.getAllThreads();

        call.enqueue(new Callback<ThreadMultiResponse>() {
            @Override
            public void onResponse(Call<ThreadMultiResponse> call, Response<ThreadMultiResponse> response) {
                try {
                    ThreadMultiResponse threadMultiResponse = response.body();
                    threadDataList = threadMultiResponse.getData();
                    SetupData();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ThreadMultiResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
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
        ThreadAdapter threadAdapter = new ThreadAdapter(threadDataList);

        thread_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        thread_list.setAdapter(threadAdapter);
    }

}
