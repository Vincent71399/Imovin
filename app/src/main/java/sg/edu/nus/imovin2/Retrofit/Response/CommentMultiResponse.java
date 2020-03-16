package sg.edu.nus.imovin2.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.CommentData;

public class CommentMultiResponse {
    private List<CommentData> data;
    private String message;

    public List<CommentData> getData() {
        return data;
    }

    public void setData(List<CommentData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
