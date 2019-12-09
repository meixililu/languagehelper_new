// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EssayFragment_ViewBinding implements Unbinder {
  private EssayFragment target;

  private View view7f080046;

  @UiThread
  public EssayFragment_ViewBinding(final EssayFragment target, View source) {
    this.target = target;

    View view;
    target.question = Utils.findRequiredViewAsType(source, R.id.question, "field 'question'", TextView.class);
    target.answer = Utils.findRequiredViewAsType(source, R.id.answer, "field 'answer'", TextView.class);
    view = Utils.findRequiredView(source, R.id.answer_cover, "field 'answerCover' and method 'onClick'");
    target.answerCover = Utils.castView(view, R.id.answer_cover, "field 'answerCover'", FrameLayout.class);
    view7f080046 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EssayFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.question = null;
    target.answer = null;
    target.answerCover = null;

    view7f080046.setOnClickListener(null);
    view7f080046 = null;
  }
}
