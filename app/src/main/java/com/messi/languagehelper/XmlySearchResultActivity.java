package com.messi.languagehelper;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;

import com.messi.languagehelper.adapter.XmlySearchResultAdapter;
import com.messi.languagehelper.databinding.JokeActivityBinding;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;

public class XmlySearchResultActivity extends BaseActivity implements FragmentProgressbarListener {

    private XmlySearchResultAdapter pageAdapter;
    private String search_text;
    private int position;
    private JokeActivityBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = JokeActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        position = getIntent().getIntExtra(KeyUtil.PositionKey,0);
        search_text = getIntent().getStringExtra(KeyUtil.SearchKey);

        pageAdapter = new XmlySearchResultAdapter(getSupportFragmentManager(), this, search_text);
        binding.viewpager.setAdapter(pageAdapter);
        binding.viewpager.setOffscreenPageLimit(5);
        binding.tablayout.setupWithViewPager(binding.viewpager);
        binding.viewpager.setCurrentItem(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

}
