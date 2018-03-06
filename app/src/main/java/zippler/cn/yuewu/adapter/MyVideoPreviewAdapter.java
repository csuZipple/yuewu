package zippler.cn.yuewu.adapter;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.holders.MyVideoPreviewHolder;

import static android.content.ContentValues.TAG;

/**
 * Created by zipple on 2018/3/4.
 * 为recycle view设置数据源
 */

public class MyVideoPreviewAdapter extends RecyclerView.Adapter<MyVideoPreviewHolder> {
    private List<String> paths;    //传入数据来源

    public MyVideoPreviewAdapter(List<String> videoList) {
        super();
        this.paths = videoList;
    }

    @Override
    public MyVideoPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_video_recycle_view_item,parent,false);//绑定子项的布局
        return new MyVideoPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyVideoPreviewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: video子项 "+position);
        //在这里可以设置图像
//        holder.getImageView().setImageResource(paths.get(position));
        //设置视频首帧预览
        //不应该每次都要重新获取首帧....有点卡
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(paths.get(position));//设置数据源
        Bitmap bitmap = media.getFrameAtTime();
        holder.getImageView().setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }
}
