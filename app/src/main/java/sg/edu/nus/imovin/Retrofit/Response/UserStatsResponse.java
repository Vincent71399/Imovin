package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.DailySummaryData;


public class UserStatsResponse {
    private String _id;
    private String created_at;
    private List<DailySummaryData> daily_summaries;
    private String email;
    private Boolean fitbitAuthenticated;
    private List<Integer> motives;
    private String name;
    private Integer profile;
    private List<Integer> primary_features;
    private Integer riskLapse;
    private Integer target;
    private String updated_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<DailySummaryData> getDaily_summaries() {
        return daily_summaries;
    }

    public void setDaily_summaries(List<DailySummaryData> daily_summaries) {
        this.daily_summaries = daily_summaries;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFitbitAuthenticated() {
        return fitbitAuthenticated;
    }

    public void setFitbitAuthenticated(Boolean fitbitAuthenticated) {
        this.fitbitAuthenticated = fitbitAuthenticated;
    }

    public List<Integer> getMotives() {
        return motives;
    }

    public void setMotives(List<Integer> motives) {
        this.motives = motives;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProfile() {
        return profile;
    }

    public void setProfile(Integer profile) {
        this.profile = profile;
    }

    public List<Integer> getPrimary_features() {
        return primary_features;
    }

    public void setPrimary_features(List<Integer> primary_features) {
        this.primary_features = primary_features;
    }

    public Integer getRiskLapse() {
        return riskLapse;
    }

    public void setRiskLapse(Integer riskLapse) {
        this.riskLapse = riskLapse;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
