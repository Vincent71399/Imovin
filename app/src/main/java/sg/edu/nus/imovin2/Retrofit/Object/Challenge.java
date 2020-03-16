package sg.edu.nus.imovin2.Retrofit.Object;

import java.util.List;

public class Challenge {
    private List<List<Integer>> dailySteps;
    private List<List<Integer>> dailyDistance;
    private List<List<Integer>> stepsTarget;
    private List<List<Integer>> activeDays;
    private List<List<Integer>> weeklyEx;

    public List<List<Integer>> getDailySteps() {
        return dailySteps;
    }

    public void setDailySteps(List<List<Integer>> dailySteps) {
        this.dailySteps = dailySteps;
    }

    public List<List<Integer>> getDailyDistance() {
        return dailyDistance;
    }

    public void setDailyDistance(List<List<Integer>> dailyDistance) {
        this.dailyDistance = dailyDistance;
    }

    public List<List<Integer>> getStepsTarget() {
        return stepsTarget;
    }

    public void setStepsTarget(List<List<Integer>> stepsTarget) {
        this.stepsTarget = stepsTarget;
    }

    public List<List<Integer>> getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(List<List<Integer>> activeDays) {
        this.activeDays = activeDays;
    }

    public List<List<Integer>> getWeeklyEx() {
        return weeklyEx;
    }

    public void setWeeklyEx(List<List<Integer>> weeklyEx) {
        this.weeklyEx = weeklyEx;
    }
}
