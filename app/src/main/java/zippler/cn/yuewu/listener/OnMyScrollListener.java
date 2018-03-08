package zippler.cn.yuewu.listener;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.Map;

import zippler.cn.yuewu.adapter.VideoAdapter;
import zippler.cn.yuewu.holders.MViewHolder;
import zippler.cn.yuewu.util.LinerLayoutManager;

import static android.content.ContentValues.TAG;

/**
 * Created by zipple on 2018/3/3.
 */
public class OnMyScrollListener extends RecyclerView.OnScrollListener {
    private LinerLayoutManager layoutManager;
    private int size;
    private boolean scrollTop = false;
    public OnMyScrollListener() {
        super();
    }
    public OnMyScrollListener(LinerLayoutManager linerLayoutManager,int size) {
        super();
        this.layoutManager=linerLayoutManager;
        this.size = size;
    }

    /**
     *  newState：即滑动的状态。分为三种 0，1，2
     =0 表示停止滑动的状态 SCROLL_STATE_IDLE
     =1表示正在滚动，用户手指在屏幕上 SCROLL_STATE_TOUCH_SCROLL
     =2表示正在滑动。用户手指已经离开屏幕 SCROLL_STATE_FLING
     */
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        layoutManager.setSpeedSlow();//设置滚动速度---自定的layoutManager

        int  pos =layoutManager.findFirstVisibleItemPosition();

        switch (newState){
            case 2:
//                View view2 = layoutManager.findViewByPosition(pos);
//                 MViewHolder mv2 = (MViewHolder) recyclerView.getChildViewHolder(view2);//获取到当前控件
//                mv2.getVideo().setVisibility(View.GONE);
//                mv2.getImageView().setVisibility(View.VISIBLE);
            case 0:
                pos =layoutManager.findFirstVisibleItemPosition();
                Log.d(TAG, "onScrollStateChanged: 00000000000   pos:"+pos);

                Map map = ((VideoAdapter) (recyclerView.getAdapter())).getMapViewHolder();
                if(map.containsKey(pos+1)){
                    Log.d(TAG, "onScrollStateChanged: pos+1 ");

                    MViewHolder holder = (MViewHolder) map.get(pos + 1);
                    holder.getImageView().setVisibility(View.VISIBLE);
                }
                if(map.containsKey(pos-1)){
                    Log.d(TAG, "onScrollStateChanged: pos-1 ");
                    MViewHolder holder = (MViewHolder) map.get(pos - 1);
                    holder.getImageView().setVisibility(View.VISIBLE);
                }

                View firstVisibleChildView = layoutManager.findViewByPosition(pos);
                int itemHeight = firstVisibleChildView.getHeight();
                //停止滑动时将最上面的那个item居中显示，等需要优化的时候增加上下滑判断
                if (scrollTop){
                    Log.d(TAG, "onScrollStateChanged: scrollTop");
                    if (firstVisibleChildView.getTop()<-0.3*itemHeight){
                        pos++;
                    }
                }else{
                    if (firstVisibleChildView.getTop()>-0.7*itemHeight){
//                        pos++;
                    }
                }
                recyclerView.smoothScrollToPosition(pos);

//                recyclerView.smoothScrollToPosition(pos);
                //播放滚动之后position的视频
                View view = layoutManager.findViewByPosition(pos);
                final MViewHolder mv = (MViewHolder) recyclerView.getChildViewHolder(view);//获取到当前控件
                final VideoView v =mv.getVideo();
                //播放
                if (v.isPlaying()) {
                    ImageView img =mv.getImageView();
                    img.setVisibility(View.GONE);

                    mv.getPauseView().setVisibility(View.GONE);
                    return;
                }
                v.seekTo(500);
                v.setVisibility(View.VISIBLE);
                v.start();


                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                            ImageView img =mv.getImageView();
                            img.setVisibility(View.GONE);
                            mv.getPauseView().setVisibility(View.GONE);
                }
                },500);


                //循环播放
                v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        v.setVisibility(View.VISIBLE);
                        v.start();
                    }
                });
                break;
            case 1:
                pos =layoutManager.findFirstVisibleItemPosition();
                view = layoutManager.findViewByPosition(pos);
                Log.d(TAG, "onScrollStateChanged: 1111111111111 pos"+pos);

                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        scrollTop = dy>0;
    }
}
