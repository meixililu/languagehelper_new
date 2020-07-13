// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.core.widget.NestedScrollView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReadingDetailActivity_ViewBinding implements Unbinder {
  private ReadingDetailActivity target;

  @UiThread
  public ReadingDetailActivity_ViewBinding(ReadingDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReadingDetailActivity_ViewBinding(ReadingDetailActivity target, View source) {
    this.target = target;

    target.toolbar_layout = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'toolbar_layout'", CollapsingToolbarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", TextView.class);
    target.ad_sign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'ad_sign'", TextView.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", FrameLayout.class);
    target.ad_layout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'ad_layout'", FrameLayout.class);
    target.ad_img = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'ad_img'", SimpleDraweeView.class);
    target.next_composition = Utils.findRequiredViewAsType(source, R.id.next_composition, "field 'next_composition'", LinearLayout.class);
    target.scrollview = Utils.findRequiredViewAsType(source, R.id.scrollview, "field 'scrollview'", NestedScrollView.class);
    target.player_layout = Utils.findRequiredViewAsType(source, R.id.player_layout, "field 'player_layout'", LinearLayout.class);
    target.btn_play = Utils.findRequiredViewAsType(source, R.id.btn_play, "field 'btn_play'", ImageView.class);
    target.seekbar = Utils.findRequiredViewAsType(source, R.id.seekbar, "field 'seekbar'", SeekBar.class);
    target.time_current = Utils.findRequiredViewAsType(source, R.id.time_current, "field 'time_current'", TextView.class);
    target.time_duration = Utils.findRequiredViewAsType(source, R.id.time_duration, "field 'time_duration'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ReadingDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar_layout = null;
    target.title = null;
    target.content = null;
    target.ad_sign = null;
    target.xx_ad_layout = null;
    target.ad_layout = null;
    target.ad_img = null;
    target.next_composition = null;
    target.scrollview = null;
    target.player_layout = null;
    target.btn_play = null;
    target.seekbar = null;
    target.time_current = null;
    target.time_duration = null;
  }
}
