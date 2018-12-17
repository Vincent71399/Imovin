package sg.edu.nus.imovin.Common;


import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class IntValueFormatter implements com.github.mikephil.charting.formatter.IValueFormatter {
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value);
    }
}
