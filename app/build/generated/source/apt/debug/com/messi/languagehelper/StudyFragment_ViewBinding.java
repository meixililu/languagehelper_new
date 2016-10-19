// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StudyFragment_ViewBinding<T extends StudyFragment> implements Unbinder {
  protected T target;

  @UiThread
  public StudyFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.symbol_study_cover = Utils.findRequiredViewAsType(source, R.id.symbol_study_cover, "field 'symbol_study_cover'", TextView.class);
    target.daily_sentence_item_img = Utils.findRequiredViewAsType(source, R.id.daily_sentence_item_img, "field 'daily_sentence_item_img'", SimpleDraweeView.class);
    target.daily_sentence_list_item_middle = Utils.findRequiredViewAsType(source, R.id.daily_sentence_list_item_middle, "field 'daily_sentence_list_item_middle'", LinearLayout.class);
    target.dailysentence_txt = Utils.findRequiredViewAsType(source, R.id.dailysentence_txt, "field 'dailysentence_txt'", TextView.class);
    target.play_img = Utils.findRequiredViewAsType(source, R.id.play_img, "field 'play_img'", ImageView.class);
    target.study_daily_sentence = Utils.findRequiredViewAsType(source, R.id.study_daily_sentence, "field 'study_daily_sentence'", FrameLayout.class);
    target.word_study_cover = Utils.findRequiredViewAsType(source, R.id.word_study_cover, "field 'word_study_cover'", FrameLayout.class);
    target.study_listening_layout = Utils.findRequiredViewAsType(source, R.id.study_listening_layout, "field 'study_listening_layout'", FrameLayout.class);
    target.news_layout = Utils.findRequiredViewAsType(source, R.id.news_layout, "field 'news_layout'", FrameLayout.class);
    target.study_spoken_english = Utils.findRequiredViewAsType(source, R.id.study_spoken_english, "field 'study_spoken_english'", FrameLayout.class);
    target.study_test = Utils.findRequiredViewAsType(source, R.id.study_test, "field 'study_test'", FrameLayout.class);
    target.en_examination_layout = Utils.findRequiredViewAsType(source, R.id.en_examination_layout, "field 'en_examination_layout'", FrameLayout.class);
    target.study_composition = Utils.findRequiredViewAsType(source, R.id.study_composition, "field 'study_composition'", FrameLayout.class);
    target.juhe_layout = Utils.findRequiredViewAsType(source, R.id.juhe_layout, "field 'juhe_layout'", FrameLayout.class);
    target.jokes_layout = Utils.findRequiredViewAsType(source, R.id.jokes_layout, "field 'jokes_layout'", FrameLayout.class);
    target.instagram_layout = Utils.findRequiredViewAsType(source, R.id.instagram_layout, "field 'instagram_layout'", FrameLayout.class);
    target.story_layout = Utils.findRequiredViewAsType(source, R.id.story_layout, "field 'story_layout'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.symbol_study_cover = null;
    target.daily_sentence_item_img = null;
    target.daily_sentence_list_item_middle = null;
    target.dailysentence_txt = null;
    target.play_img = null;
    target.study_daily_sentence = null;
    target.word_study_cover = null;
    target.study_listening_layout = null;
    target.news_layout = null;
    target.study_spoken_english = null;
    target.study_test = null;
    target.en_examination_layout = null;
    target.study_composition = null;
    target.juhe_layout = null;
    target.jokes_layout = null;
    target.instagram_layout = null;
    target.story_layout = null;

    this.target = null;
  }
}
