// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyDuYinSuJiActivity_ViewBinding implements Unbinder {
  private WordStudyDuYinSuJiActivity target;

  private View view7f080419;

  private View view7f080358;

  private View view7f080135;

  @UiThread
  public WordStudyDuYinSuJiActivity_ViewBinding(WordStudyDuYinSuJiActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyDuYinSuJiActivity_ViewBinding(final WordStudyDuYinSuJiActivity target,
      View source) {
    this.target = target;

    View view;
    target.wordName = Utils.findRequiredViewAsType(source, R.id.word_name, "field 'wordName'", TextView.class);
    target.wordMean = Utils.findRequiredViewAsType(source, R.id.word_mean, "field 'wordMean'", TextView.class);
    target.wordPlayImg = Utils.findRequiredViewAsType(source, R.id.word_play_img, "field 'wordPlayImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.word_suji_layout, "field 'wordSujiLayout' and method 'onViewClicked'");
    target.wordSujiLayout = Utils.castView(view, R.id.word_suji_layout, "field 'wordSujiLayout'", FrameLayout.class);
    view7f080419 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.try_again_layout, "field 'tryAgainLayout' and method 'onViewClicked'");
    target.tryAgainLayout = Utils.castView(view, R.id.try_again_layout, "field 'tryAgainLayout'", FrameLayout.class);
    view7f080358 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.finish_test_layout, "field 'finishTestLayout' and method 'onViewClicked'");
    target.finishTestLayout = Utils.castView(view, R.id.finish_test_layout, "field 'finishTestLayout'", FrameLayout.class);
    view7f080135 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.resultLayout = Utils.findRequiredViewAsType(source, R.id.result_layout, "field 'resultLayout'", LinearLayout.class);
    target.wordSymbol = Utils.findRequiredViewAsType(source, R.id.word_symbol, "field 'wordSymbol'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WordStudyDuYinSuJiActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.wordName = null;
    target.wordMean = null;
    target.wordPlayImg = null;
    target.wordSujiLayout = null;
    target.listview = null;
    target.tryAgainLayout = null;
    target.finishTestLayout = null;
    target.resultLayout = null;
    target.wordSymbol = null;

    view7f080419.setOnClickListener(null);
    view7f080419 = null;
    view7f080358.setOnClickListener(null);
    view7f080358 = null;
    view7f080135.setOnClickListener(null);
    view7f080135 = null;
  }
}
