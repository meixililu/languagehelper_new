package com.messi.languagehelper

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.messi.languagehelper.adapter.RcBiliSearchResultAdapter
import com.messi.languagehelper.bean.BoutiquesBean
import com.messi.languagehelper.bean.RespoData
import com.messi.languagehelper.databinding.BoutiquesFragmentBinding
import com.messi.languagehelper.impl.FragmentProgressbarListener
import com.messi.languagehelper.util.KeyUtil
import com.messi.languagehelper.util.LogUtil
import com.messi.languagehelper.util.ToastUtil
import com.messi.languagehelper.viewmodels.BiliSearchResultViewModel
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.coroutines.launch

class BiliSearchResultFragmeng : BaseFragment() {

    var keyword:String? = null
    lateinit var mLinearLayoutManager: LinearLayoutManager
    lateinit var binding: BoutiquesFragmentBinding
    lateinit var viewmodel: BiliSearchResultViewModel
    lateinit var adapter: RcBiliSearchResultAdapter

    companion object {
        fun getInstance(keyword: String): BiliSearchResultFragmeng? {
            val fragment = BiliSearchResultFragmeng()
            val args = Bundle()
            args.putString(KeyUtil.KeyWord, keyword)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mProgressbarListener = try {
            activity as FragmentProgressbarListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement FragmentProgressbarListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = arguments!!.getString(KeyUtil.KeyWord)
        viewmodel = ViewModelProvider(requireActivity()).get(BiliSearchResultViewModel::class.java)
        viewmodel.keyword = keyword.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = BoutiquesFragmentBinding.inflate(inflater);
        initSwipeRefresh(binding.root)
        init()
        return binding.root;
    }

    fun init(){
        adapter = RcBiliSearchResultAdapter()
        adapter.footer = Any()
        adapter.setItems(viewmodel.list)
        hideFooterview()
        mLinearLayoutManager = LinearLayoutManager(context)
        binding.listview.layoutManager = mLinearLayoutManager
        binding.listview.addItemDecoration(
                HorizontalDividerItemDecoration.Builder(context)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.padding_7)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build())
        binding.listview.adapter = adapter
        setListOnScrollListener()
        initLiveData()
    }

    private fun initLiveData() {
        viewmodel.dataList?.observe(viewLifecycleOwner, Observer<RespoData<String>> { data: RespoData<String>? -> onDataChange(data) })
    }

    private fun onDataChange(data: RespoData<*>?) {
        LogUtil.DefalutLog("ViewModel---onDataChange---")
        if (data != null) {
            if (data.code == 1) {
                if (adapter != null) {
                    adapter.notifyItemRangeInserted(data.positionStart, data.itemCount)
                }
            } else {
                ToastUtil.diaplayMesShort(activity, data.errStr)
            }
            if (data.isHideFooter) {
                hideFooterview()
            } else {
                showFooterview()
            }
        } else {
            ToastUtil.diaplayMesShort(activity, "网络异常，请检查网络连接。")
        }
    }

    fun setListOnScrollListener() {
        binding.listview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visible = mLinearLayoutManager.childCount
                val total = mLinearLayoutManager.itemCount
                val firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()
                isADInList(recyclerView, firstVisibleItem, visible)
                if (visible + firstVisibleItem >= total) {
                    viewmodel.loadData(false)
                }
            }
        })
    }

    private fun isADInList(view: RecyclerView, first: Int, vCount: Int) {
        if (viewmodel.list.size > 3) {
            for (i in first until first + vCount) {
                if (i < viewmodel.list.size && i > 0) {
                    val mAVObject: BoutiquesBean = viewmodel.list[i]!!
                    if (mAVObject != null && mAVObject.isAd && mAVObject.adData != null) {
                        val mAdData = mAVObject.adData
                        if (mAdData!!.mNativeADDataRef != null && !mAdData.isAdShow) {
                            val mNativeADDataRef = mAdData.mNativeADDataRef
                            val isExposure = mNativeADDataRef!!.onExposure(view.getChildAt(i % vCount))
                            mAdData.isAdShow = isExposure
                            LogUtil.DefalutLog("isExposure:$isExposure")
                        }
                    }
                }
            }
        }
    }

    override fun onSwipeRefreshLayoutRefresh() {
        hideFooterview()
        viewmodel.loadData(true)
    }

    override fun loadDataOnStart() {
        viewmodel.loadData(true)
    }

    private fun hideFooterview() {
        adapter.hideFooter()
    }

    private fun showFooterview() {
        adapter.showFooter()
    }

    override fun showProgressbar() {
        super.showProgressbar()
        if (binding != null && binding.myAwesomeToolbar.isShown) {
            binding.progressBarCircularIndetermininate.visibility = View.VISIBLE
        }
    }

    override fun hideProgressbar() {
        super.hideProgressbar()
        if (binding != null && binding.myAwesomeToolbar.isShown) {
            binding.progressBarCircularIndetermininate.visibility = View.GONE
        }
    }

}