package sg.edu.nus.imovin.Retrofit.Request;

public class CreateCommentRequest {
    private String tid;
    private String message;

    public CreateCommentRequest(String tid, String message) {
        this.tid = tid;
        this.message = message;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
