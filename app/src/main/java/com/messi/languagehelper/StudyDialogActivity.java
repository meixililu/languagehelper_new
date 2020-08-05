package com.messi.languagehelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.SDCardUtil;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class StudyDialogActivity extends BaseActivity implements OnClickListener{

	public static final String TypeListen = "TypeListen";
	public static final String TypeRead = "TypeRead";
	public static final String TypeA = "TypeA";
	public static final String TypeB = "TypeB";
	
	private String vedioPath;
	private TextView error_txt;
	private LinearLayout study_dialog_type;
	private String SDCode;
	private String SDLCode;
	private AVObject avObject;
	private String content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_dialog_activity);
		initData();
		initViews();
		new QueryTask(this).execute();
	}
	
	private void initData(){
		SDCode = getIntent().getStringExtra(AVOUtil.StudyDialogListCategory.SDCode);
		SDLCode = getIntent().getStringExtra(AVOUtil.StudyDialogListCategory.SDLCode);
		vedioPath = SDCardUtil.StudyDialogPath + SDCode + SDCardUtil.Delimiter + SDLCode + SDCardUtil.Delimiter;
	}

	private void initViews() {
		getSupportActionBar().setTitle(getResources().getString(R.string.title_dialog_select));

		error_txt.setOnClickListener(this);
	}

	private class QueryTask extends AsyncTask<Void, Void, Void> {

		private WeakReference<StudyDialogActivity> mainActivity;

		public QueryTask(StudyDialogActivity mActivity){
			mainActivity = new WeakReference<>(mActivity);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressbar();
			study_dialog_type.setVisibility(View.GONE);
			error_txt.setVisibility(View.GONE);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.StudyDialogDetail.StudyDialogDetail);
			query.whereEqualTo(AVOUtil.StudyDialogDetail.SDCode, SDCode);
			query.whereEqualTo(AVOUtil.StudyDialogDetail.SDLCode, SDLCode);
			List<AVObject> avObjects  = null;
			try{
				avObjects = query.find();
			}catch (Exception e){
				e.printStackTrace();
			}
			if(NullUtil.isNotEmpty(avObjects)){
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
					content = avObject.getString(AVOUtil.StudyDialogDetail.SDDContent);
					if(!TextUtils.isEmpty(content)){
						study_dialog_type.setVisibility(View.VISIBLE);
					}else{
						error_txt.setVisibility(View.VISIBLE);
					}
				}else{
					error_txt.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){

		case R.id.error_txt:
			new QueryTask(this).execute();
			break;
		default:
			break;
		}
	}
	
	private void toDialogStudyActivity(String type){
		Intent intent = new Intent(this, DialogPracticeActivity.class);
		intent.putExtra(KeyUtil.SDcardPathKey, vedioPath + type + SDCardUtil.Delimiter);
		intent.putExtra(KeyUtil.ContextKey, content);
		intent.putExtra(KeyUtil.StudyDialogAction, type);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
