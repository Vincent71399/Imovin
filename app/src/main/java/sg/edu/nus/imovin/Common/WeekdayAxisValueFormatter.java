package sg.edu.nus.imovin.Common;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by wcafricanus on 12/12/18.
 */

public class WeekdayAxisValueFormatter implements IAxisValueFormatter {

    private final ArrayList<String> weekdayList;

    public WeekdayAxisValueFormatter(ArrayList<String> valueList){
        this.weekdayList = valueList;
    }
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int intValue = (int) value;
        return this.weekdayList.get(intValue);
    }
}
