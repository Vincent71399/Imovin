package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.Event.EnableNextEvent;
import sg.edu.nus.imovin.Event.EnableSkipEvent;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin.Retrofit.Object.QuestionData;

import static sg.edu.nus.imovin.Activities.QuestionnaireActivity.NUM;

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
    public QuestionData getQuestionData() {
        return questionData;
    }

    @Override
    public AnswerData getAnswer() {
        AnswerData answerData = new AnswerData();
        answerData.setAnswer(question_input.getText().toString());
        answerData.setQuestion(questionData.get_id());

        return answerData;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(!charSequence.toString().equals("")){
            EventBus.getDefault().post(new EnableNextEvent());
        }else{
            EventBus.getDefault().post(new EnableNextEvent(false));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
