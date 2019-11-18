package sg.edu.nus.imovin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import sg.edu.nus.imovin.Adapters.CheckboxAdapter;
import sg.edu.nus.imovin.Event.ChangeCheckEvent;
import sg.edu.nus.imovin.Objects.CheckboxOption;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin.Retrofit.Object.QuestionData;


public class MCQMOFragment extends QuesFragment implements TextWatcher {
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
        question_checklist.setAdapter(checkboxAdapter);
    }

    private void Init(){
        other_input.addTextChangedListener(this);
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

//        AnswerData answerData = new AnswerData();
//        answerData.setAnswer(String.valueOf(selectedIndex));
//        if(selectedIndex == questionData.getChoices().size()){
//            answerData.setOthers(other_input.getText().toString());
//        }
//        answerData.setQuestion(questionData.get_id());
//
//        hasAnswer = true;
//
//        List<AnswerData> answerDataList = new ArrayList<>();
//        answerDataList.add(answerData);

//        return answerDataList;
        return null;
    }

    @Override
    public void setAnswer(List<AnswerData> answerDataList) {
//        AnswerData answerData = answerDataList.get(0);
//        int count = mcq.getChildCount();
//        for (int i=0;i<count;i++) {
//            View view = mcq.getChildAt(i);
//            if (view instanceof RadioButton) {
//                if(String.valueOf(i).equals(answerData.getAnswer())){
//                    ((RadioButton)view).setChecked(true);
//                }
//            }
//        }
//        other_input.setText(answerData.getOthers());
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(ChangeCheckEvent event) {
        
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        if(questionData.getQuestion_type().equals(MCQ_O)) {
//            if(hasAnswer){
//                hasAnswer = false;
//            }else {
//                if (!charSequence.toString().equals("")) {
//                    EventBus.getDefault().post(new EnableNextEvent());
//                } else {
//                    EventBus.getDefault().post(new EnableNextEvent(false));
//                }
//            }
//        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void HideKeyboardAll(){
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(other_input.getWindowToken(), 0);
    }
}
