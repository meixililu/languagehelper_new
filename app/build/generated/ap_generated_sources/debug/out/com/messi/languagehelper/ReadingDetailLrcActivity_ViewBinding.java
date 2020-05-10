// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReadingDetailLrcActivity_ViewBinding implements Unbinder {
  private ReadingDetailLrcActivity target;

  private View view7f080083;

  @UiThread
  public ReadingDetailLrcActivity_ViewBinding(ReadingDetailLrcActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReadingDetailLrcActivity_ViewBinding(final ReadingDetailLrcActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar_layout = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'toolbar_layout'", CollapsingToolbarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", TextView.class);
    target.ad_sign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'ad_sign'", TextView.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", FrameLayout.class);
    target.ad_layout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'ad_layout'", FrameLayout.class);
    target.ad_img = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'ad_img'", SimpleDraweeView.class);
    target.scrollview = Utils.findRequiredViewAsType(source, R.id.scrollview, "field 'scrollview'", NestedScrollView.class);
    target.player_layout = Utils.findRequiredViewAsType(source, R.id.player_layout, "field 'player_layout'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.btn_play, "field 'btn_play' and method 'onClick'");
    target.btn_play = Utils.castView(view, R.id.btn_play, "field 'btn_play'", ImageView.class);
    view7f080083 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.seekbar = Utils.findRequiredViewAsType(source, R.id.seekbar, "field 'seekbar'", SeekBar.class);
    target.time_current = Utils.findRequiredViewAsType(source, R.id.time_current, "field 'time_current'", TextView.class);
    target.time_duration = Utils.findRequiredViewAsType(source, R.id.time_duration, "field 'time_duration'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReadingDetailLrcActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar_layout = null;
    target.title = null;
    target.content = null;
    target.ad_sign = null;
    target.xx_ad_layout = null;
    target.ad_layout = null;
    target.ad_img = null;
    target.scrollview = null;
    target.player_layout = null;
    target.btn_play = null;
    target.seekbar = null;
    target.time_current = null;
    target.time_duration = null;

    view7f080083.setOnClickListener(null);
    view7f080083 = null;
  }
}
