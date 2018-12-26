package sg.edu.nus.imovin.Event;

public class PlanEvent {
    private String message;
    private String id;

    public PlanEvent(String message) {
        this.message = message;
    }

    public PlanEvent(String message, String id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }
}
