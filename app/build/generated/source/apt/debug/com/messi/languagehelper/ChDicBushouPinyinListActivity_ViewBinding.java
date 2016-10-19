// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChDicBushouPinyinListActivity_ViewBinding<T extends ChDicBushouPinyinListActivity> implements Unbinder {
  protected T target;

  @UiThread
  public ChDicBushouPinyinListActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.studycategoryLv = Utils.findRequiredViewAsType(source, R.id.studycategory_lv, "field 'studycategoryLv'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.studycategoryLv = null;

    this.target = null;
  }
}
