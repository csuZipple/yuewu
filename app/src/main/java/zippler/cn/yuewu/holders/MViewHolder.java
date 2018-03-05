package zippler.cn.yuewu.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import zippler.cn.yuewu.R;

/**
 * Created by zipple on 2018/3/2.
 */
public class MViewHolder extends RecyclerView.ViewHolder{

    private ImageView imageView;
    private VideoView video;
    private View item;

    public MViewHolder(View itemView) {
        super(itemView);
        item = itemView;
        video = (VideoView) itemView.findViewById(R.id.videoView);
        imageView = (ImageView) itemView.findViewById(R.id.img);
    }

    public VideoView getVideo() {
        return video;
    }

    public void setVideo(VideoView video) {
        this.video = video;
    }

    public View getItem() {
        return item;
    }

    public void setItem(View item) {
        this.item = item;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
