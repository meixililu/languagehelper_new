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
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LeisureFragment_ViewBinding implements Unbinder {
  private LeisureFragment target;

  private View view2131624282;

  private View view2131624283;

  private View view2131624112;

  private View view2131624279;

  private View view2131624280;

  private View view2131624287;

  private View view2131624286;

  private View view2131624284;

  private View view2131624288;

  private View view2131624281;

  private View view2131624285;

  @UiThread
  public LeisureFragment_ViewBinding(final LeisureFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.yuedu_layout, "field 'yueduLayout' and method 'onViewClicked'");
    target.yueduLayout = Utils.castView(view, R.id.yuedu_layout, "field 'yueduLayout'", FrameLayout.class);
    view2131624282 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.twists_layout, "field 'twistsLayout' and method 'onViewClicked'");
    target.twistsLayout = Utils.castView(view, R.id.twists_layout, "field 'twistsLayout'", FrameLayout.class);
    view2131624283 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.xx_ad_layout, "field 'xx_ad_layout' and method 'onViewClicked'");
    target.xx_ad_layout = Utils.castView(view, R.id.xx_ad_layout, "field 'xx_ad_layout'", RelativeLayout.class);
    view2131624112 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.cailing_layout, "field 'cailing_layout' and method 'onViewClicked'");
    target.cailing_layout = Utils.castView(view, R.id.cailing_layout, "field 'cailing_layout'", FrameLayout.class);
    view2131624279 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.baidu_layout, "field 'baidu_layout' and method 'onViewClicked'");
    target.baidu_layout = Utils.castView(view, R.id.baidu_layout, "field 'baidu_layout'", FrameLayout.class);
    view2131624280 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_layout, "field 'app_layout' and method 'onViewClicked'");
    target.app_layout = Utils.castView(view, R.id.app_layout, "field 'app_layout'", FrameLayout.class);
    view2131624287 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.news_layout, "field 'news_layout' and method 'onViewClicked'");
    target.news_layout = Utils.castView(view, R.id.news_layout, "field 'news_layout'", FrameLayout.class);
    view2131624286 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.game_layout, "field 'game_layout' and method 'onViewClicked'");
    target.game_layout = Utils.castView(view, R.id.game_layout, "field 'game_layout'", FrameLayout.class);
    view2131624284 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.invest_layout, "field 'invest_layout' and method 'onViewClicked'");
    target.invest_layout = Utils.castView(view, R.id.invest_layout, "field 'invest_layout'", FrameLayout.class);
    view2131624288 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.sougou_layout, "field 'sougou_layout' and method 'onViewClicked'");
    target.sougou_layout = Utils.castView(view, R.id.sougou_layout, "field 'sougou_layout'", FrameLayout.class);
    view2131624281 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shenhuifu_layout, "field 'shenhuifuLayout' and method 'onViewClicked'");
    target.shenhuifuLayout = Utils.castView(view, R.id.shenhuifu_layout, "field 'shenhuifuLayout'", FrameLayout.class);
    view2131624285 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LeisureFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.yueduLayout = null;
    target.twistsLayout = null;
    target.xx_ad_layout = null;
    target.cailing_layout = null;
    target.baidu_layout = null;
    target.app_layout = null;
    target.news_layout = null;
    target.game_layout = null;
    target.invest_layout = null;
    target.sougou_layout = null;
    target.shenhuifuLayout = null;
    target.adImg = null;

    view2131624282.setOnClickListener(null);
    view2131624282 = null;
    view2131624283.setOnClickListener(null);
    view2131624283 = null;
    view2131624112.setOnClickListener(null);
    view2131624112 = null;
    view2131624279.setOnClickListener(null);
    view2131624279 = null;
    view2131624280.setOnClickListener(null);
    view2131624280 = null;
    view2131624287.setOnClickListener(null);
    view2131624287 = null;
    view2131624286.setOnClickListener(null);
    view2131624286 = null;
    view2131624284.setOnClickListener(null);
    view2131624284 = null;
    view2131624288.setOnClickListener(null);
    view2131624288 = null;
    view2131624281.setOnClickListener(null);
    view2131624281 = null;
    view2131624285.setOnClickListener(null);
    view2131624285 = null;
  }
}
