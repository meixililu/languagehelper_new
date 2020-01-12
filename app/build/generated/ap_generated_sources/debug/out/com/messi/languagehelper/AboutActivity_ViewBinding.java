// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AboutActivity_ViewBinding implements Unbinder {
  private AboutActivity target;

  private View view7f0800e5;

  private View view7f080140;

  @UiThread
  public AboutActivity_ViewBinding(AboutActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AboutActivity_ViewBinding(final AboutActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.email_layout, "field 'email_layout' and method 'onClick'");
    target.email_layout = Utils.castView(view, R.id.email_layout, "field 'email_layout'", TextView.class);
    view7f0800e5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.app_version = Utils.findRequiredViewAsType(source, R.id.app_version, "field 'app_version'", TextView.class);
    view = Utils.findRequiredView(source, R.id.img_logo, "field 'img_logo' and method 'onImgClick'");
    target.img_logo = Utils.castView(view, R.id.img_logo, "field 'img_logo'", ImageView.class);
    view7f080140 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onImgClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AboutActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.email_layout = null;
    target.app_version = null;
    target.img_logo = null;

    view7f0800e5.setOnClickListener(null);
    view7f0800e5 = null;
    view7f080140.setOnClickListener(null);
    view7f080140 = null;
  }
}
