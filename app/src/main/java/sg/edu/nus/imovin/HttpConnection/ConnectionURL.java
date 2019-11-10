package sg.edu.nus.imovin.HttpConnection;

public class ConnectionURL {
//    public static final String SERVER = "http://3.0.200.147";
    public static final String SERVER = "http://18.139.138.129";
//    public static final String SERVER = "http://172.25.122.136:5000";

    public static final String REQUEST_EMAIL_LOGIN = "/api/auth/email-login";
    public static final String REQUEST_AUTH_FITBIT = "/api/user/auth-fitbit";
    public static final String REQUEST_GET_STATISTICS = "/api/user/stats";

    //below is in imovin-flask
    public static final String REQUEST_RESET_PASSWORD = "/security/reset";
    public static final String REQUEST_GET_USER_INFO = "/api/users/me";
    public static final String REQUEST_GET_QUESTIONNAIRE = "/api/questionnaire/me";
    public static final String REQUEST_UPLOAD_USER_CONSENT_SIGNATURE = "/api/signature/me";
    public static final String REQUEST_UPLOAD_QUESTIONNAIRE = "/api/questionnaire-answer";
    public static final String REQUEST_GET_ARTICLES = "/api/article";
    public static final String REQUEST_GET_USER_STATS_OVERVIEW = "/api/overview/me";
    public static final String REQUEST_GET_USER_CHALLENGE = "/api/challenge/me";
    public static final String REQUEST_GET_DAILY_SUMMARIES = "/api/daily-summaries";

    public static final String REQUEST_GET_ALL_PLANS = "/api/plans";
    public static final String REQUEST_CREATE_PLAN = "/api/plans";
    public static final String REQUEST_UPDATE_PLAN = "/api/plans/%s";
    public static final String REQUEST_DELETE_PLAN = "/api/plans/delete/%s";
    public static final String REQUEST_SELECT_PLAN = "/api/plans/select/%s";

    public static final String REQUEST_CREATE_THREAD = "/api/forum/thread";
    public static final String REQUEST_THREAD_WITH_ID = "/api/forum/thread/%s";
    public static final String REQUEST_GET_ALL_THREADS = "/api/forum/threads";
    public static final String REQUEST_LIKE_THREAD = "/api/forum/thread/%s/like";
    public static final String REQUEST_GET_THREAD_COMMENT = "/api/forum/thread/%s/comments";
    public static final String REQUEST_CREATE_COMMENT = "/api/forum/thread/%s/comment";
    public static final String REQUEST_LIKE_COMMENT = "/api/forum/thread/comment/%s/like";
    public static final String REQUEST_COMMENT_WITH_ID = "/api/forum/comment/%s";

    public static final String REQUEST_CREATE_SOCIAL_POST = "/api/social/post";
    public static final String REQUEST_UPLOAD_IMAGE = "/api/social/post/upload-image";
    public static final String REQUEST_GET_ALL_SOCIAL_POSTS = "/api/social/posts";
    public static final String REQUEST_GET_SOCIAL_POST_IMAGE = "/api/social/post/%s/image";
    public static final String REQUEST_CREATE_SOCIAL_COMMENT = "/api/social/post/comment";
    public static final String REQUEST_LIKE_SOCIAL_COMMENT = "/api/social/post/comment/%s/like";

    public static final String REQUEST_POST_DAILY_LOG = "/api/daily-log";

    public static final String PARAMETER_DAYS = "days";
    public static final String PARAMETER_PASSWORD = "password";

    public static final String PARAMETER_DAILY_SUMMARY = "{\"$and\": [{\"date\":{\"$gte\":\"%s\"}}, {\"date\":{\"$lte\":\"%s\"}}]}";
}
