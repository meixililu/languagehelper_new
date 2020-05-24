package com.messi.languagehelper.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

public class MyPlayer {

    public static MyPlayer myPlayer;
    private SimpleExoPlayer exoPlayer;
    private Context context;
    private String lastContent;

    public static MyPlayer getInstance(Context context){
        if (myPlayer == null) {
            myPlayer = new MyPlayer(context);
        }
        return myPlayer;
    }

    public MyPlayer(Context mContext){
        context = mContext.getApplicationContext();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(context);
    }

    public void startMedia(String media_url){
        LogUtil.DefalutLog("isPlaying:"+isPlaying()+"---media_url:"+media_url);
        start("",media_url,null);
    }

    public void start(String content){
        LogUtil.DefalutLog("isPlaying:"+isPlaying()+"---content:"+content);
        start(content,"",null);
    }

    public void start(String content, SynthesizerListener mListener){
        LogUtil.DefalutLog("isPlaying:"+isPlaying()+"---content:"+content);
        start(content,"",mListener);
    }

    public void start(String content, String media_url){
        start(content, media_url, null);
    }

    public void start(String content, String media_url, SynthesizerListener mListener){
        LogUtil.DefalutLog("isPlaying:"+isPlaying()+"---content:"+content + "---media_url:"+media_url);
        if (isPlaying()) {
            stop();
            if (!content.equals(lastContent)) {
                play(content, media_url, mListener);
            }
        } else {
            play(content, media_url, mListener);
        }
    }

    public void play(String content, String media_url, SynthesizerListener mListener){
        LogUtil.DefalutLog("---play---");
        if (!TextUtils.isEmpty(content)) {
            lastContent = content;
            String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
            String md5 = MD5.encode(content);
            String fileName = md5 + ".mp3";
            String mp3Path = path + fileName;
            String pcmPath = path + md5 + ".pcm";
            if (SDCardUtil.isFileExist(mp3Path)) {
                playMediaUrl(mp3Path);
                return;
            }else if (SDCardUtil.isFileExist(pcmPath)) {
                playPcm(pcmPath);
                return;
            }else if (!TextUtils.isEmpty(media_url)) {
                DownLoadUtil.downloadFile(context, media_url, SDCardUtil.sdPath, fileName, null);
            }
        }
        if (!TextUtils.isEmpty(media_url)) {
            if(!TextUtils.isEmpty(content)){
                lastContent = content;
            }else {
                lastContent = media_url;
            }
            playMediaUrl(media_url);
        } else {
            playTTS(content, mListener);
        }
    }

    public void playTTS(String content, SynthesizerListener mListener){
        LogUtil.DefalutLog("---playTTS---");
        if (!TextUtils.isEmpty(content)) {
            lastContent = content;
            showSpeechSynthesizer(context, content,"", mListener);
        }
    }

    public void playPcm(String filePath){
        LogUtil.DefalutLog("---playPcm---"+filePath);
        if (!TextUtils.isEmpty(filePath)) {
            PCMAudioPlayer.getInstance().startPlay(filePath);
        }
    }

    public void playMediaUrl(String url){
        LogUtil.DefalutLog("---playMediaUrl---"+url);
        if (!TextUtils.isEmpty(url)) {
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, "xbkj");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    public boolean isPlaying(){
        return PCMAudioPlayer.getInstance().isPlaying()
                || ( exoPlayer.getPlaybackState() == Player.STATE_READY && exoPlayer.getPlayWhenReady() )
                || ( SpeechSynthesizer.getSynthesizer() != null && SpeechSynthesizer.getSynthesizer().isSpeaking() );
    }

    public void stop(){
        try {
            PCMAudioPlayer.getInstance().stopPlay();
            exoPlayer.stop();
            SpeechSynthesizer.getSynthesizer().stopSpeaking();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSpeechSynthesizer(Context mContext,
                                             String source,
                                             String speaker,
                                             SynthesizerListener mSynthesizerListener) {
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        String filepath = path + MD5.encode(source) + ".pcm";
        SpeechSynthesizer mSpeechSynthesizer = SpeechSynthesizer.getSynthesizer();
        if (mSpeechSynthesizer == null) {
            mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext,null);
        }
        mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
        if (TextUtils.isEmpty(speaker)) {
            StringUtils.setSpeaker(source);
            mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, Setings.role);
        } else {
            mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, speaker);
        }
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
                String.valueOf( Setings.getSharedPreferences(mContext).getInt("tts_speed", 50) ));
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME, "100");
        mSpeechSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");//离线 local
        mSpeechSynthesizer.startSpeaking(source, mSynthesizerListener);
    }

    public static void release(){
        try {
            if (myPlayer != null) {
                myPlayer.context = null;
                myPlayer.exoPlayer.stop();
                myPlayer.exoPlayer.release();
                if (SpeechSynthesizer.getSynthesizer() != null) {
                    SpeechSynthesizer.getSynthesizer().stopSpeaking();
                    SpeechSynthesizer.getSynthesizer().destroy();
                }
                myPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
