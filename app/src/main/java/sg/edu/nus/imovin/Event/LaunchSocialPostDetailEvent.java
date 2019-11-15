package sg.edu.nus.imovin.Event;

public class LaunchSocialPostDetailEvent {
    private String post_id;

    public LaunchSocialPostDetailEvent(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_id() {
        return post_id;
    }
}
