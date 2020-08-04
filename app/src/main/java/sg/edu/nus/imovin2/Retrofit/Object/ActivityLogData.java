package sg.edu.nus.imovin2.Retrofit.Object;

import java.io.Serializable;

public class ActivityLogData implements Serializable {
    private String date;
    private String name;
    private Integer average_hr;
    private Integer calories;
    private Float distance;
    private Integer duration;
    private Integer steps;
    private Boolean display_title = true;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAverage_hr() {
        return average_hr;
    }

    public void setAverage_hr(Integer average_hr) {
        this.average_hr = average_hr;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Boolean getDisplay_title() {
        return display_title;
    }

    public void setDisplay_title(Boolean display_title) {
        this.display_title = display_title;
    }
}
