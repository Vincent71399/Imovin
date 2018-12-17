package sg.edu.nus.imovin.Retrofit.Request;

public class LikeCommentRequest {
    private String tid;
    private Boolean isLike;

    public LikeCommentRequest(String tid, Boolean isLike) {
        this.tid = tid;
        this.isLike = isLike;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }
}
