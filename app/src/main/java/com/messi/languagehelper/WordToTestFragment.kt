package com.messi.languagehelper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.messi.languagehelper.databinding.WordStudyToTestBinding
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.viewmodels.WordStudyViewModel

class WordToTestFragment : BaseFragment() {

    lateinit var binding: WordStudyToTestBinding
    private lateinit var viewModel: WordStudyViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = WordStudyToTestBinding.inflate(inflater)
        initData()
        return binding.root
    }

    fun initData() {
        binding.startToTest.setOnClickListener {
            ToAvtivity(WordStudyFightActivity::class.java)
        }
    }

    private fun ToAvtivity(toClass: Class<*>) {
        val intent = Intent(context, toClass)
        intent.putParcelableArrayListExtra(KeyUtil.List, viewModel.itemList)
        intent.putExtra(KeyUtil.WordTestType, viewModel.wordTestType)
        startActivity(intent)
        activity?.finish()
    }

}