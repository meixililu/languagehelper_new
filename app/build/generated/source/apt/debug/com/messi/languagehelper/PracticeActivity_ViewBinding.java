// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PracticeActivity_ViewBinding<T extends PracticeActivity> implements Unbinder {
  protected T target;

  @UiThread
  public PracticeActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.voice_btn = Utils.findRequiredViewAsType(source, R.id.voice_btn, "field 'voice_btn'", TextView.class);
    target.record_question_cover = Utils.findRequiredViewAsType(source, R.id.record_question_cover, "field 'record_question_cover'", FrameLayout.class);
    target.record_answer_cover = Utils.findRequiredViewAsType(source, R.id.record_answer_cover, "field 'record_answer_cover'", FrameLayout.class);
    target.practice_page_exchange = Utils.findRequiredViewAsType(source, R.id.practice_page_exchange, "field 'practice_page_exchange'", FrameLayout.class);
    target.voice_play_answer = Utils.findRequiredViewAsType(source, R.id.voice_play_answer, "field 'voice_play_answer'", ImageButton.class);
    target.voice_play_question = Utils.findRequiredViewAsType(source, R.id.voice_play_question, "field 'voice_play_question'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.voice_btn = null;
    target.record_question_cover = null;
    target.record_answer_cover = null;
    target.practice_page_exchange = null;
    target.voice_play_answer = null;
    target.voice_play_question = null;

    this.target = null;
  }
}
