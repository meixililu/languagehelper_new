package com.messi.languagehelper

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.messi.languagehelper.coursefragment.CourseListFragment.Companion.getInstance
import com.messi.languagehelper.databinding.ActivityBottomTabsBinding
import com.messi.languagehelper.impl.FragmentProgressbarListener
import com.messi.languagehelper.util.AVAnalytics

class SymbolActivity : BaseActivity(), FragmentProgressbarListener {

    private lateinit var mWordHomeFragment: Fragment
    private lateinit var dashboardFragment: Fragment
    private lateinit var shadowFragment: Fragment
    private lateinit var binding: ActivityBottomTabsBinding

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                hideAllFragment()
                supportFragmentManager.beginTransaction().show(dashboardFragment!!).commit()
                AVAnalytics.onEvent(this@SymbolActivity, "spoken_home")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_shadow -> {
                hideAllFragment()
                supportFragmentManager.beginTransaction().show(mWordHomeFragment!!).commit()
                AVAnalytics.onEvent(this@SymbolActivity, "spoken_shadow")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_practice -> {
                hideAllFragment()
                supportFragmentManager.beginTransaction().show(shadowFragment!!).commit()
                AVAnalytics.onEvent(this@SymbolActivity, "spoken_practice")
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
        binding.navigation.inflateMenu(R.menu.tabs_symbol)
        binding.navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        dashboardFragment = getInstance(3, getString(R.string.selection), "symbol")
        mWordHomeFragment = BoutiquesFragment.Builder()
                .category("symbol")
                .title(getString(R.string.title_english_video))
                .build()
        shadowFragment = SymbolListFragment.getInstance()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.content, dashboardFragment)
                .add(R.id.content, mWordHomeFragment)
                .add(R.id.content, shadowFragment)
                .commit()
        hideAllFragment()
        supportFragmentManager
                .beginTransaction().show(dashboardFragment).commit()
    }

    private fun hideAllFragment() {
        supportFragmentManager
                .beginTransaction()
                .hide(dashboardFragment)
                .hide(mWordHomeFragment)
                .hide(shadowFragment)
                .commit()
    }
}