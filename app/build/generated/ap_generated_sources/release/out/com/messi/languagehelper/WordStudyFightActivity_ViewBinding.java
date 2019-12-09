// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyFightActivity_ViewBinding implements Unbinder {
  private WordStudyFightActivity target;

  private View view7f080116;

  private View view7f0803b6;

  private View view7f08024b;

  private View view7f08024d;

  private View view7f08024f;

  private View view7f080251;

  @UiThread
  public WordStudyFightActivity_ViewBinding(WordStudyFightActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyFightActivity_ViewBinding(final WordStudyFightActivity target, View source) {
    this.target = target;

    View view;
    target.ad_layout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'ad_layout'", RelativeLayout.class);
    target.ad_img = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'ad_img'", SimpleDraweeView.class);
    target.ad_source = Utils.findRequiredViewAsType(source, R.id.ad_source, "field 'ad_source'", TextView.class);
    target.contentLayout = Utils.findRequiredViewAsType(source, R.id.content_layout, "field 'contentLayout'", LinearLayout.class);
    target.numberProgressBar = Utils.findRequiredViewAsType(source, R.id.number_progress_bar, "field 'numberProgressBar'", NumberProgressBar.class);
    target.progressTv = Utils.findRequiredViewAsType(source, R.id.progress_tv, "field 'progressTv'", TextView.class);
    target.score = Utils.findRequiredViewAsType(source, R.id.score, "field 'score'", TextView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.finish_test_layout, "field 'finishTestLayout' and method 'onClick'");
    target.finishTestLayout = Utils.castView(view, R.id.finish_test_layout, "field 'finishTestLayout'", FrameLayout.class);
    view7f080116 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.resultLayout = Utils.findRequiredViewAsType(source, R.id.result_layout, "field 'resultLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.word_tv, "field 'wordTv' and method 'onClick'");
    target.wordTv = Utils.castView(view, R.id.word_tv, "field 'wordTv'", TextView.class);
    view7f0803b6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection1 = Utils.findRequiredViewAsType(source, R.id.selection_1, "field 'selection1'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_1_layout, "field 'selection1Layout' and method 'onClick'");
    target.selection1Layout = Utils.castView(view, R.id.selection_1_layout, "field 'selection1Layout'", FrameLayout.class);
    view7f08024b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection2 = Utils.findRequiredViewAsType(source, R.id.selection_2, "field 'selection2'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_2_layout, "field 'selection2Layout' and method 'onClick'");
    target.selection2Layout = Utils.castView(view, R.id.selection_2_layout, "field 'selection2Layout'", FrameLayout.class);
    view7f08024d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection3 = Utils.findRequiredViewAsType(source, R.id.selection_3, "field 'selection3'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_3_layout, "field 'selection3Layout' and method 'onClick'");
    target.selection3Layout = Utils.castView(view, R.id.selection_3_layout, "field 'selection3Layout'", FrameLayout.class);
    view7f08024f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection4 = Utils.findRequiredViewAsType(source, R.id.selection_4, "field 'selection4'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_4_layout, "field 'selection4Layout' and method 'onClick'");
    target.selection4Layout = Utils.castView(view, R.id.selection_4_layout, "field 'selection4Layout'", FrameLayout.class);
    view7f080251 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.fightResutlTv = Utils.findRequiredViewAsType(source, R.id.fight_resutl_tv, "field 'fightResutlTv'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WordStudyFightActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ad_layout = null;
    target.ad_img = null;
    target.ad_source = null;
    target.contentLayout = null;
    target.numberProgressBar = null;
    target.progressTv = null;
    target.score = null;
    target.listview = null;
    target.finishTestLayout = null;
    target.resultLayout = null;
    target.wordTv = null;
    target.selection1 = null;
    target.selection1Layout = null;
    target.selection2 = null;
    target.selection2Layout = null;
    target.selection3 = null;
    target.selection3Layout = null;
    target.selection4 = null;
    target.selection4Layout = null;
    target.fightResutlTv = null;

    view7f080116.setOnClickListener(null);
    view7f080116 = null;
    view7f0803b6.setOnClickListener(null);
    view7f0803b6 = null;
    view7f08024b.setOnClickListener(null);
    view7f08024b = null;
    view7f08024d.setOnClickListener(null);
    view7f08024d = null;
    view7f08024f.setOnClickListener(null);
    view7f08024f = null;
    view7f080251.setOnClickListener(null);
    view7f080251 = null;
  }
}
