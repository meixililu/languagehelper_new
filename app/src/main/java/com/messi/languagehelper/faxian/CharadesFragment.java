package com.messi.languagehelper.faxian;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.BaseFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.TwistaItem;
import com.messi.languagehelper.bean.TwistaResult;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CharadesFragment extends BaseFragment {

	@BindView(R.id.question)
	TextView question;
	@BindView(R.id.answer)
	TextView answer;
	TwistaItem mTwistaItem;
	private boolean isLoading;
	private int count;

	@Override
	public void onAttach(Context context) {
		try {
			super.onAttach(context);
			mProgressbarListener = (FragmentProgressbarListener) context;
		} catch (Exception e) {
			throw new ClassCastException(context.toString() + " must implement FragmentProgressbarListener");
		}
	}

	@Override
	public void loadDataOnStart() {
		super.loadDataOnStart();
		requestData();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.essay_fragment, container, false);
		ButterKnife.bind(this, view);
		count = Setings.getSharedPreferences(getContext()).getInt(KeyUtil.IsShowClickToNext,0);
		return view;
	}

	private void requestData() {
		isLoading = true;
		showProgressbar();
		LanguagehelperHttpClient.get(Setings.CaizimiApi, new UICallback(getActivity()) {
			@Override
			public void onResponsed(String responseString) {
				try {
					if (JsonParser.isJson(responseString)) {
						TwistaResult mRoot = JSON.parseObject(responseString, TwistaResult.class);
						if (mRoot.getCode() == 200) {
							if (mRoot.getNewslist() != null && mRoot.getNewslist().size() > 0) {
								mTwistaItem = mRoot.getNewslist().get(0);
								question.setText(mTwistaItem.getContent());
								answer.setText("轻触看答案");
							}

						} else {
							ToastUtil.diaplayMesShort(getContext(), mRoot.getMsg());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailured() {
				ToastUtil.diaplayMesShort(getContext(), getResources().getString(R.string.network_error));
			}

			@Override
			public void onFinished() {
				isLoading = false;
				hideProgressbar();
			}
		});
	}

	private void refresh() {
		if (!isLoading) {
			requestData();
		}
	}

	@OnClick({R.id.question, R.id.answer})
	public void onViewClicked() {
		if (mTwistaItem != null) {
			if (!mTwistaItem.isShowResult()) {
				if(count == 0){
					answer.setText(mTwistaItem.getAnswer() + "\n\n" + mTwistaItem.getReason() + "\n\n\n\n\n" + "轻触看下一条");
					count++;
					Setings.saveSharedPreferences(Setings.getSharedPreferences(getContext()),
							KeyUtil.IsShowClickToNext,1);
				}else {
					answer.setText(mTwistaItem.getAnswer() + "\n\n" + mTwistaItem.getReason());
				}
				mTwistaItem.setShowResult(true);
			} else {
				refresh();
			}
		} else {
			refresh();
		}
	}
}
