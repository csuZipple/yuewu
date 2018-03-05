package zippler.cn.yuewu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import zippler.cn.yuewu.activity.CameraFragment;
import zippler.cn.yuewu.activity.MeFragment;
import zippler.cn.yuewu.activity.RefreshFragment;

/**
 * Created by zipple on 2018/3/1.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new RefreshFragment();
        }else if (position==1){
            return new CameraFragment();
        }else if (position==2){
            return new MeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
