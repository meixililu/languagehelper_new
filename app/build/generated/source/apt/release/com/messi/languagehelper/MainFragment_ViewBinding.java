// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainFragment_ViewBinding implements Unbinder {
  private MainFragment target;

  @UiThread
  public MainFragment_ViewBinding(MainFragment target, View source) {
    this.target = target;

    target.content_layout = Utils.findRequiredViewAsType(source, R.id.content_layout, "field 'content_layout'", LinearLayout.class);
    target.input_type_btn = Utils.findRequiredViewAsType(source, R.id.input_type_btn, "field 'input_type_btn'", ImageView.class);
    target.mic_layout = Utils.findRequiredViewAsType(source, R.id.mic_layout, "field 'mic_layout'", LinearLayout.class);
    target.input_et = Utils.findRequiredViewAsType(source, R.id.input_et, "field 'input_et'", AppCompatEditText.class);
    target.clear_btn_layout = Utils.findRequiredViewAsType(source, R.id.clear_btn_layout, "field 'clear_btn_layout'", FrameLayout.class);
    target.submit_btn = Utils.findRequiredViewAsType(source, R.id.submit_btn, "field 'submit_btn'", TextView.class);
    target.submit_btn_cover = Utils.findRequiredViewAsType(source, R.id.submit_btn_cover, "field 'submit_btn_cover'", CardView.class);
    target.keybord_layout = Utils.findRequiredViewAsType(source, R.id.keybord_layout, "field 'keybord_layout'", LinearLayout.class);
    target.input_type_layout = Utils.findRequiredViewAsType(source, R.id.input_type_layout, "field 'input_type_layout'", LinearLayout.class);
    target.more_tools_layout = Utils.findRequiredViewAsType(source, R.id.more_tools_layout, "field 'more_tools_layout'", LinearLayout.class);
    target.voice_btn = Utils.findRequiredViewAsType(source, R.id.voice_btn, "field 'voice_btn'", TextView.class);
    target.record_anim_img = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'record_anim_img'", ImageView.class);
    target.record_layout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'record_layout'", LinearLayout.class);
    target.voice_btn_cover = Utils.findRequiredViewAsType(source, R.id.voice_btn_cover, "field 'voice_btn_cover'", CardView.class);
    target.input_layout = Utils.findRequiredViewAsType(source, R.id.input_layout, "field 'input_layout'", LinearLayout.class);
    target.speakLanguageTv = Utils.findRequiredViewAsType(source, R.id.speak_language_tv, "field 'speakLanguageTv'", TextView.class);
    target.speakLanguageLayout = Utils.findRequiredViewAsType(source, R.id.speak_language_layout, "field 'speakLanguageLayout'", LinearLayout.class);
    target.more_tools_layout_mic = Utils.findRequiredViewAsType(source, R.id.more_tools_layout_mic, "field 'more_tools_layout_mic'", LinearLayout.class);
    target.actionLayout = Utils.findRequiredViewAsType(source, R.id.action_layout, "field 'actionLayout'", LinearLayout.class);
    target.bottomLayout = Utils.findRequiredViewAsType(source, R.id.bottom_layout, "field 'bottomLayout'", CardView.class);
    target.actionPhotoTranBtn = Utils.findRequiredViewAsType(source, R.id.action_photo_tran_btn, "field 'actionPhotoTranBtn'", CardView.class);
    target.moreToolsImgMic = Utils.findRequiredViewAsType(source, R.id.more_tools_img_mic, "field 'moreToolsImgMic'", ImageView.class);
    target.moreToolsImg = Utils.findRequiredViewAsType(source, R.id.more_tools_img, "field 'moreToolsImg'", ImageView.class);
    target.tablayout = Utils.findRequiredViewAsType(source, R.id.tablayout, "field 'tablayout'", TabLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.content_layout = null;
    target.input_type_btn = null;
    target.mic_layout = null;
    target.input_et = null;
    target.clear_btn_layout = null;
    target.submit_btn = null;
    target.submit_btn_cover = null;
    target.keybord_layout = null;
    target.input_type_layout = null;
    target.more_tools_layout = null;
    target.voice_btn = null;
    target.record_anim_img = null;
    target.record_layout = null;
    target.voice_btn_cover = null;
    target.input_layout = null;
    target.speakLanguageTv = null;
    target.speakLanguageLayout = null;
    target.more_tools_layout_mic = null;
    target.actionLayout = null;
    target.bottomLayout = null;
    target.actionPhotoTranBtn = null;
    target.moreToolsImgMic = null;
    target.moreToolsImg = null;
    target.tablayout = null;
  }
}
