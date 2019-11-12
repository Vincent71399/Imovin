package sg.edu.nus.imovin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.Common.ImagePicker;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Request.CreateSocialPostRequest;
import sg.edu.nus.imovin.Retrofit.Request.UploadImageRequest;
import sg.edu.nus.imovin.Retrofit.Response.SocialPostResponse;
import sg.edu.nus.imovin.Retrofit.Response.UploadImageResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.BaseActivity;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.IntentConstants;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class SocialNewPostActivity extends BaseActivity implements View.OnClickListener {

    private View customActionBar;
    ProgressDialog dialog = null;
    String uploadFileName = "";
    String imageString = "";

    @BindView(R.id.navigator_middle_title)
    TextView navigator_middle_title;

    @BindView(R.id.navigator_left)
    LinearLayout navigator_left;
    @BindView(R.id.navigator_left_image)
    ImageView navigator_left_image;
    @BindView(R.id.navigator_left_text) TextView navigator_left_text;

    @BindView(R.id.navigator_right)
    RelativeLayout navigator_right;
    @BindView(R.id.navigator_right_text) TextView navigator_right_text;

    @BindView(R.id.socialPostMessageInput)
    EditText message_input;
    @BindView(R.id.socialPostUploadImage) ImageView image_input;
    @BindView(R.id.uploadingText) TextView uploadingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_new_post);

        SetActionBar();
        LinkUIbyId();
        SetFunction();
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
                    PostNewSocialFeed(new CreateSocialPostRequest(message, imageString));
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

        Call<SocialPostResponse> call = service.createSocialPost(createSocialPost);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case IntentConstants.SELECT_PICTURE:
                if(resultCode == RESULT_OK) {
                    Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);

                    image_input.setImageBitmap(bitmap);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    final byte[] bytes = byteArrayOutputStream.toByteArray();
                    imageString = new String(Base64.encode(bytes, 0), StandardCharsets.UTF_8);
                }
                break;
        }
    }

    private void postImage(UploadImageRequest uploadImageRequest){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        Call<UploadImageResponse> call = service.uploadImage(uploadImageRequest);

        call.enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                try {
                    UploadImageResponse commentResponse = response.body();
                    Log.d(LogConstants.LogTag, "SocialUploadImage : " + commentResponse.getMessage());

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception SocialUploadImage : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadImageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure SocialUploadImage : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
