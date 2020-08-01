// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MomentsActivity_ViewBinding implements Unbinder {
  private MomentsActivity target;

  private View view7f080212;

  @UiThread
  public MomentsActivity_ViewBinding(MomentsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MomentsActivity_ViewBinding(final MomentsActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.moments_add, "field 'momentsAdd' and method 'onClick'");
    target.momentsAdd = Utils.castView(view, R.id.moments_add, "field 'momentsAdd'", LinearLayout.class);
    view7f080212 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MomentsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.momentsAdd = null;
    target.listview = null;

    view7f080212.setOnClickListener(null);
    view7f080212 = null;
  }
}
