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

public class WordStudySummaryListActivity_ViewBinding implements Unbinder {
  private WordStudySummaryListActivity target;

  @UiThread
  public WordStudySummaryListActivity_ViewBinding(WordStudySummaryListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudySummaryListActivity_ViewBinding(WordStudySummaryListActivity target,
      View source) {
    this.target = target;

    target.menulistview = Utils.findRequiredViewAsType(source, R.id.menulistview, "field 'menulistview'", RecyclerView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WordStudySummaryListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.menulistview = null;
    target.listview = null;
  }
}
