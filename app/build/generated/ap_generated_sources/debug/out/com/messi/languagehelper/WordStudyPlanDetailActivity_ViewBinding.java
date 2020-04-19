// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyPlanDetailActivity_ViewBinding implements Unbinder {
  private WordStudyPlanDetailActivity target;

  private View view7f080078;

  private View view7f080263;

  private View view7f0800fd;

  private View view7f0800cf;

  private View view7f08009e;

  private View view7f0800d0;

  private View view7f080221;

  @UiThread
  public WordStudyPlanDetailActivity_ViewBinding(WordStudyPlanDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyPlanDetailActivity_ViewBinding(final WordStudyPlanDetailActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.bottom, "field 'bottom' and method 'onClick'");
    target.bottom = Utils.castView(view, R.id.bottom, "field 'bottom'", CardView.class);
    view7f080078 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.renzhi_layout, "field 'renzhiLayout' and method 'onClick'");
    target.renzhiLayout = Utils.castView(view, R.id.renzhi_layout, "field 'renzhiLayout'", FrameLayout.class);
    view7f080263 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.duyinxuanci_layout, "field 'duyinxuanciLayout' and method 'onClick'");
    target.duyinxuanciLayout = Utils.castView(view, R.id.duyinxuanci_layout, "field 'duyinxuanciLayout'", FrameLayout.class);
    view7f0800fd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.danciceshi_layout, "field 'danciceshiLayout' and method 'onClick'");
    target.danciceshiLayout = Utils.castView(view, R.id.danciceshi_layout, "field 'danciceshiLayout'", CardView.class);
    view7f0800cf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ciyixuanci_layout, "field 'ciyixuanciLayout' and method 'onClick'");
    target.ciyixuanciLayout = Utils.castView(view, R.id.ciyixuanci_layout, "field 'ciyixuanciLayout'", CardView.class);
    view7f08009e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.unitList = Utils.findRequiredViewAsType(source, R.id.unit_list, "field 'unitList'", GridView.class);
    view = Utils.findRequiredView(source, R.id.dancisuji_layout, "field 'dancisujiLayout' and method 'onClick'");
    target.dancisujiLayout = Utils.castView(view, R.id.dancisuji_layout, "field 'dancisujiLayout'", CardView.class);
    view7f0800d0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.pinxie_layout, "field 'pinxieLayout' and method 'onClick'");
    target.pinxieLayout = Utils.castView(view, R.id.pinxie_layout, "field 'pinxieLayout'", CardView.class);
    view7f080221 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    WordStudyPlanDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bottom = null;
    target.renzhiLayout = null;
    target.duyinxuanciLayout = null;
    target.danciceshiLayout = null;
    target.ciyixuanciLayout = null;
    target.unitList = null;
    target.dancisujiLayout = null;
    target.pinxieLayout = null;

    view7f080078.setOnClickListener(null);
    view7f080078 = null;
    view7f080263.setOnClickListener(null);
    view7f080263 = null;
    view7f0800fd.setOnClickListener(null);
    view7f0800fd = null;
    view7f0800cf.setOnClickListener(null);
    view7f0800cf = null;
    view7f08009e.setOnClickListener(null);
    view7f08009e = null;
    view7f0800d0.setOnClickListener(null);
    view7f0800d0 = null;
    view7f080221.setOnClickListener(null);
    view7f080221 = null;
  }
}
