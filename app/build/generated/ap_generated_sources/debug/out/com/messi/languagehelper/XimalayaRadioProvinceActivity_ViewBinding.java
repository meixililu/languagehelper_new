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

public class XimalayaRadioProvinceActivity_ViewBinding implements Unbinder {
  private XimalayaRadioProvinceActivity target;

  @UiThread
  public XimalayaRadioProvinceActivity_ViewBinding(XimalayaRadioProvinceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public XimalayaRadioProvinceActivity_ViewBinding(XimalayaRadioProvinceActivity target,
      View source) {
    this.target = target;

    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XimalayaRadioProvinceActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview = null;
  }
}
