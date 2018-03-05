package zippler.cn.yuewu.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import zippler.cn.yuewu.R;

/**
 * Created by zipple on 2018/3/3.
 */

public class VideoLayout extends RelativeLayout {
    ImageView imageView;
    VideoView videoView;
    public VideoLayout(Context context) {
        super(context);
    }

    public VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.video_layout, this);
        imageView = (ImageView) view.findViewById(R.id.img);
        videoView = (VideoView) view.findViewById(R.id.videoView);
    }

    public void start(){
        videoView.start();
        imageView.setVisibility(GONE);
    }

    public void pause(){
        videoView.pause();
        imageView.setVisibility(VISIBLE);
    }

}
