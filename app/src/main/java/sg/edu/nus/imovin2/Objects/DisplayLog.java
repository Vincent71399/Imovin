package sg.edu.nus.imovin2.Objects;

import java.io.Serializable;
import java.util.List;

import sg.edu.nus.imovin2.Retrofit.Object.ActivityLogData;

public class DisplayLog implements Serializable {
    List<ActivityLogData> activityLogDataList;

    public DisplayLog(List<ActivityLogData> activityLogDataList) {
        this.activityLogDataList = activityLogDataList;
    }

    public List<ActivityLogData> getActivityLogDataList() {
        return activityLogDataList;
    }
}
