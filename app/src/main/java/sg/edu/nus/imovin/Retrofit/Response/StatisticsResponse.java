package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.StatisticsData;

public class StatisticsResponse {
    private List<StatisticsData> data;
    private String message;

    public List<StatisticsData> getData() {
        return data;
    }

    public void setData(List<StatisticsData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
