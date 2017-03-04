// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SymbolDetailActivity_ViewBinding implements Unbinder {
  private SymbolDetailActivity target;

  @UiThread
  public SymbolDetailActivity_ViewBinding(SymbolDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SymbolDetailActivity_ViewBinding(SymbolDetailActivity target, View source) {
    this.target = target;

    target.videoplayer = Utils.findRequiredViewAsType(source, R.id.videoplayer, "field 'videoplayer'", JCVideoPlayerStandard.class);
    target.symbol_en = Utils.findRequiredViewAsType(source, R.id.symbol_en, "field 'symbol_en'", TextView.class);
    target.symbol_des = Utils.findRequiredViewAsType(source, R.id.symbol_des, "field 'symbol_des'", TextView.class);
    target.symbol_play_img = Utils.findRequiredViewAsType(source, R.id.symbol_play_img, "field 'symbol_play_img'", ImageButton.class);
    target.symbol_cover = Utils.findRequiredViewAsType(source, R.id.symbol_cover, "field 'symbol_cover'", FrameLayout.class);
    target.play_img = Utils.findRequiredViewAsType(source, R.id.play_img, "field 'play_img'", ImageView.class);
    target.teacher_play_img = Utils.findRequiredViewAsType(source, R.id.teacher_play_img, "field 'teacher_play_img'", ImageButton.class);
    target.teacher_cover = Utils.findRequiredViewAsType(source, R.id.teacher_cover, "field 'teacher_cover'", FrameLayout.class);
    target.symbol_info = Utils.findRequiredViewAsType(source, R.id.symbol_info, "field 'symbol_info'", TextView.class);
    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", LinearLayout.class);
    target.error_txt = Utils.findRequiredViewAsType(source, R.id.error_txt, "field 'error_txt'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SymbolDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.videoplayer = null;
    target.symbol_en = null;
    target.symbol_des = null;
    target.symbol_play_img = null;
    target.symbol_cover = null;
    target.play_img = null;
    target.teacher_play_img = null;
    target.teacher_cover = null;
    target.symbol_info = null;
    target.content = null;
    target.error_txt = null;
  }
}
