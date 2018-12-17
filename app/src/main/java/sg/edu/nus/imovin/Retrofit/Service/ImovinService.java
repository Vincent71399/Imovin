package sg.edu.nus.imovin.Retrofit.Service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.Url;
import sg.edu.nus.imovin.HttpConnection.ConnectionURL;
import sg.edu.nus.imovin.Retrofit.Request.AuthFitbitRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreateCommentRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreatePlanRequest;
import sg.edu.nus.imovin.Retrofit.Request.CreateThreadRequest;
import sg.edu.nus.imovin.Retrofit.Request.EmailLoginRequest;
import sg.edu.nus.imovin.Retrofit.Request.LikeCommentRequest;
import sg.edu.nus.imovin.Retrofit.Response.AuthFitbitResponse;
import sg.edu.nus.imovin.Retrofit.Response.CommentResponse;
import sg.edu.nus.imovin.Retrofit.Response.EmailLoginResponse;
import sg.edu.nus.imovin.Retrofit.Response.PlanMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.PlanResponse;
import sg.edu.nus.imovin.Retrofit.Response.StatisticsResponse;
import sg.edu.nus.imovin.Retrofit.Response.ThreadMultiResponse;
import sg.edu.nus.imovin.Retrofit.Response.ThreadResponse;

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

//    @Headers("content-type : application/json")
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

    @GET
    Call<PlanResponse> getPlan();

    @DELETE
    Call<PlanResponse> deletePlan();

}
