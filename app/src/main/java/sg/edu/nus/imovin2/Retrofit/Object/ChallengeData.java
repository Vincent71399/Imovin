package sg.edu.nus.imovin2.Retrofit.Object;

import java.util.List;

public class ChallengeData {
    private Integer challenge_points;
    private Integer challenge_rank;
    private List<MedalData> medals;
    private Integer steps;
    private Integer steps_rank;
    private Integer total_users;

    public Integer getChallenge_points() {
        return challenge_points;
    }

    public void setChallenge_points(Integer challenge_points) {
        this.challenge_points = challenge_points;
    }

    public Integer getChallenge_rank() {
        return challenge_rank;
    }

    public void setChallenge_rank(Integer challenge_rank) {
        this.challenge_rank = challenge_rank;
    }

    public List<MedalData> getMedals() {
        return medals;
    }

    public void setMedals(List<MedalData> medals) {
        this.medals = medals;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Integer getSteps_rank() {
        return steps_rank;
    }

    public void setSteps_rank(Integer steps_rank) {
        this.steps_rank = steps_rank;
    }

    public Integer getTotal_users() {
        return total_users;
    }

    public void setTotal_users(Integer total_users) {
        this.total_users = total_users;
    }
}
