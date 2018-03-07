package zippler.cn.yuewu.activity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.util.BaseActivity;

/**
 * 录制视频
 */
public class PopRecordActivity extends BaseActivity implements TextureView.SurfaceTextureListener {

    private static final String TAG = "PopRecordActivity";
    private TextureView myTexture;
    private Camera camera;

    private ImageButton closeBtn;
    private ImageButton recordBtn;
    Camera.Parameters parameters;

    private ImageButton fresh_btn;
    private ImageButton pauseBtn;

    private SurfaceTexture surface;

    private int current_camera;


    private boolean clicked = false;
    private boolean isPause = false;

    private MediaPlayer music;//播放录制声音

    private MediaRecorder mediaRecorder;//录制视频

    private String previewVideoPath;//预览视频路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        //调用摄像头
        myTexture = (TextureView) findViewById(R.id.texture);
        if (myTexture != null) {
            myTexture.setSurfaceTextureListener(this);
        }

        closeBtn = (ImageButton) findViewById(R.id.record_close_btn);
        if (closeBtn != null) {
            closeBtn.setOnClickListener(this);
        }

        fresh_btn = (ImageButton) findViewById(R.id.fresh_btn);
        if (fresh_btn != null) {
            fresh_btn.setOnClickListener(this);
        }

        recordBtn = (ImageButton) findViewById(R.id.recordBtn);
        assert recordBtn != null;
        recordBtn.setOnClickListener(this);

        pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
        assert pauseBtn != null;
        pauseBtn.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        openCamera(Camera.CameraInfo.CAMERA_FACING_BACK,surface);
        this.surface = surface;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        openCamera(Camera.CameraInfo.CAMERA_FACING_BACK,surface);//启动预览
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        camera.stopPreview();
        camera.release();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    //点击事件监听
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.record_close_btn:
                PopRecordActivity.this.finish();
                break;

            case R.id.recordBtn:
                //开始录制视频
                if (!clicked){
                    fresh_btn.setVisibility(View.GONE);//必须设置为gone，这样才不会被点击到
                    playMusic(R.raw.di);
                    clicked=true;
                    isPause = false;
                    pauseBtn.setImageResource(R.mipmap.pause);
                    recordBtn.setImageResource(R.mipmap.stop1);
                    pauseBtn.setVisibility(View.VISIBLE);


                    //调用录制视频逻辑
                    record();
                }else{
                    fresh_btn.setVisibility(View.VISIBLE);
                    playMusic(R.raw.po);
                    clicked = false;
                    recordBtn.setImageResource(0);
                    pauseBtn.setVisibility(View.INVISIBLE);

                    stop();

                    //调用另一个activity预览

                    Intent intent = new Intent(this,PreviewRecordVideoActivity.class);
                    intent.putExtra("videoPath",previewVideoPath);
                    startActivity(intent);


                }

                break;

            case R.id.fresh_btn:
                //切换前后摄像头
                Log.d(TAG, "onClick: 切换摄像头 "+current_camera);
                camera.stopPreview();
                camera.release();
                if (current_camera!=Camera.CameraInfo.CAMERA_FACING_FRONT){
                    current_camera = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                }else{
                    current_camera = Camera.CameraInfo.CAMERA_FACING_BACK;
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                }
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                try {
                    camera.setPreviewTexture(surface);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                camera.startPreview();//开始预览

                break;

            case R.id.pauseBtn:

                if (!isPause){
                    Log.d(TAG, "onClick: 点击暂停");
                    toast("暂停录制");
                    isPause = true;
                    pauseBtn.setImageResource(R.mipmap.play);
                    pause();
                }else{
                    isPause = false;
                    toast("开始录制");
                    pauseBtn.setImageResource(R.mipmap.pause);
                    continueRecord();
                }



                break;
            default:
                break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //点击对焦
        Camera.AutoFocusCallback mAutoFocusCallback;
        mAutoFocusCallback = new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera c) {
                // TODO Auto-generated method stub
                if(success){
                    camera.setOneShotPreviewCallback(null);
                    Log.d(TAG, "onAutoFocus: 自动对焦成功");
                }
            }
        };
        Point point = new Point((int)event.getRawX(),(int)event.getRawY());
        onFocus(point,mAutoFocusCallback);

        return super.onTouchEvent(event);
    }

    /**
     * 手动聚焦
     *
     * @param point 触屏坐标
     */
    protected boolean onFocus(Point point, Camera.AutoFocusCallback callback) {
        if (camera == null) {
            return false;
        }
        Camera.Parameters parameters = null;
        try {
            parameters = camera.getParameters();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //不支持设置自定义聚焦，则使用自动聚焦，返回
        if(Build.VERSION.SDK_INT >= 14) {
            if (parameters.getMaxNumFocusAreas() <= 0) {
                return focus(callback);
            }

            Log.i(TAG, "onCameraFocus:" + point.x + "," + point.y);

            //定点对焦
            List<Camera.Area> areas = new ArrayList<Camera.Area>();
            int left = point.x - 300;
            int top = point.y - 300;
            int right = point.x + 300;
            int bottom = point.y + 300;
            left = left < -1000 ? -1000 : left;
            top = top < -1000 ? -1000 : top;
            right = right > 1000 ? 1000 : right;
            bottom = bottom > 1000 ? 1000 : bottom;
            areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
            parameters.setFocusAreas(areas);
            try {
                camera.setParameters(parameters);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return false;
            }
        }


        return focus(callback);
    }

    private boolean focus(Camera.AutoFocusCallback callback) {
        try {
            camera.autoFocus(callback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 打开摄像头
     * @param position 摄像头位置
     */
    public void openCamera(int position,SurfaceTexture surface){
        current_camera = position;
        camera = Camera.open(position);//打开摄像头

        parameters= camera.getParameters();//设置参数
        //设置自动对焦
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        Camera.Size previewSize = MyCamera.getInstance().getPreviewSize(parameters.getSupportedPreviewSizes(),myTexture.getWidth());
//        myTexture.setLayoutParams(new RelativeLayout.LayoutParams(previewSize.height , previewSize.width));
        try {
            parameters.setPreviewSize(1920,1080);
            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            camera.setPreviewTexture(surface);
        } catch (IOException t) {
            Log.d(TAG, "onSurfaceTextureAvailable: IO异常");
        }
        camera.startPreview();//开始预览
        myTexture.setAlpha(1.0f);
    }

    private void playMusic(int MusicId) {
        music = MediaPlayer.create(this, MusicId);
        music.start();

        music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                music.release();
            }
        });
    }

    /**
     * 录制视频
     */
    public void record(){
        Log.d(TAG, "record: 开始录制");
        mediaRecorder = new MediaRecorder();
        camera.unlock();
        mediaRecorder.setCamera(camera);
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

//        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)); //setProfile不能和后面的setOutputFormat等方法一起使用

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 设置视频的输出格式 为MP4

        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // 设置音频的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); // 设置视频的编码格式
//        mediaRecorder.setVideoSize(176, 144);  // 设置视频大小
//        mediaRecorder.setVideoSize(320, 240);  // 设置视频大小
        mediaRecorder.setVideoSize(1920, 1080);  // 设置视频大小
        mediaRecorder.setVideoEncodingBitRate(5*1024*1024);
        mediaRecorder.setVideoFrameRate(60); // 设置帧率

        /*
        * 设置视频文件的翻转角度
        * 改变保存后的视频文件播放时是否横屏(不加这句，视频文件播放的时候角度是反的)
        * */
        if (current_camera == Camera.CameraInfo.CAMERA_FACING_FRONT){
            mediaRecorder.setOrientationHint(270);
        }else if (current_camera == Camera.CameraInfo.CAMERA_FACING_BACK){
            mediaRecorder.setOrientationHint(90);
        }

//        mRecorder.setMaxDuration(10000); //设置最大录像时间为10s
//        mediaRecorder.setPreviewDisplay();
//        mediaRecorder.setPreviewDisplay(myTexture);


        //设置视频存储路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu");
        if (!file.exists()) {
            //多级文件夹的创建
            file.mkdirs();
        }

        previewVideoPath = file.getPath() + File.separator + "乐舞_" + System.currentTimeMillis() + ".mp4";
        mediaRecorder.setOutputFile(previewVideoPath);

        //开始录制
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停录制
     */
    public void pause(){
        //获取上一个视频，合并
        Log.d(TAG, "pause:  录制视频暂停方式等待实现");
    }

    /**
     * 继续录制
     */
    public void continueRecord(){
        //继续录制，合并
        Log.d(TAG, "continueRecord:  录制视频继续  方式等待实现");
    }

    /**
     * 停止录制
     */
    public void stop(){
//        mediaRecorder.setOnErrorListener(null);
//        mediaRecorder.setOnInfoListener(null);
        camera.lock();

        mediaRecorder.stop();
        mediaRecorder.release();

//        openCamera(current_camera,surface);//启动预览

        Log.d(TAG, "stop: 录制完成");
    }
}
