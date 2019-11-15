package sg.edu.nus.imovin.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.Common.ImagePicker;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin.Retrofit.Request.CreateSocialPostRequest;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_GET_SOCIAL_POST_IMAGE;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_SOCIAL_POST_WITH_ID;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class SocialNewPostActivity extends BaseActivity implements View.OnClickListener {

    private View customActionBar;

    @BindView(R.id.navigator_middle_title) TextView navigator_middle_title;
    @BindView(R.id.navigator_left) LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image) ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;
    @BindView(R.id.navigator_right) RelativeLayout navigator_right;
    @BindView(R.id.navigator_right_text) TextView navigator_right_text;
    @BindView(R.id.socialPostMessageInput) EditText message_input;
    @BindView(R.id.socialPostUploadImage) ImageView image_input;
    @BindView(R.id.uploadingText) TextView uploadingText;

    private File imageFile;
    private SocialFeedData socialFeedData;
    private boolean isNewPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_new_post);

        SetActionBar();
        LinkUIbyId();
        SetFunction();
        SetupData();
    }

    private void SetActionBar(){
        ActionBar actionBar = getSupportActionBar();
        customActionBar = getLayoutInflater().inflate(R.layout.main_navigator, null);

        if(actionBar != null){
            actionBar.show();
            actionBar.setCustomView(customActionBar);
            actionBar.setDisplayShowCustomEnabled(true);
        }

        Toolbar parent =(Toolbar) customActionBar.getParent();
        parent.setPadding(0,0,0,0);
        parent.setContentInsetsAbsolute(0, 0);
    }

    private void LinkUIbyId(){
        ButterKnife.bind(this);
    }

    private void SetFunction(){
        navigator_left_text.setText(getString(R.string.social_feed));
        navigator_left_text.setVisibility(View.VISIBLE);
        navigator_left_image.setVisibility(View.VISIBLE);

        navigator_middle_title.setText(getString(R.string.new_post));

        navigator_right_text.setText(getString(R.string.post));
        navigator_right_text.setVisibility(View.VISIBLE);

        navigator_left.setOnClickListener(this);
        navigator_right.setOnClickListener(this);
        image_input.setOnClickListener(this);
    }

    private void SetupData(){
        socialFeedData = (SocialFeedData) getIntent().getSerializableExtra(IntentConstants.SOCIAL_POST_DATA);
        if(socialFeedData == null){
            isNewPost = true;
        }else{
            isNewPost = false;
            message_input.setText(socialFeedData.getMessage());
            if(socialFeedData.getImage() != null) {
                String imageUrl = SERVER + String.format(
                        Locale.ENGLISH,REQUEST_GET_SOCIAL_POST_IMAGE, socialFeedData.get_id());
                ImageLoader.getInstance().displayImage(imageUrl, image_input);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navigator_left:
                finish();
                break;
            case R.id.navigator_right:
                String message = message_input.getText().toString();
                if(message.equals("")){
                    Toast.makeText(this, "Title and message cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(isNewPost) {
                        PostNewSocialFeed(new CreateSocialPostRequest(message));
                    }else{
                        EditSocialPost(socialFeedData.get_id(), new CreateSocialPostRequest(message));
                    }
                }
                break;
            case R.id.socialPostUploadImage:
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, IntentConstants.SELECT_PICTURE);
        }
    }

    private void PostNewSocialFeed(CreateSocialPostRequest createSocialPost){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("plain"), createSocialPost.getMessage());
        MultipartBody.Part imagePart = null;
        if(imageFile != null) {
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("form-data"), imageFile);
            imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), fileReqBody);
        }

        Call<SocialPostResponse> call = service.createSocialPost(requestBody, imagePart);

        call.enqueue(new Callback<SocialPostResponse>() {
            @Override
            public void onResponse(Call<SocialPostResponse> call, Response<SocialPostResponse> response) {
                try {
                    SocialPostResponse socialPostResponse = response.body();
                    Log.d(LogConstants.LogTag, "SocialNewSocialFeedActivity : " + socialPostResponse.getMessage());
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception SocialNewSocialFeedActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialPostResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure SocialNewSocialFeedActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EditSocialPost(String post_id, CreateSocialPostRequest createSocialPost){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_SOCIAL_POST_WITH_ID, post_id);

        RequestBody requestBody = RequestBody.create(MediaType.parse("plain"), createSocialPost.getMessage());
        MultipartBody.Part imagePart = null;
        if(imageFile != null) {
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("form-data"), imageFile);
            imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), fileReqBody);
        }

        Call<SocialPostResponse> call = service.editSocialPost(url, requestBody, imagePart);

        call.enqueue(new Callback<SocialPostResponse>() {
            @Override
            public void onResponse(Call<SocialPostResponse> call, Response<SocialPostResponse> response) {
                try {
                    SocialPostResponse socialPostResponse = response.body();
                    Log.d(LogConstants.LogTag, "SocialNewSocialFeedActivity : " + socialPostResponse.getMessage());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(IntentConstants.SOCIAL_POST_DATA, socialPostResponse.getData());
                    setResult(RESULT_OK, resultIntent);
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception ForumNewPostActivity : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialPostResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure ForumNewPostActivity : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case IntentConstants.SELECT_PICTURE:
                if(resultCode == RESULT_OK) {
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                    image_input.setImageBitmap(bitmap);
                    String storeLocation = CommonFunc.saveToInternalStorage(getApplicationContext(), bitmap);
                    imageFile = CommonFunc.loadImageFromStorage(storeLocation);
                }
                break;
        }
    }

}
