package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class SocialPostMultiResponse {
    private List<SocialFeedData> data;
    private String message;

    public List<SocialFeedData> getData() {
        return data;
    }

    public void setData(List<SocialFeedData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
