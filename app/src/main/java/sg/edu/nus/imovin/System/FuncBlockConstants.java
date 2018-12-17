package sg.edu.nus.imovin.System;

import android.support.v4.app.Fragment;

import sg.edu.nus.imovin.Fragments.ForumFragment;
import sg.edu.nus.imovin.Fragments.HomeFragment;
import sg.edu.nus.imovin.Fragments.LibraryFragment;

public class FuncBlockConstants {
    public static final String HOME = "Home";
    public static final String LIBRARY = "Library";
    public static final String FORUM = "Forum";

    public static Fragment getFunctionFragment(String title){
        Fragment fragment = null;
        switch (title){
            case HOME:
                fragment = HomeFragment.getInstance();
                break;
            case LIBRARY:
                fragment = LibraryFragment.getInstance();
                break;
            case FORUM:
                fragment = ForumFragment.getInstance();
                break;

        }
        return fragment;
    }
}
