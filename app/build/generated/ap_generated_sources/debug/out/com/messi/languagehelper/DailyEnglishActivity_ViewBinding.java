// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DailyEnglishActivity_ViewBinding implements Unbinder {
  private DailyEnglishActivity target;

  @UiThread
  public DailyEnglishActivity_ViewBinding(DailyEnglishActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public DailyEnglishActivity_ViewBinding(DailyEnglishActivity target, View source) {
    this.target = target;

    target.start_btn = Utils.findRequiredViewAsType(source, R.id.start_btn, "field 'start_btn'", TextView.class);
    target.english_tv = Utils.findRequiredViewAsType(source, R.id.english_tv, "field 'english_tv'", TextView.class);
    target.chinese_tv = Utils.findRequiredViewAsType(source, R.id.chinese_tv, "field 'chinese_tv'", TextView.class);
    target.start_btn_cover = Utils.findRequiredViewAsType(source, R.id.start_btn_cover, "field 'start_btn_cover'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    DailyEnglishActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.start_btn = null;
    target.english_tv = null;
    target.chinese_tv = null;
    target.start_btn_cover = null;
  }
}
