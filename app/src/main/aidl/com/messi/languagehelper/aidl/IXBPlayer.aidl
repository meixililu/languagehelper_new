package com.messi.languagehelper.aidl;

interface IXBPlayer {

    void initAndPlay(String data, boolean isPlayList);

    void initPlayList(String lists, int position);

    int getPlayStatus();

    int getCurrentPosition();

    int getDuration();

    boolean MPlayerIsPlaying();

    void MPlayerPause();

    void MPlayerRestart();

    void MPlayerSeekTo(int position);

    boolean MPlayerIsSameMp3(String oid);
}
