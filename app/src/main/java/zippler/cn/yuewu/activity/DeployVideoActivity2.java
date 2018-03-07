package zippler.cn.yuewu.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
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

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;
import zippler.cn.yuewu.R;
import zippler.cn.yuewu.util.BaseActivity;

/**
 * 视频发布页面，发布完成后跳转到个人中心
 */
public class DeployVideoActivity2 extends BaseActivity {

    private static final String TAG = "upload";
    private ImageView videoImg ;
    private Button deploy;
    private  String videoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_video);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);//方便在主线程中调用
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
                        String audio;
                        try {
                            String jsonString = upLoadByCommonPost();
                            if (jsonString!=null){
                                int type = Integer.parseInt(String.valueOf(jsonString.charAt(jsonString.lastIndexOf("}")-1)));
                                Log.d(TAG, "onClick: 上传成功");
                                toast("上传成功");
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
                                final String outfilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + File.separator + "yuewu"+File.separator + "乐舞_test.mp4";
                                File file = new File(audio);
                                Log.d(TAG, "onClick: 音频文件 ： "+file.exists());
                                File file2 = new File(videoPath);
                                Log.d(TAG, "onClick: 视频文件 ： "+file2.exists());
                                EpEditor.music(videoPath,audio , outfilePath, 0, 1, new OnEditorListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d(TAG, "onClick: 视频合成成功，开始跳转");
                                        Intent intent1 = new Intent(DeployVideoActivity2.this,CameraActivity.class);
                                        intent1.putExtra("ms","上传成功");
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
                            }
                        } catch (IOException e) {
                            Log.d(TAG, "onClick: 上传失败");
                            toast("上传失败");
                            e.printStackTrace();
                        }
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
}
