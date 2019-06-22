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

public class MiaosouDetailActivity_ViewBinding implements Unbinder {
  private MiaosouDetailActivity target;

  private View view7f08003b;

  private View view7f0802c4;

  private View view7f080063;

  private View view7f080251;

  @UiThread
  public MiaosouDetailActivity_ViewBinding(MiaosouDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MiaosouDetailActivity_ViewBinding(final MiaosouDetailActivity target, View source) {
    this.target = target;

    View view;
    target.itemImgBg = Utils.findRequiredViewAsType(source, R.id.item_img_bg, "field 'itemImgBg'", SimpleDraweeView.class);
    target.itemImg = Utils.findRequiredViewAsType(source, R.id.item_img, "field 'itemImg'", SimpleDraweeView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.tags = Utils.findRequiredViewAsType(source, R.id.tags, "field 'tags'", TextView.class);
    target.author = Utils.findRequiredViewAsType(source, R.id.author, "field 'author'", TextView.class);
    target.source = Utils.findRequiredViewAsType(source, R.id.source, "field 'source'", TextView.class);
    target.views = Utils.findRequiredViewAsType(source, R.id.views, "field 'views'", TextView.class);
    target.itemLayout = Utils.findRequiredViewAsType(source, R.id.item_layout, "field 'itemLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.add_bookshelf, "field 'addBookshelf' and method 'onViewClicked'");
    target.addBookshelf = Utils.castView(view, R.id.add_bookshelf, "field 'addBookshelf'", TextView.class);
    view7f08003b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.to_read, "field 'toRead' and method 'onViewClicked'");
    target.toRead = Utils.castView(view, R.id.to_read, "field 'toRead'", TextView.class);
    view7f0802c4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.des = Utils.findRequiredViewAsType(source, R.id.des, "field 'des'", TextView.class);
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    target.adSign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'adSign'", TextView.class);
    target.xxAdLayout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xxAdLayout'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.back_btn, "field 'backBtn' and method 'onViewClicked'");
    target.backBtn = Utils.castView(view, R.id.back_btn, "field 'backBtn'", ImageView.class);
    view7f080063 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.share_img, "field 'shareImg' and method 'onViewClicked'");
    target.shareImg = Utils.castView(view, R.id.share_img, "field 'shareImg'", ImageView.class);
    view7f080251 = view;
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
    MiaosouDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.itemImgBg = null;
    target.itemImg = null;
    target.name = null;
    target.tags = null;
    target.author = null;
    target.source = null;
    target.views = null;
    target.itemLayout = null;
    target.addBookshelf = null;
    target.toRead = null;
    target.des = null;
    target.adImg = null;
    target.adSign = null;
    target.xxAdLayout = null;
    target.backBtn = null;
    target.shareImg = null;

    view7f08003b.setOnClickListener(null);
    view7f08003b = null;
    view7f0802c4.setOnClickListener(null);
    view7f0802c4 = null;
    view7f080063.setOnClickListener(null);
    view7f080063 = null;
    view7f080251.setOnClickListener(null);
    view7f080251 = null;
  }
}
