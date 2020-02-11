package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.Challenge;

public class UserInfoResponse {
    private Integer __v;
    private String _id;
    private Boolean active;
    private Challenge challenges;
    private String createdAt;
    private String created_at;
    private String email;
    private String etag;
    private String filename;
    private Boolean fitbitAuthenticated;
    private String fitbitId;
    private Boolean has_consented;
    private String name;
    private String password;
    private Integer profile;
    private List<Integer> primary_features;
    private Integer riskLapse;
    private List<String> roles;
    private String signature;
    private List<String> statistics;
    private String updated_at;

    public Integer get__v() {
        return __v;
    }

    public void set__v(Integer __v) {
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Challenge getChallenges() {
        return challenges;
    }

    public void setChallenges(Challenge challenges) {
        this.challenges = challenges;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Boolean getFitbitAuthenticated() {
        return fitbitAuthenticated;
    }

    public void setFitbitAuthenticated(Boolean fitbitAuthenticated) {
        this.fitbitAuthenticated = fitbitAuthenticated;
    }

    public String getFitbitId() {
        return fitbitId;
    }

    public void setFitbitId(String fitbitId) {
        this.fitbitId = fitbitId;
    }

    public Boolean getHas_consented() {
        return has_consented;
    }

    public void setHas_consented(Boolean has_consented) {
        this.has_consented = has_consented;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<String> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<String> statistics) {
        this.statistics = statistics;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
