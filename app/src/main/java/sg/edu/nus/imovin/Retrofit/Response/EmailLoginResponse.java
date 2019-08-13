package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.LoginData;


public class EmailLoginResponse {
    private LoginData data;
    private String message;

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
