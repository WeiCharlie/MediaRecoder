package mediarecorder.charlie.com.mediarecorder;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 自定义拍照功能，使用android.hardware.camera 类；
 */
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback, Camera.PreviewCallback {
    // 照相机API
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        // 1 获取SurfaceView
        SurfaceView preview = (SurfaceView) findViewById(R.id.preview_surface);
        // 2 要想使用surfaceView就要用到SurfaceHolder
        //      通过Holder就可以进行绘制；
        SurfaceHolder previewHolder = preview.getHolder();
        // 3 最终，SurfaceView会有一些状态变化，当SurfaceView准备好绘制的时候
        //      就可以回调设置的callBack进行操作
        previewHolder.addCallback(this);
    }

    /**
     * 当SurfaceView准备好，从屏幕上划分出一块区域，作为Surface时，
     * 回调这个方法，代表了可以在这个方法以及之后进行绘制
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 1 准备camera,此方法默认调用后置摄像头，没有返回空
        camera = Camera.open();
//        Camera.CameraInfo.CAMERA_FACING_FRONT;  前置摄像头，camera2中有直接获取方式
        // 2 开始预览
        if (camera != null) {
            try {
                // 2.1 设置预览界面，实际上通过holder来绘制surfaceView
                camera.setPreviewDisplay(holder);

                // 设置预览的参数
                // 设置预览方向，
                camera.setDisplayOrientation(90);

                // 获取当前照相机的参数信息（可以直接修改）
                Camera.Parameters parameters = camera.getParameters();
                // 获取支持的颜色效果
                List<String> supportedColorEffects = parameters.getSupportedColorEffects();
                if (supportedColorEffects != null){
                    for (String effect : supportedColorEffects) {
                        Log.d("Effect","Effect -" + effect);
                    }
                }

                // 设置JPEG图片的质量，数值范围 1-100，100文件最大
                parameters.setJpegQuality(100);
                // 设置实际拍照的图片尺寸
                parameters.setPictureSize(1920, 1080);
                // 设置当前的图像是旋转的，默认倒着
                parameters.setRotation(90);

                // 设置拍照图片像素存储的格式，对于camera.takePicture有影响
                parameters.setPictureFormat(ImageFormat.JPEG);

                // 是否开闪光灯，需要第二个权限
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                parameters.setColorEffect(Camera.Parameters.EFFECT_MONO);
                camera.setParameters(parameters);

                // 设置预览回调
                camera.setPreviewCallback(this);

                // 2.2 开始预览
                camera.startPreview();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 当SurfaceView不再使用，内部的屏幕区域就要释放和销毁，这个时候，不要再进行任何绘制了
     *
     * @param holder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            try {
                // 停止预览
                camera.stopPreview();

            } catch (Exception e) {

            }
            camera.release();// 关，释放摄像头，可以避免摄像头占用，导致其他程序不能使用摄像头

        }
    }

    /**
     * 按钮点击进行拍照
     *
     * @param view
     */
    public void btnTakePicture(View view) {
        if (camera != null) {
            // 拍照
            camera.takePicture(
                    null,// 快门回调，当图片拍照之后，回调这个接口
                    null,// raw模式，数据回调
                    this// PictureCallback，对应JPEG模式的数据
            );

        }
    }

    /**
     * takePicture回调接口
     * PictureCallback的回调方法，用于接收数据
     *
     * @param data：对于JPEG模式，数据就是一个JPEG文件
     * @param camera
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File imageFile = FileUtil.getImageFile(this);
        try {
            FileOutputStream fout = new FileOutputStream(imageFile);
            fout.write(data);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 因为takePicture调用之后预览就停止了，那么需要再次执行startPreview
        camera.startPreview();
    }

    /**
     * 预览中，刷新一帧，回调一次，把预览的内容存成data数组，传给程序
     * @param data
     * @param camera
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }
}
