package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Common.CustomViewPager;
import sg.edu.nus.imovin.Event.EnableNextEvent;
import sg.edu.nus.imovin.Fragments.MCQFragment;
import sg.edu.nus.imovin.Fragments.QuesFragment;
import sg.edu.nus.imovin.Fragments.RateFragment;
import sg.edu.nus.imovin.Fragments.TextQFragment;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin.Retrofit.Object.QuestionData;
import sg.edu.nus.imovin.Retrofit.Object.SectionData;
import sg.edu.nus.imovin.Retrofit.Request.UploadQuestionRequest;
import sg.edu.nus.imovin.Retrofit.Response.QuestionnaireResponse;
import sg.edu.nus.imovin.Retrofit.Response.UploadQuestionnaireResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;
import sg.edu.nus.imovin.utils.ViewFindUtils;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class QuestionnaireActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{
    public static final String MCQ = "MCQ";
    public static final String MCQ_O = "MCQ_O";
    public static final String TEXT = "TEXT";
    public static final String NUM = "NUM";

    @BindView(R.id.nextBtn) Button nextBtn;
    @BindView(R.id.prevBtn) Button prevBtn;

    private View decorView;
    private CustomViewPager vp;

    private MyPagerAdapter mAdapter;
    private ArrayList<QuesFragment> mFragments;
    private String[] mTitles;

    private int currentFragmentIndex;
    private int currentSection;
    private List<SectionData> sectionDataList;
    private HashMap<Integer, AnswerData> answerDataHashMap;

    private Stack<Integer> questionOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        EventBus.getDefault().register(this);

        LinkUIbyId();
        SetFunction();
        Init();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this);

        decorView = getWindow().getDecorView();
        vp = ViewFindUtils.find(decorView, R.id.vp);
    }

    private void SetFunction(){
        prevBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
    }

    private void Init(){
        questionOrder = new Stack<>();

        QuestionnaireResponse questionnaireResponse = (QuestionnaireResponse) getIntent().getSerializableExtra(IntentConstants.QUESTION_DATA);
        sectionDataList = questionnaireResponse.getSections();

        answerDataHashMap = new HashMap<>();

        currentSection = 0;

        vp.addOnPageChangeListener(this);

        List<QuestionData> questionDataList = new ArrayList<>();

        for(SectionData sectionData : sectionDataList){
            questionDataList.addAll(sectionData.getQuestions());
        }

        prevBtn.setEnabled(false);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(sectionDataList.get(currentSection).getDisplay_name());
        ab.setBackgroundDrawable(getDrawable(R.color.theme_purple));
        SetSectionQuestion(questionDataList);
    }

    private void SetSectionQuestion(List<QuestionData> questionDataList){
        ClearPreviousFragments();

        List<String> titleList = new ArrayList<>();
        if(mFragments == null){
            mFragments = new ArrayList<>();
        }else{
            mFragments.clear();
        }

        for(QuestionData questionData : questionDataList){
            titleList.add(questionData.getName());
            switch (questionData.getQuestion_type()){
                case TEXT:
                    mFragments.add(TextQFragment.getInstance(questionData));
                    break;
                case NUM:
                    mFragments.add(TextQFragment.getInstance(questionData));
                    break;
                case MCQ:
                    if(questionData.getIs_custom()) {
                        mFragments.add(MCQFragment.getInstance(questionData));
                    }else{
                        mFragments.add(RateFragment.getInstance(questionData));
                    }
                    break;
                case MCQ_O:
                    mFragments.add(MCQFragment.getInstance(questionData));
                    break;
            }
        }
        mTitles = titleList.toArray(new String[titleList.size()]);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = ViewFindUtils.find(decorView, R.id.sub_tab_layout);
        tabLayout.setViewPager(vp);
    }

    private void SetTitle(int index){
        String title = "";
        for(SectionData sectionData : sectionDataList){
            if(title.equals("") && index < sectionData.getQuestions().size()){
                title = sectionData.getDisplay_name();
            }
            else{
                index -= sectionData.getQuestions().size();
            }
        }
        ActionBar ab = getSupportActionBar();
        ab.setTitle(title);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EnableNextEvent event){
        nextBtn.setEnabled(event.getEnable());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextBtn:
                AnswerData answerData = mFragments.get(currentFragmentIndex).getAnswer();

                if(answerDataHashMap.containsKey(currentFragmentIndex)) {
                    answerDataHashMap.replace(currentFragmentIndex, answerData);
                }else{
                    answerDataHashMap.put(currentFragmentIndex, answerData);
                }

                if(mFragments.get(currentFragmentIndex).getQuestionData().getIs_skippable()){
                    if(answerData.getAnswer().equals("0")){
                        if(currentFragmentIndex < mFragments.size() - 1) {
                            questionOrder.push(vp.getCurrentItem());
                            vp.setCurrentItem(vp.getCurrentItem() + 1);
                            SetTitle(vp.getCurrentItem());
                        }
                        else{
                            SubmitQuestionNaire();
                        }
                    }else{
                        if(currentFragmentIndex < mFragments.size() - 2) {
                            questionOrder.push(vp.getCurrentItem());
                            vp.setCurrentItem(vp.getCurrentItem() + 2);
                            SetTitle(vp.getCurrentItem());
                        }
                        else{
                            SubmitQuestionNaire();
                        }
                    }
                }else{
                    if(currentFragmentIndex < mFragments.size() - 1) {
                        questionOrder.push(vp.getCurrentItem());
                        vp.setCurrentItem(vp.getCurrentItem() + 1);
                        SetTitle(vp.getCurrentItem());
                    }
                    else{
                        SubmitQuestionNaire();
                    }
                }
                break;
            case R.id.prevBtn:
                if(nextBtn.isEnabled()) {
                    AnswerData answerData2 = mFragments.get(currentFragmentIndex).getAnswer();

                    if (answerDataHashMap.containsKey(currentFragmentIndex)) {
                        answerDataHashMap.replace(currentFragmentIndex, answerData2);
                    } else {
                        answerDataHashMap.put(currentFragmentIndex, answerData2);
                    }
                }

                Integer lastQuestionIndex = questionOrder.pop();
                vp.setCurrentItem(lastQuestionIndex);
                SetTitle(vp.getCurrentItem());

                break;
        }
    }

    private void SubmitQuestionNaire(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Collection<AnswerData> values = answerDataHashMap.values();
        List<AnswerData> answerDataList = new ArrayList<>(values);

        UploadQuestionRequest uploadQuestionRequest = new UploadQuestionRequest(answerDataList);

        Call<UploadQuestionnaireResponse> call = service.uploadQuestionnaire(uploadQuestionRequest);
        call.enqueue(new Callback<UploadQuestionnaireResponse>() {
            @Override
            public void onResponse(Call<UploadQuestionnaireResponse> call, Response<UploadQuestionnaireResponse> response) {
                try {
                    UploadQuestionnaireResponse uploadQuestionnaireResponse = response.body();
                    String result = uploadQuestionnaireResponse.get_status();
                    Log.d(LogConstants.LogTag, result);

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception upload answer : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadQuestionnaireResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure upload answer : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
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
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

        currentFragmentIndex = i;

        QuesFragment quesFragment = mFragments.get(i);
        QuestionData questionData = quesFragment.getQuestionData();

        if(questionData.getQuestion_type().equals(MCQ) && !questionData.getIs_custom()){
            nextBtn.setEnabled(true);
        }else {
            if(answerDataHashMap.containsKey(i)) {
                nextBtn.setEnabled(true);
            }else{
                nextBtn.setEnabled(false);
            }
        }

        if(answerDataHashMap.containsKey(i)){
            quesFragment.setAnswer(answerDataHashMap.get(i));
        }

        if(currentFragmentIndex == 0){
            prevBtn.setEnabled(false);
        }else{
            prevBtn.setEnabled(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

}
