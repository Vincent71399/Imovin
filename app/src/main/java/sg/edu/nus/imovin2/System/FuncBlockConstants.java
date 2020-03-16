package sg.edu.nus.imovin2.System;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.imovin2.Fragments.ChallengeFragment;
import sg.edu.nus.imovin2.Fragments.ForumFragment;
import sg.edu.nus.imovin2.Fragments.GoalFragment;
import sg.edu.nus.imovin2.Fragments.HomeFragment;
import sg.edu.nus.imovin2.Fragments.LibraryFragment;
import sg.edu.nus.imovin2.Fragments.MessageFragment;
import sg.edu.nus.imovin2.Fragments.MonitorFragment;
import sg.edu.nus.imovin2.Fragments.RewardsFragment;
import sg.edu.nus.imovin2.Fragments.SocialFeedFragment;
import sg.edu.nus.imovin2.R;

public class FuncBlockConstants {
    public static final String HOME = "Home";
    public static final String LIBRARY = "Library";
    public static final String FORUM = "Forum";
    public static final String GOAL = "Goal";
    public static final String MONITOR = "Monitor";
    public static final String COMMUNITY = "Community";
    public static final String CHALLENGE = "Challenge";
    public static final String MORE = "More";

    public static final String MESSAGE = "Message";
    public static final String REWARDS = "Rewards";

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
            case COMMUNITY:
                fragment = SocialFeedFragment.getInstance();
                break;
            case CHALLENGE:
                fragment = ChallengeFragment.getInstance();
                break;
            case MESSAGE:
                fragment = MessageFragment.getInstance();
                break;
            case REWARDS:
                fragment = RewardsFragment.getInstance();
                break;
        }
        return fragment;
    }

    public static int getUnselectIcon_by_title(String title){
        int icon = 0;
        switch (title){
            case HOME:
                icon = R.drawable.icon_home_grey;
                break;
            case LIBRARY:
                icon = R.drawable.icon_library_grey;
                break;
            case FORUM:
                icon = R.drawable.icon_forum_grey;
                break;
            case GOAL:
                icon = R.drawable.icon_planning_grey;
                break;
            case MONITOR:
                icon = R.drawable.icon_monitor_grey;
                break;
            case COMMUNITY:
                icon = R.drawable.icon_feed_grey;
                break;
            case CHALLENGE:
                icon = R.drawable.icon_challenge_grey;
                break;
            case MORE:
                icon = R.drawable.icon_more_grey;
                break;
            case MESSAGE:
                icon = R.drawable.icon_message_grey;
                break;
            case REWARDS:
                icon = R.drawable.icon_reward_grey;
                break;
        }
        return icon;
    }

    public static int getSelectIcon_by_title(String title){
        int icon = 0;
        switch (title){
            case HOME:
                icon = R.drawable.icon_home_purple;
                break;
            case LIBRARY:
                icon = R.drawable.icon_library_purple;
                break;
            case FORUM:
                icon = R.drawable.icon_forum_purple;
                break;
            case GOAL:
                icon = R.drawable.icon_planning_purple;
                break;
            case MONITOR:
                icon = R.drawable.icon_monitor_purple;
                break;
            case COMMUNITY:
                icon = R.drawable.icon_feed_purple;
                break;
            case CHALLENGE:
                icon = R.drawable.icon_challenge_purple;
                break;
            case MORE:
                icon = R.drawable.icon_more_purple;
                break;
            case MESSAGE:
                icon = R.drawable.icon_message_purple;
                break;
            case REWARDS:
                icon = R.drawable.icon_reward_purple;
                break;
        }
        return icon;
    }

    public static String[] getFunctionBlockTitles_by_primary_features(List<Integer> primary_features){
        List<String> mTitleList = new ArrayList<>();
        mTitleList.add(HOME);
        for(Integer feature : primary_features){
            switch (feature){
                case 0:
                    mTitleList.add(MESSAGE);
                    break;
                case 1:
                    mTitleList.add(REWARDS);
                    break;
                case 2:
                    mTitleList.add(COMMUNITY);
                    break;
                case 3:
                    mTitleList.add(LIBRARY);
                    break;
                case 4:
                    mTitleList.add(FORUM);
                    break;
                case 5:
                    mTitleList.add(MONITOR);
                    break;
                case 6:
                    mTitleList.add(GOAL);
                    break;
                case 7:
                    mTitleList.add(CHALLENGE);
                    break;
            }
        }
        if(primary_features.size() < 7){
            mTitleList.add(MORE);
        }

        String[] mTitles = new String[mTitleList.size()];
        mTitles = mTitleList.toArray(mTitles);
        return mTitles;
    }

    public static String[] getFunctionBlockMoreTitles_by_primary_features(List<Integer> primary_features){
        List<String> mTitleList = new ArrayList<>();
        if(!primary_features.contains(0)){
            mTitleList.add(MESSAGE);
        }
        if(!primary_features.contains(1)){
            mTitleList.add(REWARDS);
        }
        if(!primary_features.contains(2)){
            mTitleList.add(COMMUNITY);
        }
        if(!primary_features.contains(3)){
            mTitleList.add(LIBRARY);
        }
        if(!primary_features.contains(4)){
            mTitleList.add(FORUM);
        }
        if(!primary_features.contains(5)){
            mTitleList.add(MONITOR);
        }
        if(!primary_features.contains(6)){
            mTitleList.add(GOAL);
        }
        if(!primary_features.contains(7)){
            mTitleList.add(CHALLENGE);
        }

        String[] mTitles = new String[mTitleList.size()];
        mTitles = mTitleList.toArray(mTitles);
        return mTitles;
    }

    public static Integer[] getFunctionBlockUnselectIcons_by_primary_features(List<Integer> primary_features){
        String[] mTitles = getFunctionBlockTitles_by_primary_features(primary_features);
        List<Integer> iconList = new ArrayList<>();
        for(String title : mTitles){
            iconList.add(getUnselectIcon_by_title(title));
        }

        Integer[] icons = new Integer[iconList.size()];
        icons = iconList.toArray(icons);
        return icons;
    }

    public static Integer[] getFunctionBlockSelectIcons_by_primary_features(List<Integer> primary_features){
        String[] mTitles = getFunctionBlockTitles_by_primary_features(primary_features);
        List<Integer> iconList = new ArrayList<>();
        for(String title : mTitles){
            iconList.add(getSelectIcon_by_title(title));
        }

        Integer[] icons = new Integer[iconList.size()];
        icons = iconList.toArray(icons);
        return icons;
    }

    public static String getRedirectTitle(int redirect){
        String title = "";
        switch (redirect){
            case 0:
                title = MESSAGE;
                break;
            case 1:
                title = REWARDS;
                break;
            case 2:
                title = COMMUNITY;
                break;
            case 3:
                title = LIBRARY;
                break;
            case 4:
                title = FORUM;
                break;
            case 5:
                title = MONITOR;
                break;
            case 6:
                title = GOAL;
                break;
            case 7:
                title = CHALLENGE;
                break;
        }
        return title;
    }
}
