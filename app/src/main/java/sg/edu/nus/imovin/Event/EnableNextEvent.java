package sg.edu.nus.imovin.Event;

public class EnableNextEvent {
    private Boolean enable;

    public EnableNextEvent() {
        this.enable = true;
    }

    public EnableNextEvent(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        return enable;
    }
}
