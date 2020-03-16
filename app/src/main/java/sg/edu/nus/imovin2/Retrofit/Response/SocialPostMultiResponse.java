package sg.edu.nus.imovin2.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.SocialFeedData;

/**
 * Created by wcafricanus on 19/12/18.
 */

public class SocialPostMultiResponse {
    private List<SocialFeedData> data;
    private Integer page;
    private Integer total;
    private String message;

    public List<SocialFeedData> getData() {
        return data;
    }

    public void setData(List<SocialFeedData> data) {
        this.data = data;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
