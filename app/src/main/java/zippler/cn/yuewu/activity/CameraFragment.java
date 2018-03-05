package zippler.cn.yuewu.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zippler.cn.yuewu.R;

/**
 * A simple {@link Fragment} subclass.
 * 设置录制样式等等
 */
public class CameraFragment extends Fragment {


    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_layout,container,false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
