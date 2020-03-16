package sg.edu.nus.imovin2.Retrofit.Object;

import java.io.Serializable;

public class RewardsSlotItemData implements Serializable {
    private String slot_id;
    private String slot_time;

    public String getSlot_id() {
        return slot_id;
    }

    public void setSlot_id(String slot_id) {
        this.slot_id = slot_id;
    }

    public String getSlot_time() {
        return slot_time;
    }

    public void setSlot_time(String slot_time) {
        this.slot_time = slot_time;
    }
}
