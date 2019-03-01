package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Event.ForumEvent;
import sg.edu.nus.imovin.Event.PlanEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.EventConstants;
import sg.edu.nus.imovin.System.FuncBlockConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.utils.TabEntity;
import sg.edu.nus.imovin.utils.ViewFindUtils;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener {

    private View customActionBar;
    @BindView(R.id.navigator_middle_title) TextView navigator_middle_title;
    @BindView(R.id.navigator_right) RelativeLayout navigator_right;
    @BindView(R.id.navigator_right_text) TextView navigator_right_text;

    private View decorView;
    private CommonTabLayout sub_tab_layout;
    private ViewPager vp;
    private String[] mTitles;

    private final int defaultPagePosition = 0;
    private int currentPagePosition = 0;
    private ArrayList<Fragment> mFragments;
    private Integer[] mIconUnselectIds;

    private Integer[] mIconSelectIds;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private MyPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        SetActionBar();
        LinkUIbyId();
        SetFunction();
        Init();
    }

    private void SetActionBar(){
        ActionBar actionBar = getSupportActionBar();
        customActionBar = getLayoutInflater().inflate(R.layout.main_navigator, null);
        ButterKnife.bind(this, customActionBar);

        if(actionBar != null){
            actionBar.show();
            actionBar.setCustomView(customActionBar);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Toolbar parent =(Toolbar) customActionBar.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void LinkUIbyId(){
        decorView = getWindow().getDecorView();
        vp = ViewFindUtils.find(decorView, R.id.vp);
    }

    private void SetFunction(){
        navigator_right.setOnClickListener(this);
        int profile = ImovinApplication.getUserData().getProfile();
        mTitles = FuncBlockConstants.getFunctionBlockTitles_by_profile(profile);
        mIconUnselectIds = FuncBlockConstants.getFunctionBlockUnselectIcons_by_profile(profile);
        mIconSelectIds = FuncBlockConstants.getFunctionBlockSelectIcons_by_profile(profile);
    }

    private void Init(){
        ClearPreviousFragments();
        if(mFragments == null){
            mFragments = new ArrayList<>();
        }else {
            mFragments.clear();
        }

        for (String title : mTitles) {
            Fragment fragment = FuncBlockConstants.getFunctionFragment(title);
            if(fragment != null) {
                mFragments.add(fragment);
            }
        }

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        sub_tab_layout = ViewFindUtils.find(decorView, R.id.sub_tab_layout);
        sub_tab_layout.setTabData(mTabEntities);
        if(mTitles.length > 5) {
            sub_tab_layout.setTextsize(8);
        }

        sub_tab_layout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                navigator_middle_title.setText(mTitles[position]);
                SetTopNavFunc(mTitles[position]);
                vp.setCurrentItem(position);
                currentPagePosition = position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sub_tab_layout.setCurrentTab(position);
                navigator_middle_title.setText(mTitles[position]);
                SetTopNavFunc(mTitles[position]);
                currentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigator_middle_title.setText(mTitles[defaultPagePosition]);
        SetTopNavFunc(mTitles[defaultPagePosition]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigator_right:
                navRightClickFunc(currentPagePosition);
                break;
        }
    }

    private void SetTopNavFunc(String title){
        switch (title){
            case FuncBlockConstants.HOME:
                navigator_right.setEnabled(false);
                navigator_right_text.setVisibility(View.INVISIBLE);
                break;
            case FuncBlockConstants.LIBRARY:
                navigator_right.setEnabled(false);
                navigator_right_text.setVisibility(View.INVISIBLE);
                break;
            case FuncBlockConstants.FORUM:
                navigator_right.setEnabled(true);
                navigator_right_text.setVisibility(View.VISIBLE);
                navigator_right_text.setText("+");
                break;
            case FuncBlockConstants.GOAL:
                navigator_right.setEnabled(true);
                navigator_right_text.setVisibility(View.VISIBLE);
                navigator_right_text.setText("+");
                break;
            case FuncBlockConstants.MONITOR:
                navigator_right.setEnabled(false);
                navigator_right_text.setVisibility(View.INVISIBLE);
                break;
            case FuncBlockConstants.SOCIAL:
                navigator_right.setEnabled(true);
                navigator_right_text.setVisibility(View.VISIBLE);
                navigator_right_text.setText("+");
                break;
            case FuncBlockConstants.CHALLENGE:
                navigator_right.setEnabled(false);
                navigator_right_text.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void navRightClickFunc(int position){
        switch (mTitles[position]){
            case FuncBlockConstants.FORUM:
                Intent intentForum = new Intent();
                intentForum.setClass(this, ForumNewPostActivity.class);
                startActivityForResult(intentForum, IntentConstants.FORUM_NEW_POST);
                break;
            case FuncBlockConstants.GOAL:
                Intent intentGoal = new Intent();
                intentGoal.setClass(this, AddPlanActivity.class);
                startActivityForResult(intentGoal, IntentConstants.GOAL_NEW_PLAN);
                break;
            case FuncBlockConstants.SOCIAL:
                Intent newSocialIntent = new Intent();
                newSocialIntent.setClass(this, SocialNewPostActivity.class);
                startActivityForResult(newSocialIntent, IntentConstants.SOCIAL_NEW_POST);
                break;
        }
    }

    private void ClearPreviousFragments(){
        // Clean fragments (only if the app is recreated (When user disable permission))
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // Remove previous fragments (case of the app was restarted after changed permission on android 6 and higher)
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            }
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case IntentConstants.FORUM_NEW_POST:
                if(resultCode == Activity.RESULT_OK){
                    ImovinApplication.setNeedRefreshForum(true);
                    EventBus.getDefault().post(new ForumEvent(EventConstants.REFRESH));
                }
                break;
            case IntentConstants.GOAL_NEW_PLAN:
                if(resultCode == Activity.RESULT_OK){
                    ImovinApplication.setNeedRefreshForum(true);
                    EventBus.getDefault().post(new PlanEvent(EventConstants.REFRESH));
                }
                break;
            case IntentConstants.GOAL_EDIT_PLAN:
                if(resultCode == Activity.RESULT_OK){
                    ImovinApplication.setNeedRefreshForum(true);
                    EventBus.getDefault().post(new PlanEvent(EventConstants.REFRESH));
                }
                break;
        }
    }
}
