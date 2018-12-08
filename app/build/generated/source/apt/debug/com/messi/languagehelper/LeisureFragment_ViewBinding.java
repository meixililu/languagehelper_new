// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LeisureFragment_ViewBinding implements Unbinder {
  private LeisureFragment target;

  private View view7f08030f;

  private View view7f0802c1;

  private View view7f08030d;

  private View view7f08006a;

  private View view7f080057;

  private View view7f08004a;

  private View view7f08017e;

  private View view7f080115;

  private View view7f080131;

  private View view7f080236;

  private View view7f080229;

  private View view7f080305;

  private View view7f080200;

  private View view7f08018f;

  private View view7f08006d;

  private View view7f08013c;

  @UiThread
  public LeisureFragment_ViewBinding(final LeisureFragment target, View source) {
    this.target = target;

    View view;
    target.ad_sign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'ad_sign'", TextView.class);
    view = Utils.findRequiredView(source, R.id.yuedu_layout, "field 'yueduLayout' and method 'onViewClicked'");
    target.yueduLayout = Utils.castView(view, R.id.yuedu_layout, "field 'yueduLayout'", FrameLayout.class);
    view7f08030f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.twists_layout, "field 'twistsLayout' and method 'onViewClicked'");
    target.twistsLayout = Utils.castView(view, R.id.twists_layout, "field 'twistsLayout'", FrameLayout.class);
    view7f0802c1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.xx_ad_layout, "field 'xx_ad_layout' and method 'onViewClicked'");
    target.xx_ad_layout = Utils.castView(view, R.id.xx_ad_layout, "field 'xx_ad_layout'", FrameLayout.class);
    view7f08030d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.cailing_layout, "field 'cailing_layout' and method 'onViewClicked'");
    target.cailing_layout = Utils.castView(view, R.id.cailing_layout, "field 'cailing_layout'", FrameLayout.class);
    view7f08006a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.baidu_layout, "field 'baidu_layout' and method 'onViewClicked'");
    target.baidu_layout = Utils.castView(view, R.id.baidu_layout, "field 'baidu_layout'", FrameLayout.class);
    view7f080057 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_layout, "field 'app_layout' and method 'onViewClicked'");
    target.app_layout = Utils.castView(view, R.id.app_layout, "field 'app_layout'", FrameLayout.class);
    view7f08004a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.news_layout, "field 'news_layout' and method 'onViewClicked'");
    target.news_layout = Utils.castView(view, R.id.news_layout, "field 'news_layout'", FrameLayout.class);
    view7f08017e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.game_layout, "field 'game_layout' and method 'onViewClicked'");
    target.game_layout = Utils.castView(view, R.id.game_layout, "field 'game_layout'", FrameLayout.class);
    view7f080115 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.invest_layout, "field 'invest_layout' and method 'onViewClicked'");
    target.invest_layout = Utils.castView(view, R.id.invest_layout, "field 'invest_layout'", FrameLayout.class);
    view7f080131 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.sougou_layout, "field 'sougou_layout' and method 'onViewClicked'");
    target.sougou_layout = Utils.castView(view, R.id.sougou_layout, "field 'sougou_layout'", FrameLayout.class);
    view7f080236 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shenhuifu_layout, "field 'shenhuifuLayout' and method 'onViewClicked'");
    target.shenhuifuLayout = Utils.castView(view, R.id.shenhuifu_layout, "field 'shenhuifuLayout'", FrameLayout.class);
    view7f080229 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.wyyx_layout, "field 'wyyx_layout' and method 'onViewClicked'");
    target.wyyx_layout = Utils.castView(view, R.id.wyyx_layout, "field 'wyyx_layout'", FrameLayout.class);
    view7f080305 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_layout, "field 'search_layout' and method 'onViewClicked'");
    target.search_layout = Utils.castView(view, R.id.search_layout, "field 'search_layout'", FrameLayout.class);
    view7f080200 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.novel_layout, "field 'novelLayout' and method 'onViewClicked'");
    target.novelLayout = Utils.castView(view, R.id.novel_layout, "field 'novelLayout'", FrameLayout.class);
    view7f08018f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.caricature_layout, "field 'caricatureLayout' and method 'onViewClicked'");
    target.caricatureLayout = Utils.castView(view, R.id.caricature_layout, "field 'caricatureLayout'", FrameLayout.class);
    view7f08006d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.jd_layout, "field 'jdLayout' and method 'onViewClicked'");
    target.jdLayout = Utils.castView(view, R.id.jd_layout, "field 'jdLayout'", FrameLayout.class);
    view7f08013c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.rootView = Utils.findRequiredViewAsType(source, R.id.root_view, "field 'rootView'", NestedScrollView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LeisureFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ad_sign = null;
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
    target.wyyx_layout = null;
    target.search_layout = null;
    target.adImg = null;
    target.novelLayout = null;
    target.caricatureLayout = null;
    target.jdLayout = null;
    target.rootView = null;

    view7f08030f.setOnClickListener(null);
    view7f08030f = null;
    view7f0802c1.setOnClickListener(null);
    view7f0802c1 = null;
    view7f08030d.setOnClickListener(null);
    view7f08030d = null;
    view7f08006a.setOnClickListener(null);
    view7f08006a = null;
    view7f080057.setOnClickListener(null);
    view7f080057 = null;
    view7f08004a.setOnClickListener(null);
    view7f08004a = null;
    view7f08017e.setOnClickListener(null);
    view7f08017e = null;
    view7f080115.setOnClickListener(null);
    view7f080115 = null;
    view7f080131.setOnClickListener(null);
    view7f080131 = null;
    view7f080236.setOnClickListener(null);
    view7f080236 = null;
    view7f080229.setOnClickListener(null);
    view7f080229 = null;
    view7f080305.setOnClickListener(null);
    view7f080305 = null;
    view7f080200.setOnClickListener(null);
    view7f080200 = null;
    view7f08018f.setOnClickListener(null);
    view7f08018f = null;
    view7f08006d.setOnClickListener(null);
    view7f08006d = null;
    view7f08013c.setOnClickListener(null);
    view7f08013c = null;
  }
}
