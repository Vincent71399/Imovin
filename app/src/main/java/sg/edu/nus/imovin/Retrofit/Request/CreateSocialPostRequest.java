package sg.edu.nus.imovin.Retrofit.Request;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class CreateSocialPostRequest {
    private String message;
    private String imageString;

    public CreateSocialPostRequest(String message, String imageString) {
        this.message = message;
        this.imageString = imageString;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString){
        this.imageString = imageString;
    }

}
