package sg.edu.nus.imovin.Event;

public class PlanEvent {
    public static final int MODULE_GOAL = 1;
    public static final int MODULE_MONITOR = 2;

    private int module;
    private String message;
    private String id;

    public PlanEvent(String message) {
        this.message = message;
        this.module = MODULE_GOAL;
    }

    public PlanEvent(String message, String id) {
        this.message = message;
        this.id = id;
        this.module = MODULE_GOAL;
    }

    public PlanEvent(String message, String id, int module) {
        this.message = message;
        this.id = id;
        this.module = module;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public int getModule() {
        return module;
    }
}
