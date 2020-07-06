package com.messi.languagehelper

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.databinding.WordStudyDuyinxuanciBinding
import com.messi.languagehelper.util.MyPlayer
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class WordStudyDuYinXuanCiFragment : BaseFragment() {

    lateinit var binding: WordStudyDuyinxuanciBinding
    private lateinit var viewModel: WordStudyViewModel
    lateinit var ourSounds: SoundPool
    private var answer_right = 0
    private var answer_wrong = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = WordStudyDuyinxuanciBinding.inflate(inflater)
        initializeSoundPool()
        setData()
        return binding.root
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
        answer_right = ourSounds.load(context, R.raw.answer_right, 1)
        answer_wrong = ourSounds.load(context, R.raw.answer_wrong, 1)
    }

    fun setData() {
        var item = viewModel.currentItem
        binding.wordLayout.setOnClickListener { playMp3(item.name, item.sound) }
        binding.wordDes.text = item.desc
        var word = item.name
        binding.selection1.text = word
        binding.selection2.text = word
        binding.selection3.text = word
        binding.selection4.text = word
    }

    private fun playMp3(content: String, url: String) {
        MyPlayer.getInstance(context).start(content, url)
    }

    private fun playSoundPool(isRight: Boolean) {
        if (isRight) {
            ourSounds.play(answer_right, 1f, 1f, 1, 0, 1f)
        } else {
            ourSounds.play(answer_wrong, 1f, 1f, 1, 0, 1f)
        }
    }

    //        if (index < randomPlayIndex.size()) {
    //            position = randomPlayIndex.get(index);
    //            List<Integer> tv_list = NumberUtil.getRanbomNumberContantExceptAndNotRepeat(
    //                    totalSum < 4 ? 10 : totalSum,
    //                    0, 3, position);
    //            if (tv_list.size() == 4) {
    //                if(totalSum > tv_list.get(0)){
    //                    selection1.setText(WordStudyFragment.itemList.get(tv_list.get(0)).getName());
    //                }else {
    //                    selection1.setText(BoxHelper.getBench().getName());
    //                }
    //                if(totalSum > tv_list.get(1)){
    //                    selection2.setText(WordStudyFragment.itemList.get(tv_list.get(1)).getName());
    //                }else {
    //                    selection2.setText(BoxHelper.getBench().getName());
    //                }
    //                if(totalSum > tv_list.get(2)){
    //                    selection3.setText(WordStudyFragment.itemList.get(tv_list.get(2)).getName());
    //                }else {
    //                    selection3.setText(BoxHelper.getBench().getName());
    //                }
    //                if(totalSum > tv_list.get(3)){
    //                    selection4.setText(WordStudyFragment.itemList.get(tv_list.get(3)).getName());
    //                }else {
    //                    selection4.setText(BoxHelper.getBench().getName());
    //                }
    //            }
    //        }
    //    }
}