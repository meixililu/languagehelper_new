// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SpokenEnglishDetailActivity_ViewBinding implements Unbinder {
  private SpokenEnglishDetailActivity target;

  @UiThread
  public SpokenEnglishDetailActivity_ViewBinding(SpokenEnglishDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SpokenEnglishDetailActivity_ViewBinding(SpokenEnglishDetailActivity target, View source) {
    this.target = target;

    target.evaluation_en_tv = Utils.findRequiredViewAsType(source, R.id.evaluation_en_tv, "field 'evaluation_en_tv'", TextView.class);
    target.voice_play_answer = Utils.findRequiredViewAsType(source, R.id.voice_play_answer, "field 'voice_play_answer'", ImageButton.class);
    target.record_answer_cover = Utils.findRequiredViewAsType(source, R.id.record_answer_cover, "field 'record_answer_cover'", FrameLayout.class);
    target.evaluation_zh_tv = Utils.findRequiredViewAsType(source, R.id.evaluation_zh_tv, "field 'evaluation_zh_tv'", TextView.class);
    target.show_zh_img = Utils.findRequiredViewAsType(source, R.id.show_zh_img, "field 'show_zh_img'", FrameLayout.class);
    target.previous_btn = Utils.findRequiredViewAsType(source, R.id.previous_btn, "field 'previous_btn'", FrameLayout.class);
    target.start_btn = Utils.findRequiredViewAsType(source, R.id.start_btn, "field 'start_btn'", TextView.class);
    target.start_btn_cover = Utils.findRequiredViewAsType(source, R.id.start_btn_cover, "field 'start_btn_cover'", FrameLayout.class);
    target.next_btn = Utils.findRequiredViewAsType(source, R.id.next_btn, "field 'next_btn'", FrameLayout.class);
    target.user_speak_content = Utils.findRequiredViewAsType(source, R.id.user_speak_content, "field 'user_speak_content'", TextView.class);
    target.user_speak_score = Utils.findRequiredViewAsType(source, R.id.user_speak_score, "field 'user_speak_score'", TextView.class);
    target.user_speak_cover = Utils.findRequiredViewAsType(source, R.id.user_speak_cover, "field 'user_speak_cover'", FrameLayout.class);
    target.sentence_cb = Utils.findRequiredViewAsType(source, R.id.sentence_cb, "field 'sentence_cb'", RadioButton.class);
    target.sentence_cover = Utils.findRequiredViewAsType(source, R.id.sentence_cover, "field 'sentence_cover'", FrameLayout.class);
    target.continuity_cb = Utils.findRequiredViewAsType(source, R.id.continuity_cb, "field 'continuity_cb'", RadioButton.class);
    target.continuity_cover = Utils.findRequiredViewAsType(source, R.id.continuity_cover, "field 'continuity_cover'", FrameLayout.class);
    target.conversation_cb = Utils.findRequiredViewAsType(source, R.id.conversation_cb, "field 'conversation_cb'", RadioButton.class);
    target.conversation_cover = Utils.findRequiredViewAsType(source, R.id.conversation_cover, "field 'conversation_cover'", FrameLayout.class);
    target.record_anim_img = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'record_anim_img'", ImageView.class);
    target.user_voice_play_img = Utils.findRequiredViewAsType(source, R.id.user_voice_play_img, "field 'user_voice_play_img'", ImageView.class);
    target.record_layout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'record_layout'", LinearLayout.class);
    target.record_animation_text = Utils.findRequiredViewAsType(source, R.id.record_animation_text, "field 'record_animation_text'", TextView.class);
    target.record_animation_layout = Utils.findRequiredViewAsType(source, R.id.record_animation_layout, "field 'record_animation_layout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SpokenEnglishDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.evaluation_en_tv = null;
    target.voice_play_answer = null;
    target.record_answer_cover = null;
    target.evaluation_zh_tv = null;
    target.show_zh_img = null;
    target.previous_btn = null;
    target.start_btn = null;
    target.start_btn_cover = null;
    target.next_btn = null;
    target.user_speak_content = null;
    target.user_speak_score = null;
    target.user_speak_cover = null;
    target.sentence_cb = null;
    target.sentence_cover = null;
    target.continuity_cb = null;
    target.continuity_cover = null;
    target.conversation_cb = null;
    target.conversation_cover = null;
    target.record_anim_img = null;
    target.user_voice_play_img = null;
    target.record_layout = null;
    target.record_animation_text = null;
    target.record_animation_layout = null;
  }
}
