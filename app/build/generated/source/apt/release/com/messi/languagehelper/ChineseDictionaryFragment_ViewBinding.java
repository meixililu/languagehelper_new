// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChineseDictionaryFragment_ViewBinding implements Unbinder {
  private ChineseDictionaryFragment target;

  private View view7f080073;

  private View view7f080076;

  private View view7f0801e7;

  private View view7f080206;

  private View view7f0800af;

  private View view7f08024e;

  @UiThread
  public ChineseDictionaryFragment_ViewBinding(final ChineseDictionaryFragment target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_bushou, "field 'btnBushou' and method 'onViewClicked'");
    target.btnBushou = Utils.castView(view, R.id.btn_bushou, "field 'btnBushou'", TextView.class);
    view7f080073 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_pinyin, "field 'btnPinyin' and method 'onViewClicked'");
    target.btnPinyin = Utils.castView(view, R.id.btn_pinyin, "field 'btnPinyin'", TextView.class);
    view7f080076 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.question_tv = Utils.findRequiredViewAsType(source, R.id.question_tv, "field 'question_tv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.question_tv_cover, "field 'question_tv_cover' and method 'onViewClicked'");
    target.question_tv_cover = Utils.castView(view, R.id.question_tv_cover, "field 'question_tv_cover'", FrameLayout.class);
    view7f0801e7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.result_tv = Utils.findRequiredViewAsType(source, R.id.result_tv, "field 'result_tv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.result_tv_cover, "field 'result_tv_cover' and method 'onViewClicked'");
    target.result_tv_cover = Utils.castView(view, R.id.result_tv_cover, "field 'result_tv_cover'", FrameLayout.class);
    view7f080206 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.copy_btn, "field 'copy_btn' and method 'onViewClicked'");
    target.copy_btn = Utils.castView(view, R.id.copy_btn, "field 'copy_btn'", FrameLayout.class);
    view7f0800af = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.share_btn, "field 'share_btn' and method 'onViewClicked'");
    target.share_btn = Utils.castView(view, R.id.share_btn, "field 'share_btn'", FrameLayout.class);
    view7f08024e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.chdic_sv = Utils.findRequiredViewAsType(source, R.id.chdic_sv, "field 'chdic_sv'", ScrollView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChineseDictionaryFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnBushou = null;
    target.btnPinyin = null;
    target.question_tv = null;
    target.question_tv_cover = null;
    target.result_tv = null;
    target.result_tv_cover = null;
    target.copy_btn = null;
    target.share_btn = null;
    target.chdic_sv = null;

    view7f080073.setOnClickListener(null);
    view7f080073 = null;
    view7f080076.setOnClickListener(null);
    view7f080076 = null;
    view7f0801e7.setOnClickListener(null);
    view7f0801e7 = null;
    view7f080206.setOnClickListener(null);
    view7f080206 = null;
    view7f0800af.setOnClickListener(null);
    view7f0800af = null;
    view7f08024e.setOnClickListener(null);
    view7f08024e = null;
  }
}
