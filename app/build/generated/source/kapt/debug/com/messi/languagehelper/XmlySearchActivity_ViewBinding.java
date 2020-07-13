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

  private View view7f0802ca;

  private View view7f0800bb;

  @UiThread
  public XmlySearchActivity_ViewBinding(XmlySearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XmlySearchActivity_ViewBinding(final XmlySearchActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.search_btn, "method 'onViewClicked'");
    view7f0802ca = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.clear_history, "method 'onViewClicked'");
    view7f0800bb = view;
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


    view7f0802ca.setOnClickListener(null);
    view7f0802ca = null;
    view7f0800bb.setOnClickListener(null);
    view7f0800bb = null;
  }
}
