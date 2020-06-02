package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.impl.PracticeProgressListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ViewUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

import static com.messi.languagehelper.util.AVOUtil.PracticeDetail.PracticeDetail;

public class AiSpokenBasicActivity extends BaseActivity implements PracticeProgressListener {

	public static String vedioPath = "";

	private LinearLayout page_navigation, page_content;
	private TextView error_txt;
	private String[] studyContent;
	private FragmentManager fragmentManager;
	public int pageIndex;// third level
	private SharedPreferences mSharedPreferences;
	private Fragment mContent;
	private SpeechSynthesizer mSpeechSynthesizer;
	private AVObject avObject;
	private int currentSection;
	private String Section;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_activity);
		initData();
		initViews();
		new QueryTask(this).execute();
	}
	
	private void initData(){
		mSharedPreferences = Setings.getSharedPreferences(this);
		currentSection = mSharedPreferences.getInt(KeyUtil.AiBaseCurrentSection,0);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this,null);
		fragmentManager = getSupportFragmentManager();
		Section = "第"+ (currentSection+1) + "关  ";
		getSupportActionBar().setTitle(Section);
	}

	private void initViews() {
		page_navigation = (LinearLayout) findViewById(R.id.page_navigation);
		page_content = (LinearLayout) findViewById(R.id.page_content);
		error_txt = (TextView) findViewById(R.id.error_txt);
		error_txt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new QueryTask(AiSpokenBasicActivity.this).execute();
			}
		});
	}
	
	private void setData(){
		addIndicator();
		addFragment();
	}

	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<AiSpokenBasicActivity> mainActivity;

		public QueryTask(AiSpokenBasicActivity mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
			error_txt.setVisibility(View.GONE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(PracticeDetail);
			query.skip(currentSection);
			query.limit(1);
			query.addAscendingOrder(AVOUtil.PracticeDetail.PCCode);
			List<AVObject> avObjects  = query.find();
			if(avObjects != null && !avObjects.isEmpty()){
				avObject = avObjects.get(0);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(mainActivity.get() != null){
				hideProgressbar();
				if(avObject != null){
					String content = avObject.getString(AVOUtil.PracticeDetail.PDContent);
					if(!TextUtils.isEmpty(content)){
						vedioPath = SDCardUtil.PracticePath + avObject.getString(AVOUtil.PracticeDetail.PCCode)
								+ SDCardUtil.Delimiter + avObject.getString(AVOUtil.PracticeDetail.PCLCode)
								+ SDCardUtil.Delimiter;
						studyContent = content.split("@");
						setData();
					}else{
						error_txt.setVisibility(View.VISIBLE);
					}
				}else{
					error_txt.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	private void addIndicator() {
		ViewUtil.addIndicator(studyContent.length, page_navigation,this);
	}

	private void addFragment() {
		try {
			String[]contents = studyContent[pageIndex].split("#");
			String type = contents[contents.length-1];
			getSupportActionBar().setTitle( getActionbarTitle(type) );
			Fragment mpramf = getStudyType(type);
			getSupportActionBar().setTitle(Section +
					getResources().getString(R.string.practice_spoken_englist_style_studyevery));
			fragmentManager.beginTransaction()
					.add(R.id.page_content, mpramf)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setFragment(Fragment mFragment) {
		try {
			if (mContent != mFragment) {
				mContent = mFragment;
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out)
						.replace(R.id.page_content, mFragment) // 替换Fragment，实现切换
						.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void toNextPage() {
		LogUtil.DefalutLog("toNextPage");
		ViewUtil.setPracticeIndicator(this, page_navigation, pageIndex);
		pageIndex++;
		if (pageIndex < studyContent.length) {
			String[]contents = studyContent[pageIndex].split("#");
			String type = contents[contents.length-1];
			String mTitle = getActionbarTitle(type);
			/** 根据标题来判断这个版本是否有该功能，没有就跳过 **/
			if(!TextUtils.isEmpty(mTitle)){
				getSupportActionBar().setTitle( mTitle );
				Fragment mpramf = getStudyType(type);
				setFragment(mpramf);
			}else{
				toNextPage();
			}
		}else {
			getSupportActionBar().setTitle(Section +
					getResources().getString(R.string.practice_spoken_englist_finish));
			FinishFragment mpramf = FinishFragment.newInstance(this);
			setFragment(mpramf);
			AVAnalytics.onEvent(this, "study_pg_finish");
		}
	}
	
	private Fragment getStudyType(String type){
		Fragment mpramf = new Fragment();
		if(type.equalsIgnoreCase(KeyUtil.Study_Every)){
			mpramf = PracticeEveryFragment.newInstance(studyContent[pageIndex], this,
					vedioPath + KeyUtil.Study_Every + SDCardUtil.Delimiter,mSharedPreferences,mSpeechSynthesizer);
			
			AVAnalytics.onEvent(this, "study_pg_studyevery");
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_FourInOne)){
			mpramf = PracticeFourChooseOneFragment.newInstance(studyContent[pageIndex], this,
					vedioPath + KeyUtil.Practice_FourInOne + SDCardUtil.Delimiter,mSharedPreferences,mSpeechSynthesizer);
			
			AVAnalytics.onEvent(this, "study_pg_fourinone");
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_ReadAfterMe)){
			mpramf = PracticeReadAfterMeFragment.newInstace(studyContent[pageIndex], this,
					vedioPath + KeyUtil.Practice_ReadAfterMe + SDCardUtil.Delimiter,mSharedPreferences,mSpeechSynthesizer);
			
			AVAnalytics.onEvent(this, "study_pg_readafterme");
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_Translate)){
			mpramf = PracticeWriteFragment.newInstance(studyContent[pageIndex], this,
					vedioPath + KeyUtil.Practice_Translate + SDCardUtil.Delimiter,mSharedPreferences,mSpeechSynthesizer);
			
			AVAnalytics.onEvent(this, "study_pg_write");
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_SpeakAfterMe)){
			mpramf = PracticeReadAfterMeFragment.newInstace(studyContent[pageIndex], this,
					vedioPath + KeyUtil.Practice_SpeakAfterMe + SDCardUtil.Delimiter,mSharedPreferences,mSpeechSynthesizer);
			
			AVAnalytics.onEvent(this, "study_pg_speakafterme");
		}else{
			mpramf = FinishFragment.newInstance(this);
		}
		return mpramf;
	}
	
	private String getActionbarTitle(String type){
		String mpramf = "";
		if(type.equalsIgnoreCase(KeyUtil.Study_Every)){
			mpramf = Section + getResources().getString(R.string.practice_spoken_englist_style_studyevery);
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_FourInOne)){
			mpramf = Section + getResources().getString(R.string.practice_spoken_englist_style_fourinone);
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_ReadAfterMe)){
			mpramf = Section + getResources().getString(R.string.practice_spoken_englist_style_readafterme);
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_Translate)){
			mpramf = Section + getResources().getString(R.string.practice_spoken_englist_style_write);
		}else if(type.equalsIgnoreCase(KeyUtil.Practice_SpeakAfterMe)){
			mpramf = Section + getResources().getString(R.string.practice_spoken_englist_style_speakafterme);
		}else{
			mpramf = "";
		}
		return mpramf;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		pageIndex = 0;
		vedioPath = "";
		if(mSpeechSynthesizer != null){
			mSpeechSynthesizer.destroy();
			mSpeechSynthesizer = null;
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
	}

	@Override
	public void onLoading() {
		showProgressbar();
	}

	@Override
	public void onCompleteLoading() {
		hideProgressbar();
	}

	@Override
	public void finishActivity() {
		this.finish();
	}

}
