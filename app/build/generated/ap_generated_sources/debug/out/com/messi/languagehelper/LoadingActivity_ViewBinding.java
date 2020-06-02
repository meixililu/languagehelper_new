// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoadingActivity_ViewBinding implements Unbinder {
  private LoadingActivity target;

  @UiThread
  public LoadingActivity_ViewBinding(LoadingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoadingActivity_ViewBinding(LoadingActivity target, View source) {
    this.target = target;

    target.ad_source = Utils.findRequiredViewAsType(source, R.id.ad_source, "field 'ad_source'", TextView.class);
    target.skip_view = Utils.findRequiredViewAsType(source, R.id.skip_view, "field 'skip_view'", TextView.class);
    target.ad_img = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'ad_img'", SimpleDraweeView.class);
    target.splash_container = Utils.findRequiredViewAsType(source, R.id.splash_container, "field 'splash_container'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LoadingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ad_source = null;
    target.skip_view = null;
    target.ad_img = null;
    target.splash_container = null;
  }
}
