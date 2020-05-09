// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.flexbox.FlexboxLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CNSearchActivity_ViewBinding implements Unbinder {
  private CNSearchActivity target;

  private View view7f0802ee;

  private View view7f0802a9;

  private View view7f0802a6;

  private View view7f0802a1;

  private View view7f08029f;

  private View view7f0800a2;

  @UiThread
  public CNSearchActivity_ViewBinding(CNSearchActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CNSearchActivity_ViewBinding(final CNSearchActivity target, View source) {
    this.target = target;

    View view;
    target.input_et = Utils.findRequiredViewAsType(source, R.id.input_et, "field 'input_et'", EditText.class);
    target.voice_btn = Utils.findRequiredViewAsType(source, R.id.voice_btn, "field 'voice_btn'", Button.class);
    target.input_layout = Utils.findRequiredViewAsType(source, R.id.input_layout, "field 'input_layout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.speak_round_layout, "field 'speak_round_layout' and method 'onViewClicked'");
    target.speak_round_layout = Utils.castView(view, R.id.speak_round_layout, "field 'speak_round_layout'", LinearLayout.class);
    view7f0802ee = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.record_anim_img = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'record_anim_img'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.search_novel, "field 'search_novel' and method 'onViewClicked'");
    target.search_novel = Utils.castView(view, R.id.search_novel, "field 'search_novel'", TextView.class);
    view7f0802a9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_internet, "field 'search_internet' and method 'onViewClicked'");
    target.search_internet = Utils.castView(view, R.id.search_internet, "field 'search_internet'", TextView.class);
    view7f0802a6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_caricature, "field 'search_caricature' and method 'onViewClicked'");
    target.search_caricature = Utils.castView(view, R.id.search_caricature, "field 'search_caricature'", TextView.class);
    view7f0802a1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.record_layout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'record_layout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.search_btn, "field 'search_btn' and method 'onViewClicked'");
    target.search_btn = Utils.castView(view, R.id.search_btn, "field 'search_btn'", FrameLayout.class);
    view7f08029f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.auto_wrap_layout = Utils.findRequiredViewAsType(source, R.id.auto_wrap_layout, "field 'auto_wrap_layout'", FlexboxLayout.class);
    target.hot_wrap_layout = Utils.findRequiredViewAsType(source, R.id.hot_wrap_layout, "field 'hot_wrap_layout'", FlexboxLayout.class);
    view = Utils.findRequiredView(source, R.id.clear_history, "field 'clearHistory' and method 'onViewClicked'");
    target.clearHistory = Utils.castView(view, R.id.clear_history, "field 'clearHistory'", FrameLayout.class);
    view7f0800a2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CNSearchActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.input_et = null;
    target.voice_btn = null;
    target.input_layout = null;
    target.speak_round_layout = null;
    target.record_anim_img = null;
    target.search_novel = null;
    target.search_internet = null;
    target.search_caricature = null;
    target.record_layout = null;
    target.search_btn = null;
    target.auto_wrap_layout = null;
    target.hot_wrap_layout = null;
    target.clearHistory = null;

    view7f0802ee.setOnClickListener(null);
    view7f0802ee = null;
    view7f0802a9.setOnClickListener(null);
    view7f0802a9 = null;
    view7f0802a6.setOnClickListener(null);
    view7f0802a6 = null;
    view7f0802a1.setOnClickListener(null);
    view7f0802a1 = null;
    view7f08029f.setOnClickListener(null);
    view7f08029f = null;
    view7f0800a2.setOnClickListener(null);
    view7f0800a2 = null;
  }
}
