package mediarecorder.charlie.com.mediarecorder;

import android.media.MediaRecorder;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;

public class AudioRecordActivity extends AppCompatActivity {
    // 录音功能，首先需要MediaRecord
    private MediaRecorder recorder;
    // 是否正在录音
    private boolean isRecordering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        recorder = new MediaRecorder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recorder.release();// 释放
        recorder = null;
    }

    /**
     * 开始录音
     *
     * @param view
     */
    public void btnStartRecord(View view) {
        // 录音的步骤：
        if (!isRecordering) {


            // 1 设置输入源
            // TODO 分析每一种输入源的特点
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 2 设置生成的文件的格式
            recorder.setOutputFormat(
                    MediaRecorder.OutputFormat.MPEG_4 // mp4文件
            );
            // 3 设置声音编码器,代码中的机型版本适配
            if (Build.VERSION.SDK_INT >= 16) {
                recorder.setAudioEncoder(
                        MediaRecorder.AudioEncoder.HE_AAC
                );
            } else if (Build.VERSION.SDK_INT >= 10) {
                recorder.setAudioEncoder(
                        MediaRecorder.AudioEncoder.AAC
                );
            } else {
                recorder.setAudioEncoder(
                        MediaRecorder.AudioEncoder.DEFAULT
                );
            }
            // 设置录音的声道，2代表立体声
            recorder.setAudioChannels(2);

            int bitRate = 256 * 1024;
            recorder.setAudioEncodingBitRate(bitRate);// 128k

            recorder.setAudioSamplingRate(bitRate);
            // 4设置录音要保存的位置，
            File folder = FileUtil.getMediaRecorderFolder(this);
            File targetFile = new File(folder, "audio-" + System.currentTimeMillis() + ".m4a");
            recorder.setOutputFile(targetFile.getAbsolutePath());

//            recorder.setMaxDuration(long);// 设置最长的时间单位毫秒
//            recorder.setMaxFileSize(long);  // 最大尺寸单位字节数
            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder.start();
            isRecordering = true;
        }
    }

    /**
     * 注意：1 MediaRecorder没有pause状态，只能stop，要继续录制，需要重新reset之后设置start
     * 2 setAudioChannels（2）代表立体声，
     * 3 setAudioEncodingBitRate（int）设置音频一秒钟包含多少的数据位
     *      如果是AAC编码，128*1024即可
     *
     * @param view
     */
    public void btnPauseRecord(View view) {

    }

    /**
     * 停止录音
     *
     * @param view
     */
    public void btnStopRecord(View view) {
        if (isRecordering) {
            isRecordering = false;
            recorder.stop();
            recorder.reset();
        }
    }
}
