package sg.edu.nus.imovin.Retrofit.Object;

public class LoginData {

    private Boolean fitbitAuthenticated;
    private Boolean has_consented;
    private String token;
    private String uid;

    public Boolean getFitbitAuthenticated() {
        return fitbitAuthenticated;
    }

    public void setFitbitAuthenticated(Boolean fitbitAuthenticated) {
        this.fitbitAuthenticated = fitbitAuthenticated;
    }

    public Boolean getHas_consented() {
        return has_consented;
    }

    public void setHas_consented(Boolean has_consented) {
        this.has_consented = has_consented;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
