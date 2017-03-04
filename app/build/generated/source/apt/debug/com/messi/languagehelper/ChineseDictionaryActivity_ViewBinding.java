// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChineseDictionaryActivity_ViewBinding implements Unbinder {
  private ChineseDictionaryActivity target;

  @UiThread
  public ChineseDictionaryActivity_ViewBinding(ChineseDictionaryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChineseDictionaryActivity_ViewBinding(ChineseDictionaryActivity target, View source) {
    this.target = target;

    target.tablayout = Utils.findRequiredViewAsType(source, R.id.tablayout, "field 'tablayout'", TabLayout.class);
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.pager, "field 'viewPager'", ViewPager.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChineseDictionaryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tablayout = null;
    target.viewPager = null;
  }
}
