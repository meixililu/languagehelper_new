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

public class XVideoDetailActivity_ViewBinding implements Unbinder {
  private XVideoDetailActivity target;

  private View view7f080082;

  @UiThread
  public XVideoDetailActivity_ViewBinding(XVideoDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XVideoDetailActivity_ViewBinding(final XVideoDetailActivity target, View source) {
    this.target = target;

    View view;
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.back_btn, "field 'back_btn' and method 'onClick'");
    target.back_btn = Utils.castView(view, R.id.back_btn, "field 'back_btn'", LinearLayout.class);
    view7f080082 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    XVideoDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview = null;
    target.back_btn = null;

    view7f080082.setOnClickListener(null);
    view7f080082 = null;
  }
}
