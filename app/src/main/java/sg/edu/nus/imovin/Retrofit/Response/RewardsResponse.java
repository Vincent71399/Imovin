package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.RewardsData;

public class RewardsResponse {
    private RewardsData data;
    private String message;

    public RewardsData getData() {
        return data;
    }

    public void setData(RewardsData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
