package sg.edu.nus.imovin2.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.RewardsSlotData;

public class RewardsSlotsResponse {
    private List<RewardsSlotData> data;
    private String message;

    public List<RewardsSlotData> getData() {
        return data;
    }

    public void setData(List<RewardsSlotData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
