package sg.edu.nus.imovin2.Event;

public class LikeCommentEvent {
    private String comment_id;
    private Boolean is_like;

    public LikeCommentEvent(String comment_id, Boolean is_like) {
        this.comment_id = comment_id;
        this.is_like = is_like;
    }

    public String getComment_id() {
        return comment_id;
    }

    public Boolean getIs_like() {
        return is_like;
    }
}
