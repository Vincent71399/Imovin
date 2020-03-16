package sg.edu.nus.imovin2.Retrofit.Response;

import sg.edu.nus.imovin2.Retrofit.Object.CommentData;

public class CommentResponse {
    private CommentData data;
    private String message;

    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
