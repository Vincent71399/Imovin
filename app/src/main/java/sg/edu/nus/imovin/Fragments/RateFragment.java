package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin.Retrofit.Object.QuestionData;

import static android.view.View.GONE;


public class RateFragment extends QuesFragment {

    @BindView(R.id.container1) LinearLayout container1;
    @BindView(R.id.container2) LinearLayout container2;
    @BindView(R.id.container3) LinearLayout container3;
    @BindView(R.id.container4) LinearLayout container4;
    @BindView(R.id.container5) LinearLayout container5;
    @BindView(R.id.question1) TextView question1;
    @BindView(R.id.question2) TextView question2;
    @BindView(R.id.question3) TextView question3;
    @BindView(R.id.question4) TextView question4;
    @BindView(R.id.question5) TextView question5;
    @BindView(R.id.rate_input1) IndicatorSeekBar rate_input1;
    @BindView(R.id.rate_input2) IndicatorSeekBar rate_input2;
    @BindView(R.id.rate_input3) IndicatorSeekBar rate_input3;
    @BindView(R.id.rate_input4) IndicatorSeekBar rate_input4;
    @BindView(R.id.rate_input5) IndicatorSeekBar rate_input5;

    private View rootView;
    private List<QuestionData> questionDataList;

    public static RateFragment getInstance(List<QuestionData> questionDataList) {
        RateFragment rateFragment = new RateFragment();
        rateFragment.questionDataList = questionDataList;

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
        if(questionDataList.size() >= 1) {
            question1.setText(questionDataList.get(0).getQuestion());
        }else{
            container1.setVisibility(GONE);
        }


        if(questionDataList.size() >= 2) {
            question2.setText(questionDataList.get(1).getQuestion());
        }else{
            container2.setVisibility(GONE);
        }

        if(questionDataList.size() >= 3) {
            question3.setText(questionDataList.get(2).getQuestion());
        }else{
            container3.setVisibility(GONE);
        }

        if(questionDataList.size() >= 4) {
            question4.setText(questionDataList.get(3).getQuestion());
        }else{
            container4.setVisibility(GONE);
        }

        if(questionDataList.size() >= 5) {
            question5.setText(questionDataList.get(4).getQuestion());
        }else{
            container5.setVisibility(GONE);
        }

    }

    @Override
    public List<QuestionData> getQuestionData() {
        return questionDataList;
    }

    @Override
    public List<AnswerData> getAnswer() {
        List<AnswerData> answerDataList = new ArrayList<>();

        if(questionDataList.size() >= 1) {
            int progress = rate_input1.getProgress() - 1;
            AnswerData answerData = new AnswerData();
            answerData.setAnswer(String.valueOf(progress));
            answerData.setQuestion(questionDataList.get(0).get_id());
            answerDataList.add(answerData);
        }

        if(questionDataList.size() >= 2) {
            int progress = rate_input2.getProgress() - 1;
            AnswerData answerData = new AnswerData();
            answerData.setAnswer(String.valueOf(progress));
            answerData.setQuestion(questionDataList.get(1).get_id());
            answerDataList.add(answerData);
        }

        if(questionDataList.size() >= 3) {
            int progress = rate_input3.getProgress() - 1;
            AnswerData answerData = new AnswerData();
            answerData.setAnswer(String.valueOf(progress));
            answerData.setQuestion(questionDataList.get(2).get_id());
            answerDataList.add(answerData);
        }

        if(questionDataList.size() >= 4) {
            int progress = rate_input4.getProgress() - 1;
            AnswerData answerData = new AnswerData();
            answerData.setAnswer(String.valueOf(progress));
            answerData.setQuestion(questionDataList.get(3).get_id());
            answerDataList.add(answerData);
        }

        if(questionDataList.size() >= 5) {
            int progress = rate_input5.getProgress() - 1;
            AnswerData answerData = new AnswerData();
            answerData.setAnswer(String.valueOf(progress));
            answerData.setQuestion(questionDataList.get(4).get_id());
            answerDataList.add(answerData);
        }

        return answerDataList;
    }

    @Override
    public void setAnswer(List<AnswerData> answerDataList) {
        if(answerDataList.size() >= 1){
            rate_input1.setProgress(Integer.parseInt(answerDataList.get(0).getAnswer()) + 1);
        }

        if(answerDataList.size() >= 2){
            rate_input2.setProgress(Integer.parseInt(answerDataList.get(1).getAnswer()) + 1);
        }

        if(answerDataList.size() >= 3){
            rate_input3.setProgress(Integer.parseInt(answerDataList.get(2).getAnswer()) + 1);
        }

        if(answerDataList.size() >= 4){
            rate_input4.setProgress(Integer.parseInt(answerDataList.get(3).getAnswer()) + 1);
        }

        if(answerDataList.size() >= 5){
            rate_input5.setProgress(Integer.parseInt(answerDataList.get(4).getAnswer()) + 1);
        }
    }


}
