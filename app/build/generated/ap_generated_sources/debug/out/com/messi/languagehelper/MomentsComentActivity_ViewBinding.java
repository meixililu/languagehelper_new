// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MomentsComentActivity_ViewBinding implements Unbinder {
  private MomentsComentActivity target;

  private View view7f080322;

  @UiThread
  public MomentsComentActivity_ViewBinding(MomentsComentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MomentsComentActivity_ViewBinding(final MomentsComentActivity target, View source) {
    this.target = target;

    View view;
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", RecyclerView.class);
    target.content_tv = Utils.findRequiredViewAsType(source, R.id.content, "field 'content_tv'", TextView.class);
    target.noComment = Utils.findRequiredViewAsType(source, R.id.no_comment, "field 'noComment'", TextView.class);
    target.inputEt = Utils.findRequiredViewAsType(source, R.id.input_et, "field 'inputEt'", AppCompatEditText.class);
    view = Utils.findRequiredView(source, R.id.submit_btn, "field 'submitBtn' and method 'onClick'");
    target.submitBtn = Utils.castView(view, R.id.submit_btn, "field 'submitBtn'", TextView.class);
    view7f080322 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MomentsComentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.listview = null;
    target.content_tv = null;
    target.noComment = null;
    target.inputEt = null;
    target.submitBtn = null;

    view7f080322.setOnClickListener(null);
    view7f080322 = null;
  }
}
