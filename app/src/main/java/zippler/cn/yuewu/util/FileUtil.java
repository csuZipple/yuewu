package zippler.cn.yuewu.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zipple on 2018/3/5.
 * 文件相关操作
 */

public class FileUtil {

    /**
     * 删除文件操作
     * @param path 文件路径
     * @return 返回操作是否成功
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * 获取路径下的所有文件路径
     * @param path 根路径
     * @return 返回路径列表
     */
    public static List<String> traverseFolder(String path) {

        List<String> paths = new ArrayList<>();
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                return null;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder(file2.getAbsolutePath());
                    } else {
//                        System.out.println("文件:" + file2.getAbsolutePath());
                        paths.add(file2.getAbsolutePath());
                    }
                }
            }
        } else {
            return null;
        }
        return paths;
    }
}
