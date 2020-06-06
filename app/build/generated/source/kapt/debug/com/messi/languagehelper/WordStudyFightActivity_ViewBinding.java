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

public class WordStudyFightActivity_ViewBinding implements Unbinder {
  private WordStudyFightActivity target;

  private View view7f080142;

  private View view7f08048c;

  private View view7f0802d9;

  private View view7f0802db;

  private View view7f0802dd;

  private View view7f0802df;

  @UiThread
  public WordStudyFightActivity_ViewBinding(WordStudyFightActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyFightActivity_ViewBinding(final WordStudyFightActivity target, View source) {
    this.target = target;

    View view;
    target.contentLayout = Utils.findRequiredViewAsType(source, R.id.content_layout, "field 'contentLayout'", LinearLayout.class);
    target.score = Utils.findRequiredViewAsType(source, R.id.score, "field 'score'", TextView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.finish_test_layout, "field 'finishTestLayout' and method 'onClick'");
    target.finishTestLayout = Utils.castView(view, R.id.finish_test_layout, "field 'finishTestLayout'", FrameLayout.class);
    view7f080142 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.resultLayout = Utils.findRequiredViewAsType(source, R.id.result_layout, "field 'resultLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.word_tv, "field 'wordTv' and method 'onClick'");
    target.wordTv = Utils.castView(view, R.id.word_tv, "field 'wordTv'", TextView.class);
    view7f08048c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection1 = Utils.findRequiredViewAsType(source, R.id.selection_1, "field 'selection1'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_1_layout, "field 'selection1Layout' and method 'onClick'");
    target.selection1Layout = Utils.castView(view, R.id.selection_1_layout, "field 'selection1Layout'", FrameLayout.class);
    view7f0802d9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection2 = Utils.findRequiredViewAsType(source, R.id.selection_2, "field 'selection2'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_2_layout, "field 'selection2Layout' and method 'onClick'");
    target.selection2Layout = Utils.castView(view, R.id.selection_2_layout, "field 'selection2Layout'", FrameLayout.class);
    view7f0802db = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection3 = Utils.findRequiredViewAsType(source, R.id.selection_3, "field 'selection3'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_3_layout, "field 'selection3Layout' and method 'onClick'");
    target.selection3Layout = Utils.castView(view, R.id.selection_3_layout, "field 'selection3Layout'", FrameLayout.class);
    view7f0802dd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.selection4 = Utils.findRequiredViewAsType(source, R.id.selection_4, "field 'selection4'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_4_layout, "field 'selection4Layout' and method 'onClick'");
    target.selection4Layout = Utils.castView(view, R.id.selection_4_layout, "field 'selection4Layout'", FrameLayout.class);
    view7f0802df = view;
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

    target.contentLayout = null;
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

    view7f080142.setOnClickListener(null);
    view7f080142 = null;
    view7f08048c.setOnClickListener(null);
    view7f08048c = null;
    view7f0802d9.setOnClickListener(null);
    view7f0802d9 = null;
    view7f0802db.setOnClickListener(null);
    view7f0802db = null;
    view7f0802dd.setOnClickListener(null);
    view7f0802dd = null;
    view7f0802df.setOnClickListener(null);
    view7f0802df = null;
  }
}
