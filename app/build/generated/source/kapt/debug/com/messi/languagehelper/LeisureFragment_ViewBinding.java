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

  private View view7f0804a0;

  private View view7f08044c;

  private View view7f080098;

  private View view7f080080;

  private View view7f080069;

  private View view7f08022a;

  private View view7f080157;

  private View view7f080188;

  private View view7f0802fe;

  private View view7f0802ef;

  private View view7f0801a8;

  private View view7f0802c6;

  private View view7f080245;

  private View view7f08009e;

  private View view7f080198;

  private View view7f080116;

  private View view7f0801ac;

  private View view7f0801a0;

  private View view7f0801a3;

  private View view7f080286;

  private View view7f0801ad;

  private View view7f0801aa;

  private View view7f08019a;

  private View view7f0801a5;

  @UiThread
  public LeisureFragment_ViewBinding(final LeisureFragment target, View source) {
    this.target = target;

    View view;
    target.ad_sign = Utils.findRequiredViewAsType(source, R.id.ad_sign, "field 'ad_sign'", TextView.class);
    view = Utils.findRequiredView(source, R.id.yuedu_layout, "field 'yueduLayout' and method 'onViewClicked'");
    target.yueduLayout = Utils.castView(view, R.id.yuedu_layout, "field 'yueduLayout'", FrameLayout.class);
    view7f0804a0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.twists_layout, "field 'twistsLayout' and method 'onViewClicked'");
    target.twistsLayout = Utils.castView(view, R.id.twists_layout, "field 'twistsLayout'", FrameLayout.class);
    view7f08044c = view;
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
    view7f080098 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.baidu_layout, "field 'baidu_layout' and method 'onViewClicked'");
    target.baidu_layout = Utils.castView(view, R.id.baidu_layout, "field 'baidu_layout'", FrameLayout.class);
    view7f080080 = view;
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
    view7f08022a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.game_layout, "field 'game_layout' and method 'onViewClicked'");
    target.game_layout = Utils.castView(view, R.id.game_layout, "field 'game_layout'", FrameLayout.class);
    view7f080157 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.invest_layout, "field 'invest_layout' and method 'onViewClicked'");
    target.invest_layout = Utils.castView(view, R.id.invest_layout, "field 'invest_layout'", FrameLayout.class);
    view7f080188 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.sougou_layout, "field 'sougou_layout' and method 'onViewClicked'");
    target.sougou_layout = Utils.castView(view, R.id.sougou_layout, "field 'sougou_layout'", FrameLayout.class);
    view7f0802fe = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.shenhuifu_layout, "field 'shenhuifuLayout' and method 'onViewClicked'");
    target.shenhuifuLayout = Utils.castView(view, R.id.shenhuifu_layout, "field 'shenhuifuLayout'", FrameLayout.class);
    view7f0802ef = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_riddle, "field 'layout_riddle' and method 'onViewClicked'");
    target.layout_riddle = Utils.castView(view, R.id.layout_riddle, "field 'layout_riddle'", FrameLayout.class);
    view7f0801a8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.search_layout, "field 'search_layout' and method 'onViewClicked'");
    target.search_layout = Utils.castView(view, R.id.search_layout, "field 'search_layout'", FrameLayout.class);
    view7f0802c6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.adImg = Utils.findRequiredViewAsType(source, R.id.ad_img, "field 'adImg'", SimpleDraweeView.class);
    view = Utils.findRequiredView(source, R.id.novel_layout, "field 'novelLayout' and method 'onViewClicked'");
    target.novelLayout = Utils.castView(view, R.id.novel_layout, "field 'novelLayout'", FrameLayout.class);
    view7f080245 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.caricature_layout, "field 'caricatureLayout' and method 'onViewClicked'");
    target.caricatureLayout = Utils.castView(view, R.id.caricature_layout, "field 'caricatureLayout'", FrameLayout.class);
    view7f08009e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.jd_layout, "field 'jdLayout' and method 'onViewClicked'");
    target.jdLayout = Utils.castView(view, R.id.jd_layout, "field 'jdLayout'", FrameLayout.class);
    view7f080198 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.english_essay_layout, "field 'english_essay_layout' and method 'onViewClicked'");
    target.english_essay_layout = Utils.castView(view, R.id.english_essay_layout, "field 'english_essay_layout'", FrameLayout.class);
    view7f080116 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_whyy, "field 'layout_whyy' and method 'onViewClicked'");
    target.layout_whyy = Utils.castView(view, R.id.layout_whyy, "field 'layout_whyy'", FrameLayout.class);
    view7f0801ac = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_conjecture, "field 'layout_conjecture' and method 'onViewClicked'");
    target.layout_conjecture = Utils.castView(view, R.id.layout_conjecture, "field 'layout_conjecture'", FrameLayout.class);
    view7f0801a0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_history, "field 'layout_history' and method 'onViewClicked'");
    target.layout_history = Utils.castView(view, R.id.layout_history, "field 'layout_history'", FrameLayout.class);
    view7f0801a3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.radio_layout, "field 'radio_layout' and method 'onViewClicked'");
    target.radio_layout = Utils.castView(view, R.id.radio_layout, "field 'radio_layout'", FrameLayout.class);
    view7f080286 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_xiehouyu, "field 'layout_xiehouyu' and method 'onViewClicked'");
    target.layout_xiehouyu = Utils.castView(view, R.id.layout_xiehouyu, "field 'layout_xiehouyu'", FrameLayout.class);
    view7f0801ad = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_tongue_twister, "field 'layout_tongue_twister' and method 'onViewClicked'");
    target.layout_tongue_twister = Utils.castView(view, R.id.layout_tongue_twister, "field 'layout_tongue_twister'", FrameLayout.class);
    view7f0801aa = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ksearch_layout, "field 'ksearch_layout' and method 'onViewClicked'");
    target.ksearch_layout = Utils.castView(view, R.id.ksearch_layout, "field 'ksearch_layout'", FrameLayout.class);
    view7f08019a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.cnk_layout = Utils.findRequiredViewAsType(source, R.id.cnk_layout, "field 'cnk_layout'", LinearLayout.class);
    target.rootView = Utils.findRequiredViewAsType(source, R.id.root_view, "field 'rootView'", NestedScrollView.class);
    view = Utils.findRequiredView(source, R.id.layout_meiriyiju, "method 'onViewClicked'");
    view7f0801a5 = view;
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

    view7f0804a0.setOnClickListener(null);
    view7f0804a0 = null;
    view7f08044c.setOnClickListener(null);
    view7f08044c = null;
    view7f080098.setOnClickListener(null);
    view7f080098 = null;
    view7f080080.setOnClickListener(null);
    view7f080080 = null;
    view7f080069.setOnClickListener(null);
    view7f080069 = null;
    view7f08022a.setOnClickListener(null);
    view7f08022a = null;
    view7f080157.setOnClickListener(null);
    view7f080157 = null;
    view7f080188.setOnClickListener(null);
    view7f080188 = null;
    view7f0802fe.setOnClickListener(null);
    view7f0802fe = null;
    view7f0802ef.setOnClickListener(null);
    view7f0802ef = null;
    view7f0801a8.setOnClickListener(null);
    view7f0801a8 = null;
    view7f0802c6.setOnClickListener(null);
    view7f0802c6 = null;
    view7f080245.setOnClickListener(null);
    view7f080245 = null;
    view7f08009e.setOnClickListener(null);
    view7f08009e = null;
    view7f080198.setOnClickListener(null);
    view7f080198 = null;
    view7f080116.setOnClickListener(null);
    view7f080116 = null;
    view7f0801ac.setOnClickListener(null);
    view7f0801ac = null;
    view7f0801a0.setOnClickListener(null);
    view7f0801a0 = null;
    view7f0801a3.setOnClickListener(null);
    view7f0801a3 = null;
    view7f080286.setOnClickListener(null);
    view7f080286 = null;
    view7f0801ad.setOnClickListener(null);
    view7f0801ad = null;
    view7f0801aa.setOnClickListener(null);
    view7f0801aa = null;
    view7f08019a.setOnClickListener(null);
    view7f08019a = null;
    view7f0801a5.setOnClickListener(null);
    view7f0801a5 = null;
  }
}
