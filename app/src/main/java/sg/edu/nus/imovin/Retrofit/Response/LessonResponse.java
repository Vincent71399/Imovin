package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

public class LessonResponse {
    private List<List<Object>> data;
    private String message;

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
