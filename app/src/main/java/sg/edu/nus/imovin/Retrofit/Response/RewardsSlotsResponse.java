package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.RewardsSlotData;

public class RewardsSlotsResponse {
    private RewardsSlotData data;
    private String message;

    public RewardsSlotData getData() {
        return data;
    }

    public void setData(RewardsSlotData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
