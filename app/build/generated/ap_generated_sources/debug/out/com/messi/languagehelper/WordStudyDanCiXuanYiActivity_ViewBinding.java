// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyDanCiXuanYiActivity_ViewBinding implements Unbinder {
  private WordStudyDanCiXuanYiActivity target;

  private View view7f0802b2;

  private View view7f0802b4;

  private View view7f0802b6;

  private View view7f0802b8;

  private View view7f08038e;

  private View view7f080130;

  private View view7f080451;

  private View view7f08045a;

  @UiThread
  public WordStudyDanCiXuanYiActivity_ViewBinding(WordStudyDanCiXuanYiActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyDanCiXuanYiActivity_ViewBinding(final WordStudyDanCiXuanYiActivity target,
      View source) {
    this.target = target;

    View view;
    target.wordTv = Utils.findRequiredViewAsType(source, R.id.word_tv, "field 'wordTv'", TextView.class);
    target.selection1 = Utils.findRequiredViewAsType(source, R.id.selection_1, "field 'selection1'", TextView.class);
    target.selection2 = Utils.findRequiredViewAsType(source, R.id.selection_2, "field 'selection2'", TextView.class);
    target.selection3 = Utils.findRequiredViewAsType(source, R.id.selection_3, "field 'selection3'", TextView.class);
    target.selection4 = Utils.findRequiredViewAsType(source, R.id.selection_4, "field 'selection4'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_1_layout, "field 'selection1Layout' and method 'onClick'");
    target.selection1Layout = Utils.castView(view, R.id.selection_1_layout, "field 'selection1Layout'", FrameLayout.class);
    view7f0802b2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.selection_2_layout, "field 'selection2Layout' and method 'onClick'");
    target.selection2Layout = Utils.castView(view, R.id.selection_2_layout, "field 'selection2Layout'", FrameLayout.class);
    view7f0802b4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.selection_3_layout, "field 'selection3Layout' and method 'onClick'");
    target.selection3Layout = Utils.castView(view, R.id.selection_3_layout, "field 'selection3Layout'", FrameLayout.class);
    view7f0802b6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.selection_4_layout, "field 'selection4Layout' and method 'onClick'");
    target.selection4Layout = Utils.castView(view, R.id.selection_4_layout, "field 'selection4Layout'", FrameLayout.class);
    view7f0802b8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.score = Utils.findRequiredViewAsType(source, R.id.score, "field 'score'", TextView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    target.resultLayout = Utils.findRequiredViewAsType(source, R.id.result_layout, "field 'resultLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.try_again_layout, "field 'tryAgainLayout' and method 'onClick'");
    target.tryAgainLayout = Utils.castView(view, R.id.try_again_layout, "field 'tryAgainLayout'", FrameLayout.class);
    view7f08038e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.finish_test_layout, "field 'finishTestLayout' and method 'onClick'");
    target.finishTestLayout = Utils.castView(view, R.id.finish_test_layout, "field 'finishTestLayout'", FrameLayout.class);
    view7f080130 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.volumeImg = Utils.findRequiredViewAsType(source, R.id.volume_img, "field 'volumeImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.volume_layout, "field 'volumeLayout' and method 'onClick'");
    target.volumeLayout = Utils.castView(view, R.id.volume_layout, "field 'volumeLayout'", FrameLayout.class);
    view7f080451 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.word_layout, "field 'wordLayout' and method 'onClick'");
    target.wordLayout = Utils.castView(view, R.id.word_layout, "field 'wordLayout'", RelativeLayout.class);
    view7f08045a = view;
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
    WordStudyDanCiXuanYiActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.wordTv = null;
    target.selection1 = null;
    target.selection2 = null;
    target.selection3 = null;
    target.selection4 = null;
    target.selection1Layout = null;
    target.selection2Layout = null;
    target.selection3Layout = null;
    target.selection4Layout = null;
    target.score = null;
    target.listview = null;
    target.resultLayout = null;
    target.tryAgainLayout = null;
    target.finishTestLayout = null;
    target.volumeImg = null;
    target.volumeLayout = null;
    target.wordLayout = null;

    view7f0802b2.setOnClickListener(null);
    view7f0802b2 = null;
    view7f0802b4.setOnClickListener(null);
    view7f0802b4 = null;
    view7f0802b6.setOnClickListener(null);
    view7f0802b6 = null;
    view7f0802b8.setOnClickListener(null);
    view7f0802b8 = null;
    view7f08038e.setOnClickListener(null);
    view7f08038e = null;
    view7f080130.setOnClickListener(null);
    view7f080130 = null;
    view7f080451.setOnClickListener(null);
    view7f080451 = null;
    view7f08045a.setOnClickListener(null);
    view7f08045a = null;
  }
}
