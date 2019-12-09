// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.messi.languagehelper.views.ZoomableDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ImgViewActivity_ViewBinding implements Unbinder {
  private ImgViewActivity target;

  private View view7f080095;

  private View view7f0800dd;

  private View view7f08025f;

  @UiThread
  public ImgViewActivity_ViewBinding(ImgViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ImgViewActivity_ViewBinding(final ImgViewActivity target, View source) {
    this.target = target;

    View view;
    target.itemImg = Utils.findRequiredViewAsType(source, R.id.item_img, "field 'itemImg'", ZoomableDraweeView.class);
    view = Utils.findRequiredView(source, R.id.close_img, "field 'closeImg' and method 'onViewClicked'");
    target.closeImg = Utils.castView(view, R.id.close_img, "field 'closeImg'", ImageView.class);
    view7f080095 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.download_img, "field 'downloadImg' and method 'onViewClicked'");
    target.downloadImg = Utils.castView(view, R.id.download_img, "field 'downloadImg'", ImageView.class);
    view7f0800dd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.share_img, "field 'shareImg' and method 'onViewClicked'");
    target.shareImg = Utils.castView(view, R.id.share_img, "field 'shareImg'", ImageView.class);
    view7f08025f = view;
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
    ImgViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.itemImg = null;
    target.closeImg = null;
    target.downloadImg = null;
    target.shareImg = null;

    view7f080095.setOnClickListener(null);
    view7f080095 = null;
    view7f0800dd.setOnClickListener(null);
    view7f0800dd = null;
    view7f08025f.setOnClickListener(null);
    view7f08025f = null;
  }
}
