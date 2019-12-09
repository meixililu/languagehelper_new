// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DictionaryFragmentOld_ViewBinding implements Unbinder {
  private DictionaryFragmentOld target;

  @UiThread
  public DictionaryFragmentOld_ViewBinding(DictionaryFragmentOld target, View source) {
    this.target = target;

    target.recent_used_lv = Utils.findRequiredViewAsType(source, R.id.recent_used_lv, "field 'recent_used_lv'", RecyclerView.class);
    target.cidianResultLayout = Utils.findRequiredViewAsType(source, R.id.cidian_result_layout, "field 'cidianResultLayout'", NestedScrollView.class);
    target.dicResultLayout = Utils.findRequiredViewAsType(source, R.id.dic_result_layout, "field 'dicResultLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DictionaryFragmentOld target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recent_used_lv = null;
    target.cidianResultLayout = null;
    target.dicResultLayout = null;
  }
}
