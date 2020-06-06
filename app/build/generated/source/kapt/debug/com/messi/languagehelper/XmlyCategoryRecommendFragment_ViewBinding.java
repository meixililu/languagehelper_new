// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class XmlyCategoryRecommendFragment_ViewBinding implements Unbinder {
  private XmlyCategoryRecommendFragment target;

  @UiThread
  public XmlyCategoryRecommendFragment_ViewBinding(XmlyCategoryRecommendFragment target,
      View source) {
    this.target = target;

    target.contentTv = Utils.findRequiredViewAsType(source, R.id.content_tv, "field 'contentTv'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    XmlyCategoryRecommendFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.contentTv = null;
  }
}
