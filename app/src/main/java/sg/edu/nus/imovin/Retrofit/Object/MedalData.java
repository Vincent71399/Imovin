package sg.edu.nus.imovin.Retrofit.Object;

public class MedalData {
    private String _id;
    private String category;
    private String description;
    private String name;
    private Integer obtained_count;
    private Integer points;
    private Integer threshold;
    private String tier;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }
}
