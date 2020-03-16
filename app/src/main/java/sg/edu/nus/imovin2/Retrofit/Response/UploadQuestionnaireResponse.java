package sg.edu.nus.imovin2.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.ItemData;

public class UploadQuestionnaireResponse {
    private List<ItemData> _items;
    private String _status;

    public List<ItemData> get_items() {
        return _items;
    }

    public void set_items(List<ItemData> _items) {
        this._items = _items;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }
}
