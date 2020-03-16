package sg.edu.nus.imovin2.Retrofit.Response;

import sg.edu.nus.imovin2.Retrofit.Object.ChallengeData;

public class ChallengeResponse {
    private ChallengeData data;
    private String message;

    public ChallengeData getData() {
        return data;
    }

    public void setData(ChallengeData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
