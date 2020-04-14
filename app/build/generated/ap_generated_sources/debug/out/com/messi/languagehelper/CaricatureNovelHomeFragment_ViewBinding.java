// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CaricatureNovelHomeFragment_ViewBinding implements Unbinder {
  private CaricatureNovelHomeFragment target;

  private View view7f080288;

  private View view7f08018e;

  private View view7f080192;

  private View view7f080194;

  private View view7f080191;

  @UiThread
  public CaricatureNovelHomeFragment_ViewBinding(final CaricatureNovelHomeFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.search_btn, "field 'searchBtn' and method 'onViewClicked'");
    target.searchBtn = Utils.castView(view, R.id.search_btn, "field 'searchBtn'", FrameLayout.class);
    view7f080288 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.myAwesomeToolbar = Utils.findRequiredViewAsType(source, R.id.my_awesome_toolbar, "field 'myAwesomeToolbar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.layout_free_novel, "field 'layoutFreeNovel' and method 'onViewClicked'");
    target.layoutFreeNovel = Utils.castView(view, R.id.layout_free_novel, "field 'layoutFreeNovel'", FrameLayout.class);
    view7f08018e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_rank_novel, "field 'layoutRankNovel' and method 'onViewClicked'");
    target.layoutRankNovel = Utils.castView(view, R.id.layout_rank_novel, "field 'layoutRankNovel'", FrameLayout.class);
    view7f080192 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_short_novel, "field 'layoutShortNovel' and method 'onViewClicked'");
    target.layoutShortNovel = Utils.castView(view, R.id.layout_short_novel, "field 'layoutShortNovel'", FrameLayout.class);
    view7f080194 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_novel_collected, "field 'layout_novel_collected' and method 'onViewClicked'");
    target.layout_novel_collected = Utils.castView(view, R.id.layout_novel_collected, "field 'layout_novel_collected'", FrameLayout.class);
    view7f080191 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mainContent = Utils.findRequiredViewAsType(source, R.id.main_content, "field 'mainContent'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CaricatureNovelHomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.searchBtn = null;
    target.myAwesomeToolbar = null;
    target.layoutFreeNovel = null;
    target.layoutRankNovel = null;
    target.layoutShortNovel = null;
    target.layout_novel_collected = null;
    target.mainContent = null;

    view7f080288.setOnClickListener(null);
    view7f080288 = null;
    view7f08018e.setOnClickListener(null);
    view7f08018e = null;
    view7f080192.setOnClickListener(null);
    view7f080192 = null;
    view7f080194.setOnClickListener(null);
    view7f080194 = null;
    view7f080191.setOnClickListener(null);
    view7f080191 = null;
  }
}
