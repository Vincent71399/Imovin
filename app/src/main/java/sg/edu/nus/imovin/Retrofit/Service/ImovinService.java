package sg.edu.nus.imovin.Retrofit.Service;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import sg.edu.nus.imovin.HttpConnection.ConnectionURL;
import sg.edu.nus.imovin.Retrofit.Request.AuthFitbitRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreateCommentRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreatePlanRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreateSocialCommentRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreateSocialPostRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreateThreadRequest;
import sg.edu.nus.imovin.Retrofit.Request.EmailLoginRequest;
import sg.edu.nus.imovin.Retrofit.Request.LikeCommentRequest;
import sg.edu.nus.imovin.Retrofit.Request.LikeSocialCommentRequest;
import sg.edu.nus.imovin.Retrofit.Request.ResetPasswordRequest;
import sg.edu.nus.imovin.Retrofit.Request.UpdatePlanRequest;
import sg.edu.nus.imovin.Retrofit.Request.UploadImageRequest;
import sg.edu.nus.imovin.Retrofit.Request.UploadQuestionRequest;
import sg.edu.nus.imovin.Retrofit.Response.ArticleResponse;
import sg.edu.nus.imovin.Retrofit.Response.AuthFitbitResponse;
import sg.edu.nus.imovin.Retrofit.Response.ChallengeResponse;
import sg.edu.nus.imovin.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin.Retrofit.Response.CreateUpdatePlanResponse;
import sg.edu.nus.imovin.Retrofit.Response.EmailLoginResponse;
import sg.edu.nus.imovin.Retrofit.Response.MonitorDailySymmaryResponse;
import sg.edu.nus.imovin.Retrofit.Response.PlanMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.QuestionnaireResponse;
import sg.edu.nus.imovin.Retrofit.Response.ResetPasswordResponse;
import sg.edu.nus.imovin.Retrofit.Response.SelectDeletePlanResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialCommentResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialImageResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostResponse;
import sg.edu.nus.imovin.Retrofit.Response.ThreadMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.ThreadResponse;
import sg.edu.nus.imovin.Retrofit.Response.UploadConsentResponse;
import sg.edu.nus.imovin.Retrofit.Response.UploadImageResponse;
import sg.edu.nus.imovin.Retrofit.Response.UploadQuestionnaireResponse;
import sg.edu.nus.imovin.Retrofit.Response.UserInfoResponse;
import sg.edu.nus.imovin.Retrofit.Response.UserStatsResponse;

public interface ImovinService {
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

    //Forum
    @POST(ConnectionURL.REQUEST_CREATE_THREAD)
    Call<ThreadResponse> createThread(
            @Body CreateThreadRequest createThreadRequest
    );

    @GET
    Call<ThreadResponse> getThread(
            @Url String url
    );

    @GET(ConnectionURL.REQUEST_GET_ALL_THREADS)
    Call<ThreadMultiResponse> getAllThreads();

    @POST(ConnectionURL.REQUEST_CREATE_COMMENT)
    Call<CommentResponse> createComment(
            @Body CreateCommentRequest createCommentRequest
    );

    @PUT
    Call<CommentResponse> likeComment(
            @Url String url,
            @Body LikeCommentRequest likeCommentRequest
    );

    //Plans
    @GET(ConnectionURL.REQUEST_GET_ALL_PLANS)
    Call<PlanMultiResponse> getAllPlans();

    @POST(ConnectionURL.REQUEST_CREATE_PLAN)
    Call<CreateUpdatePlanResponse> createPlan(
            @Body CreatePlanRequest createPlanRequest
    );

    @PATCH
    Call<CreateUpdatePlanResponse> updatePlan(
            @Url String url,
            @Body UpdatePlanRequest updatePlanRequest
    );

    @POST
    Call<SelectDeletePlanResponse> selectPlan(
            @Url String url
    );

    @POST
    Call<SelectDeletePlanResponse> deletePlan(
            @Url String url
    );

    //Monitor
    @GET(ConnectionURL.REQUEST_GET_DAILY_SUMMARIES)
    Call<MonitorDailySymmaryResponse> getMonitorDailySummary(
            @Query("where") String where
    );

    //Social Feed
    @POST(ConnectionURL.REQUEST_CREATE_SOCIAL_POST)
    Call<SocialPostResponse> createSocialPost(
            @Body CreateSocialPostRequest createSocialPostRequest
    );

    @GET
    Call<SocialPostResponse> getSocialPost(
            @Url String url
    );

    @GET(ConnectionURL.REQUEST_GET_ALL_SOCIAL_POSTS)
    Call<SocialPostMultiResponse> getAllSocialPosts();

    @GET
    Call<SocialImageResponse> getSocialImage_by_Id(
            @Url String url
    );

    @POST(ConnectionURL.REQUEST_CREATE_SOCIAL_COMMENT)
    Call<SocialCommentResponse> createSocialComment(
            @Body CreateSocialCommentRequest createSocialCommentRequest
    );

    @PUT
    Call<CommentResponse> likeComment(
            @Url String url,
            @Body LikeSocialCommentRequest likeSocialCommentRequest
    );

    @POST(ConnectionURL.REQUEST_UPLOAD_IMAGE)
    Call<UploadImageResponse> uploadImage(
            @Body UploadImageRequest uploadImageRequest
    );

    //Challenge
    @GET(ConnectionURL.REQUEST_GET_USER_CHALLENGE)
    Call<ChallengeResponse> getChallenge();

    //Library
    @GET(ConnectionURL.REQUEST_GET_ARTICLES)
    Call<ArticleResponse> getArticles();
}
