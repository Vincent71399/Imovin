package sg.edu.nus.imovin.Retrofit.Response;

import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class SocialPostResponse {
    private SocialFeedData data;
    private String message;

    public SocialFeedData getData() {
        return data;
    }

    public void setData(SocialFeedData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
