package sg.edu.nus.imovin2.Event;

public class ChangeCheckEvent {
    private Integer index;
    private Boolean isChecked;

    public ChangeCheckEvent(Integer index, Boolean isChecked) {
        this.index = index;
        this.isChecked = isChecked;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
