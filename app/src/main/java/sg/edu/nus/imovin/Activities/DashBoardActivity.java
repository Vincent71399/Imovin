package sg.edu.nus.imovin.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.pusher.pushnotifications.PushNotifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Adapters.TabAdapter;
import sg.edu.nus.imovin.Common.OtherFunc;
import sg.edu.nus.imovin.GreenDAO.LogFuncClick;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.FuncBlockConstants;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;
import sg.edu.nus.imovin.System.SystemConstant;
import sg.edu.nus.imovin.utils.TabEntity;
import sg.edu.nus.imovin.utils.ViewFindUtils;

import static sg.edu.nus.imovin.System.FuncBlockConstants.MORE;

public class DashBoardActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private boolean spinner_init = true;

    private View customActionBar;
    @BindView(R.id.navigator_title) TextView navigator_title;
    @BindView(R.id.navigator_help) TextView navigator_help;
    @BindView(R.id.navigator_logout) ImageView navigator_logout;
    @BindView(R.id.more_spinner) Spinner more_spinner;

    private View decorView;
    private CommonTabLayout sub_tab_layout;
    private ViewPager vp;
    private String[] mTitles;
    private String[] moreTitles;

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
        customActionBar = getLayoutInflater().inflate(R.layout.main_navigator_new, null);

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
        ButterKnife.bind(this);

        decorView = getWindow().getDecorView();
        vp = ViewFindUtils.find(decorView, R.id.vp);
    }

    private void SetFunction(){
        navigator_help.setOnClickListener(this);
        navigator_logout.setOnClickListener(this);

//        int profile = ImovinApplication.getUserInfoResponse().getProfile();
//        mTitles = FuncBlockConstants.getFunctionBlockTitles_by_profile(profile);
//        moreTitles = FuncBlockConstants.getFunctionBlockMoreTitles_by_profile(profile);
//        mIconUnselectIds = FuncBlockConstants.getFunctionBlockUnselectIcons_by_profile(profile);
//        mIconSelectIds = FuncBlockConstants.getFunctionBlockSelectIcons_by_profile(profile);

        List<Integer> primaryFeatures = ImovinApplication.getUserInfoResponse().getPrimary_features();
        mTitles = FuncBlockConstants.getFunctionBlockTitles_by_primary_features(primaryFeatures);
        moreTitles = FuncBlockConstants.getFunctionBlockMoreTitles_by_primary_features(primaryFeatures);
        mIconUnselectIds = FuncBlockConstants.getFunctionBlockUnselectIcons_by_primary_features(primaryFeatures);
        mIconSelectIds = FuncBlockConstants.getFunctionBlockSelectIcons_by_primary_features(primaryFeatures);

        TabAdapter adapter = new TabAdapter(getApplicationContext(), Arrays.asList(moreTitles));
        more_spinner.setAdapter(adapter);
        more_spinner.setOnItemSelectedListener(this);
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

        for (String title : moreTitles){
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

                if(mTitles[position].equals(MORE)){
                    more_spinner.performClick();
                }else {
                    sub_tab_layout.setCurrentTab(position);
                    setTitleText(position);
                    vp.setCurrentItem(position);
                    currentPagePosition = position;
                }
            }

            @Override
            public void onTabReselect(int position) {
                if(mTitles[position].equals(MORE)){
                    more_spinner.performClick();
                }
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                logFunc(position);
                if(position >= mTitles.length - 1){
                    sub_tab_layout.setCurrentTab(mTitles.length - 1);
                    ((TabAdapter)more_spinner.getAdapter()).setSelection(position - mTitles.length + 1);
                }else {
                    sub_tab_layout.setCurrentTab(position);
                    ((TabAdapter)more_spinner.getAdapter()).setSelection(-1);
                }
                setTitleText(position);
                currentPagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        navigator_title.setText(mTitles[defaultPagePosition]);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        OtherFunc.GetDBFunction().addHomeCount(year, month, day);
        LogFuncClick logFuncClick1 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
        Log.d(LogConstants.DailyLogTag, "Home Count : " + logFuncClick1.getHomeCount());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigator_help:
                helpIconFunc(currentPagePosition);
                break;
            case R.id.navigator_logout:
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(SystemConstant.SHARE_PREFERENCE_LOCATION, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(SystemConstant.USERNAME);
                editor.remove(SystemConstant.PASSWORD);
                editor.apply();

                PushNotifications.unsubscribe(ImovinApplication.getUserInfoResponse().getEmail());

                Intent intent = new Intent();
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if(spinner_init){
            spinner_init = false;
        }else {
            vp.setCurrentItem(mTitles.length - 1 + position);
            sub_tab_layout.setCurrentTab(mTitles.length - 1);
            ((TabAdapter)more_spinner.getAdapter()).setSelection(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setTitleText(int position){
        String title = "";
        if(moreTitles.length > 0){
            if(position >= mTitles.length - 1){
                title = moreTitles[position - mTitles.length + 1];
            }else{
                title = mTitles[position];
            }
        }else{
            title = mTitles[position];
        }

        navigator_title.setText(title);
    }

    private void helpIconFunc(int position){
        String help_text = "";

        String title = "";
        if(moreTitles.length > 0){
            if(position >= mTitles.length - 1){
                title = moreTitles[position - mTitles.length + 1];
            }else{
                title = mTitles[position];
            }
        }else{
            title = mTitles[position];
        }

        switch (title){
            case FuncBlockConstants.HOME:
                help_text = getString(R.string.help_home);
                break;
            case FuncBlockConstants.CHALLENGE:
                help_text = getString(R.string.help_challenge);
                break;
            case FuncBlockConstants.SOCIAL:
                help_text = getString(R.string.help_social_feed);
                break;
            case FuncBlockConstants.LIBRARY:
                help_text = getString(R.string.help_library);
                break;
            case FuncBlockConstants.GOAL:
                help_text = getString(R.string.help_goal_plan);
                break;
            case FuncBlockConstants.MONITOR:
                help_text = getString(R.string.help_monitor_plan);
                break;
            case FuncBlockConstants.FORUM:
                help_text = getString(R.string.help_forum);
                break;
        }

        openDialogBox(title, help_text);
    }

    private void openDialogBox(String title, String text){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle(title);
        builderSingle.setMessage(text);
        builderSingle.setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.show();
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

    private void logFunc(int position){
        String funcBlockName = "";
        if(position < mTitles.length - 1){
            funcBlockName = mTitles[position];
        }else if(position - mTitles.length + 1 < moreTitles.length){
            funcBlockName = moreTitles[position - mTitles.length + 1];
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        switch (funcBlockName){
            case FuncBlockConstants.HOME:
                OtherFunc.GetDBFunction().addHomeCount(year, month, day);
                LogFuncClick logFuncClick1 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
                Log.d(LogConstants.DailyLogTag, "Home Count : " + logFuncClick1.getHomeCount());
                break;
            case FuncBlockConstants.LIBRARY:
                OtherFunc.GetDBFunction().addLibraryCount(year, month, day);
                LogFuncClick logFuncClick2 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
                Log.d(LogConstants.DailyLogTag, "Library Count : " + logFuncClick2.getLibraryCount());
                break;
            case FuncBlockConstants.FORUM:
                OtherFunc.GetDBFunction().addForumCount(year, month, day);
                LogFuncClick logFuncClick3 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
                Log.d(LogConstants.DailyLogTag, "Forum Count : " + logFuncClick3.getForumCount());
                break;
            case FuncBlockConstants.GOAL:
                OtherFunc.GetDBFunction().addGoalCount(year, month, day);
                LogFuncClick logFuncClick4 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
                Log.d(LogConstants.DailyLogTag, "Goal Count : " + logFuncClick4.getGoalCount());
                break;
            case FuncBlockConstants.MONITOR:
                OtherFunc.GetDBFunction().addMonitorCount(year, month, day);
                LogFuncClick logFuncClick5 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
                Log.d(LogConstants.DailyLogTag, "Monitor Count : " + logFuncClick5.getMonitorCount());
                break;
            case FuncBlockConstants.SOCIAL:
                OtherFunc.GetDBFunction().addSocialCount(year, month, day);
                LogFuncClick logFuncClick6 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
                Log.d(LogConstants.DailyLogTag, "Social Count : " + logFuncClick6.getSocialCount());
                break;
            case FuncBlockConstants.CHALLENGE:
                OtherFunc.GetDBFunction().addChallengeCount(year, month, day);
                LogFuncClick logFuncClick7 = OtherFunc.GetDBFunction().queryLogFuncClick_by_Date(year, month, day);
                Log.d(LogConstants.DailyLogTag, "Challenge Count : " + logFuncClick7.getChallengeCount());
                break;
        }

    }

}
