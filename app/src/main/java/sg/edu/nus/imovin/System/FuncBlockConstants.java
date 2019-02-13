package sg.edu.nus.imovin.System;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.imovin.Fragments.ChallengeFragment;
import sg.edu.nus.imovin.Fragments.ForumFragment;
import sg.edu.nus.imovin.Fragments.GoalFragment;
import sg.edu.nus.imovin.Fragments.HomeFragment;
import sg.edu.nus.imovin.Fragments.LibraryFragment;
import sg.edu.nus.imovin.Fragments.MonitorFragment;
import sg.edu.nus.imovin.Fragments.SocialFeedFragment;
import sg.edu.nus.imovin.R;

public class FuncBlockConstants {
    public static final String HOME = "Home";
    public static final String LIBRARY = "Library";
    public static final String FORUM = "Forum";
    public static final String GOAL = "Goal";
    public static final String MONITOR = "Monitor";
    public static final String SOCIAL = "Social";
    public static final String CHALLENGE = "Challenge";

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
            case GOAL:
                fragment = GoalFragment.getInstance();
                break;
            case MONITOR:
                fragment = MonitorFragment.getInstance();
                break;
            case SOCIAL:
                fragment = SocialFeedFragment.getInstance();
                break;
            case CHALLENGE:
                fragment = ChallengeFragment.getInstance();
                break;
        }
        return fragment;
    }

    private static int getUnselectIcon_by_title(String title){
        int icon = 0;
        switch (title){
            case HOME:
                icon = R.drawable.icon_home_unselect;
                break;
            case LIBRARY:
                icon = R.drawable.icon_library_unselect;
                break;
            case FORUM:
                icon = R.drawable.icon_forum_unselect;
                break;
            case GOAL:
                icon = R.drawable.icon_goal_unselect;
                break;
            case MONITOR:
                icon = R.drawable.icon_monior_unselect;
                break;
            case SOCIAL:
                icon = R.drawable.icon_social_feed_unselect;
                break;
            case CHALLENGE:
                icon = R.drawable.icon_challenge_unselect;
                break;
        }
        return icon;
    }

    private static int getSelectIcon_by_title(String title){
        int icon = 0;
        switch (title){
            case HOME:
                icon = R.drawable.icon_home_select;
                break;
            case LIBRARY:
                icon = R.drawable.icon_library_select;
                break;
            case FORUM:
                icon = R.drawable.icon_forum_select;
                break;
            case GOAL:
                icon = R.drawable.icon_goal_select;
                break;
            case MONITOR:
                icon = R.drawable.icon_monior_select;
                break;
            case SOCIAL:
                icon = R.drawable.icon_social_feed_select;
                break;
            case CHALLENGE:
                icon = R.drawable.icon_challenge_select;
                break;
        }
        return icon;
    }

    public static String[] getFunctionBlockTitles_by_profile(int profile){
        List<String> mTitleList = new ArrayList<>();
        switch (profile){
            case 0:
                mTitleList.add(HOME);
                mTitleList.add(CHALLENGE);
                break;
            case 1:
                mTitleList.add(HOME);
                mTitleList.add(SOCIAL);
                break;
            case 2:
                mTitleList.add(HOME);
                mTitleList.add(LIBRARY);
                break;
            case 3:
                mTitleList.add(HOME);
                mTitleList.add(GOAL);
                mTitleList.add(LIBRARY);
                break;
            case 4:
                mTitleList.add(HOME);
                mTitleList.add(GOAL);
                mTitleList.add(MONITOR);
                break;
            case 5:
                mTitleList.add(HOME);
                mTitleList.add(GOAL);
                mTitleList.add(MONITOR);
                break;
            case 6:
                mTitleList.add(HOME);
                mTitleList.add(LIBRARY);
                mTitleList.add(FORUM);
                break;
            default:
                mTitleList.add(HOME);
                mTitleList.add(LIBRARY);
                mTitleList.add(FORUM);
                mTitleList.add(GOAL);
                mTitleList.add(MONITOR);
                mTitleList.add(SOCIAL);
                mTitleList.add(CHALLENGE);
                break;

        }

        String[] mTitles = new String[mTitleList.size()];
        mTitles = mTitleList.toArray(mTitles);
        return mTitles;
    }

    public static Integer[] getFunctionBlockUnselectIcons_by_profile(int profile){
        String[] mTitles = getFunctionBlockTitles_by_profile(profile);
        List<Integer> iconList = new ArrayList<>();
        for(String title : mTitles){
            iconList.add(getUnselectIcon_by_title(title));
        }

        Integer[] icons = new Integer[iconList.size()];
        icons = iconList.toArray(icons);
        return icons;
    }

    public static Integer[] getFunctionBlockSelectIcons_by_profile(int profile){
        String[] mTitles = getFunctionBlockTitles_by_profile(profile);
        List<Integer> iconList = new ArrayList<>();
        for(String title : mTitles){
            iconList.add(getSelectIcon_by_title(title));
        }

        Integer[] icons = new Integer[iconList.size()];
        icons = iconList.toArray(icons);
        return icons;
    }

}
