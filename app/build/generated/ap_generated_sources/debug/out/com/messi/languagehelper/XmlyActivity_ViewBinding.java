// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XmlyActivity_ViewBinding implements Unbinder {
  private XmlyActivity target;

  @UiThread
  public XmlyActivity_ViewBinding(XmlyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XmlyActivity_ViewBinding(XmlyActivity target, View source) {
    this.target = target;

    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XmlyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.content = null;
  }
}
