// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.exoplayer2.ui.PlayerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XVideoDetailActivity_ViewBinding implements Unbinder {
  private XVideoDetailActivity target;

  private View view7f080063;

  @UiThread
  public XVideoDetailActivity_ViewBinding(XVideoDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XVideoDetailActivity_ViewBinding(final XVideoDetailActivity target, View source) {
    this.target = target;

    View view;
    target.simpleExoPlayerView = Utils.findRequiredViewAsType(source, R.id.player_view, "field 'simpleExoPlayerView'", PlayerView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    view = Utils.findRequiredView(source, R.id.back_btn, "field 'backBtn' and method 'onViewClicked'");
    target.backBtn = Utils.castView(view, R.id.back_btn, "field 'backBtn'", LinearLayout.class);
    view7f080063 = view;
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
    XVideoDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.simpleExoPlayerView = null;
    target.title = null;
    target.backBtn = null;

    view7f080063.setOnClickListener(null);
    view7f080063 = null;
  }
}
