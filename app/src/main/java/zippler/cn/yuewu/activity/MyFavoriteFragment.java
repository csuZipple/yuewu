package zippler.cn.yuewu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.adapter.MyFavoriteVideoPreviewAdapter;
import zippler.cn.yuewu.util.GridSpacingItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MyFavoriteFragment extends Fragment {
    private RecyclerView recyclerView ;
    private ImageView imageView ;

    public MyFavoriteFragment() {
        // Required empty public constructor
    }


    //与video相同布局，只需修改数据 imglist 与 相应的点击事件
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_video, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_video_recycler_view);
        imageView = (ImageView) view.findViewById(R.id.none_img_view);
        //设置布局管理器，用于list的排列
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);//垂直排列
        recyclerView.setLayoutManager(layoutManager);

             /*以下加载预览图像部分*/
        List<String> videoList = new ArrayList<>();
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v1);
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v2);
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v3);
        videoList.add("android.resource://" + getActivity().getPackageName() + "/" + R.raw.v4);


        //ImageList
        if (videoList.size()>0){
            imageView.setVisibility(View.GONE);
            recyclerView.setAdapter(new MyFavoriteVideoPreviewAdapter(getContext(),videoList));
        }else{
            //数据为空，显示默认ImageView
            imageView.setVisibility(View.VISIBLE);
        }


        int spanCount = 3; // 3 columns
        int spacing = 50; // 50px
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, 0, false));
        return view;
    }

}
