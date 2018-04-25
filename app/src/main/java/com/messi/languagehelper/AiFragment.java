package com.messi.languagehelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AiFragment extends BaseFragment  {

    @BindView(R.id.tv_ai_chat)
    TextView tvAiChat;
    @BindView(R.id.tv_ai_dialogue)
    TextView tvAiDialogue;
    @BindView(R.id.tv_ai_basic)
    TextView tvAiBasic;

    public static AiFragment getInstance(){
        return new AiFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.ai_activity, null);
        ButterKnife.bind(this, view);
        return view;
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
