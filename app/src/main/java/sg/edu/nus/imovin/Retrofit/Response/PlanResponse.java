package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.PlanData;

public class PlanResponse {
    private PlanData data;
    private String message;

    public PlanData getData() {
        return data;
    }

    public void setData(PlanData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
