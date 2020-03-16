package sg.edu.nus.imovin2.Retrofit.Request;

import java.util.List;

public class RewardsPostCheckoutRedemptionRequest {
    private String slot;
    private List<String> items;

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
