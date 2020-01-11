package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;

public class XVideoHomeActivity extends BaseActivity implements FragmentProgressbarListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_fragment);
        initViews();
    }

//    {"推荐","英语","搞笑","游戏","舞蹈","美女","明星","帅哥"}
    private void initViews() {
        String type = getIntent().getStringExtra(KeyUtil.Category);
        Fragment fragment = XVideoFragment.newInstance(type);
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contont_layout, fragment)
                    .commit();
        }else {
            finish();
        }

    }
}
