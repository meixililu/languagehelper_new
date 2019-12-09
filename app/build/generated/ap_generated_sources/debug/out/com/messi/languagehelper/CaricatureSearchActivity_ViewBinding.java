// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.flexbox.FlexboxLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CaricatureSearchActivity_ViewBinding implements Unbinder {
  private CaricatureSearchActivity target;

  private View view7f080241;

  private View view7f080091;

  @UiThread
  public CaricatureSearchActivity_ViewBinding(CaricatureSearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CaricatureSearchActivity_ViewBinding(final CaricatureSearchActivity target, View source) {
    this.target = target;

    View view;
    target.searchEt = Utils.findRequiredViewAsType(source, R.id.search_et, "field 'searchEt'", EditText.class);
    view = Utils.findRequiredView(source, R.id.search_btn, "field 'searchBtn' and method 'onViewClicked'");
    target.searchBtn = Utils.castView(view, R.id.search_btn, "field 'searchBtn'", FrameLayout.class);
    view7f080241 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.auto_wrap_layout = Utils.findRequiredViewAsType(source, R.id.auto_wrap_layout, "field 'auto_wrap_layout'", FlexboxLayout.class);
    view = Utils.findRequiredView(source, R.id.clear_history, "field 'clearHistory' and method 'onViewClicked'");
    target.clearHistory = Utils.castView(view, R.id.clear_history, "field 'clearHistory'", FrameLayout.class);
    view7f080091 = view;
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
    CaricatureSearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.searchEt = null;
    target.searchBtn = null;
    target.auto_wrap_layout = null;
    target.clearHistory = null;

    view7f080241.setOnClickListener(null);
    view7f080241 = null;
    view7f080091.setOnClickListener(null);
    view7f080091 = null;
  }
}
