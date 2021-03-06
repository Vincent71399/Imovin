package sg.edu.nus.imovin2.Retrofit.Service;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import sg.edu.nus.imovin2.HttpConnection.ConnectionURL;
import sg.edu.nus.imovin2.Retrofit.Request.AuthFitbitRequest;
import sg.edu.nus.imovin2.Retrofit.Request.CreateCommentRequest;
import sg.edu.nus.imovin2.Retrofit.Request.CreatePlanRequest;
import sg.edu.nus.imovin2.Retrofit.Request.CreateThreadRequest;
import sg.edu.nus.imovin2.Retrofit.Request.DailyLogRequest;
import sg.edu.nus.imovin2.Retrofit.Request.EmailLoginRequest;
import sg.edu.nus.imovin2.Retrofit.Request.LikeRequest;
import sg.edu.nus.imovin2.Retrofit.Request.ResetPasswordRequest;
import sg.edu.nus.imovin2.Retrofit.Request.RewardsPostCheckoutRedemptionRequest;
import sg.edu.nus.imovin2.Retrofit.Request.UpdatePlanRequest;
import sg.edu.nus.imovin2.Retrofit.Request.UploadQuestionRequest;
import sg.edu.nus.imovin2.Retrofit.Response.ArticleResponse;
import sg.edu.nus.imovin2.Retrofit.Response.AuthFitbitResponse;
import sg.edu.nus.imovin2.Retrofit.Response.ChallengeResponse;
import sg.edu.nus.imovin2.Retrofit.Response.CommentMultiResponse;
import sg.edu.nus.imovin2.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin2.Retrofit.Response.CommonMessageResponse;
import sg.edu.nus.imovin2.Retrofit.Response.DailyLogResponse;
import sg.edu.nus.imovin2.Retrofit.Response.EmailLoginResponse;
import sg.edu.nus.imovin2.Retrofit.Response.LikeResponse;
import sg.edu.nus.imovin2.Retrofit.Response.MessageResponse;
import sg.edu.nus.imovin2.Retrofit.Response.MonitorDailySymmaryResponse;
import sg.edu.nus.imovin2.Retrofit.Response.PlanMultiResponse;
import sg.edu.nus.imovin2.Retrofit.Response.QuestionnaireResponse;
import sg.edu.nus.imovin2.Retrofit.Response.ResetPasswordResponse;
import sg.edu.nus.imovin2.Retrofit.Response.RewardsResponse;
import sg.edu.nus.imovin2.Retrofit.Response.RewardsSlotsResponse;
import sg.edu.nus.imovin2.Retrofit.Response.SocialPostMultiResponse;
import sg.edu.nus.imovin2.Retrofit.Response.SocialPostResponse;
import sg.edu.nus.imovin2.Retrofit.Response.ThreadMultiResponse;
import sg.edu.nus.imovin2.Retrofit.Response.ThreadResponse;
import sg.edu.nus.imovin2.Retrofit.Response.UploadConsentResponse;
import sg.edu.nus.imovin2.Retrofit.Response.UploadQuestionnaireResponse;
import sg.edu.nus.imovin2.Retrofit.Response.UserInfoResponse;
import sg.edu.nus.imovin2.Retrofit.Response.UserStatsResponse;

public interface ImovinService {

    static final String WHERE = "where";
    static final String PAGE = "page";
    static final String MESSAGE = "message";

    //login
    @POST(ConnectionURL.REQUEST_EMAIL_LOGIN)
    Call<EmailLoginResponse> emailLogin(
            @Body EmailLoginRequest emailLoginRequest
    );

    @POST(ConnectionURL.REQUEST_AUTH_FITBIT)
    Call<AuthFitbitResponse> authFitbit(
            @Body AuthFitbitRequest authFitbitRequest
    );

    @POST(ConnectionURL.REQUEST_RESET_PASSWORD)
    Call<ResetPasswordResponse> resetPassword(
            @Body ResetPasswordRequest resetPasswordRequest
    );

    @GET(ConnectionURL.REQUEST_GET_USER_INFO)
    Call<UserInfoResponse> getUserInfo();

    @Multipart
    @PATCH(ConnectionURL.REQUEST_UPLOAD_USER_CONSENT_SIGNATURE)
    Call<UploadConsentResponse> uploadConsent(
            @Part MultipartBody.Part filename
    );

    @GET(ConnectionURL.REQUEST_GET_QUESTIONNAIRE)
    Call<QuestionnaireResponse> getQuestionnaire();

    @POST(ConnectionURL.REQUEST_UPLOAD_QUESTIONNAIRE)
    Call<UploadQuestionnaireResponse> uploadQuestionnaire(
            @Body UploadQuestionRequest uploadQuestionRequest
    );

    //home page
    @GET(ConnectionURL.REQUEST_GET_USER_STATS_OVERVIEW)
    Call<UserStatsResponse> getUserStatsOverview();

    //Plans
    @GET(ConnectionURL.REQUEST_GET_ALL_PLANS)
    Call<PlanMultiResponse> getAllPlans();

    @POST(ConnectionURL.REQUEST_CREATE_PLAN)
    Call<CommonMessageResponse> createPlan(
            @Body CreatePlanRequest createPlanRequest
    );

    @PATCH
    Call<CommonMessageResponse> updatePlan(
            @Url String url,
            @Body UpdatePlanRequest updatePlanRequest
    );

    @POST
    Call<CommonMessageResponse> selectPlan(
            @Url String url
    );

    @POST
    Call<CommonMessageResponse> deletePlan(
            @Url String url
    );

    //Monitor
    @GET(ConnectionURL.REQUEST_GET_DAILY_SUMMARIES)
    Call<MonitorDailySymmaryResponse> getMonitorDailySummary(
            @Query(WHERE) String where
    );

    //Forum
    @POST(ConnectionURL.REQUEST_CREATE_THREAD)
    Call<ThreadResponse> createThread(
            @Body CreateThreadRequest createThreadRequest
    );

    @PATCH
    Call<ThreadResponse> editThread(
            @Url String url,
            @Body CreateThreadRequest createThreadRequest
    );

    @DELETE
    Call<CommonMessageResponse> deleteThread(
            @Url String url
    );

    @GET
    Call<ThreadResponse> getThread(
            @Url String url
    );

    @GET(ConnectionURL.REQUEST_GET_ALL_THREADS)
    Call<ThreadMultiResponse> getAllThreads(
            @Query(PAGE) Integer page
    );

    @POST
    Call<LikeResponse> likeThread(
            @Url String url,
            @Body LikeRequest createThreadRequest
    );

    //comment
    @GET
    Call<CommentMultiResponse> getAllComment(
            @Url String url
    );

    @POST
    Call<CommentResponse> createComment(
            @Url String url,
            @Body CreateCommentRequest createCommentRequest
    );

    @POST
    Call<LikeResponse> likeComment(
            @Url String url,
            @Body LikeRequest likeRequest
    );

    @PATCH
    Call<CommentResponse> editComment(
            @Url String url,
            @Body CreateCommentRequest createCommentRequest
    );

    @DELETE
    Call<CommonMessageResponse> deleteComment(
            @Url String url
    );

    //Social Feed
    @Multipart
    @POST(ConnectionURL.REQUEST_CREATE_SOCIAL_POST)
    Call<SocialPostResponse> createSocialPost(
            @Part(MESSAGE) RequestBody requestBody,
            @Part MultipartBody.Part filename
    );

    @GET(ConnectionURL.REQUEST_GET_ALL_SOCIAL_POSTS)
    Call<SocialPostMultiResponse> getAllSocialPosts(
            @Query(PAGE) Integer page
    );

    @Multipart
    @PATCH
    Call<SocialPostResponse> editSocialPost(
            @Url String url,
            @Part(MESSAGE) RequestBody requestBody,
            @Part MultipartBody.Part filename
    );

    @DELETE
    Call<CommonMessageResponse> deleteSocialPost(
            @Url String url
    );

    @GET
    Call<SocialPostResponse> getSocialPost(
            @Url String url
    );

    @POST
    Call<LikeResponse> likeSocialPost(
            @Url String url,
            @Body LikeRequest createThreadRequest
    );

    //Challenge
    @GET(ConnectionURL.REQUEST_GET_USER_CHALLENGE)
    Call<ChallengeResponse> getChallenge();

    //Library
    @GET(ConnectionURL.REQUEST_GET_ARTICLES)
    Call<ArticleResponse> getArticles();

    //Message
    @GET(ConnectionURL.REQUEST_GET_MESSAGE)
    Call<MessageResponse> getMessage();

    //Rewards
    @GET(ConnectionURL.REQUEST_GET_REWARDS)
    Call<RewardsResponse> getRewards();

    @GET(ConnectionURL.REQUEST_GET_REWARDS_COLLECTION_SLOTS)
    Call<RewardsSlotsResponse> getRewardsSlots();

    @POST(ConnectionURL.REQUEST_POST_CHECKOUT_REWARDS_REDEMPTION)
    Call<CommonMessageResponse> postRewardsCheckoutRedemption(
        @Body RewardsPostCheckoutRedemptionRequest rewardsPostCheckoutRedemptionRequest
    );

    //Log
    @POST(ConnectionURL.REQUEST_POST_DAILY_LOG)
    Call<DailyLogResponse> postDailyLog(
            @Body DailyLogRequest dailyLogRequest
    );


}
