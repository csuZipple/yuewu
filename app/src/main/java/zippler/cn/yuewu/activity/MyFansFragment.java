package zippler.cn.yuewu.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.adapter.MyFansAdapter;
import zippler.cn.yuewu.entitiy.Fans;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class MyFansFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    public MyFansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_fans, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.fans_recycle_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //加载数据
        List<Fans> data = new ArrayList<>();
        data.add(new Fans("石英猫",R.drawable.wangjianzheng+"","这个人很懒，连签名都没有"));
        data.add(new Fans("皮卡丘",R.drawable.pikachu+"","人生就是在不断的学习中进步，所以最后我总会遇到你"));
        data.add(new Fans("Lyphen",R.drawable.liuliwen+"","捧起星火燎原为你做序"));
        data.add(new Fans("Zipple",R.drawable.avatar+"","如何在有限的生命里，活得更快乐"));

        recyclerView.setAdapter(new MyFansAdapter(data));
        return view;
    }
}
