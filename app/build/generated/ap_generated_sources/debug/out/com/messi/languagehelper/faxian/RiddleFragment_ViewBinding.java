// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper.faxian;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.messi.languagehelper.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RiddleFragment_ViewBinding implements Unbinder {
  private RiddleFragment target;

  private View view7f080251;

  private View view7f08004c;

  @UiThread
  public RiddleFragment_ViewBinding(final RiddleFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.question, "field 'question' and method 'onClick'");
    target.question = Utils.castView(view, R.id.question, "field 'question'", TextView.class);
    view7f080251 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.answer, "field 'answer' and method 'onClick'");
    target.answer = Utils.castView(view, R.id.answer, "field 'answer'", TextView.class);
    view7f08004c = view;
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
    RiddleFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.question = null;
    target.answer = null;

    view7f080251.setOnClickListener(null);
    view7f080251 = null;
    view7f08004c.setOnClickListener(null);
    view7f08004c = null;
  }
}
