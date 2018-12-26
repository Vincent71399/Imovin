package sg.edu.nus.imovin.Activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Request.CreateSocialCommentRequest;
import sg.edu.nus.imovin.Retrofit.Response.SocialCommentResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class SocialNewCommentActivity extends ForumNewCommentActivity {
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_post:
                if(comment_input.getText().toString().equals("")){
                    Toast.makeText(this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    PostComment(new CreateSocialCommentRequest(thread_id, comment_input.getText().toString()));
                }
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void PostComment(CreateSocialCommentRequest createSocialCommentRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<SocialCommentResponse> call = service.createSocialComment(createSocialCommentRequest);

        call.enqueue(new Callback<SocialCommentResponse>() {
            @Override
            public void onResponse(Call<SocialCommentResponse> call, Response<SocialCommentResponse> response) {
                try {
                    SocialCommentResponse socialCommentResponse = response.body();
                    Log.d(LogConstants.LogTag, "SocialNewCommentActivity : " + socialCommentResponse.getMessage());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(IntentConstants.COMMENT_DATA, socialCommentResponse.getData());
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception SocialNewCommentActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialCommentResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure SocialNewCommentActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
