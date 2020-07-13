package com.messi.languagehelper

import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import cn.leancloud.AVException
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.callback.FindCallback
import cn.leancloud.convertor.ObserverBuilder
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.messi.languagehelper.databinding.DailyEnglishActivityBinding
import com.messi.languagehelper.http.LanguagehelperHttpClient
import com.messi.languagehelper.util.*


class DailyEnglishActivity : BaseActivity() {

    lateinit var mSharedPreferences: SharedPreferences
    lateinit var mAVObject: AVObject
    lateinit var binding: DailyEnglishActivityBinding
    private var courseID = 0
    private var current = 0
    private var lists: ArrayList<AVObject> = ArrayList()
    private lateinit var ourSounds: SoundPool
    private var answerRight = 0
    private var answerWrong = 0
    private var startPosition = 0L
    private var endPosition = 0L
    lateinit var player: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarTextColor(true)
        setStatusbarColor(R.color.white)
        binding = DailyEnglishActivityBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
        initializeSoundPool()
        queryData()
    }

    private fun initViews() {
        IPlayerUtil.pauseAudioPlayer(this)
        player = SimpleExoPlayer.Builder(this).build()
        player.addListener(listener)
        mSharedPreferences = Setings.getSharedPreferences(this)
        courseID = mSharedPreferences.getInt(KeyUtil.DailyListenCourseID,0)
        binding.closeBtn.setOnClickListener { finish() }
        binding.playBtn.setOnClickListener { playItem() }
        binding.checkBtn.setOnClickListener { checkOrNext() }
    }

    private fun checkOrNext() {
        if (binding.checkBtn.text.toString() == "Check") {
            binding.checkBtn.text = "Next"
            check()
        } else if (binding.checkBtn.text.toString() == "Next") {
            var nProgress = binding.classProgress.progress + 1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.classProgress.setProgress(nProgress, true)
            }else{
                binding.classProgress.progress = nProgress
            }
            queryData()
        }
    }

    private fun queryData() {
        showProgressbar()
        val query = AVQuery<AVObject>(AVOUtil.ListenCourse.ListenCourse)
        query.addAscendingOrder(AVOUtil.ListenCourse.order)
        query.limit(20)
        query.skip(courseID)
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver<AVObject>(object : FindCallback<AVObject>() {
            override fun done(avObjects: List<AVObject>, avException: AVException?) {
                hideProgressbar()
                if (avObjects.size > 0) {
                    current = 0
                    lists.clear()
                    lists.addAll(avObjects)
                    init()
                }
            }
        }))
    }

    private fun init() {
        if(lists.size > current){
            mAVObject = lists[current]
            binding.checkBtn.isEnabled = false
            binding.checkBtn.text = "Check"
            binding.levelTv.text = "L" + level
            wordToCharacter()
            playItem()
        }else {
            queryData()
        }
    }

    private fun wordToCharacter() {
        binding.resultLayout.visibility = View.GONE
        binding.autoWrapOptions.removeAllViews()
        binding.autoWrapResult.removeAllViews()
        binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
        val content = englishContent
        val contents = content.split(" ")
        for ((index,item) in contents.shuffled().withIndex()) {
            KViewUtil.createNewFlexItemTextView(this,
                    binding.autoWrapOptions,
                    binding.autoWrapResult,
                    binding.checkBtn,
                    item.trim(),
                    index)
        }
    }

    private fun check() {
        val content = englishContent
        if (binding.autoWrapResult.childCount > 0) {
            var selectedStr = getSelectedResult()
            binding.checkBtn.text = "Next"
            binding.resultLayout.visibility = View.VISIBLE
            if (content == selectedStr) {
                courseID ++
                current ++
                Setings.saveSharedPreferences(mSharedPreferences,KeyUtil.DailyListenCourseID,courseID)
                playSoundPool(true)
                binding.checkSuccess.setAnimation("check_success.json")
                binding.checkSuccess.progress
                binding.checkSuccess.playAnimation()
                binding.resultTv.text = "正确"
                binding.chineseTv.text = chineseContent
                binding.resultLayout.setBackgroundResource(R.color.correct_bg)
                binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_green_selecter)
                binding.chineseTv.setTextColor(resources.getColor(R.color.correct_text))
                binding.resultTv.setTextColor(resources.getColor(R.color.correct_text))
            }else{
                playSoundPool(false)
                binding.checkSuccess.setAnimation("cross.json")
                binding.checkSuccess.playAnimation()
                binding.resultTv.text = "正确答案"
                binding.chineseTv.text = englishContent + "\n" + chineseContent
                binding.resultLayout.setBackgroundResource(R.color.wrong_bg)
                binding.checkBtn.setBackgroundResource(R.drawable.border_shadow_red_selecter)
                binding.chineseTv.setTextColor(resources.getColor(R.color.wrong_text))
                binding.resultTv.setTextColor(resources.getColor(R.color.wrong_text))
            }
        }else{
            ToastUtil.diaplayMesShort(this,"请先做题，做完再Check！")
        }
    }

    private fun getSelectedResult(): String{
        var sb = StringBuilder()
        for (index in 0 until binding.autoWrapResult.childCount){
            var item = ((binding.autoWrapResult[index] as ViewGroup)[0] as TextView).text.toString()
            if (!TextUtils.isEmpty(item)){
                sb.append(item)
                if (index != binding.autoWrapResult.childCount-1){
                    sb.append(" ")
                }
            }
        }
        return sb.toString()
    }

    private fun initializeSoundPool() {
        ourSounds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build()
        } else {
            SoundPool(5, AudioManager.STREAM_MUSIC, 1)
        }
        answerRight = ourSounds.load(this, R.raw.right_answer, 1)
        answerWrong = ourSounds.load(this, R.raw.wrong_answer, 1)
    }

    private fun playSoundPool(isRight: Boolean) {
        if (isRight) {
            ourSounds.play(answerRight, 1f, 1f, 1, 0, 1f)
        } else {
            ourSounds.play(answerWrong, 1f, 1f, 1, 0, 1f)
        }
    }

    private val level: String
        private get() {
            return mAVObject.getString(AVOUtil.ListenCourse.level)
        }

    private val englishContent: String
        private get() {
            var sb = StringBuilder()
            var content = mAVObject.getString(AVOUtil.ListenCourse.content)
            var contents = content.split(" ")
            for (item in contents) {
                if (!TextUtils.isEmpty(item)) {
                    sb.append(item)
                    sb.append(" ")
                }
            }
            return sb.toString().trim()
        }

    private val chineseContent: String
        private get() {
            return mAVObject.getString(AVOUtil.ListenCourse.chinese)
        }

    fun playItem() {
        if (mAVObject != null) {
            binding.playBtn.playAnimation()
            var mp3Url = mAVObject.getString(AVOUtil.ListenCourse.mp3_url)
            var startTime = mAVObject.getString(AVOUtil.ListenCourse.start_time)
            var endTime = mAVObject.getString(AVOUtil.ListenCourse.end_time)
            if(TextUtils.isEmpty(mp3Url)){
                mp3Url = MyPlayer.playUrl + englishContent
            }
            if(!TextUtils.isEmpty(startTime)){
                startPosition = KStringUtils.getTimeMills(startTime).toLong()
                if(!TextUtils.isEmpty(endTime)){
                    endPosition = KStringUtils.getTimeMills(endTime).toLong()
                }
            }
            val videoSource = MyPlayer.getMediaSource(mp3Url)
            player.prepare(videoSource)
            player.playWhenReady = true
        }
    }

    private fun stopAtEndPosition() {
        if(endPosition > 0){
            Handler().postDelayed({
                if (player.currentPosition > endPosition) {
                    binding.playBtn.cancelAnimation()
                    player.playWhenReady = false
                }else{
                    stopAtEndPosition()
                }
            }, 10)
        }
    }

    val isPlaying: Boolean
        get() = player.playbackState == Player.STATE_READY && player.playWhenReady

    override fun onDestroy() {
        super.onDestroy()
        MyPlayer.getInstance(this).onDestroy()
        if(player != null){
            player.stop()
            player.release()
        }
    }

    var listener = object : Player.EventListener{
        override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
            LogUtil.DefalutLog("---onTracksChanged---")
        }
        override fun onLoadingChanged(isLoading: Boolean) {
            LogUtil.DefalutLog("---onLoadingChanged---")
        }
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            LogUtil.DefalutLog("---onPlayerStateChanged---")
            when (playbackState) {
                Player.STATE_IDLE -> LogUtil.DefalutLog("STATE_IDLE")
                Player.STATE_BUFFERING -> LogUtil.DefalutLog("STATE_BUFFERING")
                Player.STATE_READY -> {
                    if(playWhenReady){
                        if (startPosition > 0) {
                            player.seekTo(startPosition)
                            player.playWhenReady = true
                            startPosition = 0
                            stopAtEndPosition()
                        }
                    }
                }
                Player.STATE_ENDED -> {
                    binding.playBtn.cancelAnimation()
                }
            }
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            LogUtil.DefalutLog("---onRepeatModeChanged---")
        }
        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            LogUtil.DefalutLog("---onShuffleModeEnabledChanged---")
        }
        override fun onPlayerError(error: ExoPlaybackException) {

        }
        override fun onPositionDiscontinuity(reason: Int) {
            LogUtil.DefalutLog("---onPositionDiscontinuity---")
        }
        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
            LogUtil.DefalutLog("---onPlaybackParametersChanged---")
        }
        override fun onSeekProcessed() {
            LogUtil.DefalutLog("---onSeekProcessed---")
        }
    }

}