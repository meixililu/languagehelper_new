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

  private View view7f0802ae;

  private View view7f080290;

  private View view7f0800e7;

  private View view7f08028d;

  private View view7f080293;

  private View view7f08009b;

  private View view7f080292;

  private View view7f0800e8;

  private View view7f0800e9;

  private View view7f0803bb;

  private View view7f0802f0;

  private View view7f080291;

  @UiThread
  public YYJHomeFragment_ViewBinding(final YYJHomeFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.symbol_study_cover, "field 'symbolStudyCover' and method 'onViewClicked'");
    target.symbolStudyCover = Utils.castView(view, R.id.symbol_study_cover, "field 'symbolStudyCover'", FrameLayout.class);
    view7f0802ae = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_listening_layout, "field 'studyListeningLayout' and method 'onViewClicked'");
    target.studyListeningLayout = Utils.castView(view, R.id.study_listening_layout, "field 'studyListeningLayout'", FrameLayout.class);
    view7f080290 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.en_examination_layout, "field 'enExaminationLayout' and method 'onViewClicked'");
    target.enExaminationLayout = Utils.castView(view, R.id.en_examination_layout, "field 'enExaminationLayout'", FrameLayout.class);
    view7f0800e7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_composition, "field 'studyComposition' and method 'onViewClicked'");
    target.studyComposition = Utils.castView(view, R.id.study_composition, "field 'studyComposition'", FrameLayout.class);
    view7f08028d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_word_layout, "field 'studyWordLayout' and method 'onViewClicked'");
    target.studyWordLayout = Utils.castView(view, R.id.study_word_layout, "field 'studyWordLayout'", FrameLayout.class);
    view7f080293 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.collected_layout, "field 'collectedLayout' and method 'onViewClicked'");
    target.collectedLayout = Utils.castView(view, R.id.collected_layout, "field 'collectedLayout'", FrameLayout.class);
    view7f08009b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_spoken_english, "field 'studySpokenEnglish' and method 'onViewClicked'");
    target.studySpokenEnglish = Utils.castView(view, R.id.study_spoken_english, "field 'studySpokenEnglish'", FrameLayout.class);
    view7f080292 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.en_grammar, "field 'enGrammar' and method 'onViewClicked'");
    target.enGrammar = Utils.castView(view, R.id.en_grammar, "field 'enGrammar'", FrameLayout.class);
    view7f0800e8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.en_story_layout, "field 'enStoryLayout' and method 'onViewClicked'");
    target.enStoryLayout = Utils.castView(view, R.id.en_story_layout, "field 'enStoryLayout'", FrameLayout.class);
    view7f0800e9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.xmly_layout, "field 'xmlyLayout' and method 'onViewClicked'");
    target.xmlyLayout = Utils.castView(view, R.id.xmly_layout, "field 'xmlyLayout'", FrameLayout.class);
    view7f0803bb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.translate_layout, "field 'translateLayout' and method 'onViewClicked'");
    target.translateLayout = Utils.castView(view, R.id.translate_layout, "field 'translateLayout'", FrameLayout.class);
    view7f0802f0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.study_setting, "field 'studySetting' and method 'onViewClicked'");
    target.studySetting = Utils.castView(view, R.id.study_setting, "field 'studySetting'", FrameLayout.class);
    view7f080291 = view;
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

    view7f0802ae.setOnClickListener(null);
    view7f0802ae = null;
    view7f080290.setOnClickListener(null);
    view7f080290 = null;
    view7f0800e7.setOnClickListener(null);
    view7f0800e7 = null;
    view7f08028d.setOnClickListener(null);
    view7f08028d = null;
    view7f080293.setOnClickListener(null);
    view7f080293 = null;
    view7f08009b.setOnClickListener(null);
    view7f08009b = null;
    view7f080292.setOnClickListener(null);
    view7f080292 = null;
    view7f0800e8.setOnClickListener(null);
    view7f0800e8 = null;
    view7f0800e9.setOnClickListener(null);
    view7f0800e9 = null;
    view7f0803bb.setOnClickListener(null);
    view7f0803bb = null;
    view7f0802f0.setOnClickListener(null);
    view7f0802f0 = null;
    view7f080291.setOnClickListener(null);
    view7f080291 = null;
  }
}
