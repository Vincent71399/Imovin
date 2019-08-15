package sg.edu.nus.imovin.Retrofit.Response;

import java.util.List;

import sg.edu.nus.imovin.Retrofit.Object.DailySummaryData;

public class UserStatsResponse {
    private String _id;
    private String created_at;
    private List<DailySummaryData> daily_summaries;
    private String email;
    private Boolean fitbitAuthenticated;
}
