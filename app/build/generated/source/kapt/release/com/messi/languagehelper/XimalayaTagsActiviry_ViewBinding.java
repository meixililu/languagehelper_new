// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XimalayaTagsActiviry_ViewBinding implements Unbinder {
  private XimalayaTagsActiviry target;

  @UiThread
  public XimalayaTagsActiviry_ViewBinding(XimalayaTagsActiviry target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XimalayaTagsActiviry_ViewBinding(XimalayaTagsActiviry target, View source) {
    this.target = target;

    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    target.tablayout = Utils.findRequiredViewAsType(source, R.id.tablayout, "field 'tablayout'", TabLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XimalayaTagsActiviry target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview = null;
    target.tablayout = null;
  }
}
