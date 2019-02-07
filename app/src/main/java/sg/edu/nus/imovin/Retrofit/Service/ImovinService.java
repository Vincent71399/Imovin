package sg.edu.nus.imovin.Retrofit.Service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
import sg.edu.nus.imovin.Retrofit.Request.UploadImageRequest;
import sg.edu.nus.imovin.Retrofit.Response.AuthFitbitResponse;
import sg.edu.nus.imovin.Retrofit.Response.ChallengeResponse;
import sg.edu.nus.imovin.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin.Retrofit.Response.EmailLoginResponse;
import sg.edu.nus.imovin.Retrofit.Response.LessonResponse;
import sg.edu.nus.imovin.Retrofit.Response.PlanMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.PlanResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialCommentResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostResponse;
import sg.edu.nus.imovin.Retrofit.Response.StatisticsResponse;
import sg.edu.nus.imovin.Retrofit.Response.ThreadMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.ThreadResponse;
import sg.edu.nus.imovin.Retrofit.Response.UploadImageResponse;

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

    //home page
    @GET(ConnectionURL.REQUEST_GET_STATISTICS)
    Call<StatisticsResponse> getStatistics(
            @Query(ConnectionURL.PARAMETER_DAYS) int days
    );

    @GET(ConnectionURL.REQUEST_UPDATE_STATISTICS)
    Call<StatisticsResponse> updateStatistics();

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
    Call<PlanResponse> createPlan(
            @Body CreatePlanRequest createPlanRequest
    );

    @PUT
    Call<PlanResponse> selectPlan(
            @Url String url
    );

    @GET
    Call<PlanResponse> getPlan(
            @Url String url
    );

    @DELETE
    Call<PlanResponse> deletePlan(
            @Url String url
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
    @GET(ConnectionURL.REQUEST_UPDATE_CHALLENGE)
    Call<ChallengeResponse> getChallenge();


    //Library
    @GET(ConnectionURL.REQUEST_GET_LESSON)
    Call<LessonResponse> getLesson();
}
