package sg.edu.nus.imovin2.Retrofit.Object;

import java.io.Serializable;

public class RewardsPointHistoryData implements Serializable {
    private String date;
    private Integer points;
    private Integer steps;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }
}
