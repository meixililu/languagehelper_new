// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper.faxian;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.messi.languagehelper.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ConjectureFragment_ViewBinding implements Unbinder {
  private ConjectureFragment target;

  private View view7f080290;

  private View view7f080066;

  @UiThread
  public ConjectureFragment_ViewBinding(final ConjectureFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.question, "field 'question' and method 'onViewClicked'");
    target.question = Utils.castView(view, R.id.question, "field 'question'", TextView.class);
    view7f080290 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.answer, "field 'answer' and method 'onViewClicked'");
    target.answer = Utils.castView(view, R.id.answer, "field 'answer'", TextView.class);
    view7f080066 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ConjectureFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.question = null;
    target.answer = null;

    view7f080290.setOnClickListener(null);
    view7f080290 = null;
    view7f080066.setOnClickListener(null);
    view7f080066 = null;
  }
}
