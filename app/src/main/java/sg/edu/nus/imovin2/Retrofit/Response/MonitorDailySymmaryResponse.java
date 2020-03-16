package sg.edu.nus.imovin2.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.DailySummaryData;
import sg.edu.nus.imovin2.Retrofit.Object.MetaPageData;

public class MonitorDailySymmaryResponse {
    private List<DailySummaryData> _items;
    private MetaPageData _meta;

    public List<DailySummaryData> get_items() {
        return _items;
    }

    public void set_items(List<DailySummaryData> _items) {
        this._items = _items;
    }

    public MetaPageData get_meta() {
        return _meta;
    }

    public void set_meta(MetaPageData _meta) {
        this._meta = _meta;
    }
}
