package zippler.cn.yuewu.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import zippler.cn.yuewu.activity.MyFansFragment;
import zippler.cn.yuewu.activity.MyFavoriteFragment;
import zippler.cn.yuewu.activity.MyVideoFragment;

/**
 * Created by zipple on 2018/3/4.
 */

public class MeViewPagerAdapter extends FragmentPagerAdapter  {

    public MeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new MyVideoFragment();
        }else if (position==1){
            return new MyFavoriteFragment();
        }else if (position==2){
            return new MyFansFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
