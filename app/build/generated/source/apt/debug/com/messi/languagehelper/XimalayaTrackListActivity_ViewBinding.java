// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XimalayaTrackListActivity_ViewBinding implements Unbinder {
  private XimalayaTrackListActivity target;

  private View view7f080078;

  private View view7f080075;

  @UiThread
  public XimalayaTrackListActivity_ViewBinding(XimalayaTrackListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XimalayaTrackListActivity_ViewBinding(final XimalayaTrackListActivity target,
      View source) {
    this.target = target;

    View view;
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    target.itemImg = Utils.findRequiredViewAsType(source, R.id.item_img, "field 'itemImg'", SimpleDraweeView.class);
    target.trackTitle = Utils.findRequiredViewAsType(source, R.id.track_title, "field 'trackTitle'", TextView.class);
    target.trackInfo = Utils.findRequiredViewAsType(source, R.id.track_info, "field 'trackInfo'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_sort, "field 'btnSort' and method 'onViewClicked'");
    target.btnSort = Utils.castView(view, R.id.btn_sort, "field 'btnSort'", FrameLayout.class);
    view7f080078 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_download, "field 'btnDownload' and method 'onViewClicked'");
    target.btnDownload = Utils.castView(view, R.id.btn_download, "field 'btnDownload'", FrameLayout.class);
    view7f080075 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.imgSort = Utils.findRequiredViewAsType(source, R.id.img_sort, "field 'imgSort'", ImageView.class);
    target.pageCount = Utils.findRequiredViewAsType(source, R.id.page_count, "field 'pageCount'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XimalayaTrackListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview = null;
    target.itemImg = null;
    target.trackTitle = null;
    target.trackInfo = null;
    target.btnSort = null;
    target.btnDownload = null;
    target.imgSort = null;
    target.pageCount = null;

    view7f080078.setOnClickListener(null);
    view7f080078 = null;
    view7f080075.setOnClickListener(null);
    view7f080075 = null;
  }
}
