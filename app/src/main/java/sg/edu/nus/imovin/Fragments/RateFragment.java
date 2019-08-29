package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warkiz.widget.IndicatorSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin.Retrofit.Object.QuestionData;


public class RateFragment extends QuesFragment {

    @BindView(R.id.question) TextView question;
    @BindView(R.id.rate_input) IndicatorSeekBar rate_input;

    private View rootView;
    private QuestionData questionData;

    public static RateFragment getInstance(QuestionData questionData) {
        RateFragment rateFragment = new RateFragment();
        rateFragment.questionData = questionData;

        return rateFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_rate, null);

        LinkUIbyId();
        SetFunction();

        return rootView;
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction() {
        question.setText(questionData.getQuestion());
    }

    @Override
    public QuestionData getQuestionData() {
        return questionData;
    }

    @Override
    public AnswerData getAnswer() {
        int progress = rate_input.getProgress() - 1;

        AnswerData answerData = new AnswerData();
        answerData.setAnswer(String.valueOf(progress));
        answerData.setQuestion(questionData.get_id());

        return answerData;
    }

    @Override
    public void setAnswer(AnswerData answerData) {
        rate_input.setProgress(Integer.parseInt(answerData.getAnswer()) + 1);
    }
}
