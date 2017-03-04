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

public class ChineseDictionaryFragment_ViewBinding implements Unbinder {
  private ChineseDictionaryFragment target;

  @UiThread
  public ChineseDictionaryFragment_ViewBinding(ChineseDictionaryFragment target, View source) {
    this.target = target;

    target.btnBushou = Utils.findRequiredViewAsType(source, R.id.btn_bushou, "field 'btnBushou'", TextView.class);
    target.btnPinyin = Utils.findRequiredViewAsType(source, R.id.btn_pinyin, "field 'btnPinyin'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChineseDictionaryFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnBushou = null;
    target.btnPinyin = null;
  }
}
