package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.System.BaseFragment;

public class MessageFragment extends BaseFragment {
    private View rootView;

    public static MessageFragment getInstance() {
        MessageFragment messageFragment = new MessageFragment();
        return messageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_message, null);

        return rootView;
    }
}
