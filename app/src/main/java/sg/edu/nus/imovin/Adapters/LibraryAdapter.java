package sg.edu.nus.imovin.Adapters;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import sg.edu.nus.imovin.Common.CommonFunc;
import sg.edu.nus.imovin.R;
import sg.edu.nus.imovin.Retrofit.Object.LibraryData;
import sg.edu.nus.imovin.System.Config;
import sg.edu.nus.imovin.System.ImovinApplication;

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

        public LibraryData_ViewHolder(View itemView){
            super(itemView);
            link_container = itemView.findViewById(R.id.link_container);
            pic_container = itemView.findViewById(R.id.pic_container);
            link_header = itemView.findViewById(R.id.link_header);
            link_subtitle = itemView.findViewById(R.id.link_subtitle);
            link_info = itemView.findViewById(R.id.link_info);
            link_pic = itemView.findViewById(R.id.link_pic);
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
    public void onBindViewHolder(LibraryData_ViewHolder holder, int position) {
        final LibraryData libraryData = libraryDataList.get(position);

        holder.link_header.setText(libraryData.getTitle());
        holder.link_subtitle.setText(libraryData.getSubtitle());
        holder.link_info.setText(libraryData.getPublish() + " " + libraryData.getYear());

        if(libraryData.getVideo_url() == null) {
            if (libraryData.getPic_url() != null && !libraryData.getPic_url().equals("")) {
                holder.pic_container.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(libraryData.getPic_url(), holder.link_pic);
            } else {
                holder.pic_container.setVisibility(View.GONE);
            }
        }else{
            YouTubePlayerSupportFragment video = YouTubePlayerSupportFragment.newInstance();
            video.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                    youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
                        @Override
                        public void onFullscreen(boolean fullscreen) {
                            if(fullscreen) {
                                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            }
                            else {
                                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            }
                        }
                    });
                    if (!wasRestored) {
                        youTubePlayer.cueVideo(CommonFunc.RemoveVideoPrefix(libraryData.getVideo_url()));
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });

            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.video_container, video);
            transaction.commit();
        }

        holder.link_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(libraryData.getLink_url()));
                ImovinApplication.getInstance().startActivity(i);
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
