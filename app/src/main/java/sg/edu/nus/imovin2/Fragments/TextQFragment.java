package sg.edu.nus.imovin2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin2.Event.EnableNextEvent;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin2.Retrofit.Object.QuestionData;

import static sg.edu.nus.imovin2.Activities.QuestionnaireActivity.NUM;

public class TextQFragment extends QuesFragment implements TextWatcher {

    @BindView(R.id.question) TextView question;
    @BindView(R.id.question_input) EditText question_input;

    private View rootView;
    private QuestionData questionData;

    public static TextQFragment getInstance(QuestionData questionData) {
        TextQFragment textQFragment = new TextQFragment();
        textQFragment.questionData = questionData;
        return textQFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_text_q, null);

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

        if(questionData.getQuestion_type().equals(NUM)){
            question_input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            question_input.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }
    }

    private void Init(){
        question_input.addTextChangedListener(this);
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
        answerData.setAnswer(question_input.getText().toString());
        answerData.setQuestion(questionData.get_id());

        List<AnswerData> answerDataList = new ArrayList<>();
        answerDataList.add(answerData);

        return answerDataList;
    }

    @Override
    public void setAnswer(List<AnswerData> answerDataList) {
        question_input.setText(answerDataList.get(0).getAnswer());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(!charSequence.toString().equals("")){
            if(questionData.getRegex().equals("") || Pattern.matches(questionData.getRegex(), charSequence.toString())) {
                EventBus.getDefault().post(new EnableNextEvent());
            }else{
                EventBus.getDefault().post(new EnableNextEvent(false));
            }
        }else{
            EventBus.getDefault().post(new EnableNextEvent(false));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void HideKeyboardAll(){
        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(question_input.getWindowToken(), 0);
    }
}
