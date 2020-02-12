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
import sg.edu.nus.imovin.R;
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

    public static MessageFragment getInstance() {
        MessageFragment messageFragment = new MessageFragment();
        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, null);

        LinkUIById();
        SetFunction();

        return rootView;
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        List<String> demoList = new ArrayList<>();
        demoList.add("Monitor");
        demoList.add("Challenge");
        demoList.add("Reminder");
        demoList.add("Rewards");

        MessageTitleAdapter messageTitleAdapter = new MessageTitleAdapter(demoList);
        message_title_selection.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        message_title_selection.setAdapter(messageTitleAdapter);

        List<MessageItemData> messageItemDataList = new ArrayList<>();
        MessageItemData messageItemData1 = new MessageItemData();
        messageItemData1.setTitle("title1");
        messageItemData1.setDate("date1");
        messageItemData1.setContent("content");
        messageItemDataList.add(messageItemData1);

        MessageAdapter messageAdapter = new MessageAdapter(messageItemDataList);
        message_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        message_list.setAdapter(messageAdapter);
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
