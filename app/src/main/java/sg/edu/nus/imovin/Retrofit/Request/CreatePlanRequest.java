package sg.edu.nus.imovin.Retrofit.Request;

public class CreatePlanRequest {
    private String name;
    private String target;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
