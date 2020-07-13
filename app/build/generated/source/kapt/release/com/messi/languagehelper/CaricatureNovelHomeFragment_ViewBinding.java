// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CaricatureNovelHomeFragment_ViewBinding implements Unbinder {
  private CaricatureNovelHomeFragment target;

  private View view7f0802a2;

  private View view7f0801aa;

  private View view7f0801af;

  private View view7f0801b1;

  private View view7f0801ae;

  @UiThread
  public CaricatureNovelHomeFragment_ViewBinding(final CaricatureNovelHomeFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.search_btn, "field 'searchBtn' and method 'onViewClicked'");
    target.searchBtn = Utils.castView(view, R.id.search_btn, "field 'searchBtn'", FrameLayout.class);
    view7f0802a2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.myAwesomeToolbar = Utils.findRequiredViewAsType(source, R.id.my_awesome_toolbar, "field 'myAwesomeToolbar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.layout_free_novel, "field 'layoutFreeNovel' and method 'onViewClicked'");
    target.layoutFreeNovel = Utils.castView(view, R.id.layout_free_novel, "field 'layoutFreeNovel'", FrameLayout.class);
    view7f0801aa = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_rank_novel, "field 'layoutRankNovel' and method 'onViewClicked'");
    target.layoutRankNovel = Utils.castView(view, R.id.layout_rank_novel, "field 'layoutRankNovel'", FrameLayout.class);
    view7f0801af = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_short_novel, "field 'layoutShortNovel' and method 'onViewClicked'");
    target.layoutShortNovel = Utils.castView(view, R.id.layout_short_novel, "field 'layoutShortNovel'", FrameLayout.class);
    view7f0801b1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_novel_collected, "field 'layout_novel_collected' and method 'onViewClicked'");
    target.layout_novel_collected = Utils.castView(view, R.id.layout_novel_collected, "field 'layout_novel_collected'", FrameLayout.class);
    view7f0801ae = view;
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

    view7f0802a2.setOnClickListener(null);
    view7f0802a2 = null;
    view7f0801aa.setOnClickListener(null);
    view7f0801aa = null;
    view7f0801af.setOnClickListener(null);
    view7f0801af = null;
    view7f0801b1.setOnClickListener(null);
    view7f0801b1 = null;
    view7f0801ae.setOnClickListener(null);
    view7f0801ae = null;
  }
}
