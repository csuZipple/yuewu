package zippler.cn.yuewu.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.util.BaseActivity;

/**
 * 视频发布页面，发布完成后跳转到个人中心
 */
public class DeployVideoActivity extends BaseActivity {

    private ImageView videoImg ;

    private Button deploy;

    private  String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_video);

        videoImg = (ImageView) findViewById(R.id.preview_video_img);
        deploy = (Button) findViewById(R.id.deploy_button);

        videoImg.setOnClickListener(this);
        assert deploy != null;
        deploy.setOnClickListener(this);

        videoPath = getIntent().getStringExtra("videoPath");

        //设置首帧预览
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);//设置数据源
        Bitmap bitmap = media.getFrameAtTime();
        videoImg.setImageBitmap(bitmap);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.deploy_button:
                //上传至服务器
                //等待返回结果
                break;
            case R.id.preview_video_img:
                //跳转至video预览
                Intent intent = new Intent(this,FullPreviewVideoActivity.class);
                intent.putExtra("videoPath",videoPath);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this,videoImg,"videoImg").toBundle());
                break;
            default:
                break;
        }
    }
}
