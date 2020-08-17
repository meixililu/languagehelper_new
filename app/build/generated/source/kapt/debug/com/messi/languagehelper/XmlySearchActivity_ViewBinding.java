// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XmlySearchActivity_ViewBinding implements Unbinder {
  private XmlySearchActivity target;

  private View view7f0802f6;

  private View view7f0800c5;

  @UiThread
  public XmlySearchActivity_ViewBinding(XmlySearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XmlySearchActivity_ViewBinding(final XmlySearchActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.search_btn, "method 'onViewClicked'");
    view7f0802f6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.clear_history, "method 'onViewClicked'");
    view7f0800c5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view7f0802f6.setOnClickListener(null);
    view7f0802f6 = null;
    view7f0800c5.setOnClickListener(null);
    view7f0800c5 = null;
  }
}
