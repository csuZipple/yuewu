package zippler.cn.yuewu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.holders.MyVideoPreviewHolder;
import zippler.cn.yuewu.util.FileUtil;

/**
 * Created by zipple on 2018/3/4.
 * 为recycle view设置数据源
 */

public class MyVideoPreviewAdapter extends RecyclerView.Adapter<MyVideoPreviewHolder> {
    private Context c;

    public MyVideoPreviewAdapter(Context context) {
        super();
        this.c = context;
    }

    @Override
    public MyVideoPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_video_recycle_view_item,parent,false);//绑定子项的布局
        return new MyVideoPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyVideoPreviewHolder holder, final int position) {
        //获取本地文件
        String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu";
        final List<String> videoList = FileUtil.traverseFolder(basePath);
        //不应该每次都要重新获取首帧....有点卡
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        if (videoList != null) {
            media.setDataSource(videoList.get(position));//设置数据源
        }else{
            Log.d("bindHolder", "onBindViewHolder: 本地视频缓存为空");
        }
        Bitmap bitmap = media.getFrameAtTime();


        holder.getImageView().setImageBitmap(bitmap);


        final int pos = position;
        ImageView img = holder.getImageView();
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                GestureDetector detector = new GestureDetector(c, new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                        FileUtil.deleteFile(videoList.get(pos));
                        Toast.makeText(c,"长按执行删除操作..",Toast.LENGTH_SHORT).show();
                        MyVideoPreviewAdapter.this.notifyDataSetChanged();//刷新整个recycleview
                    }
                });

                detector.onTouchEvent(event);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        //获取本地文件
        String basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu";
        final List<String> videoList = FileUtil.traverseFolder(basePath);
        if (videoList != null) {
            return videoList.size();
        }else return 0;
    }
}
