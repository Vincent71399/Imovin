package sg.edu.nus.imovin2.Retrofit.Object;

import java.io.Serializable;

public class MedalData implements Serializable {
    private String _id;
    private Integer category;
    private String description;
    private String name;
    private Integer obtained_count;
    private Integer points;
    private String last_achieved_at;
    private Integer threshold;
    private Integer tier;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getObtained_count() {
        return obtained_count;
    }

    public void setObtained_count(Integer obtained_count) {
        this.obtained_count = obtained_count;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getLast_achieved_at() {
        return last_achieved_at;
    }

    public void setLast_achieved_at(String last_achieved_at) {
        this.last_achieved_at = last_achieved_at;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }
}
