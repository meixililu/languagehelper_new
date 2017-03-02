package com.messi.languagehelper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcDictionaryListAdapter;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.DicHelperListener;
import com.messi.languagehelper.impl.DictionaryTranslateListener;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DictionaryHelper;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.messi.languagehelper.util.XFUtil;
import com.messi.languagehelper.views.DividerItemDecoration;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DictionaryFragment extends Fragment implements OnClickListener,
        DictionaryTranslateListener, DicHelperListener {

    public static Dictionary mDictionaryBean;
    public static DictionaryFragment mMainFragment;

    @BindView(R.id.recent_used_lv)
    RecyclerView recent_used_lv;
    @BindView(R.id.input_type_btn)
    ImageView input_type_btn;
    @BindView(R.id.voice_btn)
    TextView voice_btn;
    @BindView(R.id.voice_btn_cover)
    CardView voice_btn_cover;
    @BindView(R.id.mic_layout)
    LinearLayout mic_layout;
    @BindView(R.id.input_et)
    AppCompatEditText input_et;
    @BindView(R.id.clear_btn_layout)
    FrameLayout clear_btn_layout;
    @BindView(R.id.submit_btn)
    TextView submit_btn;
    @BindView(R.id.submit_btn_cover)
    CardView submit_btn_cover;
    @BindView(R.id.keybord_layout)
    LinearLayout keybord_layout;
    @BindView(R.id.input_layout)
    LinearLayout input_layout;

    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.input_type_layout)
    LinearLayout input_type_layout;
    @BindView(R.id.speak_language_tv)
    TextView speakLanguageTv;
    @BindView(R.id.speak_language_layout)
    LinearLayout speakLanguageLayout;
    @BindView(R.id.more_tools_layout_mic)
    LinearLayout more_tools_layout_mic;
    @BindView(R.id.more_tools_layout)
    LinearLayout more_tools_layout;
    @BindView(R.id.bottom_layout)
    CardView bottomLayout;
    @BindView(R.id.dic_result_layout)
    LinearLayout dicResultLayout;
    @BindView(R.id.cidian_result_layout)
    ScrollView cidianResultLayout;
    @BindView(R.id.action_col_all)
    ImageView action_col_all;
    @BindView(R.id.action_collected)
    CardView actionCollected;
    @BindView(R.id.action_setting)
    CardView actionSetting;
    @BindView(R.id.action_layout)
    LinearLayout actionLayout;
    /**
     * record
     **/
    private RcDictionaryListAdapter mAdapter;
    private List<Dictionary> beans;
    // 识别对象
    private SpeechRecognizer recognizer;
    private SharedPreferences mSharedPreferences;
    //合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;
    private Bundle bundle;
    private FragmentProgressbarListener mProgressbarListener;
    private Activity mActivity;
    private String mCurrentPhotoPath;
    private boolean isShowCollected = true;
    private Thread mThread;
    private MyThread mMyThread;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            onFinishRequest();
            if (msg.what == 1) {
                setData();
            } else {
                showToast(mActivity.getResources().getString(R.string.network_error));
            }
        }
    };

    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            ToastUtil.diaplayMesShort(mActivity, err.getErrorDescription());
            finishLoadding();
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            loadding();
            finishRecord();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.DefalutLog("onResult");
            String text = JsonParser.parseIatResult(results.getResultString());
            input_et.append(text.toLowerCase());
            input_et.setSelection(input_et.length());
            if (isLast) {
                finishRecord();
                submit();
            }
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

        @Override
        public void onVolumeChanged(int volume, byte[] arg1) {
            if (volume < 4) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
            } else if (volume < 8) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_2);
            } else if (volume < 12) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_3);
            } else if (volume < 16) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_4);
            } else if (volume < 20) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_5);
            } else if (volume < 24) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_6);
            } else if (volume < 31) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_7);
            }
        }
    };

    public static DictionaryFragment getInstance(Bundle bundle, Activity mActivity) {
        if (mMainFragment == null) {
            mMainFragment = new DictionaryFragment();
            mMainFragment.bundle = bundle;
            mMainFragment.setmActivity(mActivity);
        }
        return mMainFragment;
    }

    public void setmActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.DefalutLog("MainFragment-onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.DefalutLog("MainFragment-onCreateView");
        View view = inflater.inflate(R.layout.fragment_dictionary, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mSharedPreferences = mActivity.getSharedPreferences(mActivity.getPackageName(), Activity.MODE_PRIVATE);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mActivity, null);
        recognizer = SpeechRecognizer.createRecognizer(mActivity, null);
        beans = new ArrayList<Dictionary>();
        beans.addAll(DataBaseUtil.getInstance().getDataListDictionary(0, Settings.offset));
        mAdapter = new RcDictionaryListAdapter(mActivity, beans, this);
        mMyThread = new MyThread();

        submit_btn_cover.setOnClickListener(this);
        speakLanguageLayout.setOnClickListener(this);
        voice_btn_cover.setOnClickListener(this);
        clear_btn_layout.setOnClickListener(this);
        input_layout.setOnClickListener(this);
        input_type_layout.setOnClickListener(this);
        more_tools_layout.setOnClickListener(this);
        more_tools_layout_mic.setOnClickListener(this);
        actionLayout.setOnClickListener(this);
        actionSetting.setOnClickListener(this);
        actionCollected.setOnClickListener(this);

        recent_used_lv.setHasFixedSize(true);
        recent_used_lv.setLayoutManager(new LinearLayoutManager(mActivity));
        recent_used_lv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.abc_list_divider_mtrl_alpha)));
        mAdapter.setItems(beans);
        recent_used_lv.setAdapter(mAdapter);

        setSpeakLanguageTv();
        isShowRecentList(true);
        if (mSharedPreferences.getBoolean(KeyUtil.IsShowDicKeyboardLayout, false)) {
            showKeybordLayout();
        } else {
            showMicLayout();
        }
        input_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    submit_btn_cover.setVisibility(View.VISIBLE);
                    more_tools_layout.setVisibility(View.GONE);
                } else {
                    more_tools_layout.setVisibility(View.VISIBLE);
                    submit_btn_cover.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_btn_cover) {
            submit();
            AVAnalytics.onEvent(mActivity, "tab2_submit_btn");
        } else if (v.getId() == R.id.voice_btn_cover) {
            showIatDialog();
            AVAnalytics.onEvent(mActivity, "tab2_speak_btn");
        } else if (v.getId() == R.id.clear_btn_layout) {
            input_et.setText("");
            AVAnalytics.onEvent(mActivity, "tab2_clear_btn");
        } else if (v.getId() == R.id.speak_language_layout) {
            changeSpeakLanguage();
        } else if (v.getId() == R.id.more_tools_layout || v.getId() == R.id.more_tools_layout_mic) {
            if (actionLayout.isShown()) {
                actionLayout.setVisibility(View.GONE);
            } else {
                actionLayout.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.input_type_layout) {
            changeInputType();
        } else if (v.getId() == R.id.action_setting) {
            actionLayout.setVisibility(View.GONE);
            showMoreTools();
        } else if (v.getId() == R.id.action_collected) {
            actionLayout.setVisibility(View.GONE);
            isShowRecentList(true);
            if (isShowCollected) {
                action_col_all.setImageResource(R.drawable.ic_uncollected_grey);
            } else {
                action_col_all.setImageResource(R.drawable.ic_collected_grey);
            }
            getCollectedDataTask();
        }
    }

    private void isShowRecentList(boolean value) {
        if (value) {
            recent_used_lv.setVisibility(View.VISIBLE);
            cidianResultLayout.setVisibility(View.GONE);
        } else {
            cidianResultLayout.setVisibility(View.VISIBLE);
            recent_used_lv.setVisibility(View.GONE);
        }
    }

    private void changeInputType() {
        if (keybord_layout.isShown()) {
            showMicLayout();
            Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsShowDicKeyboardLayout, false);
            hideIME();
        } else {
            showKeybordLayout();
            Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsShowDicKeyboardLayout, true);
            showIME();
            input_et.requestFocus();
        }
    }

    private void showKeybordLayout() {
        input_type_btn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_mic));
        keybord_layout.setVisibility(View.VISIBLE);
        mic_layout.setVisibility(View.GONE);
    }

    private void showMicLayout() {
        input_type_btn.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_keybord_btn));
        keybord_layout.setVisibility(View.GONE);
        mic_layout.setVisibility(View.VISIBLE);
    }

    private void changeSpeakLanguage() {
        if (mSharedPreferences.getString(KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineMD).equals(XFUtil.VoiceEngineMD)) {
            Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineEN);
            ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_english));
        } else {
            Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineMD);
            ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.speak_chinese));
        }
        setSpeakLanguageTv();
        AVAnalytics.onEvent(mActivity, "tab2_dic_sbtn");
    }

    private void setSpeakLanguageTv() {
        speakLanguageTv.setText(XFUtil.getVoiceEngineText(mSharedPreferences.getString(KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineMD)));
    }

    /**
     * send translate request
     * showapi dictionary api
     */
    private void RequestTranslateApiTask() {
        loadding();
        submit_btn_cover.setEnabled(false);
        TranslateUtil.Translate_init(mActivity, mHandler);
    }

    private void setData() {
        mDictionaryBean = (Dictionary) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
        WXEntryActivity.dataMap.clear();
        setBean();
    }

    private void setBean() {
        isShowRecentList(false);
        cidianResultLayout.scrollTo(0,0);
        DictionaryHelper.addDicContent(mActivity, dicResultLayout, mDictionaryBean, this);
        input_et.setText("");
    }

    private void onFinishRequest() {
        finishLoadding();
        submit_btn_cover.setEnabled(true);
    }

    /**
     * toast message
     *
     * @param toastString
     */
    private void showToast(String toastString) {
        ToastUtil.diaplayMesShort(mActivity, toastString);
    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        Settings.verifyStoragePermissions(mActivity, Settings.PERMISSIONS_RECORD_AUDIO);
        if (!recognizer.isListening()) {
            record_layout.setVisibility(View.VISIBLE);
            input_et.setText("");
            voice_btn.setText(mActivity.getResources().getText(R.string.click_and_finish));
            XFUtil.showSpeechRecognizer(mActivity, mSharedPreferences, recognizer, recognizerListener,
                    mSharedPreferences.getString(KeyUtil.DicUserSelectLanguage, XFUtil.VoiceEngineMD));
        } else {
            finishRecord();
            recognizer.stopListening();
            loadding();
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        voice_btn.setText(mActivity.getResources().getText(R.string.click_and_speak));
    }

    /**
     * 点击翻译之后隐藏输入法
     */
    private void hideIME() {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(submit_btn_cover.getWindowToken(), 0);
    }

    /**
     * 点击编辑之后显示输入法
     */
    private void showIME() {
        final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    private void loadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.showProgressbar();
        }
    }

    /**
     * 通过接口回调activity执行进度条显示控制
     */
    private void finishLoadding() {
        if (mProgressbarListener != null) {
            mProgressbarListener.hideProgressbar();
        }
    }

    /**
     * submit request task
     */
    private void submit() {
        Settings.q = input_et.getText().toString().trim();
        if (!TextUtils.isEmpty(Settings.q)) {
            String last = Settings.q.substring(Settings.q.length() - 1);
            if (",.?!;:'，。？！‘；：".contains(last)) {
                Settings.q = Settings.q.substring(0, Settings.q.length() - 1);
            }
            RequestTranslateApiTask();
        } else {
            showToast(mActivity.getResources().getString(R.string.input_et_hint));
            finishLoadding();
        }
    }

    @Override
    public void showItem(Dictionary word) {
        mDictionaryBean = word;
        setBean();
    }

    private void getCollectedDataTask() {
        loadding();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                beans.clear();
                if (isShowCollected) {
                    isShowCollected = false;
                    beans.addAll(DataBaseUtil.getInstance().getDataListDictionaryCollected(0, Settings.offset));
                } else {
                    isShowCollected = true;
                    beans.addAll(DataBaseUtil.getInstance().getDataListDictionary(0, Settings.offset));
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        finishLoadding();
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(String s) {
                    }
                });
    }

    @Override
    public void playPcm(Dictionary mObject,boolean isPlayResult,String result) {
        playResult(mObject,isPlayResult,result);
    }

    private void playResult(final Dictionary mBean,boolean isPlayResult,String result){
        String filepath = "";
        String speakContent = "";
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        if(isPlayResult){
            if (TextUtils.isEmpty(mBean.getResultVoiceId())) {
                mBean.setResultVoiceId(System.currentTimeMillis() + 5 + "");
            }
            filepath = path + mBean.getResultVoiceId() + ".pcm";
            speakContent = result;
        }else {
            if (TextUtils.isEmpty(mBean.getQuestionVoiceId())) {
                mBean.setQuestionVoiceId(System.currentTimeMillis() + "");
            }
            filepath = path + mBean.getQuestionVoiceId() + ".pcm";
            speakContent = mBean.getWord_name();
        }
        mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
        if (!AudioTrackUtil.isFileExists(filepath)) {
            loadding();
            XFUtil.showSpeechSynthesizer(mActivity, mSharedPreferences, mSpeechSynthesizer, speakContent,
                    new SynthesizerListener() {
                        @Override
                        public void onSpeakResumed() {
                        }

                        @Override
                        public void onSpeakProgress(int arg0, int arg1, int arg2) {
                        }

                        @Override
                        public void onSpeakPaused() {
                        }

                        @Override
                        public void onSpeakBegin() {
                            finishLoadding();
                        }
                        @Override
                        public void onCompleted(SpeechError arg0) {
                            LogUtil.DefalutLog("---onCompleted");
                            if (arg0 != null) {
                                ToastUtil.diaplayMesShort(mActivity, arg0.getErrorDescription());
                            }
                            DataBaseUtil.getInstance().update(mBean);
                        }
                        @Override
                        public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                        }
                        @Override
                        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                        }
                    });
        } else {
            mMyThread.setDataUri(filepath);
            mThread = AudioTrackUtil.startMyThread(mMyThread);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer = null;
        }
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer = null;
        }
        LogUtil.DefalutLog("MainFragment-onDestroy");
    }

    //more tools
    private void showMoreTools() {
        hideIME();
        final DialogPlus dialog = DialogPlus.newDialog(mActivity)
                .setContentHolder(new ViewHolder(R.layout.dic_more_tools_layout))
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setOverlayBackgroundResource(android.R.color.transparent)
                .create();
        initMoreToolsView(dialog.getHolderView(), dialog);
        dialog.show();
    }

    private void initMoreToolsView(View view, final DialogPlus dialog) {
        FrameLayout auto_clear = (FrameLayout) view.findViewById(R.id.setting_auto_clear);
        final CheckBox auto_clear_cb = (CheckBox) view.findViewById(R.id.setting_auto_clear_cb);
        FrameLayout clear_all = (FrameLayout) view.findViewById(R.id.setting_clear_all);

        boolean AutoClear = mSharedPreferences.getBoolean(KeyUtil.AutoClearDic, false);
        auto_clear_cb.setChecked(AutoClear);

        auto_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                auto_clear_cb.setChecked(!auto_clear_cb.isChecked());
                Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.AutoClearDic, auto_clear_cb.isChecked());
                AVAnalytics.onEvent(mActivity, "dic_tools_auto_clear");
            }
        });
        auto_clear_cb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.AutoClearDic, auto_clear_cb.isChecked());
                AVAnalytics.onEvent(mActivity, "dic_tools_auto_clear");
            }
        });

        clear_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseUtil.getInstance().clearAllDic();
                SDCardUtil.deleteOldFile();
                dialog.dismiss();
                beans.clear();
                mAdapter.notifyDataSetChanged();
                ToastUtil.diaplayMesShort(mActivity, mActivity.getResources().getString(R.string.clear_success));
                AVAnalytics.onEvent(mActivity, "setting_pg_clear_all");
            }
        });
    }
//	private void photoSelectDialog(){
//		String[] titles = {getResources().getString(R.string.take_photo),getResources().getString(R.string.photo_album)};
//		PopDialog mPhonoSelectDialog = new PopDialog(getContext(),titles);
//		mPhonoSelectDialog.setListener(new PopViewItemOnclickListener() {
//			@Override
//			public void onSecondClick(View v) {
//				getImageFromAlbum();
//			}
//			@Override
//			public void onFirstClick(View v) {
//				getImageFromCamera();
//			}
//		});
//		mPhonoSelectDialog.show();
//	}
//
//	public void getImageFromAlbum() {
//		Intent intent = new Intent(Intent.ACTION_PICK,
//				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		intent.setType("image/*");//相片类型
//		startActivityForResult(intent, CameraUtil.REQUEST_CODE_PICK_IMAGE);
//	}
//
//	public void getImageFromCamera() {
//		String state = Environment.getExternalStorageState();
//		if (state.equals(Environment.MEDIA_MOUNTED)) {
//			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			if (takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
//				File photoFile = null;
//				try {
//					photoFile = CameraUtil.createImageFile();
//					mCurrentPhotoPath = photoFile.getAbsolutePath();
//				} catch (IOException ex) {
//					ex.printStackTrace();
//				}
//				if (photoFile != null) {
//					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
//					startActivityForResult(takePictureIntent, CameraUtil.REQUEST_CODE_CAPTURE_CAMEIA);
//				}
//			} else {
//				ToastUtil.diaplayMesShort(getContext(), "请确认已经插入SD卡");
//			}
//		}
//	}
//
//	public void doCropPhoto(Uri uri) {
//		File photoTemp = null;
//		try {
//			photoTemp = new File(CameraUtil.createTempFile());
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");
//		intent.putExtra("crop", "true");
//		intent.putExtra("scale", true);
//		intent.putExtra("return-data", false);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		intent.putExtra("noFaceDetection",  false);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoTemp));
//		startActivityForResult(intent, CameraUtil.PHOTO_PICKED_WITH_DATA);
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == CameraUtil.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
//			if(data != null){
//				Uri uri = data.getData();
//				if(uri != null){
//					doCropPhoto(uri);
//				}
//			}
//		} else if (requestCode == CameraUtil.REQUEST_CODE_CAPTURE_CAMEIA && resultCode == Activity.RESULT_OK) {
//			File f = new File(mCurrentPhotoPath);
//			Uri contentUri = Uri.fromFile(f);
//			doCropPhoto(contentUri);
//		}else if (requestCode == CameraUtil.PHOTO_PICKED_WITH_DATA && resultCode == Activity.RESULT_OK) {
//			sendBaiduOCR();
//		}
//	}
//
//	public void sendBaiduOCR(){
//		try {
//			loadding();
//			LanguagehelperHttpClient.postBaiduOCR(CameraUtil.createTempFile(), new UICallback(mActivity){
//				@Override
//				public void onResponsed(String responseString){
//					finishLoadding();
//					if (!TextUtils.isEmpty(responseString)) {
//						if(JsonParser.isJson(responseString)){
//							BaiduOcrRoot mBaiduOcrRoot = JSON.parseObject(responseString, BaiduOcrRoot.class);
//							if(mBaiduOcrRoot.getErrNum().equals("0")){
//								input_et.setText("");
//								input_et.setText(CameraUtil.getOcrResult(mBaiduOcrRoot));
//							}else{
//								ToastUtil.diaplayMesShort(getContext(), mBaiduOcrRoot.getErrMsg());
//							}
//						}
//					}
//				}
//				@Override
//				public void onFailured() {
//					finishLoadding();
//					showToast(mActivity.getResources().getString(R.string.network_error));
//				}
//			});
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
