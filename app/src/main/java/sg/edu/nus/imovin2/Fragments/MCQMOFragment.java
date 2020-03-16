package sg.edu.nus.imovin2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.Adapters.CheckboxAdapter;
import sg.edu.nus.imovin2.Event.ChangeCheckEvent;
import sg.edu.nus.imovin2.Objects.CheckboxOption;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin2.Retrofit.Object.QuestionData;


public class MCQMOFragment extends QuesFragment {
    @BindView(R.id.question) TextView question;
    @BindView(R.id.question_checklist) RecyclerView question_checklist;
    @BindView(R.id.other_input) EditText other_input;

    private View rootView;
    private QuestionData questionData;

    private List<CheckboxOption> checkboxOptionList;
    private boolean hasAnswer;

    public static MCQMOFragment getInstance(QuestionData questionData) {
        MCQMOFragment mcqFragment = new MCQMOFragment();
        mcqFragment.questionData = questionData;
        return mcqFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mcq_mo, null);

        LinkUIbyId();
        SetFunction();
        Init();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        question.setText(questionData.getQuestion());

        if(checkboxOptionList == null) {
            checkboxOptionList = new ArrayList<>();
        }else{
            checkboxOptionList.clear();
        }
        for(String choices : questionData.getChoices()){
            checkboxOptionList.add(new CheckboxOption(choices));
        }

        CheckboxAdapter checkboxAdapter = new CheckboxAdapter(checkboxOptionList);
        question_checklist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        question_checklist.setAdapter(checkboxAdapter);
    }

    private void Init(){

    }

    @Override
    public List<QuestionData> getQuestionData() {
        List<QuestionData> questionDataList = new ArrayList<>();
        questionDataList.add(questionData);
        return questionDataList;
    }

    @Override
    public List<AnswerData> getAnswer() {
        HideKeyboardAll();

        AnswerData answerData = new AnswerData();
        List<String> answerStringList = new ArrayList<>();
        int index = 0;
        for(CheckboxOption checkboxOption : checkboxOptionList){
            if(checkboxOption.getIs_check()){
                answerStringList.add(String.valueOf(index));
            }
            index ++;
        }
        if(!other_input.getText().toString().equals("")){
            answerStringList.add("\"" + other_input.getText().toString() + "\"");
        }
        String answerString = "[" + TextUtils.join(",", answerStringList) + "]";

        answerData.setAnswer(answerString);
        answerData.setQuestion(questionData.get_id());

        hasAnswer = true;

        List<AnswerData> answerDataList = new ArrayList<>();
        answerDataList.add(answerData);

        return answerDataList;
    }

    @Override
    public void setAnswer(List<AnswerData> answerDataList) {
        AnswerData answerData = answerDataList.get(0);

        String answerRaw = answerData.getAnswer();
        answerRaw = answerRaw.substring(1, answerRaw.length()-1);
        String[] answerArray = answerRaw.split(",");
        for(String answer : answerArray){
            try {
                int index = Integer.parseInt(answer);
                checkboxOptionList.get(index).setIs_check(true);
            }catch (Exception e){
                other_input.setText(answer.substring(1, answer.length()-1));
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeCheckEvent event) {
        checkboxOptionList.get(event.getIndex()).setIs_check(event.getChecked());
        question_checklist.getAdapter().notifyDataSetChanged();
    }

    private void HideKeyboardAll(){
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(other_input.getWindowToken(), 0);
    }
}
