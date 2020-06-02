// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PracticeYYSActivity_ViewBinding implements Unbinder {
  private PracticeYYSActivity target;

  @UiThread
  public PracticeYYSActivity_ViewBinding(PracticeYYSActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PracticeYYSActivity_ViewBinding(PracticeYYSActivity target, View source) {
    this.target = target;

    target.voice_btn_cover = Utils.findRequiredViewAsType(source, R.id.voice_btn_cover, "field 'voice_btn_cover'", FrameLayout.class);
    target.voice_btn = Utils.findRequiredViewAsType(source, R.id.voice_btn, "field 'voice_btn'", TextView.class);
    target.record_question_cover = Utils.findRequiredViewAsType(source, R.id.record_question_cover, "field 'record_question_cover'", FrameLayout.class);
    target.record_answer_cover = Utils.findRequiredViewAsType(source, R.id.record_answer_cover, "field 'record_answer_cover'", FrameLayout.class);
    target.voice_play_answer = Utils.findRequiredViewAsType(source, R.id.voice_play_answer, "field 'voice_play_answer'", ImageButton.class);
    target.voice_play_question = Utils.findRequiredViewAsType(source, R.id.voice_play_question, "field 'voice_play_question'", ImageButton.class);
    target.record_answer = Utils.findRequiredViewAsType(source, R.id.record_answer, "field 'record_answer'", TextView.class);
    target.record_question = Utils.findRequiredViewAsType(source, R.id.record_question, "field 'record_question'", TextView.class);
    target.recent_used_lv = Utils.findRequiredViewAsType(source, R.id.recent_used_lv, "field 'recent_used_lv'", RecyclerView.class);
    target.practice_prompt = Utils.findRequiredViewAsType(source, R.id.practice_prompt, "field 'practice_prompt'", TextView.class);
    target.record_anim_img = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'record_anim_img'", ImageView.class);
    target.record_layout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'record_layout'", LinearLayout.class);
    target.record_animation_text = Utils.findRequiredViewAsType(source, R.id.record_animation_text, "field 'record_animation_text'", TextView.class);
    target.record_animation_layout = Utils.findRequiredViewAsType(source, R.id.record_animation_layout, "field 'record_animation_layout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PracticeYYSActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.voice_btn_cover = null;
    target.voice_btn = null;
    target.record_question_cover = null;
    target.record_answer_cover = null;
    target.voice_play_answer = null;
    target.voice_play_question = null;
    target.record_answer = null;
    target.record_question = null;
    target.recent_used_lv = null;
    target.practice_prompt = null;
    target.record_anim_img = null;
    target.record_layout = null;
    target.record_animation_text = null;
    target.record_animation_layout = null;
  }
}
