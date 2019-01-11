package sg.edu.nus.imovin.Retrofit.Object;

import java.util.List;

public class ChallengeData {
    private Challenge challenges;
    private Integer challegePoints;
    private Integer rank;
    private Integer numberOfChallengers;

    public Challenge getChallenges() {
        return challenges;
    }

    public void setChallenges(Challenge challenges) {
        this.challenges = challenges;
    }

    public Integer getChallengePoints() {
        return challegePoints;
    }

    public void setChallengePoints(Integer challengePoints) {
        this.challegePoints = challengePoints;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getNumberOfChallenges() {
        return numberOfChallengers;
    }

    public void setNumberOfChallenges(Integer numberOfChallenges) {
        this.numberOfChallengers = numberOfChallenges;
    }

}
