package zippler.cn.yuewu.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.adapter.MeViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private TabLayout tabLayout;
//    private NoPreloadViewPager viewPager;
    private ViewPager viewPager;
    private ImageButton more;
    private ImageButton settings;
    private TextView username;

    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =  inflater.inflate(R.layout.me_layout,container,false);

        more = (ImageButton) view.findViewById(R.id.more_info);
        settings = (ImageButton) view.findViewById(R.id.setting_btn);
        username = (TextView) view.findViewById(R.id.username);
        more.setOnClickListener(this);
        username.setOnClickListener(this);
        settings.setOnClickListener(this);
        //为两者设置点击事件

        tabLayout = (TabLayout) view.findViewById(R.id.me_tab_layout);
//        viewPager = (NoPreloadViewPager) view.findViewById(R.id.me_view_pager);
        viewPager = (ViewPager) view.findViewById(R.id.me_view_pager);

        viewPager.setAdapter(new MeViewPagerAdapter(getChildFragmentManager()));//getChildFragmentManager

        TabLayout.Tab firstTab = tabLayout.getTabAt(0);
        if (firstTab!=null){
            firstTab.select();//设置首项
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
         switch (v.getId()){
             case R.id.more_info:
                 intent= new Intent(getContext(),UserInfoActivity.class);
                 startActivity(intent);
                 getActivity().overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
                 break;
             case R.id.username:
                 intent= new Intent(getContext(),UserInfoActivity.class);
                 startActivity(intent);
                 getActivity().overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
                 break;
             case R.id.setting_btn:
                 intent = new Intent(getContext(),SettingsActivity.class);
                 startActivity(intent);
                 getActivity().overridePendingTransition(R.anim.acticity_open_anim,R.anim.acticity_close_anim);
                 break;
             default:
                 break;
         }
    }
}
