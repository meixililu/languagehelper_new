// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReadingsBySubjectFragment_ViewBinding implements Unbinder {
  private ReadingsBySubjectFragment target;

  @UiThread
  public ReadingsBySubjectFragment_ViewBinding(ReadingsBySubjectFragment target, View source) {
    this.target = target;

    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    target.emptyTv = Utils.findRequiredViewAsType(source, R.id.empty_tv, "field 'emptyTv'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReadingsBySubjectFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview = null;
    target.emptyTv = null;
  }
}
