// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MoreActivity_ViewBinding implements Unbinder {
  private MoreActivity target;

  @UiThread
  public MoreActivity_ViewBinding(MoreActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MoreActivity_ViewBinding(MoreActivity target, View source) {
    this.target = target;

    target.costom_share_layout = Utils.findRequiredViewAsType(source, R.id.costom_share_layout, "field 'costom_share_layout'", FrameLayout.class);
    target.comments_layout = Utils.findRequiredViewAsType(source, R.id.comments_layout, "field 'comments_layout'", FrameLayout.class);
    target.help_layout = Utils.findRequiredViewAsType(source, R.id.help_layout, "field 'help_layout'", FrameLayout.class);
    target.about_layout = Utils.findRequiredViewAsType(source, R.id.about_layout, "field 'about_layout'", FrameLayout.class);
    target.invite_layout = Utils.findRequiredViewAsType(source, R.id.invite_layout, "field 'invite_layout'", FrameLayout.class);
    target.qrcode_layout = Utils.findRequiredViewAsType(source, R.id.qrcode_layout, "field 'qrcode_layout'", FrameLayout.class);
    target.setting_layout = Utils.findRequiredViewAsType(source, R.id.setting_layout, "field 'setting_layout'", FrameLayout.class);
    target.offlineDicLayout = Utils.findRequiredViewAsType(source, R.id.offline_dic_layout, "field 'offlineDicLayout'", FrameLayout.class);
    target.offlineDicUnreadDot = Utils.findRequiredViewAsType(source, R.id.offline_dic_unread_dot, "field 'offlineDicUnreadDot'", ImageView.class);
    target.offline_dic_layout_line = Utils.findRequiredViewAsType(source, R.id.offline_dic_layout_line, "field 'offline_dic_layout_line'", ImageView.class);
    target.help_layout_line = Utils.findRequiredViewAsType(source, R.id.help_layout_line, "field 'help_layout_line'", ImageView.class);
    target.privacy_layout = Utils.findRequiredViewAsType(source, R.id.privacy_layout, "field 'privacy_layout'", FrameLayout.class);
    target.terms_layout = Utils.findRequiredViewAsType(source, R.id.terms_layout, "field 'terms_layout'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MoreActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.costom_share_layout = null;
    target.comments_layout = null;
    target.help_layout = null;
    target.about_layout = null;
    target.invite_layout = null;
    target.qrcode_layout = null;
    target.setting_layout = null;
    target.offlineDicLayout = null;
    target.offlineDicUnreadDot = null;
    target.offline_dic_layout_line = null;
    target.help_layout_line = null;
    target.privacy_layout = null;
    target.terms_layout = null;
  }
}
