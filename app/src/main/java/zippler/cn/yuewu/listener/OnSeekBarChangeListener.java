package zippler.cn.yuewu.listener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

/**
 * Created by zipple on 2018/3/5.
 */

public class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    private VideoView v;
    private Timer timer;
    private Handler handler;

    public OnSeekBarChangeListener(VideoView videoView, Timer timer,Handler handler){
        this.v = videoView;
        this.timer = timer;
        this.handler = handler;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (v.isPlaying()){
            v.pause();
        }
        Log.d(TAG, "onStartTrackingTouch: 暂停timer");
        if (timer!=null){
            timer.cancel();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d(TAG, "onStopTrackingTouch: seekBar "+seekBar.getProgress());
            int target2 = seekBar.getProgress() * v.getDuration()/100;
            Log.d(TAG, "onStopTrackingTouch: target/100 "+ seekBar.getProgress() * v.getDuration()/100);
            int target = seekBar.getProgress();
            Log.d(TAG, "onStopTrackingTouch: target "+target);
            Log.d(TAG, "onStopTrackingTouch: current" + v.getCurrentPosition());
            v.seekTo(target);
            v.start();
            //启动计时器
            startTimer();
    }

    public VideoView getV() {
        return v;
    }

    public void setV(VideoView v) {
        this.v = v;
    }

    public void startTimer(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int currentPosition = v.getCurrentPosition();
                int duration = v.getDuration();
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
