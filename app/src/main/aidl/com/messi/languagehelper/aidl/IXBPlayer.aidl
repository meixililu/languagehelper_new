package com.messi.languagehelper.aidl;

import com.messi.languagehelper.box.Reading;

interface IXBPlayer {

    void initAndPlay(in Reading data);

    void initPlayList(in List<Reading> list, int position);

    int getPlayStatus();

    int getCurrentPosition();

    int getDuration();

    boolean MPlayerIsPlaying();

    void MPlayerPause();

    void MPlayerRestart();

    void MPlayerSeekTo(int position);

    boolean MPlayerIsSameMp3(String oid);
}
