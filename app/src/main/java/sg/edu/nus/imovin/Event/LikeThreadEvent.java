package sg.edu.nus.imovin.Event;

public class LikeThreadEvent {
    private String thread_id;
    private Boolean is_like;

    public LikeThreadEvent(String thread_id, Boolean is_like) {
        this.thread_id = thread_id;
        this.is_like = is_like;
    }

    public String getThread_id() {
        return thread_id;
    }

    public Boolean getIs_like() {
        return is_like;
    }
}
