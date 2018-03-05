package zippler.cn.yuewu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import zippler.cn.yuewu.R;
import zippler.cn.yuewu.entitiy.Fans;
import zippler.cn.yuewu.holders.MyFansHolder;

/**
 * Created by zipple on 2018/3/5.
 * 加载粉丝列表
 */

public class MyFansAdapter extends RecyclerView.Adapter<MyFansHolder> {

    private List<Fans> data;

    public MyFansAdapter(List<Fans> data) {
        super();
        this.data = data;
    }

    @Override
    public MyFansHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载子项布局
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_fans_recycle_view_item,parent,false);
        return new MyFansHolder(view);//此处可以处理子项控件的点击事件
    }

    @Override
    public void onBindViewHolder(MyFansHolder holder, int position) {
         //设置子项参数
        ImageView avatar = holder.getAvatar();
        TextView username = holder.getUsername();
        TextView description = holder.getDescription();

        Fans fans = data.get(position);

        //如果是从服务器端获取头像路径，则这里需要修改源码
        avatar.setImageResource(Integer.parseInt(fans.getAvatar()));
        username.setText(fans.getUsername());
        description.setText(fans.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
