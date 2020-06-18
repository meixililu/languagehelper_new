// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyDuYinXuanCiActivity_ViewBinding implements Unbinder {
  private WordStudyDuYinXuanCiActivity target;

  private View view7f080488;

  private View view7f0802da;

  private View view7f0802dc;

  private View view7f0802de;

  private View view7f0802e0;

  private View view7f0803af;

  private View view7f080142;

  @UiThread
  public WordStudyDuYinXuanCiActivity_ViewBinding(WordStudyDuYinXuanCiActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyDuYinXuanCiActivity_ViewBinding(final WordStudyDuYinXuanCiActivity target,
      View source) {
    this.target = target;

    View view;
    target.wordPlayImg = Utils.findRequiredViewAsType(source, R.id.word_play_img, "field 'wordPlayImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.word_play_layout, "field 'wordPlayLayout' and method 'onViewClicked'");
    target.wordPlayLayout = Utils.castView(view, R.id.word_play_layout, "field 'wordPlayLayout'", LinearLayout.class);
    view7f080488 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.selection1 = Utils.findRequiredViewAsType(source, R.id.selection_1, "field 'selection1'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_1_layout, "field 'selection1Layout' and method 'onViewClicked'");
    target.selection1Layout = Utils.castView(view, R.id.selection_1_layout, "field 'selection1Layout'", FrameLayout.class);
    view7f0802da = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.selection2 = Utils.findRequiredViewAsType(source, R.id.selection_2, "field 'selection2'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_2_layout, "field 'selection2Layout' and method 'onViewClicked'");
    target.selection2Layout = Utils.castView(view, R.id.selection_2_layout, "field 'selection2Layout'", FrameLayout.class);
    view7f0802dc = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.selection3 = Utils.findRequiredViewAsType(source, R.id.selection_3, "field 'selection3'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_3_layout, "field 'selection3Layout' and method 'onViewClicked'");
    target.selection3Layout = Utils.castView(view, R.id.selection_3_layout, "field 'selection3Layout'", FrameLayout.class);
    view7f0802de = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.selection4 = Utils.findRequiredViewAsType(source, R.id.selection_4, "field 'selection4'", TextView.class);
    view = Utils.findRequiredView(source, R.id.selection_4_layout, "field 'selection4Layout' and method 'onViewClicked'");
    target.selection4Layout = Utils.castView(view, R.id.selection_4_layout, "field 'selection4Layout'", FrameLayout.class);
    view7f0802e0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.wordTestLayout = Utils.findRequiredViewAsType(source, R.id.word_test_layout, "field 'wordTestLayout'", LinearLayout.class);
    target.score = Utils.findRequiredViewAsType(source, R.id.score, "field 'score'", TextView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.try_again_layout, "field 'tryAgainLayout' and method 'onViewClicked'");
    target.tryAgainLayout = Utils.castView(view, R.id.try_again_layout, "field 'tryAgainLayout'", FrameLayout.class);
    view7f0803af = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.finish_test_layout, "field 'finishTestLayout' and method 'onViewClicked'");
    target.finishTestLayout = Utils.castView(view, R.id.finish_test_layout, "field 'finishTestLayout'", FrameLayout.class);
    view7f080142 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.resultLayout = Utils.findRequiredViewAsType(source, R.id.result_layout, "field 'resultLayout'", LinearLayout.class);
    target.progressBarCircularIndetermininate = Utils.findRequiredViewAsType(source, R.id.progressBarCircularIndetermininate, "field 'progressBarCircularIndetermininate'", ProgressBar.class);
    target.myAwesomeToolbar = Utils.findRequiredViewAsType(source, R.id.my_awesome_toolbar, "field 'myAwesomeToolbar'", Toolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WordStudyDuYinXuanCiActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.wordPlayImg = null;
    target.wordPlayLayout = null;
    target.selection1 = null;
    target.selection1Layout = null;
    target.selection2 = null;
    target.selection2Layout = null;
    target.selection3 = null;
    target.selection3Layout = null;
    target.selection4 = null;
    target.selection4Layout = null;
    target.wordTestLayout = null;
    target.score = null;
    target.listview = null;
    target.tryAgainLayout = null;
    target.finishTestLayout = null;
    target.resultLayout = null;
    target.progressBarCircularIndetermininate = null;
    target.myAwesomeToolbar = null;

    view7f080488.setOnClickListener(null);
    view7f080488 = null;
    view7f0802da.setOnClickListener(null);
    view7f0802da = null;
    view7f0802dc.setOnClickListener(null);
    view7f0802dc = null;
    view7f0802de.setOnClickListener(null);
    view7f0802de = null;
    view7f0802e0.setOnClickListener(null);
    view7f0802e0 = null;
    view7f0803af.setOnClickListener(null);
    view7f0803af = null;
    view7f080142.setOnClickListener(null);
    view7f080142 = null;
  }
}
