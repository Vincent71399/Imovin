package sg.edu.nus.imovin.Objects;

public class Goal {
    private Integer steps;
    private Integer goal;
    private String date;
    private Boolean isShown = false;
    private Boolean isTitle = false;

    public Goal(Integer steps, Integer goal, String date) {
        this.steps = steps;
        this.goal = goal;
        this.date = date;
        this.isShown = true;
    }

    public Goal(Boolean isShown) {
        this.isShown = isShown;
    }

    public Goal(String date){
        this.date = date;
        isTitle = true;
    }

    public Integer getSteps() {
        return steps;
    }

    public Integer getGoal() {
        return goal;
    }

    public String getDate() {
        return date;
    }

    public Boolean getShown() {
        return isShown;
    }

    public Boolean getTitle() {
        return isTitle;
    }
}
