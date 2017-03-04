// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.GridView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChDicBushouPinyinActivity_ViewBinding implements Unbinder {
  private ChDicBushouPinyinActivity target;

  @UiThread
  public ChDicBushouPinyinActivity_ViewBinding(ChDicBushouPinyinActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChDicBushouPinyinActivity_ViewBinding(ChDicBushouPinyinActivity target, View source) {
    this.target = target;

    target.studycategoryLv = Utils.findRequiredViewAsType(source, R.id.studycategory_lv, "field 'studycategoryLv'", GridView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChDicBushouPinyinActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.studycategoryLv = null;
  }
}
