// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChineseDictionaryActivity_ViewBinding implements Unbinder {
  private ChineseDictionaryActivity target;

  @UiThread
  public ChineseDictionaryActivity_ViewBinding(ChineseDictionaryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChineseDictionaryActivity_ViewBinding(ChineseDictionaryActivity target, View source) {
    this.target = target;

    target.btnBushou = Utils.findRequiredViewAsType(source, R.id.btn_bushou, "field 'btnBushou'", TextView.class);
    target.btnPinyin = Utils.findRequiredViewAsType(source, R.id.btn_pinyin, "field 'btnPinyin'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChineseDictionaryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnBushou = null;
    target.btnPinyin = null;
  }
}
