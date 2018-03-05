package zippler.cn.yuewu.util;

/**
 * Created by zipple on 2018/3/3.
 */
import android.hardware.Camera;
import android.hardware.Camera.Size;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyCamera {
    private final CameraSizeComparator sizeComparator = new CameraSizeComparator();
    private static MyCamera myCamPara = null;
    private MyCamera(){

    }
    public static MyCamera getInstance(){
        if(myCamPara == null){
            myCamPara = new MyCamera();
            return myCamPara;
        }
        else{
            return myCamPara;
        }
    }

    public  Size getPreviewSize(List<Camera.Size> list, int th){
        Collections.sort(list, sizeComparator);//按照宽度排列
        Size size=null;
        for(int i=0;i<list.size();i++){
            size=list.get(i);
            if((size.width>th)&&equalRate(size, 1.3f)){
                break;
            }
        }
        return size;
    }
    public Size getPictureSize(List<Camera.Size> list, int th){
        Collections.sort(list, sizeComparator);
        Size size=null;
        for(int i=0;i<list.size();i++){
            size=list.get(i);
            if((size.width>th)&&equalRate(size, 1.3f)){
                break;
            }
        }
        return size;

    }

    public boolean equalRate(Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.2)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public  class CameraSizeComparator implements Comparator<Camera.Size>{
        //按升序排列
        @Override
        public int compare(Size lhs, Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.width == rhs.width){
                return 0;
            }
            else if(lhs.width > rhs.width){
                return 1;
            }
            else{
                return -1;
            }
        }

    }
}