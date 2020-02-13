package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Adapters.MessageAdapter;
import sg.edu.nus.imovin.Adapters.MessageTitleAdapter;
import sg.edu.nus.imovin.Common.RecyclerItemClickListener;
import sg.edu.nus.imovin.Common.SpanningLinearLayoutManager;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.MessageData;
import sg.edu.nus.imovin.Retrofit.Object.MessageItemData;
import sg.edu.nus.imovin.Retrofit.Response.MessageResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseFragment;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class MessageFragment extends BaseFragment {
    private View rootView;

    @BindView(R.id.message_title_selection) RecyclerView message_title_selection;
    @BindView(R.id.message_list) RecyclerView message_list;

    private MessageTitleAdapter messageTitleAdapter;
    private MessageAdapter messageAdapter;

    private List<String> messageTitleList;
    private List<MessageItemData> messageItemDataList;
    private List<List<MessageItemData>> cachMessageItemDataList;

    public static MessageFragment getInstance() {
        MessageFragment messageFragment = new MessageFragment();
        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, null);

        LinkUIById();
        SetFunction();
        Init();

        return rootView;
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        if(messageTitleList == null) {
            messageTitleList = new ArrayList<>();
        }else{
            messageTitleList.clear();
        }

        if(messageItemDataList == null){
            messageItemDataList = new ArrayList<>();
        }else{
            messageItemDataList.clear();
        }

        if(cachMessageItemDataList == null){
            cachMessageItemDataList = new ArrayList<>();
        }else{
            cachMessageItemDataList.clear();
        }

        messageTitleAdapter = new MessageTitleAdapter(messageTitleList);
        message_title_selection.setLayoutManager(new SpanningLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        message_title_selection.setAdapter(messageTitleAdapter);

        message_title_selection.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                messageTitleAdapter.setSelection(position);
                messageTitleAdapter.notifyDataSetChanged();
                SetupMessageList(position);
            }
        }));

        messageAdapter = new MessageAdapter(messageItemDataList);
        message_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        message_list.setAdapter(messageAdapter);
    }

    private void Init(){
        getMessageData();
    }

    private void SetupData(MessageResponse messageResponse){
        MessageData messageData = messageResponse.getData();
        if(messageData.getMonitor() != null){
            messageTitleList.add(getString(R.string.message_monitor));
            cachMessageItemDataList.add(messageData.getMonitor());
        }
        if(messageData.getReminder() != null){
            messageTitleList.add(getString(R.string.message_reminder));
            cachMessageItemDataList.add(messageData.getReminder());
        }
        if(messageData.getRewards() != null){
            messageTitleList.add(getString(R.string.message_rewards));
            cachMessageItemDataList.add(messageData.getRewards());
        }
        if(messageData.getChallenge() != null){
            messageTitleList.add(getString(R.string.message_challenge));
            cachMessageItemDataList.add(messageData.getChallenge());
        }
        messageTitleAdapter.notifyDataSetChanged();
    }

    private void SetupMessageList(int position){
        messageItemDataList.clear();
        if(cachMessageItemDataList.size() > position) {
            messageItemDataList.addAll(cachMessageItemDataList.get(position));
        }
        messageAdapter.notifyDataSetChanged();
    }

    private void getMessageData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<MessageResponse> call = service.getMessage();

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                try {
                    MessageResponse messageResponse = response.body();
                    SetupData(messageResponse);

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception MessageFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure MessageFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
