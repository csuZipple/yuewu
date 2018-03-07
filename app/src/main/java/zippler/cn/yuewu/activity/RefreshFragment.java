package zippler.cn.yuewu.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.adapter.VideoAdapter;
import zippler.cn.yuewu.listener.OnMyScrollListener;
import zippler.cn.yuewu.util.LinerLayoutManager;
import zippler.cn.yuewu.holders.MViewHolder;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class RefreshFragment extends Fragment {


    private SwipeRefreshLayout swip;

    private RecyclerView recyclerView;

    int pageSize = -1;

    public RefreshFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.refresh_layout,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        LinerLayoutManager layoutManager = new LinerLayoutManager(this.getContext());//自定义的布局管理器
        recyclerView.setLayoutManager(layoutManager);

        /*以下加载视频部分*/
        List<String> videoList = new ArrayList<>();
        if (getArguments()!=null&&getArguments().getString("videoPath")!=null){
            videoList.add(getArguments().getString("videoPath"));
        }
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v1);
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v2);
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v3);
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v4);
        pageSize = videoList.size();
        VideoAdapter videoAdapter = new VideoAdapter(videoList,getContext());
        recyclerView.setAdapter(videoAdapter);

        recyclerView.addOnScrollListener(new OnMyScrollListener(layoutManager,videoList.size()));

        swip = (SwipeRefreshLayout) view.findViewById(R.id.swipe_fresh);
        swip.setColorSchemeColors(R.color.colorPrimary);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                Toast.makeText(getContext(),"无更多内容",Toast.LENGTH_SHORT).show();
                swip.setRefreshing(false);//隐藏进度条
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: refresh fragment destroyed");
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: pause");
        Map map = ((VideoAdapter) (recyclerView.getAdapter())).getMapViewHolder();
        for (int i = 0; i < map.size(); i++) {
            MViewHolder holder = (MViewHolder) map.get(i);
            holder.getImageView().setVisibility(View.VISIBLE);
            holder.getVideo().setVisibility(View.GONE);
        }
        super.onPause();
    }
}
