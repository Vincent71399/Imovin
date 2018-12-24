package sg.edu.nus.imovin.Retrofit.Request;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class UploadImageRequest {
    String image;
    public UploadImageRequest(String image){
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }
}
