package sg.edu.nus.imovin2.Retrofit.Request;

public class LikeRequest {
    private Boolean value;

    public LikeRequest(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
