// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AdEnglishActivity_ViewBinding implements Unbinder {
  private AdEnglishActivity target;

  @UiThread
  public AdEnglishActivity_ViewBinding(AdEnglishActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AdEnglishActivity_ViewBinding(AdEnglishActivity target, View source) {
    this.target = target;

    target.category_lv = Utils.findRequiredViewAsType(source, R.id.studycategory_lv, "field 'category_lv'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AdEnglishActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.category_lv = null;
  }
}
