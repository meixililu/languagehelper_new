// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GodReplyActivity_ViewBinding implements Unbinder {
  private GodReplyActivity target;

  private View view7f0801e5;

  @UiThread
  public GodReplyActivity_ViewBinding(GodReplyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public GodReplyActivity_ViewBinding(final GodReplyActivity target, View source) {
    this.target = target;

    View view;
    target.ad_layout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'ad_layout'", FrameLayout.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", FrameLayout.class);
    target.ad_sign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'ad_sign'", TextView.class);
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.question, "field 'question' and method 'onViewClicked'");
    target.question = Utils.castView(view, R.id.question, "field 'question'", TextView.class);
    view7f0801e5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    GodReplyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ad_layout = null;
    target.xx_ad_layout = null;
    target.ad_sign = null;
    target.adImg = null;
    target.question = null;

    view7f0801e5.setOnClickListener(null);
    view7f0801e5 = null;
  }
}
