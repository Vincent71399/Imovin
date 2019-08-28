package sg.edu.nus.imovin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rm.freedrawview.FreeDrawView;

import java.io.File;

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
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Response.UploadConsentResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;

public class ConsentSignActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.signature_box) FreeDrawView signature_box;
    @BindView(R.id.nextBtn) Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_sign);

        LinkUIById();
        SetFunction();
    }

    private void LinkUIById(){
        ButterKnife.bind(this);
    }

    private void SetFunction(){
        ActionBar ab = getSupportActionBar();
        ab.setBackgroundDrawable(getDrawable(R.color.theme_purple));
        ab.setTitle(getString(R.string.cf));

        nextBtn.setOnClickListener(this);
    }

    private void ProcessConsent(){
        signature_box.getDrawScreenshot(new FreeDrawView.DrawCreatorListener() {
            @Override
            public void onDrawCreated(Bitmap draw) {
                String storeLocation = CommonFunc.saveToInternalStorage(getApplicationContext(), draw);
                File file = CommonFunc.loadImageFromStorage(storeLocation);
                SendConsent(storeLocation, file);
            }

            @Override
            public void onDrawCreationError() {
                Toast.makeText(getApplicationContext(), getString(R.string.signature_empty_alert), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendConsent(String path, File file){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("signature", file.getName(), fileReqBody);

        Call<UploadConsentResponse> call = service.uploadConsent(part);
        call.enqueue(new Callback<UploadConsentResponse>() {
            @Override
            public void onResponse(Call<UploadConsentResponse> call, Response<UploadConsentResponse> response) {
                try {
                    UploadConsentResponse uploadConsentResponse = response.body();
                    String result = uploadConsentResponse.get_status();
                    Log.d(LogConstants.LogTag, result);

                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();


                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception signature : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadConsentResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure signature : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nextBtn:
                ProcessConsent();
                break;
        }
    }
}
