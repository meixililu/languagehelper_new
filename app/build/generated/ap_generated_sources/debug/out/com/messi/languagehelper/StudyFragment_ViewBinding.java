// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StudyFragment_ViewBinding implements Unbinder {
  private StudyFragment target;

  private View view7f08024a;

  @UiThread
  public StudyFragment_ViewBinding(final StudyFragment target, View source) {
    this.target = target;

    View view;
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    target.tablayout = Utils.findRequiredViewAsType(source, R.id.tablayout, "field 'tablayout'", TabLayout.class);
    view = Utils.findRequiredView(source, R.id.search_btn, "field 'searchBtn' and method 'onViewClicked'");
    target.searchBtn = Utils.castView(view, R.id.search_btn, "field 'searchBtn'", FrameLayout.class);
    view7f08024a = view;
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
    StudyFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview = null;
    target.tablayout = null;
    target.searchBtn = null;

    view7f08024a.setOnClickListener(null);
    view7f08024a = null;
  }
}
