// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AiDialoguePracticeYYSActivity_ViewBinding implements Unbinder {
  private AiDialoguePracticeYYSActivity target;

  private View view7f0802ef;

  @UiThread
  public AiDialoguePracticeYYSActivity_ViewBinding(AiDialoguePracticeYYSActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AiDialoguePracticeYYSActivity_ViewBinding(final AiDialoguePracticeYYSActivity target,
      View source) {
    this.target = target;

    View view;
    target.studylist_lv = Utils.findRequiredViewAsType(source, R.id.listview, "field 'studylist_lv'", RecyclerView.class);
    target.start_btn = Utils.findRequiredViewAsType(source, R.id.start_btn, "field 'start_btn'", TextView.class);
    target.start_btn_cover = Utils.findRequiredViewAsType(source, R.id.start_btn_cover, "field 'start_btn_cover'", FrameLayout.class);
    target.record_anim_img = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'record_anim_img'", ImageView.class);
    target.record_layout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'record_layout'", LinearLayout.class);
    target.voice_img = Utils.findRequiredViewAsType(source, R.id.voice_img, "field 'voice_img'", ImageButton.class);
    view = Utils.findRequiredView(source, R.id.start_to_fight, "field 'startToFight' and method 'onViewClicked'");
    target.startToFight = Utils.castView(view, R.id.start_to_fight, "field 'startToFight'", FrameLayout.class);
    view7f0802ef = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AiDialoguePracticeYYSActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.studylist_lv = null;
    target.start_btn = null;
    target.start_btn_cover = null;
    target.record_anim_img = null;
    target.record_layout = null;
    target.voice_img = null;
    target.startToFight = null;

    view7f0802ef.setOnClickListener(null);
    view7f0802ef = null;
  }
}
