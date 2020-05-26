// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OfflineDicDownloadActivity_ViewBinding implements Unbinder {
  private OfflineDicDownloadActivity target;

  private View view7f0800e2;

  private View view7f0800e7;

  private View view7f0800e5;

  private View view7f08018d;

  private View view7f08014e;

  @UiThread
  public OfflineDicDownloadActivity_ViewBinding(OfflineDicDownloadActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public OfflineDicDownloadActivity_ViewBinding(final OfflineDicDownloadActivity target,
      View source) {
    this.target = target;

    View view;
    target.dicMinimalistImg = Utils.findRequiredViewAsType(source, R.id.dic_minimalist_img, "field 'dicMinimalistImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.dic_minimalist, "field 'dicMinimalist' and method 'onViewClicked'");
    target.dicMinimalist = Utils.castView(view, R.id.dic_minimalist, "field 'dicMinimalist'", FrameLayout.class);
    view7f0800e2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.dicStandardImg = Utils.findRequiredViewAsType(source, R.id.dic_standard_img, "field 'dicStandardImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.dic_standard, "field 'dicStandard' and method 'onViewClicked'");
    target.dicStandard = Utils.castView(view, R.id.dic_standard, "field 'dicStandard'", FrameLayout.class);
    view7f0800e7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.dicSentenceImg = Utils.findRequiredViewAsType(source, R.id.dic_sentence_img, "field 'dicSentenceImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.dic_sentence, "field 'dicSentence' and method 'onViewClicked'");
    target.dicSentence = Utils.castView(view, R.id.dic_sentence, "field 'dicSentence'", FrameLayout.class);
    view7f0800e5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.numberProgressBar = Utils.findRequiredViewAsType(source, R.id.number_progress_bar, "field 'numberProgressBar'", NumberProgressBar.class);
    target.kzImg = Utils.findRequiredViewAsType(source, R.id.kz_img, "field 'kzImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.kz_layout, "field 'kzLayout' and method 'onViewClicked'");
    target.kzLayout = Utils.castView(view, R.id.kz_layout, "field 'kzLayout'", FrameLayout.class);
    view7f08018d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.hhImg = Utils.findRequiredViewAsType(source, R.id.hh_img, "field 'hhImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.hh_layout, "field 'hhLayout' and method 'onViewClicked'");
    target.hhLayout = Utils.castView(view, R.id.hh_layout, "field 'hhLayout'", FrameLayout.class);
    view7f08014e = view;
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
    OfflineDicDownloadActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.dicMinimalistImg = null;
    target.dicMinimalist = null;
    target.dicStandardImg = null;
    target.dicStandard = null;
    target.dicSentenceImg = null;
    target.dicSentence = null;
    target.numberProgressBar = null;
    target.kzImg = null;
    target.kzLayout = null;
    target.hhImg = null;
    target.hhLayout = null;

    view7f0800e2.setOnClickListener(null);
    view7f0800e2 = null;
    view7f0800e7.setOnClickListener(null);
    view7f0800e7 = null;
    view7f0800e5.setOnClickListener(null);
    view7f0800e5 = null;
    view7f08018d.setOnClickListener(null);
    view7f08018d = null;
    view7f08014e.setOnClickListener(null);
    view7f08014e = null;
  }
}
