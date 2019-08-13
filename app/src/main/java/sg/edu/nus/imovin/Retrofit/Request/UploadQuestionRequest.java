package sg.edu.nus.imovin.Retrofit.Request;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.AnswerData;

public class UploadQuestionRequest {
    private List<AnswerData> data;

    public UploadQuestionRequest(List<AnswerData> data) {
        this.data = data;
    }

    public List<AnswerData> getData() {
        return data;
    }

    public void setData(List<AnswerData> data) {
        this.data = data;
    }
}
