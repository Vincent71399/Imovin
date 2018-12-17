package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.UserData;

public class EmailLoginResponse {
    private UserData data;
    private String message;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
