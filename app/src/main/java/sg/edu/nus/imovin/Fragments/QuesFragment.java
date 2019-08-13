package sg.edu.nus.imovin.Fragments;

import android.support.v4.app.Fragment;

import sg.edu.nus.imovin.Retrofit.Object.AnswerData;
import sg.edu.nus.imovin.Retrofit.Object.QuestionData;

public abstract class QuesFragment extends Fragment {

    private QuestionData questionData;

    public abstract QuestionData getQuestionData();

    public abstract AnswerData getAnswer();
}
