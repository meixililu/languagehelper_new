// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.exoplayer2.ui.PlayerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReadDetailTouTiaoActivity_ViewBinding implements Unbinder {
  private ReadDetailTouTiaoActivity target;

  private View view7f08006c;

  private View view7f0800a9;

  @UiThread
  public ReadDetailTouTiaoActivity_ViewBinding(ReadDetailTouTiaoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReadDetailTouTiaoActivity_ViewBinding(final ReadDetailTouTiaoActivity target,
      View source) {
    this.target = target;

    View view;
    target.mWebView = Utils.findRequiredViewAsType(source, R.id.refreshable_webview, "field 'mWebView'", WebView.class);
    target.simpleExoPlayerView = Utils.findRequiredViewAsType(source, R.id.player_view, "field 'simpleExoPlayerView'", PlayerView.class);
    target.titleTv = Utils.findRequiredViewAsType(source, R.id.title_tv, "field 'titleTv'", TextView.class);
    target.videoLayout = Utils.findRequiredViewAsType(source, R.id.video_ly, "field 'videoLayout'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.back_btn, "field 'backBtn' and method 'onViewClicked'");
    target.backBtn = Utils.castView(view, R.id.back_btn, "field 'backBtn'", LinearLayout.class);
    view7f08006c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.nextComposition = Utils.findRequiredViewAsType(source, R.id.next_composition, "field 'nextComposition'", LinearLayout.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", FrameLayout.class);
    target.webview_layout = Utils.findRequiredViewAsType(source, R.id.webview_layout, "field 'webview_layout'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.collect_btn, "field 'collect_btn' and method 'onClick'");
    target.collect_btn = Utils.castView(view, R.id.collect_btn, "field 'collect_btn'", ImageView.class);
    view7f0800a9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ReadDetailTouTiaoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mWebView = null;
    target.simpleExoPlayerView = null;
    target.titleTv = null;
    target.videoLayout = null;
    target.backBtn = null;
    target.nextComposition = null;
    target.xx_ad_layout = null;
    target.webview_layout = null;
    target.collect_btn = null;

    view7f08006c.setOnClickListener(null);
    view7f08006c = null;
    view7f0800a9.setOnClickListener(null);
    view7f0800a9 = null;
  }
}
