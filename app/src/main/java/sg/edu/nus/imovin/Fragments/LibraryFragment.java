package sg.edu.nus.imovin.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Adapters.LibraryAdapter;
import sg.edu.nus.imovin.Common.RecyclerItemClickListener;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.LibraryData;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LibraryURLConstants;

public class LibraryFragment extends Fragment {
    private View rootView;

    @BindView(R.id.library_list) RecyclerView library_list;

    public static LibraryFragment getInstance() {
        LibraryFragment libraryFragment = new LibraryFragment();
        return libraryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_library, null);

        LinkUIById();
        SetFunction();

        return rootView;
    }

    private void LinkUIById(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        LibraryAdapter libraryAdapter = new LibraryAdapter(LibraryURLConstants.getLibraryList());

        library_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        library_list.setAdapter(libraryAdapter);

        library_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LibraryData libraryData = LibraryURLConstants.getLibraryList().get(position);
                OpenUrlInBrowser(libraryData.getLink_url());
            }
        }));
    }

    private void OpenUrlInBrowser(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}
