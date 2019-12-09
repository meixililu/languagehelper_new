// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HelpActivity_ViewBinding implements Unbinder {
  private HelpActivity target;

  private View view7f0802ae;

  private View view7f0802b1;

  @UiThread
  public HelpActivity_ViewBinding(HelpActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HelpActivity_ViewBinding(final HelpActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.style_one, "field 'styleOne' and method 'onClick'");
    target.styleOne = Utils.castView(view, R.id.style_one, "field 'styleOne'", RelativeLayout.class);
    view7f0802ae = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.style_two, "field 'styleTwo' and method 'onClick'");
    target.styleTwo = Utils.castView(view, R.id.style_two, "field 'styleTwo'", RelativeLayout.class);
    view7f0802b1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    HelpActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.styleOne = null;
    target.styleTwo = null;

    view7f0802ae.setOnClickListener(null);
    view7f0802ae = null;
    view7f0802b1.setOnClickListener(null);
    view7f0802b1 = null;
  }
}
