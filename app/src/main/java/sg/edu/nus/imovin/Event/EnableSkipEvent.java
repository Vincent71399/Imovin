package sg.edu.nus.imovin.Event;

public class EnableSkipEvent {
    private Boolean enable;

    public EnableSkipEvent() {
        this.enable = true;
    }

    public EnableSkipEvent(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        return enable;
    }
}
