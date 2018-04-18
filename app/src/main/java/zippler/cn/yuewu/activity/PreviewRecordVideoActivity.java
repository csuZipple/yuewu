package zippler.cn.yuewu.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;
import zippler.cn.yuewu.R;
import zippler.cn.yuewu.listener.OnSeekBarChangeListener;
import zippler.cn.yuewu.util.BaseActivity;
import zippler.cn.yuewu.util.FileUtil;

public class PreviewRecordVideoActivity extends BaseActivity {

    private static final String TAG = "preview activity";
    final String outfilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu"+File.separator + "乐舞_test.mp4";


    private VideoView videoView;
    private ImageButton back;
    private ImageButton delete;
    private Button next;

    private SeekBar seekBar;

    private String path;
    private int duration;//视频时长

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

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                duration = videoView.getDuration();
            }
        });

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
                                progressDialog.dismiss();
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
                videoView.pause();
                File file = new File(outfilePath);
                if(file.exists()){
                    FileUtil.deleteFile(outfilePath);
                }
                //跳转到发布页面
                final ProgressDialog dialog = ProgressDialog.show(this,
                        "正在上传", "请稍后....", true);//创建一个进度对话框
                new Thread(new Runnable() {//使用Runnable代码块创建了一个Thread线程
                    @Override
                    public void run() {//run()方法中的代码将在一个单独的线程中执行
                        // TODO Auto-generated method stub
                        try {
                             String audio = upload(path);
                             composeVideo(audio,outfilePath,dialog);
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }).start();
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
//                int duration = videoView.getDuration();
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


    public String upload(String videoPath){
        String audio;
        try {
            String jsonString = upLoadByCommonPost(videoPath);
            if (jsonString!=null){
                int type = Integer.parseInt(String.valueOf(jsonString.charAt(jsonString.lastIndexOf("}")-1)));
                Log.d(TAG, "onClick: 上传成功");
                switch (type){
                    case 0:
                        audio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+ File.separator +"style.mp3";
                        break;
                    case 1:
                        audio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+ File.separator +"panama.mp3";
                        break;
                    case 2:
                        audio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+ File.separator +"seve.mp3";
                        break;
                    case 3:
                        audio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+ File.separator +"hongyan.mp3";
                        break;
                    default:
                        audio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+ File.separator +"seve.mp3";
                        break;
                }

                Log.d(TAG, "onClick: "+getIntent().getStringExtra("duration"));
                //等待返回结果
                File file = new File(audio);
                Log.d(TAG, "onClick: 音频文件 ： "+file.exists());
                File file2 = new File(videoPath);
                Log.d(TAG, "onClick: 视频文件 ： "+file2.exists());

                return audio;
            }
        } catch (IOException e) {
            Log.d(TAG, "onClick: 上传失败");
            e.printStackTrace();
        }
        return null;
    }

    private String upLoadByCommonPost(String videoPath) throws IOException {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        URL url = new URL("http://121.196.220.121/");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
        // 允许输入输出流
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setUseCaches(false);
        // 使用POST方法
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        httpURLConnection.setRequestProperty("Content-Type",
                "multipart/form-data;boundary=" + boundary);

        DataOutputStream dos = new DataOutputStream(
                httpURLConnection.getOutputStream());
        dos.writeBytes(twoHyphens + boundary + end);
        dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                + videoPath.substring(videoPath.lastIndexOf("/") + 1) + "\"" + end);
        dos.writeBytes(end);

        FileInputStream fis = new FileInputStream(videoPath);
        byte[] buffer = new byte[8192]; // 8k
        int count = 0;
        // 读取文件
        while ((count = fis.read(buffer)) != -1) {
            dos.write(buffer, 0, count);
        }
        fis.close();
        dos.writeBytes(end);
        dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
        dos.flush();
        InputStream is = httpURLConnection.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String result ="";
        String temp;
        while((temp = br.readLine())!=null){
            result+= temp;
        }
        Log.d(TAG, "uploadFile: "+result);
        dos.close();
        is.close();

        return result;
    }


    /**
     * 合成视频
     * @param audio 音频路径
     * @param outfilePath 输出路径
     */
    public void composeVideo(String audio, final String outfilePath,final ProgressDialog dialog){
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        Log.d(TAG, "composeVideo: ");
        EpEditor.music(path,audio , outfilePath, 0, 1, new OnEditorListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onClick: 视频合成成功，开始跳转");
                dialog.dismiss();//5秒钟后，调用dismiss方法关闭进度对话框
                Intent intent1 = new Intent(PreviewRecordVideoActivity.this,DeployVideoActivity.class);
                intent1.putExtra("ms","上传成功");
                intent1.putExtra("videoPath",outfilePath);
                intent1.putExtra("duration",duration+"");
                startActivity(intent1);
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "onFailure: 转换视频失败");
            }
            @Override
            public void onProgress(float progress) {
                Log.d(TAG, "onProgress: "+progress);
                //这里获取处理进度
            }
        });
    }

}
