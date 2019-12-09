// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EnDicFragment_ViewBinding implements Unbinder {
  private EnDicFragment target;

  @UiThread
  public EnDicFragment_ViewBinding(EnDicFragment target, View source) {
    this.target = target;

    target.dic_content = Utils.findRequiredViewAsType(source, R.id.dic_content, "field 'dic_content'", TextView.class);
    target.dic_scrollview = Utils.findRequiredViewAsType(source, R.id.dic_scrollview, "field 'dic_scrollview'", ScrollView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EnDicFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.dic_content = null;
    target.dic_scrollview = null;
  }
}
