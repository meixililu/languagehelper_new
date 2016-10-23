// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BrainTwistsActivity_ViewBinding<T extends BrainTwistsActivity> implements Unbinder {
  protected T target;

  private View view2131558540;

  @UiThread
  public BrainTwistsActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.question = Utils.findRequiredViewAsType(source, R.id.question, "field 'question'", TextView.class);
    view = Utils.findRequiredView(source, R.id.answer, "field 'answer' and method 'onClick'");
    target.answer = Utils.castView(view, R.id.answer, "field 'answer'", TextView.class);
    view2131558540 = view;
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
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.question = null;
    target.answer = null;

    view2131558540.setOnClickListener(null);
    view2131558540 = null;

    this.target = null;
  }
}
