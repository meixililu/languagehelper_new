package com.messi.languagehelper.coursefragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.databinding.CourseListFragmentBinding
import com.messi.languagehelper.util.KeyUtil

class CourseListsFragment : BaseFragment() {

    lateinit var binding: CourseListFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        viewModel = ViewModelProvider(requireActivity()).get(WordStudyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseListFragmentBinding.inflate(inflater)
        init()
        return binding.root
    }

    private fun  init(){
        binding.tljcLayout.setOnClickListener {
            toActivity(ListenCourseActivity::class.java, null)
        }
        binding.mycourse.setOnClickListener {
            var bundle = Bundle()
            bundle.putString(KeyUtil.CourseId,"1001")
            toActivity(CoursesActivity::class.java, bundle)
        }
    }

}