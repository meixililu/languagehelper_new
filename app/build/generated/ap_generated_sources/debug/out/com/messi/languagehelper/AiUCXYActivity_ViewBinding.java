// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AiUCXYActivity_ViewBinding implements Unbinder {
  private AiUCXYActivity target;

  private View view7f080308;

  private View view7f08016f;

  private View view7f08041b;

  @UiThread
  public AiUCXYActivity_ViewBinding(AiUCXYActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AiUCXYActivity_ViewBinding(final AiUCXYActivity target, View source) {
    this.target = target;

    View view;
    target.inputEt = Utils.findRequiredViewAsType(source, R.id.input_et, "field 'inputEt'", AppCompatEditText.class);
    view = Utils.findRequiredView(source, R.id.submit_btn_cover, "field 'submitBtn' and method 'onViewClicked'");
    target.submitBtn = Utils.castView(view, R.id.submit_btn_cover, "field 'submitBtn'", CardView.class);
    view7f080308 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mWebView = Utils.findRequiredViewAsType(source, R.id.webview_layout, "field 'mWebView'", WebView.class);
    target.recordAnimImg = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'recordAnimImg'", ImageView.class);
    target.recordLayout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'recordLayout'", LinearLayout.class);
    target.inputTypeBtn = Utils.findRequiredViewAsType(source, R.id.input_type_btn, "field 'inputTypeBtn'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.input_type_layout, "field 'inputTypeLayout' and method 'onViewClicked'");
    target.inputTypeLayout = Utils.castView(view, R.id.input_type_layout, "field 'inputTypeLayout'", LinearLayout.class);
    view7f08016f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.voiceBtn = Utils.findRequiredViewAsType(source, R.id.voice_btn, "field 'voiceBtn'", TextView.class);
    view = Utils.findRequiredView(source, R.id.voice_btn_cover, "field 'voiceBtnCover' and method 'onViewClicked'");
    target.voiceBtnCover = Utils.castView(view, R.id.voice_btn_cover, "field 'voiceBtnCover'", CardView.class);
    view7f08041b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.micLayout = Utils.findRequiredViewAsType(source, R.id.mic_layout, "field 'micLayout'", LinearLayout.class);
    target.keybordLayout = Utils.findRequiredViewAsType(source, R.id.keybord_layout, "field 'keybordLayout'", LinearLayout.class);
    target.progressTv = Utils.findRequiredViewAsType(source, R.id.progress_tv, "field 'progressTv'", TextView.class);
    target.numberProgressBar = Utils.findRequiredViewAsType(source, R.id.number_progress_bar, "field 'numberProgressBar'", NumberProgressBar.class);
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    target.adSource = Utils.findRequiredViewAsType(source, R.id.ad_source, "field 'adSource'", TextView.class);
    target.adLayout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'adLayout'", RelativeLayout.class);
    target.splash_container = Utils.findRequiredViewAsType(source, R.id.splash_container, "field 'splash_container'", FrameLayout.class);
    target.skip_view = Utils.findRequiredViewAsType(source, R.id.skip_view, "field 'skip_view'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AiUCXYActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.inputEt = null;
    target.submitBtn = null;
    target.mWebView = null;
    target.recordAnimImg = null;
    target.recordLayout = null;
    target.inputTypeBtn = null;
    target.inputTypeLayout = null;
    target.voiceBtn = null;
    target.voiceBtnCover = null;
    target.micLayout = null;
    target.keybordLayout = null;
    target.progressTv = null;
    target.numberProgressBar = null;
    target.adImg = null;
    target.adSource = null;
    target.adLayout = null;
    target.splash_container = null;
    target.skip_view = null;

    view7f080308.setOnClickListener(null);
    view7f080308 = null;
    view7f08016f.setOnClickListener(null);
    view7f08016f = null;
    view7f08041b.setOnClickListener(null);
    view7f08041b = null;
  }
}
