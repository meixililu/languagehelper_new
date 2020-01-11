// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.lzyzsd.circleprogress.ArcProgress;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordHomeFragment_ViewBinding implements Unbinder {
  private WordHomeFragment target;

  private View view7f0803ca;

  private View view7f0803cb;

  private View view7f0803cc;

  @UiThread
  public WordHomeFragment_ViewBinding(final WordHomeFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.word_study_change_plan, "field 'wordStudyChangePlan' and method 'onViewClicked'");
    target.wordStudyChangePlan = Utils.castView(view, R.id.word_study_change_plan, "field 'wordStudyChangePlan'", FrameLayout.class);
    view7f0803ca = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.wordStudyBookName = Utils.findRequiredViewAsType(source, R.id.word_study_book_name, "field 'wordStudyBookName'", TextView.class);
    target.arcProgress = Utils.findRequiredViewAsType(source, R.id.arc_progress, "field 'arcProgress'", ArcProgress.class);
    view = Utils.findRequiredView(source, R.id.word_study_new_word, "method 'onViewClicked'");
    view7f0803cb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.word_study_plan, "method 'onViewClicked'");
    view7f0803cc = view;
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
    WordHomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.wordStudyChangePlan = null;
    target.wordStudyBookName = null;
    target.arcProgress = null;

    view7f0803ca.setOnClickListener(null);
    view7f0803ca = null;
    view7f0803cb.setOnClickListener(null);
    view7f0803cb = null;
    view7f0803cc.setOnClickListener(null);
    view7f0803cc = null;
  }
}