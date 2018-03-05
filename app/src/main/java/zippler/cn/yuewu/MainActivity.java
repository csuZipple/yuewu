package zippler.cn.yuewu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import zippler.cn.yuewu.activity.CameraActivity;
import zippler.cn.yuewu.util.BaseActivity;

public class MainActivity extends BaseActivity {

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        View w = findViewById(R.id.activity_main);
        assert w != null;
        w.setOnClickListener(this);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);//延迟两秒，进入欢迎页
                    if (!flag){
                        Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();//销毁当前欢迎页
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){
            case R.id.activity_main:
                flag = true;
                Intent intent = new Intent(MainActivity.this,CameraActivity.class);
                startActivity(intent);
                this.finish();
                break;
            default:
                break;
        }
    }
}
