package zippler.cn.yuewu.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import zippler.cn.yuewu.R;

/**
 * Created by zipple on 2018/3/5.
 */

public class MyFansHolder extends RecyclerView.ViewHolder {

    private ImageView avatar;
    private TextView username;
    private TextView description;

    public MyFansHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar_circle_image_view);
        username = (TextView) itemView.findViewById(R.id.username_text_view);
        description = (TextView) itemView.findViewById(R.id.desc_text_view);
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public TextView getUsername() {
        return username;
    }

    public void setUsername(TextView username) {
        this.username = username;
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(TextView description) {
        this.description = description;
    }
}
