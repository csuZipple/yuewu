package zippler.cn.yuewu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import zippler.cn.yuewu.R;

public class PreviewRecordVideoActivity extends AppCompatActivity {

    private static final String TAG = "preview activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_record_video);

        String path = getIntent().getStringExtra("videoPath");

        Log.d(TAG, "onCreate: " + path);
    }
}
