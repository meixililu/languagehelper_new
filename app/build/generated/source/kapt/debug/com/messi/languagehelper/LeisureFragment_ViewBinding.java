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

  private View view7f0804a3;

  private View view7f080454;

  private View view7f08009a;

  private View view7f080081;

  private View view7f080069;

  private View view7f080230;

  private View view7f080158;

  private View view7f08018b;

  private View view7f080306;

  private View view7f0802f6;

  private View view7f0801ac;

  private View view7f0802cd;

  private View view7f08024b;

  private View view7f0800a0;

  private View view7f08019b;

  private View view7f080115;

  private View view7f0801b0;

  private View view7f0801a3;

  private View view7f0801a7;

  private View view7f08028b;

  private View view7f0801b1;

  private View view7f0801ae;

  private View view7f08019d;

  private View view7f0801a9;

  @UiThread
  public LeisureFragment_ViewBinding(final LeisureFragment target, View source) {
    this.target = target;

    View view;
    target.ad_sign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'ad_sign'", TextView.class);
    view = Utils.findRequiredView(source, R.id.yuedu_layout, "field 'yueduLayout' and method 'onViewClicked'");
    target.yueduLayout = Utils.castView(view, R.id.yuedu_layout, "field 'yueduLayout'", FrameLayout.class);
    view7f0804a3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.twists_layout, "field 'twistsLayout' and method 'onViewClicked'");
    target.twistsLayout = Utils.castView(view, R.id.twists_layout, "field 'twistsLayout'", FrameLayout.class);
    view7f080454 = view;
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
    view7f08009a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.baidu_layout, "field 'baidu_layout' and method 'onViewClicked'");
    target.baidu_layout = Utils.castView(view, R.id.baidu_layout, "field 'baidu_layout'", FrameLayout.class);
    view7f080081 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app_layout, "field 'app_layout' and method 'onViewClicked'");
    target.app_layout = Utils.castView(view, R.id.app_layout, "field 'app_layout'", FrameLayout.class);
    view7f080069 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.news_layout, "field 'news_layout' and method 'onViewClicked'");
    target.news_layout = Utils.castView(view, R.id.news_layout, "field 'news_layout'", FrameLayout.class);
    view7f080230 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.game_layout, "field 'game_layout' and method 'onViewClicked'");
    target.game_layout = Utils.castView(view, R.id.game_layout, "field 'game_layout'", FrameLayout.class);
    view7f080158 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.invest_layout, "field 'invest_layout' and method 'onViewClicked'");
    target.invest_layout = Utils.castView(view, R.id.invest_layout, "field 'invest_layout'", FrameLayout.class);
    view7f08018b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.sougou_layout, "field 'sougou_layout' and method 'onViewClicked'");
    target.sougou_layout = Utils.castView(view, R.id.sougou_layout, "field 'sougou_layout'", FrameLayout.class);
    view7f080306 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shenhuifu_layout, "field 'shenhuifuLayout' and method 'onViewClicked'");
    target.shenhuifuLayout = Utils.castView(view, R.id.shenhuifu_layout, "field 'shenhuifuLayout'", FrameLayout.class);
    view7f0802f6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_riddle, "field 'layout_riddle' and method 'onViewClicked'");
    target.layout_riddle = Utils.castView(view, R.id.layout_riddle, "field 'layout_riddle'", FrameLayout.class);
    view7f0801ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_layout, "field 'search_layout' and method 'onViewClicked'");
    target.search_layout = Utils.castView(view, R.id.search_layout, "field 'search_layout'", FrameLayout.class);
    view7f0802cd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.novel_layout, "field 'novelLayout' and method 'onViewClicked'");
    target.novelLayout = Utils.castView(view, R.id.novel_layout, "field 'novelLayout'", FrameLayout.class);
    view7f08024b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.caricature_layout, "field 'caricatureLayout' and method 'onViewClicked'");
    target.caricatureLayout = Utils.castView(view, R.id.caricature_layout, "field 'caricatureLayout'", FrameLayout.class);
    view7f0800a0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.jd_layout, "field 'jdLayout' and method 'onViewClicked'");
    target.jdLayout = Utils.castView(view, R.id.jd_layout, "field 'jdLayout'", FrameLayout.class);
    view7f08019b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.english_essay_layout, "field 'english_essay_layout' and method 'onViewClicked'");
    target.english_essay_layout = Utils.castView(view, R.id.english_essay_layout, "field 'english_essay_layout'", FrameLayout.class);
    view7f080115 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_whyy, "field 'layout_whyy' and method 'onViewClicked'");
    target.layout_whyy = Utils.castView(view, R.id.layout_whyy, "field 'layout_whyy'", FrameLayout.class);
    view7f0801b0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_conjecture, "field 'layout_conjecture' and method 'onViewClicked'");
    target.layout_conjecture = Utils.castView(view, R.id.layout_conjecture, "field 'layout_conjecture'", FrameLayout.class);
    view7f0801a3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_history, "field 'layout_history' and method 'onViewClicked'");
    target.layout_history = Utils.castView(view, R.id.layout_history, "field 'layout_history'", FrameLayout.class);
    view7f0801a7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.radio_layout, "field 'radio_layout' and method 'onViewClicked'");
    target.radio_layout = Utils.castView(view, R.id.radio_layout, "field 'radio_layout'", FrameLayout.class);
    view7f08028b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_xiehouyu, "field 'layout_xiehouyu' and method 'onViewClicked'");
    target.layout_xiehouyu = Utils.castView(view, R.id.layout_xiehouyu, "field 'layout_xiehouyu'", FrameLayout.class);
    view7f0801b1 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_tongue_twister, "field 'layout_tongue_twister' and method 'onViewClicked'");
    target.layout_tongue_twister = Utils.castView(view, R.id.layout_tongue_twister, "field 'layout_tongue_twister'", FrameLayout.class);
    view7f0801ae = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ksearch_layout, "field 'ksearch_layout' and method 'onViewClicked'");
    target.ksearch_layout = Utils.castView(view, R.id.ksearch_layout, "field 'ksearch_layout'", FrameLayout.class);
    view7f08019d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.cnk_layout = Utils.findRequiredViewAsType(source, R.id.cnk_layout, "field 'cnk_layout'", LinearLayout.class);
    target.rootView = Utils.findRequiredViewAsType(source, R.id.root_view, "field 'rootView'", NestedScrollView.class);
    view = Utils.findRequiredView(source, R.id.layout_meiriyiju, "method 'onViewClicked'");
    view7f0801a9 = view;
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

    view7f0804a3.setOnClickListener(null);
    view7f0804a3 = null;
    view7f080454.setOnClickListener(null);
    view7f080454 = null;
    view7f08009a.setOnClickListener(null);
    view7f08009a = null;
    view7f080081.setOnClickListener(null);
    view7f080081 = null;
    view7f080069.setOnClickListener(null);
    view7f080069 = null;
    view7f080230.setOnClickListener(null);
    view7f080230 = null;
    view7f080158.setOnClickListener(null);
    view7f080158 = null;
    view7f08018b.setOnClickListener(null);
    view7f08018b = null;
    view7f080306.setOnClickListener(null);
    view7f080306 = null;
    view7f0802f6.setOnClickListener(null);
    view7f0802f6 = null;
    view7f0801ac.setOnClickListener(null);
    view7f0801ac = null;
    view7f0802cd.setOnClickListener(null);
    view7f0802cd = null;
    view7f08024b.setOnClickListener(null);
    view7f08024b = null;
    view7f0800a0.setOnClickListener(null);
    view7f0800a0 = null;
    view7f08019b.setOnClickListener(null);
    view7f08019b = null;
    view7f080115.setOnClickListener(null);
    view7f080115 = null;
    view7f0801b0.setOnClickListener(null);
    view7f0801b0 = null;
    view7f0801a3.setOnClickListener(null);
    view7f0801a3 = null;
    view7f0801a7.setOnClickListener(null);
    view7f0801a7 = null;
    view7f08028b.setOnClickListener(null);
    view7f08028b = null;
    view7f0801b1.setOnClickListener(null);
    view7f0801b1 = null;
    view7f0801ae.setOnClickListener(null);
    view7f0801ae = null;
    view7f08019d.setOnClickListener(null);
    view7f08019d = null;
    view7f0801a9.setOnClickListener(null);
    view7f0801a9 = null;
  }
}
