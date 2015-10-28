package mediarecorder.charlie.com.mediarecorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private File targetFile;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.bitmap);
        videoView = (VideoView) findViewById(R.id.videoView);

    }

    /**
     * 拍照，并且获取图片
     *
     * @param view
     */
    public void takePhoto(View view) {
        // 通过隐式意图实现
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 998);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 998) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);

                        // Bitmap转换成文件,其实里面是一种压缩
                        // Bitmap.compress(format,quality,stream)将图片保存到文件中

//                        bitmap.compress();
                    }
                }
            }
        } else if (requestCode == 199) {
            Log.d("", " ==" + resultCode);
            if (resultCode == RESULT_OK) {
                if (targetFile != null && targetFile.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bitmap = BitmapFactory.decodeFile(targetFile.getAbsolutePath(), options);

                    imageView.setImageBitmap(bitmap);
                }
            }
        } else if (requestCode == 888) {
//            if (targetFile != null && targetFile.exists()) {
//                Uri uri = Uri.fromFile(targetFile);
//                videoView.setVideoURI(uri);
//            }
       }
    }

    /**
     * 拍照并且直接保存为图片文件，不会返回bitmap结果
     *
     * @param view
     */
    public void btnTakePhotoFully(View view) {
//        MediaStore.ACTION_VIDEO_CAPTURE; 录像


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 设置EXTRA_OUTPUT数值，来指定保存的文件位置
        // 数据类型为Uri
        targetFile = null;
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
            dir = getFilesDir();
        }
        // 最终目标文件的位置
        targetFile = new File(dir, "Image-" + System.currentTimeMillis() + ".jpeg");

        Uri uri = Uri.fromFile(targetFile);// 图片存储的位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 199);

    }

    /**
     * 录像
     *
     * @param view
     */
    public void btnTakeVideo(View view) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

//        targetFile = null;
//        String state = Environment.getExternalStorageState();
//        File dir = null;
//        if (state.equals(Environment.MEDIA_MOUNTED)) {
//            // 获取公共的照相机拍照存储文件的位置
//            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//            if (!dir.exists()) {
//                // 获取存储卡中的特定目录
//                //因为公共的拍照位置不存在
//                dir = Environment.getExternalStorageDirectory();
//                dir = new File(dir, "MediaRecoder/videos");
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//            }
//
//        } else {
//            // 内部存储区
//            dir = getFilesDir();
//        }
//        // 最终目标文件的位置
//        targetFile = new File(dir, "video-" + System.currentTimeMillis() + ".mp4");
//
//        Uri uri = Uri.fromFile(targetFile);// 图片存储的位置
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(intent, 888);

    }

    public void btnJumpCamera(View view) {
        Intent intent = new Intent(this,CameraActivity.class);
        startActivity(intent);
    }
}
