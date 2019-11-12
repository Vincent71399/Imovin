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

/**
 * Created by wcafricanus on 19/12/18.
 */

public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.SocialFeedData_ViewHolder> {
    private List<SocialFeedData> socialFeedDataList;

    public SocialFeedAdapter(List<SocialFeedData> socialFeedDataList){
        this.socialFeedDataList = socialFeedDataList;
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

        holder.usernameText.setText(socialFeedData.getOwnerName());
        holder.postTimeText.setText(ConvertDateString2DisplayFormat(socialFeedData.getCreatedAt()));
        holder.feedContentText.setText(socialFeedData.getMessage());
        GetSocialImageById(socialFeedData.getId(), holder.imageView);

        holder.commentsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.numberCommentsText.setText(String.valueOf(socialFeedData.getComments().size()));
    }

    @Override
    public int getItemCount() {
        return socialFeedDataList.size();
    }

    public final static class SocialFeedData_ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView usernameText;
        TextView postTimeText;
        TextView feedContentText;
        ImageView imageView;
        ImageView commentsImageView;
        TextView numberCommentsText;
        public SocialFeedData_ViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            usernameText = itemView.findViewById(R.id.username);
            postTimeText = itemView.findViewById(R.id.post_since);
            feedContentText = itemView.findViewById(R.id.feedContentText);
            imageView = itemView.findViewById(R.id.feedImage);
            commentsImageView = itemView.findViewById(R.id.commentIcon);
            numberCommentsText = itemView.findViewById(R.id.numberCommentsText);
        }
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

