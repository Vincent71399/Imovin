package sg.edu.nus.imovin.Retrofit.Request;

public class UpdatePlanRequest {
    private Integer target;

    public UpdatePlanRequest(Integer target) {
        this.target = target;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
