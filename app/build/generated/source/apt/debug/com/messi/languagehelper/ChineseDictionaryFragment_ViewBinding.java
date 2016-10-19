// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.gc.materialdesign.views.ButtonFlat;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChineseDictionaryFragment_ViewBinding<T extends ChineseDictionaryFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ChineseDictionaryFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.btnBushou = Utils.findRequiredViewAsType(source, R.id.btn_bushou, "field 'btnBushou'", ButtonFlat.class);
    target.btnPinyin = Utils.findRequiredViewAsType(source, R.id.btn_pinyin, "field 'btnPinyin'", ButtonFlat.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.btnBushou = null;
    target.btnPinyin = null;

    this.target = null;
  }
}
