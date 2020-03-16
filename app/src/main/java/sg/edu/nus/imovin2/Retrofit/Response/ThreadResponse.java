package sg.edu.nus.imovin2.Retrofit.Response;

import sg.edu.nus.imovin2.Retrofit.Object.ThreadData;

public class ThreadResponse {
    private ThreadData data;
    private String message;

    public ThreadData getData() {
        return data;
    }

    public void setData(ThreadData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
