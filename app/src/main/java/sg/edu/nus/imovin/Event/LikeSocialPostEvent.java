package sg.edu.nus.imovin.Event;

public class LikeSocialPostEvent {
    private String post_id;
    private Boolean is_like;

    public LikeSocialPostEvent(String post_id, Boolean is_like) {
        this.post_id = post_id;
        this.is_like = is_like;
    }

    public String getPost_id() {
        return post_id;
    }

    public Boolean getIs_like() {
        return is_like;
    }
}
