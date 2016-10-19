// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import java.lang.IllegalStateException;
import java.lang.Override;

public class JokeListAdapter$ViewHolder_ViewBinding<T extends JokeListAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public JokeListAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.profileImage = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'profileImage'", SimpleDraweeView.class);
    target.name = Utils.findRequiredViewAsType(source, R.id.name, "field 'name'", TextView.class);
    target.time = Utils.findRequiredViewAsType(source, R.id.time, "field 'time'", TextView.class);
    target.des = Utils.findRequiredViewAsType(source, R.id.des, "field 'des'", TextView.class);
    target.listItemImg = Utils.findRequiredViewAsType(source, R.id.list_item_img, "field 'listItemImg'", SimpleDraweeView.class);
    target.layoutCover = Utils.findRequiredViewAsType(source, R.id.layout_cover, "field 'layoutCover'", LinearLayout.class);
    target.videoplayer = Utils.findRequiredViewAsType(source, R.id.videoplayer, "field 'videoplayer'", JCVideoPlayerStandard.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.profileImage = null;
    target.name = null;
    target.time = null;
    target.des = null;
    target.listItemImg = null;
    target.layoutCover = null;
    target.videoplayer = null;

    this.target = null;
  }
}
