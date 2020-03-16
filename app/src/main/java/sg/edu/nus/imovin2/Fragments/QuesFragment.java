package sg.edu.nus.imovin2.Fragments;

import android.support.v4.app.Fragment;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin2.Retrofit.Object.QuestionData;

public abstract class QuesFragment extends Fragment {

    private QuestionData questionData;

    public abstract List<QuestionData> getQuestionData();

    public abstract List<AnswerData> getAnswer();

    public abstract void setAnswer(List<AnswerData> answerData);
}
