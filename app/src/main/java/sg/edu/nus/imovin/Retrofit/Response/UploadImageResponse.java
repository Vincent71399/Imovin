package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.ImageUrlData;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class UploadImageResponse {
    private ImageUrlData data;
    private String message;

    public ImageUrlData getData() {
        return data;
    }

    public void setData(ImageUrlData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
