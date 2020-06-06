// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AiDialogueFightYYSActivity_ViewBinding implements Unbinder {
  private AiDialogueFightYYSActivity target;

  @UiThread
  public AiDialogueFightYYSActivity_ViewBinding(AiDialogueFightYYSActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AiDialogueFightYYSActivity_ViewBinding(AiDialogueFightYYSActivity target, View source) {
    this.target = target;

    target.studylist_lv = Utils.findRequiredViewAsType(source, R.id.listview, "field 'studylist_lv'", RecyclerView.class);
    target.previous_btn = Utils.findRequiredViewAsType(source, R.id.previous_btn, "field 'previous_btn'", FrameLayout.class);
    target.start_btn = Utils.findRequiredViewAsType(source, R.id.start_btn, "field 'start_btn'", TextView.class);
    target.start_btn_cover = Utils.findRequiredViewAsType(source, R.id.start_btn_cover, "field 'start_btn_cover'", FrameLayout.class);
    target.next_btn = Utils.findRequiredViewAsType(source, R.id.next_btn, "field 'next_btn'", FrameLayout.class);
    target.record_anim_img = Utils.findRequiredViewAsType(source, R.id.record_anim_img, "field 'record_anim_img'", ImageView.class);
    target.record_layout = Utils.findRequiredViewAsType(source, R.id.record_layout, "field 'record_layout'", LinearLayout.class);
    target.voice_img = Utils.findRequiredViewAsType(source, R.id.voice_img, "field 'voice_img'", ImageButton.class);
    target.conversationLayout = Utils.findRequiredViewAsType(source, R.id.conversation_layout, "field 'conversationLayout'", RelativeLayout.class);
    target.speakerContent = Utils.findRequiredViewAsType(source, R.id.speaker_content, "field 'speakerContent'", TextView.class);
    target.userContent = Utils.findRequiredViewAsType(source, R.id.user_content, "field 'userContent'", TextView.class);
    target.speakerImg = Utils.findRequiredViewAsType(source, R.id.speaker_img, "field 'speakerImg'", SimpleDraweeView.class);
    target.userImg = Utils.findRequiredViewAsType(source, R.id.user_img, "field 'userImg'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AiDialogueFightYYSActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.studylist_lv = null;
    target.previous_btn = null;
    target.start_btn = null;
    target.start_btn_cover = null;
    target.next_btn = null;
    target.record_anim_img = null;
    target.record_layout = null;
    target.voice_img = null;
    target.conversationLayout = null;
    target.speakerContent = null;
    target.userContent = null;
    target.speakerImg = null;
    target.userImg = null;
  }
}
