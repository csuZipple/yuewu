package zippler.cn.yuewu.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import zippler.cn.yuewu.R;

/**
 * Created by zipple on 2018/3/4.
 * 包装子项layout的控件 供外部调用
 */
public class MyVideoPreviewHolder  extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public MyVideoPreviewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.my_video_img);
    }



    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
