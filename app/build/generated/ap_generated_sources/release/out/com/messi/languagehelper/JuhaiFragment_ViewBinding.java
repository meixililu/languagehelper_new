// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class JuhaiFragment_ViewBinding implements Unbinder {
  private JuhaiFragment target;

  @UiThread
  public JuhaiFragment_ViewBinding(JuhaiFragment target, View source) {
    this.target = target;

    target.recent_used_lv = Utils.findRequiredViewAsType(source, R.id.recent_used_lv, "field 'recent_used_lv'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    JuhaiFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recent_used_lv = null;
  }
}
