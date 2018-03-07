package zippler.cn.yuewu.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;
import zippler.cn.yuewu.R;
import zippler.cn.yuewu.util.BaseActivity;

/**
 * 视频发布页面，发布完成后跳转到个人中心
 */
public class DeployVideoActivity extends BaseActivity {

    private static final String TAG = "upload";
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
                //显示进度条
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("正在上传..");
                progressDialog.setMessage("请稍后..");
                progressDialog.setCancelable(false);
                progressDialog.show();
                try {
                    upLoadByCommonPost();
                    Log.d(TAG, "onClick: 上传成功");
                    toast("上传成功");
                } catch (IOException e) {
                    Log.d(TAG, "onClick: 上传失败");
                    toast("上传失败");
                    e.printStackTrace();
                }finally {
                    progressDialog.dismiss();
                }

                //上传成功之后要合成视频
                Log.d(TAG, "onClick: "+getIntent().getStringExtra("duration"));
                //等待返回结果

                EpVideo epVideo = new EpVideo(videoPath);
               //参数分别是视频路径，音频路径，输出路径,原始视频音量(1为100%,0.7为70%,以此类推),添加音频音量
                final String outfilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu"+File.separator + "乐舞_test.mp4";
//                String audio = "android.resource://" +getPackageName() + "/" + R.raw.test;
                String audio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+ File.separator +"1.mp3";
                String audio2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+ File.separator +"2.wav";
//                Uri audioUri = Uri.parse(audio);
                File file = new File(audio);
                Log.d(TAG, "onClick: 音频文件 ： "+file.exists());
                File file2 = new File(videoPath);
                Log.d(TAG, "onClick: 视频文件 ： "+file2.exists());

                final float video_duration =Long.parseLong(getIntent().getStringExtra("duration"))/1000.0f;

                final long v_duration =Long.parseLong(getIntent().getStringExtra("duration"))/1000;

                final String cutMusicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu"+File.separator + "乐舞_test_audio.mp3";
                String cutMusic = cutIntoMusicCmd(audio,video_duration,cutMusicPath);

                EpEditor.music(videoPath,audio , outfilePath, 0, 0.7f, new OnEditorListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onClick: 视频合成成功，开始跳转");
                        Intent intent1 = new Intent(DeployVideoActivity.this,CameraActivity.class);
                        intent1.putExtra("msg","上传成功");
                        intent1.putExtra("videoPath",outfilePath);
                        startActivity(intent1);
                    }

                    @Override
                    public void onFailure() {
                        Log.d(TAG, "onFailure: 转换视频失败");
                    }
                    @Override
                    public void onProgress(float progress) {
                        //这里获取处理进度
                    }
                });

               /* EpEditor.execCmd(cutMusic, 0, new OnEditorListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: 裁剪音频成功");


                    }

                    @Override
                    public void onFailure() {
                        Log.d(TAG, "onSuccess: 裁剪音频失败");
                    }

                    @Override
                    public void onProgress(float progress) {

                    }
                });*/
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

    private void upLoadByCommonPost() throws IOException {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        URL url = new URL("localhost:5000/uploaded_file");
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
        dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
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
        String result = br.readLine();
        Log.i(TAG, result);
        dos.close();
        is.close();
    }




    /**
     * 裁剪音频 指定时长 second
     */
    public static String cutIntoMusicCmd(String musicUrl, float second, String outUrl) {
        String commands;
        commands = "-i ";
        commands += musicUrl;
        commands += " -ss ";
        commands += "00:00:00 ";
        commands += "-t ";
        commands += String.valueOf(second);
        commands += " -acodec ";
        commands += "copy ";
        commands += outUrl;
        return commands;
    }

}
