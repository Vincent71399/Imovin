package sg.edu.nus.imovin2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.Event.EnableNextEvent;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin2.Retrofit.Object.QuestionData;

import static sg.edu.nus.imovin2.Activities.QuestionnaireActivity.MCQ_O;


public class MCQFragment extends QuesFragment implements RadioGroup.OnCheckedChangeListener, TextWatcher {

    @BindView(R.id.question) TextView question;
    @BindView(R.id.mcq) RadioGroup mcq;
    @BindView(R.id.other_input) EditText other_input;

    private View rootView;
    private QuestionData questionData;

    private boolean hasAnswer;
    private int selectedIndex;

    public static MCQFragment getInstance(QuestionData questionData) {
        MCQFragment mcqFragment = new MCQFragment();
        mcqFragment.questionData = questionData;
        return mcqFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_mcq, null);

        LinkUIbyId();
        SetFunction();
        Init();

        return rootView;
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        question.setText(questionData.getQuestion());

        List<String> choices = questionData.getChoices();
        for(String choice : choices){
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(choice);
            mcq.addView(radioButton);
        }
        if(questionData.getQuestion_type().equals(MCQ_O)){
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(getString(R.string.other));
            mcq.addView(radioButton);
        }else {
            other_input.setVisibility(View.GONE);
        }

        mcq.setOnCheckedChangeListener(this);
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

        AnswerData answerData = new AnswerData();
        answerData.setAnswer(String.valueOf(selectedIndex));
        if(selectedIndex == questionData.getChoices().size()){
            answerData.setOthers(other_input.getText().toString());
        }
        answerData.setQuestion(questionData.get_id());

        hasAnswer = true;

        List<AnswerData> answerDataList = new ArrayList<>();
        answerDataList.add(answerData);

        return answerDataList;
    }

    @Override
    public void setAnswer(List<AnswerData> answerDataList) {
        AnswerData answerData = answerDataList.get(0);
        int count = mcq.getChildCount();
        for (int i=0;i<count;i++) {
            View view = mcq.getChildAt(i);
            if (view instanceof RadioButton) {
                if(String.valueOf(i).equals(answerData.getAnswer())){
                    ((RadioButton)view).setChecked(true);
                }
            }
        }
        other_input.setText(answerData.getOthers());
    }

    private int GetRadioBtnIndex(String radioBtnText){
        int index = 0;
        if(radioBtnText.equals(getString(R.string.other))){
            index = questionData.getChoices().size();
        }else {
            for (int i = 0; i < questionData.getChoices().size(); i++) {
                if (questionData.getChoices().get(i).equals(radioBtnText)) {
                    index = i;
                }
            }
        }
        return index;
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton rb = rootView.findViewById(i);

        selectedIndex = GetRadioBtnIndex(rb.getText().toString());

        if (rb.getText().toString().equals(getString(R.string.other))) {
            other_input.setEnabled(true);
            EventBus.getDefault().post(new EnableNextEvent(false));
        } else {
            other_input.setEnabled(false);
            other_input.setText("");
            EventBus.getDefault().post(new EnableNextEvent());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(questionData.getQuestion_type().equals(MCQ_O)) {
            if(hasAnswer){
                hasAnswer = false;
            }else {
                if (!charSequence.toString().equals("")) {
                    EventBus.getDefault().post(new EnableNextEvent());
                } else {
                    EventBus.getDefault().post(new EnableNextEvent(false));
                }
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void HideKeyboardAll(){
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(other_input.getWindowToken(), 0);
    }
}
