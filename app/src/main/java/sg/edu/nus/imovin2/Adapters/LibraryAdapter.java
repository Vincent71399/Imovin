package sg.edu.nus.imovin2.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONStringer;

import java.util.List;

import sg.edu.nus.imovin2.Activities.SingleVideoActivity;
import sg.edu.nus.imovin2.R;
import sg.edu.nus.imovin2.Retrofit.Object.LibraryData;
import sg.edu.nus.imovin2.System.Config;
import sg.edu.nus.imovin2.System.ImovinApplication;
import sg.edu.nus.imovin2.System.IntentConstants;
import sg.edu.nus.imovin2.System.LogConstants;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.LibraryData_ViewHolder>{
    private static final int FirstView = 0;
    private static final int PicView = 1;
    private static final int NoPicView = 2;
    private static final int FirstVideoView = 3;
    private static final int VideoView = 4;

    private List<LibraryData> libraryDataList;
    private SparseBooleanArray selectedItems;
    private FragmentActivity activity;

    public LibraryAdapter(FragmentActivity activity, List<LibraryData> libraryDataList){
        this.activity = activity;
        this.libraryDataList = libraryDataList;
        selectedItems = new SparseBooleanArray();
    }

    public final static class LibraryData_ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout link_container;
        ConstraintLayout pic_container;
        TextView link_header;
        TextView link_subtitle;
        TextView link_info;
        ImageView link_pic;
        YouTubeThumbnailView video_container;
        ImageView play_btn;

        public LibraryData_ViewHolder(View itemView){
            super(itemView);
            link_container = itemView.findViewById(R.id.link_container);
            pic_container = itemView.findViewById(R.id.pic_container);
            link_header = itemView.findViewById(R.id.link_header);
            link_subtitle = itemView.findViewById(R.id.link_subtitle);
            link_info = itemView.findViewById(R.id.link_info);
            link_pic = itemView.findViewById(R.id.link_pic);
            video_container = itemView.findViewById(R.id.video_container);
            play_btn = itemView.findViewById(R.id.play_btn);
        }
    }

    @Override
    public LibraryData_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int resource;
        switch (viewType){
            case FirstView:
                resource = R.layout.item_library_first;
                break;
            case NoPicView:
                resource = R.layout.item_library_without_image;
                break;
            case FirstVideoView:
                resource = R.layout.item_library_first_video;
                break;
            case VideoView:
                resource = R.layout.item_library_video;
                break;
            default:
                resource = R.layout.item_library;
                break;
        }
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(resource, viewGroup, false);

        return new LibraryData_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LibraryData_ViewHolder holder, int position) {
        final LibraryData libraryData = libraryDataList.get(position);

        holder.link_header.setText(libraryData.getTitle());
        holder.link_subtitle.setText(libraryData.getSubtitle());
        holder.link_info.setText(libraryData.getPublish() + " " + libraryData.getYear());

        if(libraryData.getVideo_url() == null || libraryData.getVideo_url().equals("")) {
            if (libraryData.getPic_url() != null && !libraryData.getPic_url().equals("")) {
                holder.pic_container.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(libraryData.getPic_url(), holder.link_pic);
            } else {
                holder.pic_container.setVisibility(View.GONE);
            }
        }else{
            holder.video_container.initialize(Config.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(libraryData.getVideo_url());
                    holder.play_btn.setVisibility(View.VISIBLE);
                    holder.video_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(libraryData.getVideo_url() != null && !libraryData.getVideo_url().equals("")) {
                                Intent intent = new Intent();
                                intent.setClass(activity, SingleVideoActivity.class);
                                intent.putExtra(IntentConstants.VIDEO_URL, libraryData.getVideo_url());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ImovinApplication.getInstance().startActivity(intent);
                            }
                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    Toast.makeText(ImovinApplication.getInstance(), "Fail to load video", Toast.LENGTH_SHORT).show();
                }
            });
        }

        holder.link_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(libraryData.getLink_url() != null && !libraryData.getLink_url().equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(libraryData.getLink_url()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ImovinApplication.getInstance().startActivity(intent);
                }else{
                    Log.d("link", "null");
                }
            }
        });
        holder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return libraryDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            if(libraryDataList.get(position).getVideo_url() == null) {
                return FirstView;
            }else{
                return FirstVideoView;
            }
        }else if(this.libraryDataList.get(position).getPic_url().equals("")){
            if(libraryDataList.get(position).getVideo_url() == null) {
                return NoPicView;
            }else{
                return VideoView;
            }
        }
        return PicView;
    }
}
