package com.messi.languagehelper.coursefragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.messi.languagehelper.BaseFragment
import com.messi.languagehelper.adapter.RcCourseListAdapter
import com.messi.languagehelper.databinding.CourseListFragmentBinding
import com.messi.languagehelper.viewmodels.CourseListViewModel

class CourseListFragment : BaseFragment() {

    private lateinit var viewManager: LinearLayoutManager
    lateinit var binding: CourseListFragmentBinding
    val viewModel: CourseListViewModel by viewModels()
    lateinit var adapter: RcCourseListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = CourseListFragmentBinding.inflate(inflater)
        init()
        initSwipeRefresh(binding.root)
        return binding.root
    }

    private fun  init(){
        adapter = RcCourseListAdapter()
        viewManager = GridLayoutManager(context,2)
        adapter.setItems(viewModel.datas)
        binding.listView.layoutManager = viewManager
        binding.listView.adapter = adapter
        viewModel.result.observe(viewLifecycleOwner, Observer {
            if(it.code == 0) {
                adapter.notifyDataSetChanged()
            }
            onSwipeRefreshLayoutFinish()
        })
        viewModel.loadData()
    }

    override fun onSwipeRefreshLayoutRefresh() {
        viewModel.loadData()
    }

}