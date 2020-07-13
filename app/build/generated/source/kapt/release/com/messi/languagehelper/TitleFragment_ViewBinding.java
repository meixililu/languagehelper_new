// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TitleFragment_ViewBinding implements Unbinder {
  private TitleFragment target;

  @UiThread
  public TitleFragment_ViewBinding(TitleFragment target, View source) {
    this.target = target;

    target.progressBarCircularIndetermininate = Utils.findRequiredViewAsType(source, R.id.progressBarCircularIndetermininate, "field 'progressBarCircularIndetermininate'", ProgressBar.class);
    target.myAwesomeToolbar = Utils.findRequiredViewAsType(source, R.id.my_awesome_toolbar, "field 'myAwesomeToolbar'", Toolbar.class);
    target.contontLayout = Utils.findRequiredViewAsType(source, R.id.contont_layout, "field 'contontLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TitleFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.progressBarCircularIndetermininate = null;
    target.myAwesomeToolbar = null;
    target.contontLayout = null;
  }
}
