package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.messi.languagehelper.util.KeyUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {


    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.search_btn)
    FrameLayout searchBtn;
    private long lastTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        hideTitle();
        searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    if(System.currentTimeMillis() - lastTime > 1000){
                        search();
                    }
                    lastTime = System.currentTimeMillis();
                }
                return false;
            }
        });
    }


    @OnClick(R.id.search_btn)
    public void onViewClicked() {
        search();
    }

    private void search(){
        String quest = searchEt.getText().toString();
        if(!TextUtils.isEmpty(quest)){
            Intent intent = new Intent(this,SearchResultActivity.class);
            intent.putExtra(KeyUtil.ActionbarTitle,quest);
            intent.putExtra(KeyUtil.SearchKey,quest);
            startActivity(intent);

        }
    }
}
