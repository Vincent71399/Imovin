package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.ThreadData;

public class ThreadMultiResponse {
    private List<ThreadData> data;
    private Integer page;
    private Integer total;
    private String message;

    public List<ThreadData> getData() {
        return data;
    }

    public void setData(List<ThreadData> data) {
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
