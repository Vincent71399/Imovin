package sg.edu.nus.imovin.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.SocialFeedData;
import sg.edu.nus.imovin.Retrofit.Response.SocialImageResponse;
import sg.edu.nus.imovin.Retrofit.Service.ImovinService;
import sg.edu.nus.imovin.System.ImovinApplication;
import sg.edu.nus.imovin.System.LogConstants;

import static sg.edu.nus.imovin.Common.CommonFunc.ConvertDateString2DisplayFormat;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.REQUEST_GET_SOCIAL_POST_IMAGE;
import static sg.edu.nus.imovin.HttpConnection.ConnectionURL.SERVER;


public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.SocialFeedData_ViewHolder> {
    private List<SocialFeedData> socialFeedDataList;

    public SocialFeedAdapter(List<SocialFeedData> socialFeedDataList){
        this.socialFeedDataList = socialFeedDataList;
    }

    public final static class SocialFeedData_ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container;
        LinearLayout body_container;
        TextView owner_text;
        TextView body_text;
        ImageView social_image;
        LinearLayout thumbs_up_container;
        ImageView thumbs_up_image;
        TextView likes_text;
        TextView comment_count;
        TextView post_time;

        public SocialFeedData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            body_container = itemView.findViewById(R.id.body_container);
            owner_text = itemView.findViewById(R.id.owner_text);
            body_text = itemView.findViewById(R.id.body_text);
            social_image = itemView.findViewById(R.id.social_image);
            thumbs_up_container = itemView.findViewById(R.id.thumbs_up_container);
            thumbs_up_image = itemView.findViewById(R.id.thumbs_up_image);
            likes_text = itemView.findViewById(R.id.likes_text);
            comment_count = itemView.findViewById(R.id.comment_count);
            post_time = itemView.findViewById(R.id.post_time);
        }
    }

    @NonNull
    @Override
    public SocialFeedData_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_social_feed, viewGroup, false);

        return new SocialFeedAdapter.SocialFeedData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialFeedData_ViewHolder holder, int i) {
        final SocialFeedData socialFeedData = socialFeedDataList.get(i);

        holder.body_text.setText(socialFeedData.getMessage());
        holder.owner_text.setText(socialFeedData.getUser_name());


    }

    @Override
    public int getItemCount() {
        return socialFeedDataList.size();
    }

    private void GetSocialImageById(String image_id, final ImageView imageView){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(ImovinApplication.getHttpClient().build())
                .build();

        ImovinService service = retrofit.create(ImovinService.class);

        String url = SERVER + String.format(
                Locale.ENGLISH,REQUEST_GET_SOCIAL_POST_IMAGE, image_id);

        Call<SocialImageResponse> call = service.getSocialImage_by_Id(url);

        call.enqueue(new Callback<SocialImageResponse>() {
            @Override
            public void onResponse(Call<SocialImageResponse> call, Response<SocialImageResponse> response) {
                try {
                    SocialImageResponse socialImageResponse = response.body();
                    String base64Img = socialImageResponse.getData();
                    byte[] decodedString = Base64.decode(base64Img, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    imageView.setImageBitmap(decodedByte);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d(LogConstants.LogTag, "Exception SocialFeedAdapter : " + e.toString());
                    Toast.makeText(ImovinApplication.getInstance(), ImovinApplication.getInstance().getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SocialImageResponse> call, Throwable t) {
                Log.d(LogConstants.LogTag, "Failure SocialFeedAdapter : " + t.toString());
                Toast.makeText(ImovinApplication.getInstance(), ImovinApplication.getInstance().getString(R.string.request_fail_message), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

