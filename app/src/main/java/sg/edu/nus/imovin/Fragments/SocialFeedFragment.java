package sg.edu.nus.imovin.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.nus.imovin.R;

public class SocialFeedFragment extends Fragment {
    private View rootView;

    public static SocialFeedFragment getInstance() {
        SocialFeedFragment socialFeedFragment = new SocialFeedFragment();
        return socialFeedFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_social_feed, null);

        return rootView;
    }
}
