package sg.edu.nus.imovin2.Retrofit.Request;

public class UpdatePlanRequest {
    private String name;
    private Integer target;

    public UpdatePlanRequest(String name, Integer target) {
        this.name = name;
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
