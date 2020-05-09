// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyNewWordActivity_ViewBinding implements Unbinder {
  private WordStudyNewWordActivity target;

  private View view7f08027b;

  private View view7f0800ff;

  private View view7f0800d1;

  private View view7f08009f;

  private View view7f080078;

  private View view7f0800d2;

  private View view7f080238;

  @UiThread
  public WordStudyNewWordActivity_ViewBinding(WordStudyNewWordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyNewWordActivity_ViewBinding(final WordStudyNewWordActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.renzhi_layout, "field 'renzhiLayout' and method 'onClick'");
    target.renzhiLayout = Utils.castView(view, R.id.renzhi_layout, "field 'renzhiLayout'", FrameLayout.class);
    view7f08027b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.duyinxuanci_layout, "field 'duyinxuanciLayout' and method 'onClick'");
    target.duyinxuanciLayout = Utils.castView(view, R.id.duyinxuanci_layout, "field 'duyinxuanciLayout'", FrameLayout.class);
    view7f0800ff = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.danciceshi_layout, "field 'danciceshiLayout' and method 'onClick'");
    target.danciceshiLayout = Utils.castView(view, R.id.danciceshi_layout, "field 'danciceshiLayout'", CardView.class);
    view7f0800d1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ciyixuanci_layout, "field 'ciyixuanciLayout' and method 'onClick'");
    target.ciyixuanciLayout = Utils.castView(view, R.id.ciyixuanci_layout, "field 'ciyixuanciLayout'", CardView.class);
    view7f08009f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bottom, "field 'bottom' and method 'onClick'");
    target.bottom = Utils.castView(view, R.id.bottom, "field 'bottom'", CardView.class);
    view7f080078 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.dancisuji_layout, "field 'dancisujiLayout' and method 'onClick'");
    target.dancisujiLayout = Utils.castView(view, R.id.dancisuji_layout, "field 'dancisujiLayout'", CardView.class);
    view7f0800d2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.pinxie_layout, "field 'pinxieLayout' and method 'onClick'");
    target.pinxieLayout = Utils.castView(view, R.id.pinxie_layout, "field 'pinxieLayout'", CardView.class);
    view7f080238 = view;
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
    WordStudyNewWordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.renzhiLayout = null;
    target.duyinxuanciLayout = null;
    target.danciceshiLayout = null;
    target.ciyixuanciLayout = null;
    target.bottom = null;
    target.dancisujiLayout = null;
    target.pinxieLayout = null;

    view7f08027b.setOnClickListener(null);
    view7f08027b = null;
    view7f0800ff.setOnClickListener(null);
    view7f0800ff = null;
    view7f0800d1.setOnClickListener(null);
    view7f0800d1 = null;
    view7f08009f.setOnClickListener(null);
    view7f08009f = null;
    view7f080078.setOnClickListener(null);
    view7f080078 = null;
    view7f0800d2.setOnClickListener(null);
    view7f0800d2 = null;
    view7f080238.setOnClickListener(null);
    view7f080238 = null;
  }
}
