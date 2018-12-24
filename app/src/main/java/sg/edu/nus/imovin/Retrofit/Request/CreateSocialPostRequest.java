package sg.edu.nus.imovin.Retrofit.Request;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class CreateSocialPostRequest {
    private String message;
    private String imageUrl;

    public CreateSocialPostRequest(String message, String imageUrl) {
        this.message = message;
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
