package zippler.cn.yuewu.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.listener.OnSeekBarChangeListener;
import zippler.cn.yuewu.util.BaseActivity;
import zippler.cn.yuewu.util.FileUtil;

public class PreviewRecordVideoActivity extends BaseActivity {

    private static final String TAG = "preview activity";

    private VideoView videoView;
    private ImageButton back;
    private ImageButton delete;
    private Button next;

    private SeekBar seekBar;

    private String path;

    private AlertDialog.Builder dialog;//对话框
    private Timer timer;//进度条计时器
    private  Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_record_video);
        videoView = (VideoView) findViewById(R.id.preview_video);
        back = (ImageButton) findViewById(R.id.back_button);
        delete = (ImageButton) findViewById(R.id.delete_button);
        next = (Button) findViewById(R.id.next_button);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        delete.setOnClickListener(this);
        back.setOnClickListener(this);
        next.setOnClickListener(this);

        path = getIntent().getStringExtra("videoPath");

        videoView.setVideoPath(path);

        init();
    }

    public void init(){
        videoView.start(); //开始播放

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });

        //播放途中应该要修改Seekbar
        handler = new Handler(){
            public void handleMessage(android.os.Message msg) {
                //取出消息携带的数据
                Bundle data = msg.getData();
                int currentPosition = data.getInt("currentPosition");
                int duration = data.getInt("duration");

                //设置播放进度
                seekBar.setMax(duration);
                seekBar.setProgress(currentPosition);
            }
        };
        seekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener(videoView,timer,handler));//设置监听==用于裁剪

        startTimer();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.delete_button:
                //删除对话框进度
                final ProgressDialog progressDialog = new ProgressDialog(PreviewRecordVideoActivity.this);
                progressDialog.setTitle("正在删除..");
                progressDialog.setMessage("请稍后..");
                progressDialog.setCancelable(false);

                dialog = new AlertDialog.Builder(this);//在当前界面弹出弹窗
                dialog.setTitle("注意")
                        .setMessage("您确定要删除这段视频吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //隐藏询问对话框
                                progressDialog.show();
                                //执行删除
                                if (FileUtil.deleteFile(path)){
                                    progressDialog.dismiss();
                                    toast("删除成功");
                                    finish();//从当前界面返回
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast("取消");
                    }
                });

                dialog.show();
                break;
            case R.id.next_button:
                //跳转到发布页面
                Intent intent = new Intent(this,DeployVideoActivity.class);
                intent.putExtra("videoPath",path);//此路径应为裁剪后的视频路径，将裁剪前视频删除
                startActivity(intent);
                break;
            case R.id.back_button:
                exit();
                break;
        }
    }

    public void exit(){
        dialog = new AlertDialog.Builder(this);//在当前界面弹出弹窗
        dialog.setTitle("注意")
                .setMessage("您正要离开此页面，将保存视频到本地")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast("执行保存操作");
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toast("取消");
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer!=null){
            timer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    public void startTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int currentPosition = videoView.getCurrentPosition();
                int duration = videoView.getDuration();
                Message msg = Message.obtain();
                //把播放进度存入Message中
                Bundle data = new Bundle();
                data.putInt("currentPosition", currentPosition);
                data.putInt("duration", duration);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }, 5, 100);
    }
}
