// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LeisureFragment_ViewBinding<T extends LeisureFragment> implements Unbinder {
  protected T target;

  private View view2131558684;

  @UiThread
  public LeisureFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.yueduLayout = Utils.findRequiredViewAsType(source, R.id.yuedu_layout, "field 'yueduLayout'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.twists_layout, "field 'twistsLayout' and method 'onClick'");
    target.twistsLayout = Utils.castView(view, R.id.twists_layout, "field 'twistsLayout'", FrameLayout.class);
    view2131558684 = view;
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

    target.yueduLayout = null;
    target.twistsLayout = null;

    view2131558684.setOnClickListener(null);
    view2131558684 = null;

    this.target = null;
  }
}
