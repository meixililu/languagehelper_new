// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ReadingDetailActivity_ViewBinding implements Unbinder {
  private ReadingDetailActivity target;

  private View view2131689696;

  @UiThread
  public ReadingDetailActivity_ViewBinding(ReadingDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ReadingDetailActivity_ViewBinding(final ReadingDetailActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar_layout = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'toolbar_layout'", CollapsingToolbarLayout.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", TextView.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", RelativeLayout.class);
    target.next_composition = Utils.findRequiredViewAsType(source, R.id.next_composition, "field 'next_composition'", LinearLayout.class);
    target.scrollview = Utils.findRequiredViewAsType(source, R.id.scrollview, "field 'scrollview'", NestedScrollView.class);
    view = Utils.findRequiredView(source, R.id.play_btn, "field 'fab' and method 'onClick'");
    target.fab = Utils.castView(view, R.id.play_btn, "field 'fab'", FloatingActionButton.class);
    view2131689696 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.pimgview = Utils.findRequiredViewAsType(source, R.id.item_img, "field 'pimgview'", SimpleDraweeView.class);
    target.videoplayer = Utils.findRequiredViewAsType(source, R.id.videoplayer, "field 'videoplayer'", JCVideoPlayerStandard.class);
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
    target.xx_ad_layout = null;
    target.next_composition = null;
    target.scrollview = null;
    target.fab = null;
    target.pimgview = null;
    target.videoplayer = null;

    view2131689696.setOnClickListener(null);
    view2131689696 = null;
  }
}
