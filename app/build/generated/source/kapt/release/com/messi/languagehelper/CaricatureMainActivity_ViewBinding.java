// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CaricatureMainActivity_ViewBinding implements Unbinder {
  private CaricatureMainActivity target;

  @UiThread
  public CaricatureMainActivity_ViewBinding(CaricatureMainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CaricatureMainActivity_ViewBinding(CaricatureMainActivity target, View source) {
    this.target = target;

    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", FrameLayout.class);
    target.navigation = Utils.findRequiredViewAsType(source, R.id.navigation, "field 'navigation'", BottomNavigationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CaricatureMainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.content = null;
    target.navigation = null;
  }
}
