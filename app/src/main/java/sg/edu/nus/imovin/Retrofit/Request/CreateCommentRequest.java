package sg.edu.nus.imovin.Retrofit.Request;

public class CreateCommentRequest {
    private String message;

    public CreateCommentRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
