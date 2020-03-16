package sg.edu.nus.imovin2.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.ThreadData;

public class ThreadMultiResponse {
    private List<ThreadData> data;
    private Integer page;
    private String room;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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
