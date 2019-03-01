package sg.edu.nus.imovin.Retrofit.Request;

public class UpdatePlanRequest {
    private CreatePlanRequest updatePayload;

    public UpdatePlanRequest(String name, Integer target) {
        this.updatePayload = new CreatePlanRequest(name, target);
    }

    public CreatePlanRequest getUpdatePayload() {
        return updatePayload;
    }

    public void setUpdatePayload(CreatePlanRequest updatePayload) {
        this.updatePayload = updatePayload;
    }
}
