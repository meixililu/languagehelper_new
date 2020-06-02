// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.core.widget.NestedScrollView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PybsDetailActivity_ViewBinding implements Unbinder {
  private PybsDetailActivity target;

  @UiThread
  public PybsDetailActivity_ViewBinding(PybsDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PybsDetailActivity_ViewBinding(PybsDetailActivity target, View source) {
    this.target = target;

    target.toolbar_layout = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'toolbar_layout'", CollapsingToolbarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", TextView.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", FrameLayout.class);
    target.scrollview = Utils.findRequiredViewAsType(source, R.id.scrollview, "field 'scrollview'", NestedScrollView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PybsDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar_layout = null;
    target.title = null;
    target.content = null;
    target.xx_ad_layout = null;
    target.scrollview = null;
  }
}
