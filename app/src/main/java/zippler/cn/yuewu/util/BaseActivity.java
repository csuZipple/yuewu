package zippler.cn.yuewu.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zipple on 2018/3/1.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollection.addActivity(this);
        StateBarUtil.translucentStatusBar(this,true);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       if (ActivityCollection.removeActivity(this)){
           Log.d("info",this.getClass().getSimpleName()+"destroyed...");
       }
    }

    /**
     * 发布toast
     * @param msg 信息
     */
    protected void toast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        //在此添加控件点击事件监听事件
    }
}
