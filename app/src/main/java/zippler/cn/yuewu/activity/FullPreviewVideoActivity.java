package zippler.cn.yuewu.activity;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.ChangeImageTransform;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.util.BaseActivity;

public class FullPreviewVideoActivity extends BaseActivity {

    private static final String TAG = "preview";
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_preview_video);

        ImageView img = (ImageView) findViewById(R.id.preview_video_img);

        String path = getIntent().getStringExtra("videoPath");


        videoView = (VideoView) findViewById(R.id.video_preview);

        assert videoView != null;

        //设置首帧预览
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        //设置数据源
        if (path.startsWith("android")){
            Log.d(TAG, "onCreate: " + path);
            videoView.setVideoURI(Uri.parse(path));
            media.setDataSource(this,Uri.parse(path));
        }else{
            videoView.setVideoPath(path);
            media.setDataSource(path);
        }

        Bitmap bitmap = media.getFrameAtTime();
        assert img != null;
        img.setImageBitmap(bitmap);
        getWindow().setSharedElementEnterTransition(new ChangeImageTransform());


        videoView.start();
        img.setVisibility(View.GONE);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.video_preview:
                finish();
                break;
            default:
                break;
        }
    }
}
