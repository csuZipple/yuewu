package zippler.cn.yuewu.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.activity.FullPreviewVideoActivity;
import zippler.cn.yuewu.holders.MyVideoPreviewHolder;

import static android.content.ContentValues.TAG;

/**
 * Created by zipple on 2018/3/4.
 * 为recycle view设置数据源
 */

public class MyFavoriteVideoPreviewAdapter extends RecyclerView.Adapter<MyVideoPreviewHolder> {
    private List<String> paths;    //传入数据来源
    private Context c;
    public MyFavoriteVideoPreviewAdapter(Context c,List<String> videoList) {
        super();
        this.paths = videoList;
        this.c = c;
    }

    @Override
    public MyVideoPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_video_recycle_view_item,parent,false);//绑定子项的布局
        return new MyVideoPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyVideoPreviewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: video子项 "+position);
        //设置视频首帧预览
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        Log.d(TAG, "onBindViewHolder: videoPath " + paths.get(position));
        Log.d(TAG, "onBindViewHolder: videoPath " + Uri.parse(paths.get(position)));
        media.setDataSource(c, Uri.parse(paths.get(position)));//设置数据源
        Bitmap bitmap = media.getFrameAtTime();
        holder.getImageView().setImageBitmap(bitmap);

        holder.getImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, FullPreviewVideoActivity.class);
                intent.putExtra("videoPath",paths.get(position));
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }
}
