package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.nus.imovin.R;

public class MonitorFragment extends Fragment {
    private View rootView;

    public static MonitorFragment getInstance() {
        MonitorFragment monitorFragment = new MonitorFragment();
        return monitorFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_monitor, null);

        return rootView;
    }

}
