package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.messi.languagehelper.AiTuringActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.bean.PopMenuItem;
import com.messi.languagehelper.dao.AiEntity;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.dialog.AiItemMenuDialog;
import com.messi.languagehelper.impl.OnTranslateFinishListener;
import com.messi.languagehelper.impl.OnViewClickListener;
import com.messi.languagehelper.util.AiUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.TextHandlerUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcAiTuringItemViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    public TextView ai_chat_machine;
    public TextView ai_chat_user;
    private ProgressBar mProgressbar;
    private List<AiEntity> beans;
    private RcAiTuringAdapter mAdapter;
    private List<PopMenuItem> menuItems;
    private AiTuringActivity mAiChatActivity;

    public RcAiTuringItemViewHolder(View convertView,
                                    List<AiEntity> mBeans,
                                    RcAiTuringAdapter mAdapter,
                                    ProgressBar mProgressbar,
                                    AiTuringActivity mAiChatActivity) {
        super(convertView);
        this.context = convertView.getContext();
        this.beans = mBeans;
        this.mProgressbar = mProgressbar;
        this.mAdapter = mAdapter;
        this.mAiChatActivity = mAiChatActivity;
        ai_chat_machine = (TextView) convertView.findViewById(R.id.ai_chat_machine);
        ai_chat_user = (TextView) convertView.findViewById(R.id.ai_chat_user);
        initMenuData();
    }

    public void render(final AiEntity mBean) {
        ai_chat_machine.setVisibility(View.GONE);
        ai_chat_user.setVisibility(View.GONE);
        ai_chat_machine.setOnClickListener(null);
        ai_chat_user.setOnClickListener(null);
        if(mBean.getRole().equals(AiUtil.Role_Machine)){
            ai_chat_machine.setVisibility(View.VISIBLE);
            if(mBean.getContent_type().equals(AiUtil.Content_Type_Link) && !TextUtils.isEmpty(mBean.getLink())){
                ai_chat_machine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra(KeyUtil.URL,mBean.getLink());
                        intent.putExtra(KeyUtil.ActionbarTitle," ");
                        context.startActivity(intent);
                    }
                });
                ai_chat_machine.setText(mBean.getContent());
                ai_chat_machine.setTextColor(context.getResources().getColor(R.color.material_color_blue));
            }else {
                ai_chat_machine.setTextColor(context.getResources().getColor(R.color.text_dark));
                TextHandlerUtil.handlerText(context, mProgressbar, ai_chat_machine, mBean.getContent());
            }
            if(!TextUtils.isEmpty(mBean.getTranslate())){
                ai_chat_machine.append("\n"+mBean.getTranslate());
            }
            ai_chat_machine.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showMoreMenu(mBean);
                    return true;
                }
            });
        }else {
            ai_chat_user.setVisibility(View.VISIBLE);
            ai_chat_user.setText(mBean.getContent());
            ai_chat_user.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Settings.copy(context,mBean.getContent());
                    return true;
                }
            });
        }
    }

    private void initMenuData(){
        menuItems = new ArrayList<PopMenuItem>();
        menuItems.add(new PopMenuItem(R.string.copy,"copy"));
        menuItems.add(new PopMenuItem(R.string.translate,"translate"));
        menuItems.add(new PopMenuItem(R.string.voice,"voice"));
    }

    private void showMoreMenu(final AiEntity mBean) {
        AiItemMenuDialog aiItemMenuDialog = new AiItemMenuDialog(context,menuItems);
        aiItemMenuDialog.setListener(new OnViewClickListener() {
            @Override
            public void OnViewClicked(String code) {
                if(code.equals("copy")){
                    Settings.copy(context,mBean.getContent());
                }else if(code.equals("translate")){
                    try {
                        if(TextUtils.isEmpty(mBean.getTranslate())){
                            Settings.q = mBean.getContent();
                            TranslateUtil.Translate(new OnTranslateFinishListener() {
                                @Override
                                public void OnFinishTranslate(record mRecord) {
                                    if(mRecord == null){
                                        ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.network_error));
                                    }else {
                                        mBean.setTranslate(mRecord.getEnglish());
                                        DataBaseUtil.getInstance().update(mBean);
                                        mAiChatActivity.mAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(code.equals("voice")){
                    mAiChatActivity.playVideo(mBean);
                }
            }
        });
        aiItemMenuDialog.show();
    }

}
