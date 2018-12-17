package sg.edu.nus.imovin.Retrofit.Object;

import java.util.List;

public class UserData {
    private String uid;
    private String name;
    private String email;
    private Integer profile;
    private List<Integer> motives;
    private Boolean fitbitAuthenticated;
    private String token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public List<Integer> getMotives() {
        return motives;
    }

    public void setMotives(List<Integer> motives) {
        this.motives = motives;
    }

    public Boolean getFitbitAuthenticated() {
        return fitbitAuthenticated;
    }

    public void setFitbitAuthenticated(Boolean fitbitAuthenticated) {
        this.fitbitAuthenticated = fitbitAuthenticated;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
