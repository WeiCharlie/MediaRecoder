package mediarecorder.charlie.com.mediarecorder;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by:
 * Author : Charlie Wei
 * Date : 2015/10/28.
 * Email : charlie_net@163.com
 */
public final class FileUtil {
    private FileUtil() {
    }
public static File getMediaRecorderFolder(Context context){
    File ret = null;

    String state = Environment.getExternalStorageState();
    File dir = null;
    if (state.equals(Environment.MEDIA_MOUNTED)) {
        // 获取公共的照相机拍照存储文件的位置
        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (!dir.exists()) {
            // 获取存储卡中的特定目录
            //因为公共的拍照位置不存在
            dir = Environment.getExternalStorageDirectory();
            dir = new File(dir, "MediaRecoder");
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

    } else {
        // 内部存储区
        dir = context.getFilesDir();
    }
    ret = dir;
    return ret;
}
    public static File getImageFile(Context context) {
        File targetFile = null;
        String state = Environment.getExternalStorageState();
        File dir = null;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 获取公共的照相机拍照存储文件的位置
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!dir.exists()) {
                // 获取存储卡中的特定目录
                //因为公共的拍照位置不存在
                dir = Environment.getExternalStorageDirectory();
                dir = new File(dir, "MediaRecoder/images");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }

        } else {
            // 内部存储区
            dir = context.getFilesDir();
        }
        // 最终目标文件的位置
        targetFile = new File(dir, "image-" + System.currentTimeMillis() + ".jpeg");
        return targetFile;
    }

}
