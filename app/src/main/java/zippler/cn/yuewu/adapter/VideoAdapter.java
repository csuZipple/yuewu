package zippler.cn.yuewu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.holders.MViewHolder;

import static android.content.ContentValues.TAG;

/**
 * Created by zipple on 2018/3/2.
 */

public class VideoAdapter extends RecyclerView.Adapter<MViewHolder> {

    private Map viewHolder = new HashMap<>();
    private List<String> videoList;//视频路径
    private Context c;



    public VideoAdapter(List<String> videoList,Context c) {
        this.videoList = videoList;
        this.c = c;
    }

    @Override
    public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout,parent,false);//加载子项布局
        final MViewHolder holder = new MViewHolder(view);

        //给子项注册点击事件
        holder.getVideo().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.video.pause();
                Log.d("VideoAdapter", "onClick: 触发视频item点击事件");
            }
        });

        holder.getItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getVideo().isPlaying()){
                    holder.getVideo().pause();
                }else{
                    holder.getVideo().setAlpha(1);
                    holder.getImageView().setVisibility(View.INVISIBLE);//播放时确保图片不可见
                    holder.getVideo().start();
                }
                Log.d("VideoAdapter", "onClick: 触发tem点击事件");
            }
        });

        return holder ;
    }


    @Override
    public void onBindViewHolder(final MViewHolder holder, int position) {
        String videoPath = videoList.get(position);
//        Log.d("video_info", "onBindViewHolder:视频路径检测 "+videoPath);
         VideoView v = holder.getVideo();
         ImageView image = holder.getImageView();
        v.setVideoURI(Uri.parse(videoPath));//设置路径

        //设置视频首帧预览
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(c,Uri.parse(videoPath));//设置数据源
        Bitmap bitmap = media.getFrameAtTime();
        Log.d(TAG, "onBindViewHolder: bitmap:"+bitmap.getByteCount());
        image.setImageBitmap(bitmap);

        //保存view holder
        viewHolder.put(position,holder);
    }


    @Override
    public void onViewAttachedToWindow(MViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //进入显示区域时不播放，等待recycleview滚动完成
        final VideoView v =holder.getVideo();
        //播放
        v.pause();

        //循环播放
        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                v.start();
            }
        });
    }



    @Override
    public void onViewDetachedFromWindow(MViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        final VideoView v = holder.getVideo();
        //离开显示区域的时候暂停
        ImageView img = holder.getImageView();
        v.pause();
        img.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public Map getMapViewHolder() {
        return viewHolder;
    }

    public void setViewHolder(Map viewHolder) {
        this.viewHolder = viewHolder;
    }
}
