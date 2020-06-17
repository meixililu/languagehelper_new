// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyViewAllActivity_ViewBinding implements Unbinder {
  private WordStudyViewAllActivity target;

  private View view7f0802f4;

  private View view7f08022d;

  @UiThread
  public WordStudyViewAllActivity_ViewBinding(WordStudyViewAllActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyViewAllActivity_ViewBinding(final WordStudyViewAllActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.show_all_unit_layout, "field 'previousUnitLayout' and method 'onClick'");
    target.previousUnitLayout = Utils.castView(view, R.id.show_all_unit_layout, "field 'previousUnitLayout'", FrameLayout.class);
    view7f0802f4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.next_unit_layout, "field 'nextUnitLayout' and method 'onClick'");
    target.nextUnitLayout = Utils.castView(view, R.id.next_unit_layout, "field 'nextUnitLayout'", FrameLayout.class);
    view7f08022d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.unitList = Utils.findRequiredViewAsType(source, R.id.unit_list, "field 'unitList'", GridView.class);
    target.transitionsContainer = Utils.findRequiredViewAsType(source, R.id.transitions_container, "field 'transitionsContainer'", RelativeLayout.class);
    target.studycategoryLv = Utils.findRequiredViewAsType(source, R.id.studycategory_lv, "field 'studycategoryLv'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WordStudyViewAllActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.previousUnitLayout = null;
    target.nextUnitLayout = null;
    target.unitList = null;
    target.transitionsContainer = null;
    target.studycategoryLv = null;

    view7f0802f4.setOnClickListener(null);
    view7f0802f4 = null;
    view7f08022d.setOnClickListener(null);
    view7f08022d = null;
  }
}
