package sg.edu.nus.imovin.Retrofit.Request;


public class CreateSocialPostRequest {
    private String message;

    public CreateSocialPostRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
