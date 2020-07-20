// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordStudyDanCiPinXieActivity_ViewBinding implements Unbinder {
  private WordStudyDanCiPinXieActivity target;

  private View view7f0802c7;

  private View view7f08029c;

  private View view7f0804a5;

  private View view7f0804ae;

  private View view7f0803d9;

  private View view7f080147;

  @UiThread
  public WordStudyDanCiPinXieActivity_ViewBinding(WordStudyDanCiPinXieActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WordStudyDanCiPinXieActivity_ViewBinding(final WordStudyDanCiPinXieActivity target,
      View source) {
    this.target = target;

    View view;
    target.score = Utils.findRequiredViewAsType(source, R.id.score, "field 'score'", TextView.class);
    view = Utils.findRequiredView(source, R.id.retry_tv, "field 'retry_tv' and method 'onClick'");
    target.retry_tv = Utils.castView(view, R.id.retry_tv, "field 'retry_tv'", TextView.class);
    view7f0802c7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.prompt_tv, "field 'prompt_tv' and method 'onClick'");
    target.prompt_tv = Utils.castView(view, R.id.prompt_tv, "field 'prompt_tv'", TextView.class);
    view7f08029c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.wordDes = Utils.findRequiredViewAsType(source, R.id.word_des, "field 'wordDes'", TextView.class);
    target.wordName = Utils.findRequiredViewAsType(source, R.id.word_name, "field 'wordName'", TextView.class);
    target.volumeImg = Utils.findRequiredViewAsType(source, R.id.volume_img, "field 'volumeImg'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.volume_layout, "field 'volumeLayout' and method 'onClick'");
    target.volumeLayout = Utils.castView(view, R.id.volume_layout, "field 'volumeLayout'", FrameLayout.class);
    view7f0804a5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.word_layout, "field 'wordLayout' and method 'onClick'");
    target.wordLayout = Utils.castView(view, R.id.word_layout, "field 'wordLayout'", RelativeLayout.class);
    view7f0804ae = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.wordSpellGv = Utils.findRequiredViewAsType(source, R.id.word_spell_gv, "field 'wordSpellGv'", GridView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.try_again_layout, "field 'tryAgainLayout' and method 'onClick'");
    target.tryAgainLayout = Utils.castView(view, R.id.try_again_layout, "field 'tryAgainLayout'", FrameLayout.class);
    view7f0803d9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.finish_test_layout, "field 'finishTestLayout' and method 'onClick'");
    target.finishTestLayout = Utils.castView(view, R.id.finish_test_layout, "field 'finishTestLayout'", FrameLayout.class);
    view7f080147 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.resultLayout = Utils.findRequiredViewAsType(source, R.id.result_layout, "field 'resultLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WordStudyDanCiPinXieActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.score = null;
    target.retry_tv = null;
    target.prompt_tv = null;
    target.wordDes = null;
    target.wordName = null;
    target.volumeImg = null;
    target.volumeLayout = null;
    target.wordLayout = null;
    target.wordSpellGv = null;
    target.listview = null;
    target.tryAgainLayout = null;
    target.finishTestLayout = null;
    target.resultLayout = null;

    view7f0802c7.setOnClickListener(null);
    view7f0802c7 = null;
    view7f08029c.setOnClickListener(null);
    view7f08029c = null;
    view7f0804a5.setOnClickListener(null);
    view7f0804a5 = null;
    view7f0804ae.setOnClickListener(null);
    view7f0804ae = null;
    view7f0803d9.setOnClickListener(null);
    view7f0803d9 = null;
    view7f080147.setOnClickListener(null);
    view7f080147 = null;
  }
}
