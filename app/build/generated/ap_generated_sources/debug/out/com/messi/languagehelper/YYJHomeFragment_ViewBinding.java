// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class YYJHomeFragment_ViewBinding implements Unbinder {
  private YYJHomeFragment target;

  private View view7f080325;

  private View view7f080302;

  private View view7f0800fd;

  private View view7f0802ff;

  private View view7f080306;

  private View view7f0800a7;

  private View view7f080305;

  private View view7f0800fe;

  private View view7f0800ff;

  private View view7f08046a;

  private View view7f080386;

  private View view7f080304;

  @UiThread
  public YYJHomeFragment_ViewBinding(final YYJHomeFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.symbol_study_cover, "field 'symbolStudyCover' and method 'onViewClicked'");
    target.symbolStudyCover = Utils.castView(view, R.id.symbol_study_cover, "field 'symbolStudyCover'", FrameLayout.class);
    view7f080325 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_listening_layout, "field 'studyListeningLayout' and method 'onViewClicked'");
    target.studyListeningLayout = Utils.castView(view, R.id.study_listening_layout, "field 'studyListeningLayout'", FrameLayout.class);
    view7f080302 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.en_examination_layout, "field 'enExaminationLayout' and method 'onViewClicked'");
    target.enExaminationLayout = Utils.castView(view, R.id.en_examination_layout, "field 'enExaminationLayout'", FrameLayout.class);
    view7f0800fd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_composition, "field 'studyComposition' and method 'onViewClicked'");
    target.studyComposition = Utils.castView(view, R.id.study_composition, "field 'studyComposition'", FrameLayout.class);
    view7f0802ff = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_word_layout, "field 'studyWordLayout' and method 'onViewClicked'");
    target.studyWordLayout = Utils.castView(view, R.id.study_word_layout, "field 'studyWordLayout'", FrameLayout.class);
    view7f080306 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.collected_layout, "field 'collectedLayout' and method 'onViewClicked'");
    target.collectedLayout = Utils.castView(view, R.id.collected_layout, "field 'collectedLayout'", FrameLayout.class);
    view7f0800a7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_spoken_english, "field 'studySpokenEnglish' and method 'onViewClicked'");
    target.studySpokenEnglish = Utils.castView(view, R.id.study_spoken_english, "field 'studySpokenEnglish'", FrameLayout.class);
    view7f080305 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.en_grammar, "field 'enGrammar' and method 'onViewClicked'");
    target.enGrammar = Utils.castView(view, R.id.en_grammar, "field 'enGrammar'", FrameLayout.class);
    view7f0800fe = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.en_story_layout, "field 'enStoryLayout' and method 'onViewClicked'");
    target.enStoryLayout = Utils.castView(view, R.id.en_story_layout, "field 'enStoryLayout'", FrameLayout.class);
    view7f0800ff = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.xmly_layout, "field 'xmlyLayout' and method 'onViewClicked'");
    target.xmlyLayout = Utils.castView(view, R.id.xmly_layout, "field 'xmlyLayout'", FrameLayout.class);
    view7f08046a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.translate_layout, "field 'translateLayout' and method 'onViewClicked'");
    target.translateLayout = Utils.castView(view, R.id.translate_layout, "field 'translateLayout'", FrameLayout.class);
    view7f080386 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_setting, "field 'studySetting' and method 'onViewClicked'");
    target.studySetting = Utils.castView(view, R.id.study_setting, "field 'studySetting'", FrameLayout.class);
    view7f080304 = view;
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
    YYJHomeFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.symbolStudyCover = null;
    target.studyListeningLayout = null;
    target.enExaminationLayout = null;
    target.studyComposition = null;
    target.studyWordLayout = null;
    target.collectedLayout = null;
    target.studySpokenEnglish = null;
    target.enGrammar = null;
    target.enStoryLayout = null;
    target.xmlyLayout = null;
    target.translateLayout = null;
    target.studySetting = null;

    view7f080325.setOnClickListener(null);
    view7f080325 = null;
    view7f080302.setOnClickListener(null);
    view7f080302 = null;
    view7f0800fd.setOnClickListener(null);
    view7f0800fd = null;
    view7f0802ff.setOnClickListener(null);
    view7f0802ff = null;
    view7f080306.setOnClickListener(null);
    view7f080306 = null;
    view7f0800a7.setOnClickListener(null);
    view7f0800a7 = null;
    view7f080305.setOnClickListener(null);
    view7f080305 = null;
    view7f0800fe.setOnClickListener(null);
    view7f0800fe = null;
    view7f0800ff.setOnClickListener(null);
    view7f0800ff = null;
    view7f08046a.setOnClickListener(null);
    view7f08046a = null;
    view7f080386.setOnClickListener(null);
    view7f080386 = null;
    view7f080304.setOnClickListener(null);
    view7f080304 = null;
  }
}
