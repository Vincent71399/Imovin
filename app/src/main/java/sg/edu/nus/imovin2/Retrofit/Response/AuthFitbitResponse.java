package sg.edu.nus.imovin2.Retrofit.Response;

import sg.edu.nus.imovin2.Retrofit.Object.AuthFitbitData;

public class AuthFitbitResponse {
    private AuthFitbitData data;
    private String message;

    public AuthFitbitData getData() {
        return data;
    }

    public void setData(AuthFitbitData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
