package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.ThreadData;

public class ThreadMultiResponse {
    private List<ThreadData> data;
    private String message;

    public List<ThreadData> getData() {
        return data;
    }

    public void setData(List<ThreadData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
