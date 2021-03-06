package zippler.cn.yuewu.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
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
//    private ImageView loading;

    final String outfilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu"+File.separator + "乐舞_test.mp4";
    private ProgressDialog mProgressDialog = null;
    private Handler handler = null;

    private boolean isSuccess = false;

    private String audio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_video);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);//方便在主线程中调用
        videoImg = (ImageView) findViewById(R.id.preview_video_img);
        deploy = (Button) findViewById(R.id.deploy_button);
//        loading = (ImageView) findViewById(R.id.loading);
        videoImg.setOnClickListener(this);
        assert deploy != null;
        deploy.setOnClickListener(this);
        videoPath = getIntent().getStringExtra("videoPath");
        //设置首帧预览
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);//设置数据源
        Bitmap bitmap = media.getFrameAtTime();
        videoImg.setImageBitmap(bitmap);

        //另起线程
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        mProgressDialog.dismiss();
                        break;
                    case 1:
                        mProgressDialog = ProgressDialog.show(DeployVideoActivity.this, "正在上传", "loading",false);
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.deploy_button:
                //上传至服务器

                final ProgressDialog dialog = ProgressDialog.show(this,
                        "正在发布", "请稍后....", true);//创建一个进度对话框
                new Thread(new Runnable() {//使用Runnable代码块创建了一个Thread线程
                    @Override
                    public void run() {//run()方法中的代码将在一个单独的线程中执行
                        // TODO Auto-generated method stub
                        try {
                            Thread.sleep(5000);//模拟一个耗时5秒的操作
                            audio = getIntent().getStringExtra("audio");
                            Log.d(TAG, "run: audio "+audio);
                            Intent intent1 = new Intent(DeployVideoActivity.this,CameraActivity.class);
                            intent1.putExtra("ms","上传成功");
                            intent1.putExtra("vp",outfilePath);
                            startActivity(intent1);
                            dialog.dismiss();//5秒钟后，调用dismiss方法关闭进度对话框
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                }).start();

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

    private String upLoadByCommonPost() throws IOException {
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
     * @category 上传文件至Server的方法
     * @param uploadUrl 上传路径参数
     * @param uploadFilePath 文件路径
     */
    private void uploadFile(String uploadUrl,String uploadFilePath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"test.jpg\"" + end);
//          dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                  + uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            // 文件通过输入流读到Java代码中-++++++++++++++++++++++++++++++`````````````````````````
            FileInputStream fis = new FileInputStream(uploadFilePath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);

            }
            fis.close();
            System.out.println("file send to server............");
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            // 读取服务器返回结果
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String result = br.readLine();
            Log.d(TAG, "uploadFile: "+result);
            dos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            setTitle(e.getMessage());
        }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 视频上传
     */
    public String upload(){
        String audio;
        try {
            String jsonString = upLoadByCommonPost();
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

}
