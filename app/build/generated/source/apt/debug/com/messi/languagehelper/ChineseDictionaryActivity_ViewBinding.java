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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChineseDictionaryActivity_ViewBinding implements Unbinder {
  private ChineseDictionaryActivity target;

  private View view2131230805;

  private View view2131230807;

  private View view2131231248;

  private View view2131230830;

  private View view2131231095;

  private View view2131231124;

  private View view2131230856;

  private View view2131231189;

  private View view2131231213;

  @UiThread
  public ChineseDictionaryActivity_ViewBinding(ChineseDictionaryActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChineseDictionaryActivity_ViewBinding(final ChineseDictionaryActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_bushou, "field 'btnBushou' and method 'onViewClicked'");
    target.btnBushou = Utils.castView(view, R.id.btn_bushou, "field 'btnBushou'", TextView.class);
    view2131230805 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_pinyin, "field 'btnPinyin' and method 'onViewClicked'");
    target.btnPinyin = Utils.castView(view, R.id.btn_pinyin, "field 'btnPinyin'", TextView.class);
    view2131230807 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.input_et = Utils.findRequiredViewAsType(source, R.id.input_et, "field 'input_et'", EditText.class);
    view = Utils.findRequiredView(source, R.id.submit_btn, "field 'submit_btn' and method 'onViewClicked'");
    target.submit_btn = Utils.castView(view, R.id.submit_btn, "field 'submit_btn'", FrameLayout.class);
    view2131231248 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.clear_btn_layout, "field 'clear_btn_layout' and method 'onViewClicked'");
    target.clear_btn_layout = Utils.castView(view, R.id.clear_btn_layout, "field 'clear_btn_layout'", FrameLayout.class);
    view2131230830 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.question_tv = Utils.findRequiredViewAsType(source, R.id.question_tv, "field 'question_tv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.question_tv_cover, "field 'question_tv_cover' and method 'onViewClicked'");
    target.question_tv_cover = Utils.castView(view, R.id.question_tv_cover, "field 'question_tv_cover'", FrameLayout.class);
    view2131231095 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.result_tv = Utils.findRequiredViewAsType(source, R.id.result_tv, "field 'result_tv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.result_tv_cover, "field 'result_tv_cover' and method 'onViewClicked'");
    target.result_tv_cover = Utils.castView(view, R.id.result_tv_cover, "field 'result_tv_cover'", FrameLayout.class);
    view2131231124 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.copy_btn, "field 'copy_btn' and method 'onViewClicked'");
    target.copy_btn = Utils.castView(view, R.id.copy_btn, "field 'copy_btn'", FrameLayout.class);
    view2131230856 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.share_btn, "field 'share_btn' and method 'onViewClicked'");
    target.share_btn = Utils.castView(view, R.id.share_btn, "field 'share_btn'", FrameLayout.class);
    view2131231189 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.chdic_sv = Utils.findRequiredViewAsType(source, R.id.chdic_sv, "field 'chdic_sv'", ScrollView.class);
    target.voice_btn = Utils.findRequiredViewAsType(source, R.id.voice_btn, "field 'voice_btn'", Button.class);
    view = Utils.findRequiredView(source, R.id.speak_round_layout, "field 'speak_round_layout' and method 'onViewClicked'");
    target.speak_round_layout = Utils.castView(view, R.id.speak_round_layout, "field 'speak_round_layout'", LinearLayout.class);
    view2131231213 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.layout_bottom = Utils.findRequiredViewAsType(source, R.id.layout_bottom, "field 'layout_bottom'", RelativeLayout.class);
    target.record_anim_img = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'record_anim_img'", ImageView.class);
    target.record_layout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'record_layout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChineseDictionaryActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnBushou = null;
    target.btnPinyin = null;
    target.input_et = null;
    target.submit_btn = null;
    target.clear_btn_layout = null;
    target.question_tv = null;
    target.question_tv_cover = null;
    target.result_tv = null;
    target.result_tv_cover = null;
    target.copy_btn = null;
    target.share_btn = null;
    target.chdic_sv = null;
    target.voice_btn = null;
    target.speak_round_layout = null;
    target.layout_bottom = null;
    target.record_anim_img = null;
    target.record_layout = null;

    view2131230805.setOnClickListener(null);
    view2131230805 = null;
    view2131230807.setOnClickListener(null);
    view2131230807 = null;
    view2131231248.setOnClickListener(null);
    view2131231248 = null;
    view2131230830.setOnClickListener(null);
    view2131230830 = null;
    view2131231095.setOnClickListener(null);
    view2131231095 = null;
    view2131231124.setOnClickListener(null);
    view2131231124 = null;
    view2131230856.setOnClickListener(null);
    view2131230856 = null;
    view2131231189.setOnClickListener(null);
    view2131231189 = null;
    view2131231213.setOnClickListener(null);
    view2131231213 = null;
  }
}
