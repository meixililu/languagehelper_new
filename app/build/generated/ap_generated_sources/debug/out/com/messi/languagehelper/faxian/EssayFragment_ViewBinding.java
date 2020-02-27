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

public class EssayFragment_ViewBinding implements Unbinder {
  private EssayFragment target;

  private View view7f080211;

  private View view7f080045;

  @UiThread
  public EssayFragment_ViewBinding(final EssayFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.question, "field 'question' and method 'onClick'");
    target.question = Utils.castView(view, R.id.question, "field 'question'", TextView.class);
    view7f080211 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.answer, "field 'answer' and method 'onClick'");
    target.answer = Utils.castView(view, R.id.answer, "field 'answer'", TextView.class);
    view7f080045 = view;
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

    view7f080211.setOnClickListener(null);
    view7f080211 = null;
    view7f080045.setOnClickListener(null);
    view7f080045 = null;
  }
}
