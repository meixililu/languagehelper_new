package com.messi.languagehelper

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.messi.languagehelper.coursefragment.CourseListFragment
import com.messi.languagehelper.databinding.ActivityBottomTabsBinding
import com.messi.languagehelper.impl.FragmentProgressbarListener
import com.messi.languagehelper.util.AVAnalytics
import com.messi.languagehelper.util.AVOUtil

class ListenActivity : BaseActivity(), FragmentProgressbarListener {

    private lateinit var binding: ActivityBottomTabsBinding
    private lateinit var dashboardFragment: Fragment
    private lateinit var jtFragment: Fragment
    private lateinit var boutiquesFragment: Fragment

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                hideAllFragment()
                supportFragmentManager.beginTransaction().show(dashboardFragment).commit()
                AVAnalytics.onEvent(this@ListenActivity, "wordstudy_daily")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_jingting -> {
                hideAllFragment()
                supportFragmentManager.beginTransaction().show(jtFragment).commit()
                AVAnalytics.onEvent(this@ListenActivity, "wordstudy_daily")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_video -> {
                hideAllFragment()
                supportFragmentManager.beginTransaction().show(boutiquesFragment).commit()
                AVAnalytics.onEvent(this@ListenActivity, "wordstudy_daily")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomTabsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initFragment()
    }

    private fun initFragment() {
        binding.navigation.inflateMenu(R.menu.listen_tabs)
        binding.navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        dashboardFragment = CourseListFragment.getInstance(3, getString(R.string.selection), "listen")
        jtFragment = ReadingFragment.Builder()
                .title(getString(R.string.title_intensive_listening))
                .lrc(true)
                .category(AVOUtil.Category.listening)
                .isPlayList(true)
                .build()
        boutiquesFragment = BoutiquesFragment.Builder()
                .category("listening")
                .title(getString(R.string.title_english_video))
                .build()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.content, dashboardFragment)
                .add(R.id.content, jtFragment)
                .add(R.id.content, boutiquesFragment)
                .commit()
        hideAllFragment()
        supportFragmentManager
                .beginTransaction().show(dashboardFragment).commit()
    }

    private fun hideAllFragment() {
        supportFragmentManager
                .beginTransaction()
                .hide(dashboardFragment)
                .hide(jtFragment)
                .hide(boutiquesFragment)
                .commit()
    }
}