package com.messi.languagehelper.util;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;

import com.messi.languagehelper.impl.PCMAudioPlayerListener;
import com.messi.languagehelper.task.PublicTask;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

public class PCMAudioPlayer {

    //默认配置AudioTrack-----此处是解码，要和编码的配置对应
    private static final int DEFAULT_STREAM_TYPE = AudioManager.STREAM_MUSIC;//音乐
    private static final int DEFAULT_PLAY_MODE = AudioTrack.MODE_STREAM;
    private static final int DEFAULT_SAMPLE_RATE = 16000;//采样频率
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;//注意是out
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private AudioTrack audioTrack;//音轨
    private DataInputStream dis;//流
    private boolean isPlaying = false;
    private static PCMAudioPlayer mInstance;//单例
    private int mMinBufferSize;//最小缓存大小

    private PCMAudioPlayerListener listener;

    public PCMAudioPlayer() {
        mMinBufferSize = AudioTrack.getMinBufferSize(
                DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG, AudioFormat.ENCODING_PCM_16BIT);
        //实例化AudioTrack
        audioTrack = new AudioTrack(
                DEFAULT_STREAM_TYPE, DEFAULT_SAMPLE_RATE, DEFAULT_CHANNEL_CONFIG,
                DEFAULT_AUDIO_FORMAT, mMinBufferSize * 2, DEFAULT_PLAY_MODE);
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static PCMAudioPlayer getInstance() {
        if (mInstance == null) {
            synchronized (PCMAudioPlayer.class) {
                if (mInstance == null) {
                    mInstance = new PCMAudioPlayer();
                }
            }
        }
        return mInstance;
    }

    /**
     * 播放文件
     *
     * @param path
     * @throws Exception
     */
    private void setPath(String path) throws Exception {
        File file = new File(path);
        dis = new DataInputStream(new FileInputStream(file));
    }

    /**
     * 启动播放
     *
     * @param path 文件了路径
     */
    public void startPlay(String path) {
        try {
            isPlaying = true;
            setPath(path);//设置路径--生成流dis
            playTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        try {
            isPlaying = false;
            if (audioTrack != null) {
                if (audioTrack.getState() == AudioRecord.STATE_INITIALIZED) {
                    audioTrack.stop();
                }
            }
            if (dis != null) {
                dis.close();
            }
            if (listener != null) {
                listener.onFinish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying(){
        return isPlaying;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (audioTrack != null) {
            audioTrack.release();
        }
    }

    public void setListener(PCMAudioPlayerListener listener) {
        this.listener = listener;
    }

    private void playTask() {
        PublicTask mPublicTask = new PublicTask();
        mPublicTask.setmPublicTaskListener(new PublicTask.PublicTaskListener() {
            @Override
            public void onPreExecute() {
                if (listener != null) {
                    listener.onStart();
                }
            }

            @Override
            public Object doInBackground() {
                runPlayTask();
                return null;
            }

            @Override
            public void onFinish(Object resutl) {
                stopPlay();
            }
        });
        mPublicTask.execute();
    }

    private void runPlayTask(){
        try {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            byte[] tempBuffer = new byte[mMinBufferSize];
            int readCount = 0;
            while (dis.available() > 0) {
                readCount = dis.read(tempBuffer);//读流
                if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                    continue;
                }
                if (readCount != 0 && readCount != -1) {//
                    audioTrack.play();
                    audioTrack.write(tempBuffer, 0, readCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
