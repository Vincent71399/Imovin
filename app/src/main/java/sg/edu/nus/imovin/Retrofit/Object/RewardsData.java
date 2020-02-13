package sg.edu.nus.imovin.Retrofit.Object;

import java.util.List;

public class RewardsData {
    private List<RewardsAvailableItemData> available_items;
    private Integer points;
    private List<RewardsPointHistoryData> points_history;

    public List<RewardsAvailableItemData> getAvailable_items() {
        return available_items;
    }

    public void setAvailable_items(List<RewardsAvailableItemData> available_items) {
        this.available_items = available_items;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public List<RewardsPointHistoryData> getPoints_history() {
        return points_history;
    }

    public void setPoints_history(List<RewardsPointHistoryData> points_history) {
        this.points_history = points_history;
    }
}
