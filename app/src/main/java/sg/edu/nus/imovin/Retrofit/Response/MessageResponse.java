package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.MessageData;

public class MessageResponse {
    private MessageData data;
    private String message;

    public MessageData getData() {
        return data;
    }

    public void setData(MessageData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
