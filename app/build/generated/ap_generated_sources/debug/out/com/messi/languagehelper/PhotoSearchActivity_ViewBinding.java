// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PhotoSearchActivity_ViewBinding implements Unbinder {
  private PhotoSearchActivity target;

  private View view7f080084;

  @UiThread
  public PhotoSearchActivity_ViewBinding(PhotoSearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PhotoSearchActivity_ViewBinding(final PhotoSearchActivity target, View source) {
    this.target = target;

    View view;
    target.cameraBtn = Utils.findRequiredViewAsType(source, R.id.camera_btn, "field 'cameraBtn'", Button.class);
    view = Utils.findRequiredView(source, R.id.camera_layout, "field 'cameraLayout' and method 'onClick'");
    target.cameraLayout = Utils.castView(view, R.id.camera_layout, "field 'cameraLayout'", LinearLayout.class);
    view7f080084 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.contentLayout = Utils.findRequiredViewAsType(source, R.id.content_layout, "field 'contentLayout'", LinearLayout.class);
    target.content_tv = Utils.findRequiredViewAsType(source, R.id.content_tv, "field 'content_tv'", WebView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PhotoSearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cameraBtn = null;
    target.cameraLayout = null;
    target.contentLayout = null;
    target.content_tv = null;

    view7f080084.setOnClickListener(null);
    view7f080084 = null;
  }
}
