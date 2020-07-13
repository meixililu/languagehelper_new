// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XmlyTagRecommendFragment_ViewBinding implements Unbinder {
  private XmlyTagRecommendFragment target;

  @UiThread
  public XmlyTagRecommendFragment_ViewBinding(XmlyTagRecommendFragment target, View source) {
    this.target = target;

    target.viewpager = Utils.findRequiredViewAsType(source, R.id.viewpager, "field 'viewpager'", ViewPager.class);
    target.tablayout = Utils.findRequiredViewAsType(source, R.id.tablayout, "field 'tablayout'", TabLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XmlyTagRecommendFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewpager = null;
    target.tablayout = null;
  }
}
