package sg.edu.nus.imovin.Retrofit.Object;

import java.io.Serializable;
import java.util.List;

public class RewardsSlotData implements Serializable {
    private List<RewardsSlotItemData> slots;
    private String venue;

    public List<RewardsSlotItemData> getSlots() {
        return slots;
    }

    public void setSlots(List<RewardsSlotItemData> slots) {
        this.slots = slots;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
