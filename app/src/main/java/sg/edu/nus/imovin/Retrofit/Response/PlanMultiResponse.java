package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.PlanData;

public class PlanMultiResponse {
    private List<PlanData> data;
    private String message;

    public List<PlanData> getData() {
        return data;
    }

    public void setData(List<PlanData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
