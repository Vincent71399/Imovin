package sg.edu.nus.imovin2.Event;

public class LaunchThreadDetailEvent {
    private String thread_id;

    public LaunchThreadDetailEvent(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getThread_id() {
        return thread_id;
    }
}
