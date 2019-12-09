// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SubjectFragment_ViewBinding implements Unbinder {
  private SubjectFragment target;

  @UiThread
  public SubjectFragment_ViewBinding(SubjectFragment target, View source) {
    this.target = target;

    target.category_lv = Utils.findRequiredViewAsType(source, R.id.studycategory_lv, "field 'category_lv'", RecyclerView.class);
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.my_awesome_toolbar, "field 'mToolbar'", Toolbar.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBarCircularIndetermininate, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SubjectFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.category_lv = null;
    target.mToolbar = null;
    target.progressBar = null;
  }
}
