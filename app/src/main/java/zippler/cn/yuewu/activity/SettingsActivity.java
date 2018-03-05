package zippler.cn.yuewu.activity;

import android.os.Bundle;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.util.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
    }
}
