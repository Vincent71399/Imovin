package sg.edu.nus.imovin2.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.MetaPageData;
import sg.edu.nus.imovin2.Retrofit.Object.PlanData;

public class PlanMultiResponse {
    private List<PlanData> _items;
    private MetaPageData _meta;

    public List<PlanData> get_items() {
        return _items;
    }

    public void set_items(List<PlanData> _items) {
        this._items = _items;
    }

    public MetaPageData get_meta() {
        return _meta;
    }

    public void set_meta(MetaPageData _meta) {
        this._meta = _meta;
    }
}
