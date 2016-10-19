// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.messi.languagehelper.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChDicBushouPinyinListAdapter$ViewHolder_ViewBinding<T extends ChDicBushouPinyinListAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public ChDicBushouPinyinListAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.textJijie = Utils.findRequiredViewAsType(source, R.id.text_jijie, "field 'textJijie'", TextView.class);
    target.textXiangjie = Utils.findRequiredViewAsType(source, R.id.text_xiangjie, "field 'textXiangjie'", TextView.class);
    target.shareCover = Utils.findRequiredViewAsType(source, R.id.share_cover, "field 'shareCover'", FrameLayout.class);
    target.copyCover = Utils.findRequiredViewAsType(source, R.id.copy_cover, "field 'copyCover'", FrameLayout.class);
    target.fullscreenImg = Utils.findRequiredViewAsType(source, R.id.fullscreen_img, "field 'fullscreenImg'", ImageView.class);
    target.fullscreenCover = Utils.findRequiredViewAsType(source, R.id.fullscreen_cover, "field 'fullscreenCover'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.textJijie = null;
    target.textXiangjie = null;
    target.shareCover = null;
    target.copyCover = null;
    target.fullscreenImg = null;
    target.fullscreenCover = null;

    this.target = null;
  }
}
