// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XimalayaDetailActivity_ViewBinding implements Unbinder {
  private XimalayaDetailActivity target;

  private View view7f0801c9;

  private View view7f0801cd;

  private View view7f0801cc;

  private View view7f080063;

  @UiThread
  public XimalayaDetailActivity_ViewBinding(XimalayaDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XimalayaDetailActivity_ViewBinding(final XimalayaDetailActivity target, View source) {
    this.target = target;

    View view;
    target.itemImg = Utils.findRequiredViewAsType(source, R.id.item_img, "field 'itemImg'", SimpleDraweeView.class);
    target.announcer_icon = Utils.findRequiredViewAsType(source, R.id.announcer_icon, "field 'announcer_icon'", SimpleDraweeView.class);
    target.announcer_info = Utils.findRequiredViewAsType(source, R.id.announcer_info, "field 'announcer_info'", TextView.class);
    target.albumTitle = Utils.findRequiredViewAsType(source, R.id.album_title, "field 'albumTitle'", TextView.class);
    target.trackContent = Utils.findRequiredViewAsType(source, R.id.track_content, "field 'trackContent'", TextView.class);
    target.trackIntro = Utils.findRequiredViewAsType(source, R.id.track_intro, "field 'trackIntro'", TextView.class);
    target.seekbar = Utils.findRequiredViewAsType(source, R.id.seekbar, "field 'seekbar'", SeekBar.class);
    target.playTimeCurrent = Utils.findRequiredViewAsType(source, R.id.play_time_current, "field 'playTimeCurrent'", TextView.class);
    target.playTimeDuration = Utils.findRequiredViewAsType(source, R.id.play_time_duration, "field 'playTimeDuration'", TextView.class);
    view = Utils.findRequiredView(source, R.id.play_btn, "field 'playBtn' and method 'onViewClicked'");
    target.playBtn = Utils.castView(view, R.id.play_btn, "field 'playBtn'", ImageView.class);
    view7f0801c9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.play_previous, "field 'playPrevious' and method 'onViewClicked'");
    target.playPrevious = Utils.castView(view, R.id.play_previous, "field 'playPrevious'", ImageView.class);
    view7f0801cd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.play_next, "field 'playNext' and method 'onViewClicked'");
    target.playNext = Utils.castView(view, R.id.play_next, "field 'playNext'", ImageView.class);
    view7f0801cc = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    target.adClose = Utils.findRequiredViewAsType(source, R.id.ad_close, "field 'adClose'", ImageView.class);
    target.adTitle = Utils.findRequiredViewAsType(source, R.id.ad_title, "field 'adTitle'", TextView.class);
    target.adBtn = Utils.findRequiredViewAsType(source, R.id.ad_btn, "field 'adBtn'", TextView.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", LinearLayout.class);
    target.ad_layout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'ad_layout'", FrameLayout.class);
    target.imgCover = Utils.findRequiredViewAsType(source, R.id.img_cover, "field 'imgCover'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.back_btn, "field 'backBtn' and method 'onViewClicked'");
    target.backBtn = Utils.castView(view, R.id.back_btn, "field 'backBtn'", FrameLayout.class);
    view7f080063 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    XimalayaDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.itemImg = null;
    target.announcer_icon = null;
    target.announcer_info = null;
    target.albumTitle = null;
    target.trackContent = null;
    target.trackIntro = null;
    target.seekbar = null;
    target.playTimeCurrent = null;
    target.playTimeDuration = null;
    target.playBtn = null;
    target.playPrevious = null;
    target.playNext = null;
    target.adImg = null;
    target.adClose = null;
    target.adTitle = null;
    target.adBtn = null;
    target.xx_ad_layout = null;
    target.ad_layout = null;
    target.imgCover = null;
    target.backBtn = null;

    view7f0801c9.setOnClickListener(null);
    view7f0801c9 = null;
    view7f0801cd.setOnClickListener(null);
    view7f0801cd = null;
    view7f0801cc.setOnClickListener(null);
    view7f0801cc = null;
    view7f080063.setOnClickListener(null);
    view7f080063 = null;
  }
}
