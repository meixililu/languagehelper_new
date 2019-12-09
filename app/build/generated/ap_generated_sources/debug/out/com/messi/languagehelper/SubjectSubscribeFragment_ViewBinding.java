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

public class SubjectSubscribeFragment_ViewBinding implements Unbinder {
  private SubjectSubscribeFragment target;

  @UiThread
  public SubjectSubscribeFragment_ViewBinding(SubjectSubscribeFragment target, View source) {
    this.target = target;

    target.category_lv = Utils.findRequiredViewAsType(source, R.id.studycategory_lv, "field 'category_lv'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SubjectSubscribeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.category_lv = null;
  }
}
