package com.messi.languagehelper.coursefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeremyliao.liveeventbus.LiveEventBus
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.BiliSearchResultFragmeng
import com.messi.languagehelper.adapter.RcCourseListAdapter
import com.messi.languagehelper.databinding.CourseListFragmentBinding
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.viewmodels.CourseListViewModel
import com.messi.languagehelper.views.DividerGridItemDecoration

class CourseListFragment : BaseFragment() {

    private lateinit var viewManager: LinearLayoutManager
    lateinit var binding: CourseListFragmentBinding
    val viewModel: CourseListViewModel by viewModels()
    lateinit var adapter: RcCourseListAdapter
    var title = ""
    var type = "base"
    var column = 3

    companion object {
        fun getInstance(column: Int, title: String, type: String): CourseListFragment {
            val fragment = CourseListFragment()
            val args = Bundle()
            args.putInt(KeyUtil.Column, column)
            args.putString(KeyUtil.ActionbarTitle, title)
            args.putString(KeyUtil.Type, type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseListFragmentBinding.inflate(inflater)
        init()
        initSwipeRefresh(binding.root)
        return binding.root
    }

    private fun init(){
        title = requireArguments().getString(KeyUtil.ActionbarTitle,"")
        type = requireArguments().getString(KeyUtil.Type,"")
        column = requireArguments().getInt(KeyUtil.Column,3)
        if (!title.isNullOrBlank()){
            binding.myAwesomeToolbar.visibility = View.VISIBLE
            binding.myAwesomeToolbar.title = title
        }
        adapter = RcCourseListAdapter()
        viewManager = GridLayoutManager(context,column)
        adapter.setItems(viewModel.datas)
        binding.listView.layoutManager = viewManager
        binding.listView.adapter = adapter
        viewModel.result.observe(viewLifecycleOwner, Observer {
            if(it.code == 0) {
                adapter.notifyDataSetChanged()
            }
            onSwipeRefreshLayoutFinish()
        })
        viewModel.loadData(type)
        LiveEventBus.get(KeyUtil.CourseListUpdate).observe(viewLifecycleOwner, Observer {
            viewModel.loadData(type)
        })
    }

    override fun onSwipeRefreshLayoutRefresh() {
        viewModel.loadData(type)
    }

}