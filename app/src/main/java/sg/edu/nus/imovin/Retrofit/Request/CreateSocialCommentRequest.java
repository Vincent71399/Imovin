package sg.edu.nus.imovin.Retrofit.Request;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class CreateSocialCommentRequest {
    private String pid;
    private String message;

    public CreateSocialCommentRequest(String pid, String message) {
        this.pid = pid;
        this.message = message;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
