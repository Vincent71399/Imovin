package sg.edu.nus.imovin.Retrofit.Request;

/**
 * Created by wcafricanus on 20/12/18.
 */

public class LikeSocialCommentRequest {
    private String pid;
    private Boolean isLike;

    public LikeSocialCommentRequest(String tid, Boolean isLike) {
        this.pid = tid;
        this.isLike = isLike;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }
}
