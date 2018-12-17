package sg.edu.nus.imovin.Retrofit.Request;

public class AuthFitbitRequest {
    private String accessToken;
    private String refreshToken;
    private String accessSecret;
    private String userId;

    public AuthFitbitRequest(String accessToken, String refreshToken, String accessSecret, String userId) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessSecret = accessSecret;
        this.userId = userId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
