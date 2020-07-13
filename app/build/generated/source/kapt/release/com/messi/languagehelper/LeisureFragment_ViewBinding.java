// Generated code from Butter Knife. Do not modify!
package com.messi.languagehelper;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.core.widget.NestedScrollView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LeisureFragment_ViewBinding implements Unbinder {
  private LeisureFragment target;

  private View view7f080483;

  private View view7f080434;

  private View view7f08009d;

  private View view7f080084;

  private View view7f08006a;

  private View view7f08020d;

  private View view7f08015b;

  private View view7f08018e;

  private View view7f0802e4;

  private View view7f0802d4;

  private View view7f0801b0;

  private View view7f0802aa;

  private View view7f080228;

  private View view7f0800a3;

  private View view7f08019e;

  private View view7f080119;

  private View view7f0801b4;

  private View view7f0801a7;

  private View view7f0801ab;

  private View view7f080269;

  private View view7f0801b5;

  private View view7f0801b2;

  private View view7f0801a1;

  private View view7f0801ad;

  @UiThread
  public LeisureFragment_ViewBinding(final LeisureFragment target, View source) {
    this.target = target;

    View view;
    target.ad_sign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'ad_sign'", TextView.class);
    view = Utils.findRequiredView(source, R.id.yuedu_layout, "field 'yueduLayout' and method 'onViewClicked'");
    target.yueduLayout = Utils.castView(view, R.id.yuedu_layout, "field 'yueduLayout'", FrameLayout.class);
    view7f080483 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.twists_layout, "field 'twistsLayout' and method 'onViewClicked'");
    target.twistsLayout = Utils.castView(view, R.id.twists_layout, "field 'twistsLayout'", FrameLayout.class);
    view7f080434 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.xx_ad_layout = Utils.findRequiredViewAsType(source, R.id.xx_ad_layout, "field 'xx_ad_layout'", FrameLayout.class);
    target.ad_layout = Utils.findRequiredViewAsType(source, R.id.ad_layout, "field 'ad_layout'", FrameLayout.class);
    view = Utils.findRequiredView(source, R.id.cailing_layout, "field 'cailing_layout' and method 'onViewClicked'");
    target.cailing_layout = Utils.castView(view, R.id.cailing_layout, "field 'cailing_layout'", FrameLayout.class);
    view7f08009d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.baidu_layout, "field 'baidu_layout' and method 'onViewClicked'");
    target.baidu_layout = Utils.castView(view, R.id.baidu_layout, "field 'baidu_layout'", FrameLayout.class);
    view7f080084 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_layout, "field 'app_layout' and method 'onViewClicked'");
    target.app_layout = Utils.castView(view, R.id.app_layout, "field 'app_layout'", FrameLayout.class);
    view7f08006a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.news_layout, "field 'news_layout' and method 'onViewClicked'");
    target.news_layout = Utils.castView(view, R.id.news_layout, "field 'news_layout'", FrameLayout.class);
    view7f08020d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.game_layout, "field 'game_layout' and method 'onViewClicked'");
    target.game_layout = Utils.castView(view, R.id.game_layout, "field 'game_layout'", FrameLayout.class);
    view7f08015b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.invest_layout, "field 'invest_layout' and method 'onViewClicked'");
    target.invest_layout = Utils.castView(view, R.id.invest_layout, "field 'invest_layout'", FrameLayout.class);
    view7f08018e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.sougou_layout, "field 'sougou_layout' and method 'onViewClicked'");
    target.sougou_layout = Utils.castView(view, R.id.sougou_layout, "field 'sougou_layout'", FrameLayout.class);
    view7f0802e4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shenhuifu_layout, "field 'shenhuifuLayout' and method 'onViewClicked'");
    target.shenhuifuLayout = Utils.castView(view, R.id.shenhuifu_layout, "field 'shenhuifuLayout'", FrameLayout.class);
    view7f0802d4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_riddle, "field 'layout_riddle' and method 'onViewClicked'");
    target.layout_riddle = Utils.castView(view, R.id.layout_riddle, "field 'layout_riddle'", FrameLayout.class);
    view7f0801b0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_layout, "field 'search_layout' and method 'onViewClicked'");
    target.search_layout = Utils.castView(view, R.id.search_layout, "field 'search_layout'", FrameLayout.class);
    view7f0802aa = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.novel_layout, "field 'novelLayout' and method 'onViewClicked'");
    target.novelLayout = Utils.castView(view, R.id.novel_layout, "field 'novelLayout'", FrameLayout.class);
    view7f080228 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.caricature_layout, "field 'caricatureLayout' and method 'onViewClicked'");
    target.caricatureLayout = Utils.castView(view, R.id.caricature_layout, "field 'caricatureLayout'", FrameLayout.class);
    view7f0800a3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.jd_layout, "field 'jdLayout' and method 'onViewClicked'");
    target.jdLayout = Utils.castView(view, R.id.jd_layout, "field 'jdLayout'", FrameLayout.class);
    view7f08019e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.english_essay_layout, "field 'english_essay_layout' and method 'onViewClicked'");
    target.english_essay_layout = Utils.castView(view, R.id.english_essay_layout, "field 'english_essay_layout'", FrameLayout.class);
    view7f080119 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_whyy, "field 'layout_whyy' and method 'onViewClicked'");
    target.layout_whyy = Utils.castView(view, R.id.layout_whyy, "field 'layout_whyy'", FrameLayout.class);
    view7f0801b4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_conjecture, "field 'layout_conjecture' and method 'onViewClicked'");
    target.layout_conjecture = Utils.castView(view, R.id.layout_conjecture, "field 'layout_conjecture'", FrameLayout.class);
    view7f0801a7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_history, "field 'layout_history' and method 'onViewClicked'");
    target.layout_history = Utils.castView(view, R.id.layout_history, "field 'layout_history'", FrameLayout.class);
    view7f0801ab = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.radio_layout, "field 'radio_layout' and method 'onViewClicked'");
    target.radio_layout = Utils.castView(view, R.id.radio_layout, "field 'radio_layout'", FrameLayout.class);
    view7f080269 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_xiehouyu, "field 'layout_xiehouyu' and method 'onViewClicked'");
    target.layout_xiehouyu = Utils.castView(view, R.id.layout_xiehouyu, "field 'layout_xiehouyu'", FrameLayout.class);
    view7f0801b5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_tongue_twister, "field 'layout_tongue_twister' and method 'onViewClicked'");
    target.layout_tongue_twister = Utils.castView(view, R.id.layout_tongue_twister, "field 'layout_tongue_twister'", FrameLayout.class);
    view7f0801b2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ksearch_layout, "field 'ksearch_layout' and method 'onViewClicked'");
    target.ksearch_layout = Utils.castView(view, R.id.ksearch_layout, "field 'ksearch_layout'", FrameLayout.class);
    view7f0801a1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.cnk_layout = Utils.findRequiredViewAsType(source, R.id.cnk_layout, "field 'cnk_layout'", LinearLayout.class);
    target.rootView = Utils.findRequiredViewAsType(source, R.id.root_view, "field 'rootView'", NestedScrollView.class);
    view = Utils.findRequiredView(source, R.id.layout_meiriyiju, "method 'onViewClicked'");
    view7f0801ad = view;
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
    LeisureFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ad_sign = null;
    target.yueduLayout = null;
    target.twistsLayout = null;
    target.xx_ad_layout = null;
    target.ad_layout = null;
    target.cailing_layout = null;
    target.baidu_layout = null;
    target.app_layout = null;
    target.news_layout = null;
    target.game_layout = null;
    target.invest_layout = null;
    target.sougou_layout = null;
    target.shenhuifuLayout = null;
    target.layout_riddle = null;
    target.search_layout = null;
    target.adImg = null;
    target.novelLayout = null;
    target.caricatureLayout = null;
    target.jdLayout = null;
    target.english_essay_layout = null;
    target.layout_whyy = null;
    target.layout_conjecture = null;
    target.layout_history = null;
    target.radio_layout = null;
    target.layout_xiehouyu = null;
    target.layout_tongue_twister = null;
    target.ksearch_layout = null;
    target.cnk_layout = null;
    target.rootView = null;

    view7f080483.setOnClickListener(null);
    view7f080483 = null;
    view7f080434.setOnClickListener(null);
    view7f080434 = null;
    view7f08009d.setOnClickListener(null);
    view7f08009d = null;
    view7f080084.setOnClickListener(null);
    view7f080084 = null;
    view7f08006a.setOnClickListener(null);
    view7f08006a = null;
    view7f08020d.setOnClickListener(null);
    view7f08020d = null;
    view7f08015b.setOnClickListener(null);
    view7f08015b = null;
    view7f08018e.setOnClickListener(null);
    view7f08018e = null;
    view7f0802e4.setOnClickListener(null);
    view7f0802e4 = null;
    view7f0802d4.setOnClickListener(null);
    view7f0802d4 = null;
    view7f0801b0.setOnClickListener(null);
    view7f0801b0 = null;
    view7f0802aa.setOnClickListener(null);
    view7f0802aa = null;
    view7f080228.setOnClickListener(null);
    view7f080228 = null;
    view7f0800a3.setOnClickListener(null);
    view7f0800a3 = null;
    view7f08019e.setOnClickListener(null);
    view7f08019e = null;
    view7f080119.setOnClickListener(null);
    view7f080119 = null;
    view7f0801b4.setOnClickListener(null);
    view7f0801b4 = null;
    view7f0801a7.setOnClickListener(null);
    view7f0801a7 = null;
    view7f0801ab.setOnClickListener(null);
    view7f0801ab = null;
    view7f080269.setOnClickListener(null);
    view7f080269 = null;
    view7f0801b5.setOnClickListener(null);
    view7f0801b5 = null;
    view7f0801b2.setOnClickListener(null);
    view7f0801b2 = null;
    view7f0801a1.setOnClickListener(null);
    view7f0801a1 = null;
    view7f0801ad.setOnClickListener(null);
    view7f0801ad = null;
  }
}
