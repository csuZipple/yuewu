package zippler.cn.yuewu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.adapter.ViewPagerAdapter;
import zippler.cn.yuewu.component.NoPreloadViewPager;
import zippler.cn.yuewu.util.BaseActivity;

public class CameraActivity extends BaseActivity {
    private TabLayout tabLayout;
    private NoPreloadViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        tabLayout = (TabLayout) findViewById(R.id.toolbar_tab);
        assert tabLayout != null;
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab!=null){
            tab.select();//默认选中首项
        }


       viewPager = (NoPreloadViewPager) findViewById(R.id.view_pager);
        //为viewpager设置适配器
        if (getIntent().getStringExtra("videoPath")!=null){
            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),getIntent().getStringExtra("videoPath")));
        }else{
            viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        }
        setTabListener(tabLayout);
        viewPager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1){
                    //选择第二页...
                    tabLayout.getTabAt(position).select();
//                    ((ViewPagerAdapter)viewPager.getAdapter()).getItem(position).onDestroy();
//                    getFragmentManager().beginTransaction().remove(((ViewPagerAdapter)viewPager.getAdapter()).getItem(position));
//                    transaction.commit();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        tabLayout.setupWithViewPager(viewPager);  会删除tabs?

    }


    public void setTabListener( TabLayout tabLayout){
        if (tabLayout != null) {
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()){
                        case 0:
                            tab.setIcon(R.mipmap.refresh1);
                            viewPager.setCurrentItem(tab.getPosition());//设置当前页面
                            break;
                        case 1:
                            tab.setIcon(R.mipmap.camera1);
                            Intent intent = new Intent(CameraActivity.this,PopRecordActivity.class);
                            startActivity(intent);
                            CameraActivity.this.overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
                            break;
                        case 2:
                            tab.setIcon(R.mipmap.me1);
                            viewPager.setCurrentItem(tab.getPosition());//设置当前页面
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    switch (tab.getPosition()){
                        case 0:
                            tab.setIcon(R.mipmap.refresh2);
                            break;
                        case 1:
                            tab.setIcon(R.mipmap.camera2);
                            break;
                        case 2:
                            tab.setIcon(R.mipmap.me2);
                            break;
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    onTabSelected(tab);
                }
            });
        }
    }
}
