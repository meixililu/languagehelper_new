// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LeisureFragment_ViewBinding<T extends LeisureFragment> implements Unbinder {
  protected T target;

  private View view2131558682;

  @UiThread
  public LeisureFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.yueduLayout = Utils.findRequiredViewAsType(source, R.id.yuedu_layout, "field 'yueduLayout'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.twists_layout, "field 'twistsLayout' and method 'onClick'");
    target.twistsLayout = Utils.castView(view, R.id.twists_layout, "field 'twistsLayout'", FrameLayout.class);
    view2131558682 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", RelativeLayout.class);
    target.cailing_layout = Utils.findRequiredViewAsType(source, R.id.cailing_layout, "field 'cailing_layout'", FrameLayout.class);
    target.baidu_layout = Utils.findRequiredViewAsType(source, R.id.baidu_layout, "field 'baidu_layout'", FrameLayout.class);
    target.app_layout = Utils.findRequiredViewAsType(source, R.id.app_layout, "field 'app_layout'", FrameLayout.class);
    target.news_layout = Utils.findRequiredViewAsType(source, R.id.news_layout, "field 'news_layout'", FrameLayout.class);
    target.game_layout = Utils.findRequiredViewAsType(source, R.id.game_layout, "field 'game_layout'", FrameLayout.class);
    target.invest_layout = Utils.findRequiredViewAsType(source, R.id.invest_layout, "field 'invest_layout'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.yueduLayout = null;
    target.twistsLayout = null;
    target.xx_ad_layout = null;
    target.cailing_layout = null;
    target.baidu_layout = null;
    target.app_layout = null;
    target.news_layout = null;
    target.game_layout = null;
    target.invest_layout = null;

    view2131558682.setOnClickListener(null);
    view2131558682 = null;

    this.target = null;
  }
}
