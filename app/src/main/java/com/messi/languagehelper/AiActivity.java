package com.messi.languagehelper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.impl.FragmentProgressbarListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.androidquery.util.AQUtility.getContext;

public class AiActivity extends BaseActivity implements FragmentProgressbarListener {


    @BindView(R.id.tv_ai_chat)
    TextView tvAiChat;
    @BindView(R.id.tv_ai_dialogue)
    TextView tvAiDialogue;
    @BindView(R.id.tv_ai_basic)
    TextView tvAiBasic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_activity);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_ai_learn));
    }

    @OnClick({R.id.tv_ai_chat, R.id.tv_ai_dialogue, R.id.tv_ai_basic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ai_chat:
                toActivity(AiChatActivity.class, null);
                AVAnalytics.onEvent(getContext(), "ai_to_chat");
                break;
            case R.id.tv_ai_dialogue:
                toActivity(AiDialogueSelectCourseActivity.class, null);
                AVAnalytics.onEvent(getContext(), "ai_to_dialogue");
                break;
            case R.id.tv_ai_basic:
                toActivity(AiSpokenBasicActivity.class, null);
                AVAnalytics.onEvent(getContext(), "ai_to_basic");
                break;
        }
    }
}
