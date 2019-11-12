package sg.edu.nus.imovin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin.Retrofit.Object.QuestionData;


public class SetStepFragment extends QuesFragment {

    @BindView(R.id.question) TextView question;
    @BindView(R.id.value) TextView value;
    @BindView(R.id.unit) TextView unit;
    @BindView(R.id.planStepsBar) IndicatorSeekBar planStepsBar;

    private View rootView;
    private QuestionData questionData;

    public static SetStepFragment getInstance(QuestionData questionData) {
        SetStepFragment setStepFragment = new SetStepFragment();
        setStepFragment.questionData = questionData;
        return setStepFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_set_step, null);

        LinkUIbyId();
        SetFunction();

        return rootView;
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this, rootView);
    }

    private void SetFunction(){
        question.setText(questionData.getQuestion());
        unit.setText(questionData.getUnit());

        List<String> stepData = questionData.getChoices();
        if(stepData.size() >= 4) {
            int min = Integer.parseInt(stepData.get(0));
            int max = Integer.parseInt(stepData.get(1));
            int step = Integer.parseInt(stepData.get(2));
            int default_value = Integer.parseInt(stepData.get(3));
            int tickCount = (max - min)/step + 1;
            planStepsBar.setMin(min);
            planStepsBar.setMax(max);
            planStepsBar.setTickCount(tickCount);
            value.setText(String.valueOf(default_value));
            planStepsBar.setProgress(default_value);
        }

        planStepsBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                int steps = seekParams.progress;
                value.setText(String.valueOf(steps));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }

    @Override
    public List<QuestionData> getQuestionData() {
        List<QuestionData> questionDataList = new ArrayList<>();
        questionDataList.add(questionData);
        return questionDataList;
    }

    @Override
    public List<AnswerData> getAnswer() {
        AnswerData answerData = new AnswerData();
        answerData.setAnswer(value.getText().toString());
        answerData.setQuestion(questionData.get_id());

        List<AnswerData> answerDataList = new ArrayList<>();
        answerDataList.add(answerData);

        return answerDataList;
    }

    @Override
    public void setAnswer(List<AnswerData> answerDataList) {
        AnswerData answerData = answerDataList.get(0);
        value.setText(answerData.getAnswer());
        planStepsBar.setProgress(Integer.parseInt(answerData.getAnswer()));
    }

}
