// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyCiYiXuanCiActivity_ViewBinding implements Unbinder {
  private WordStudyCiYiXuanCiActivity target;

  private View view7f080312;

  private View view7f080314;

  private View view7f080316;

  private View view7f080318;

  private View view7f0803f8;

  private View view7f080153;

  @UiThread
  public WordStudyCiYiXuanCiActivity_ViewBinding(WordStudyCiYiXuanCiActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyCiYiXuanCiActivity_ViewBinding(final WordStudyCiYiXuanCiActivity target,
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
    view7f080312 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.selection_2_layout, "field 'selection2Layout' and method 'onClick'");
    target.selection2Layout = Utils.castView(view, R.id.selection_2_layout, "field 'selection2Layout'", FrameLayout.class);
    view7f080314 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.selection_3_layout, "field 'selection3Layout' and method 'onClick'");
    target.selection3Layout = Utils.castView(view, R.id.selection_3_layout, "field 'selection3Layout'", FrameLayout.class);
    view7f080316 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.selection_4_layout, "field 'selection4Layout' and method 'onClick'");
    target.selection4Layout = Utils.castView(view, R.id.selection_4_layout, "field 'selection4Layout'", FrameLayout.class);
    view7f080318 = view;
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
    view7f0803f8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.finish_test_layout, "field 'finishTestLayout' and method 'onClick'");
    target.finishTestLayout = Utils.castView(view, R.id.finish_test_layout, "field 'finishTestLayout'", FrameLayout.class);
    view7f080153 = view;
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
    WordStudyCiYiXuanCiActivity target = this.target;
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

    view7f080312.setOnClickListener(null);
    view7f080312 = null;
    view7f080314.setOnClickListener(null);
    view7f080314 = null;
    view7f080316.setOnClickListener(null);
    view7f080316 = null;
    view7f080318.setOnClickListener(null);
    view7f080318 = null;
    view7f0803f8.setOnClickListener(null);
    view7f0803f8 = null;
    view7f080153.setOnClickListener(null);
    view7f080153 = null;
  }
}
