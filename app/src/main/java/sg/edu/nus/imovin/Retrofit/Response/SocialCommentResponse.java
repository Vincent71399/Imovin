package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.SocialCommentData;



public class SocialCommentResponse {
    private SocialCommentData data;
    private String message;

    public SocialCommentData getData() {
        return data;
    }

    public void setData(SocialCommentData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
