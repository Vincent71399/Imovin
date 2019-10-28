package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.LikeData;

public class LikeResponse {
    private LikeData data;
    private String message;

    public LikeData getData() {
        return data;
    }

    public void setData(LikeData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
