package sg.edu.nus.imovin2.Fragments;

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
import sg.edu.nus.imovin2.Adapters.LibraryAdapter;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.ArticleData;
import sg.edu.nus.imovin2.Retrofit.Object.LibraryData;
import sg.edu.nus.imovin2.Retrofit.Response.ArticleResponse;
import sg.edu.nus.imovin2.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin2.System.BaseFragment;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.LogConstants;

import static sg.edu.nus.imovin2.HttpConnection.ConnectionURL.SERVER;

public class LibraryFragment extends BaseFragment {
    private View rootView;

    @BindView(R.id.library_list) RecyclerView library_list;

    private List<LibraryData> libraryDataList;

    public static LibraryFragment getInstance() {
        LibraryFragment libraryFragment = new LibraryFragment();
        return libraryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_library, null);

        LinkUIById();
        Init();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void Init(){
        getArticleData();
    }

    private void SetupData(List<ArticleData> articleDataList){
        libraryDataList = new ArrayList<>();
        for(ArticleData articleData : articleDataList){
            LibraryData libraryData = new LibraryData(
                    articleData.getTitle(),
                    articleData.getSubtitle(),
                    articleData.getSource(),
                    String.valueOf(articleData.getYear()),
                    articleData.getPicture_url(),
                    articleData.getUrl(),
                    articleData.getVideo_url()
            );
            libraryDataList.add(libraryData);
        }

        LibraryAdapter libraryAdapter = new LibraryAdapter(getActivity(), libraryDataList);

        library_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        library_list.setAdapter(libraryAdapter);
    }

    private void getArticleData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<ArticleResponse> call = service.getArticles();

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                try {
                    ArticleResponse articleResponse = response.body();
                    SetupData(articleResponse.get_items());

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception LibraryFragment : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure LibraryFragment : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
