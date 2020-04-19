// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XimalayaRadioDetailActivity_ViewBinding implements Unbinder {
  private XimalayaRadioDetailActivity target;

  private View view7f08022c;

  @UiThread
  public XimalayaRadioDetailActivity_ViewBinding(XimalayaRadioDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XimalayaRadioDetailActivity_ViewBinding(final XimalayaRadioDetailActivity target,
      View source) {
    this.target = target;

    View view;
    target.itemImg = Utils.findRequiredViewAsType(source, R.id.item_img, "field 'itemImg'", SimpleDraweeView.class);
    target.announcer_icon = Utils.findRequiredViewAsType(source, R.id.announcer_icon, "field 'announcer_icon'", SimpleDraweeView.class);
    target.announcer_info = Utils.findRequiredViewAsType(source, R.id.announcer_info, "field 'announcer_info'", TextView.class);
    target.albumTitle = Utils.findRequiredViewAsType(source, R.id.album_title, "field 'albumTitle'", TextView.class);
    target.playBtn = Utils.findRequiredViewAsType(source, R.id.btn_play, "field 'playBtn'", ImageView.class);
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    target.adClose = Utils.findRequiredViewAsType(source, R.id.ad_close, "field 'adClose'", ImageView.class);
    target.adTitle = Utils.findRequiredViewAsType(source, R.id.ad_title, "field 'adTitle'", TextView.class);
    target.adBtn = Utils.findRequiredViewAsType(source, R.id.ad_btn, "field 'adBtn'", TextView.class);
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", LinearLayout.class);
    target.ad_layout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'ad_layout'", FrameLayout.class);
    target.imgCover = Utils.findRequiredViewAsType(source, R.id.img_cover, "field 'imgCover'", ImageView.class);
    target.sourceName = Utils.findRequiredViewAsType(source, R.id.source_name, "field 'sourceName'", TextView.class);
    target.contentTv = Utils.findRequiredViewAsType(source, R.id.content_tv, "field 'contentTv'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.playbtn_layout, "method 'onViewClicked'");
    view7f08022c = view;
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
    XimalayaRadioDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.itemImg = null;
    target.announcer_icon = null;
    target.announcer_info = null;
    target.albumTitle = null;
    target.playBtn = null;
    target.adImg = null;
    target.adClose = null;
    target.adTitle = null;
    target.adBtn = null;
    target.xx_ad_layout = null;
    target.ad_layout = null;
    target.imgCover = null;
    target.sourceName = null;
    target.contentTv = null;

    view7f08022c.setOnClickListener(null);
    view7f08022c = null;
  }
}
