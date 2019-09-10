package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import sg.edu.nus.imovin.System.Config;
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

    private View customActionBar;
    @BindView(R.id.navigator_title) TextView navigator_title;
    @BindView(R.id.navigator_help) TextView navigator_help;
    @BindView(R.id.navigator_right_text) TextView navigator_right_text;

    private View decorView;
    private CustomViewPager vp;

    private MyPagerAdapter mAdapter;
    private ArrayList<QuesFragment> mFragments;
    private String[] mTitles;

    private int currentFragmentIndex;
    private int currentSection;
    private List<SectionData> sectionDataList;
    private HashMap<Integer, List<AnswerData>> answerDataHashMap;

    private Stack<Integer> questionOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        EventBus.getDefault().register(this);

        SetActionBar();
        LinkUIbyId();
        SetFunction();
        Init();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void SetActionBar(){
        ActionBar actionBar = getSupportActionBar();
        customActionBar = getLayoutInflater().inflate(R.layout.questionnaire_navigator, null);

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
        prevBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        navigator_help.setOnClickListener(this);
    }

    private void Init(){
        questionOrder = new Stack<>();

        QuestionnaireResponse questionnaireResponse = (QuestionnaireResponse) getIntent().getSerializableExtra(IntentConstants.QUESTION_DATA);
        sectionDataList = questionnaireResponse.getSections();

        answerDataHashMap = new HashMap<>();

        currentSection = 0;

        vp.addOnPageChangeListener(this);

        List<List<QuestionData>> questionDataList = new ArrayList<>();

        for(SectionData sectionData : sectionDataList){
            if(sectionData.getName().equals(Config.FIRST_SECTION_NAME)){
                questionDataList.addAll(splitQuestionData(sectionData.getQuestions(), 1));
            }else{
                questionDataList.addAll(splitQuestionData(sectionData.getQuestions(), Config.RATING_QUESTION_NUM_PER_PAGE));
            }
        }

        prevBtn.setEnabled(false);

        navigator_title.setText(sectionDataList.get(currentSection).getDisplay_name());
        navigator_right_text.setText("1/" + sectionDataList.get(currentSection).getQuestions().size());
        SetSectionQuestion(questionDataList);
    }

    private void SetSectionQuestion(List<List<QuestionData>> questionDataMatrix){
        ClearPreviousFragments();

        List<String> titleList = new ArrayList<>();
        if(mFragments == null){
            mFragments = new ArrayList<>();
        }else{
            mFragments.clear();
        }

        for(List<QuestionData> questionDataList : questionDataMatrix){
            titleList.add(questionDataList.get(0).getName());
            switch (questionDataList.get(0).getQuestion_type()){
                case TEXT:
                    mFragments.add(TextQFragment.getInstance(questionDataList.get(0)));
                    break;
                case NUM:
                    mFragments.add(TextQFragment.getInstance(questionDataList.get(0)));
                    break;
                case MCQ:
                    if(questionDataList.get(0).getIs_custom()) {
                        mFragments.add(MCQFragment.getInstance(questionDataList.get(0)));
                    }else{
                        mFragments.add(RateFragment.getInstance(questionDataList));
                    }
                    break;
                case MCQ_O:
                    mFragments.add(MCQFragment.getInstance(questionDataList.get(0)));
                    break;
            }
        }
        mTitles = titleList.toArray(new String[titleList.size()]);

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        SlidingTabLayout tabLayout = ViewFindUtils.find(decorView, R.id.sub_tab_layout);
        tabLayout.setViewPager(vp);
    }

    private void SetTitleAndProgress(int index){
        String title = "";
        int currentProgress = 0;
        int totalQuestion = 0;
        for(SectionData sectionData : sectionDataList){
            int totalPage = sectionData.getQuestions().size();
            if(!sectionData.getName().equals(Config.FIRST_SECTION_NAME)){
                if(totalPage % Config.RATING_QUESTION_NUM_PER_PAGE > 0){
                    totalPage = totalPage/Config.RATING_QUESTION_NUM_PER_PAGE + 1;
                }else{
                    totalPage = totalPage/Config.RATING_QUESTION_NUM_PER_PAGE;
                }
            }

            if(title.equals("") && index < totalPage){
                title = sectionData.getDisplay_name();
                currentProgress = index + 1;
                totalQuestion = totalPage;
            }
            else{
                index -= totalPage;
            }
        }
        navigator_title.setText(title);

        navigator_right_text.setText(currentProgress + "/" + totalQuestion);
    }

    private List<List<QuestionData>> splitQuestionData(List<QuestionData> questionDataList, int splitNum){
        List<List<QuestionData>> questionDataSplitList = new ArrayList<>();

        int totalSize = questionDataList.size();
        int count = totalSize / splitNum;
        for(int i=0; i<count; i++){
            List<QuestionData> subQuestionDataList = questionDataList.subList(i*splitNum, (i+1)*splitNum);
            questionDataSplitList.add(subQuestionDataList);
        }
        if(totalSize % splitNum > 0){
            List<QuestionData> subQuestionDataList = questionDataList.subList(totalSize - totalSize % splitNum, totalSize);
            questionDataSplitList.add(subQuestionDataList);
        }

        return questionDataSplitList;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EnableNextEvent event){
        nextBtn.setEnabled(event.getEnable());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextBtn:
                List<AnswerData> answerDataList = mFragments.get(currentFragmentIndex).getAnswer();

                if(answerDataHashMap.containsKey(currentFragmentIndex)) {
                    answerDataHashMap.replace(currentFragmentIndex, answerDataList);
                }else{
                    answerDataHashMap.put(currentFragmentIndex, answerDataList);
                }

                if(mFragments.get(currentFragmentIndex).getQuestionData().get(0).getIs_skippable()){
                    if(answerDataList.get(0).getAnswer().equals("0")){
                        if(currentFragmentIndex < mFragments.size() - 1) {
                            questionOrder.push(vp.getCurrentItem());
                            vp.setCurrentItem(vp.getCurrentItem() + 1);
                            SetTitleAndProgress(vp.getCurrentItem());
                        }
                        else{
                            ConfirmSubmit();
                        }
                    }else{
                        if(currentFragmentIndex < mFragments.size() - 2) {
                            questionOrder.push(vp.getCurrentItem());
                            vp.setCurrentItem(vp.getCurrentItem() + 2);
                            SetTitleAndProgress(vp.getCurrentItem());
                        }
                        else{
                            ConfirmSubmit();
                        }
                    }
                }else{
                    if(currentFragmentIndex < mFragments.size() - 1) {
                        questionOrder.push(vp.getCurrentItem());
                        vp.setCurrentItem(vp.getCurrentItem() + 1);
                        SetTitleAndProgress(vp.getCurrentItem());
                    }
                    else{
                        ConfirmSubmit();
                    }
                }
                break;
            case R.id.prevBtn:
                if(nextBtn.isEnabled()) {
                    List<AnswerData> answerDataList2 = mFragments.get(currentFragmentIndex).getAnswer();

                    if (answerDataHashMap.containsKey(currentFragmentIndex)) {
                        answerDataHashMap.replace(currentFragmentIndex, answerDataList2);
                    } else {
                        answerDataHashMap.put(currentFragmentIndex, answerDataList2);
                    }
                }

                Integer lastQuestionIndex = questionOrder.pop();
                vp.setCurrentItem(lastQuestionIndex);
                SetTitleAndProgress(vp.getCurrentItem());

                break;
            case R.id.navigator_help:
                int index = vp.getCurrentItem();
                SectionData currentSectionData = null;
                for(SectionData sectionData : sectionDataList){
                    int totalPage = sectionData.getQuestions().size();
                    if(!sectionData.getName().equals(Config.FIRST_SECTION_NAME)){
                        if(totalPage % Config.RATING_QUESTION_NUM_PER_PAGE > 0){
                            totalPage = totalPage/Config.RATING_QUESTION_NUM_PER_PAGE + 1;
                        }else{
                            totalPage = totalPage/Config.RATING_QUESTION_NUM_PER_PAGE;
                        }
                    }

                    if(currentSectionData == null && index < totalPage){
                        currentSectionData = sectionData;
                    }
                    else{
                        index -= totalPage;
                    }
                }
                openDialogBox(currentSectionData.getDisplay_name(), currentSectionData.getInstruction());
                break;
        }
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

    private void ConfirmSubmit(){
        Intent intent = new Intent();
        intent.setClass(this, QuestionnaireSummaryActivity.class);
        startActivityForResult(intent, IntentConstants.QUESTIONNAIRE_CONFIRM);
    }

    private void SubmitQuestionNaire(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Collection<List<AnswerData>> values = answerDataHashMap.values();
        List<List<AnswerData>> answerDataMatrix = new ArrayList<>(values);
        List<AnswerData> answerDataList = new ArrayList<>();
        for(List<AnswerData> answerDataSubList : answerDataMatrix){
            answerDataList.addAll(answerDataSubList);
        }

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
        List<QuestionData> questionDataList = quesFragment.getQuestionData();


        if(questionDataList.size() > 0
                && questionDataList.get(0).getQuestion_type().equals(MCQ)
                && !questionDataList.get(0).getIs_custom()){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case IntentConstants.QUESTIONNAIRE_CONFIRM:
                if(resultCode == RESULT_OK) {
                    SubmitQuestionNaire();
                }
                break;
        }
    }

}
