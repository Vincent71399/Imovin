package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.MedalData;
import sg.edu.nus.imovin.Retrofit.Object.SeasonData;

public class ChallengeResponse {
    private String _id;
    private String created_at;
    private List<MedalData> medals;
    private Integer points;
    private Integer rank;
    private Integer total;
    private SeasonData season;

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

    public List<MedalData> getMedals() {
        return medals;
    }

    public void setMedals(List<MedalData> medals) {
        this.medals = medals;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public SeasonData getSeason() {
        return season;
    }

    public void setSeason(SeasonData season) {
        this.season = season;
    }
}
