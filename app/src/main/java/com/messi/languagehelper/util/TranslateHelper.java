package com.messi.languagehelper.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.bean.HjTranBean;
import com.messi.languagehelper.bean.IcibaNew;
import com.messi.languagehelper.bean.QQTranAILabRoot;
import com.messi.languagehelper.bean.TranDictResult;
import com.messi.languagehelper.bean.TranResultRoot;
import com.messi.languagehelper.bean.YoudaoApiBean;
import com.messi.languagehelper.bean.YoudaoApiResult;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.http.BgCallback;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.httpservice.RetrofitApiService;
import com.messi.languagehelper.impl.OnTranslateFinishListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class TranslateHelper {
//  jscb,youdaoweb,bingweb,hujiangweb,qqfyj,hujiangapi,youdaoapi,baidu#youdaoweb,bingweb,hujiangweb,jscb,qqfyj,youdaoapi,hujiangapi,baidu
    public static String OrderTran = "jscb,youdaoweb,bingweb,hujiangweb,hujiangapi,qqfyj,youdaoapi,baidu";
    public static final String youdaoweb = "youdaoweb";
    public static final String youdaoapi = "youdaoapi";
    public static final String bingweb = "bingweb";
    public static final String jscb = "jscb";
    public static final String hujiangapi = "hujiangapi";
    public static final String hujiangweb = "hujiangweb";
    public static final String qqfyj = "qqfyj";
    public static final String baidu = "baidu";
    public static String HJCookie = "";
    private int OrderTranCounter = 0;
    private String[] tranOrder;
    private OnTranslateFinishListener listener;

    public static void init(SharedPreferences sp){
        initTranOrder(sp);
    }

    public void Translate(OnTranslateFinishListener listener) throws Exception{
        this.listener = listener;
        tranOrder = OrderTran.split(",");
        OrderTranCounter = 0;
        Tran_Task();
    }

    private void DoTranslateByMethod(ObservableEmitter<Record> e){
        String method = getTranslateMethod();
        LogUtil.DefalutLog("DoTranslateByMethod---"+method);
        if(!TextUtils.isEmpty(method)){
            if(youdaoweb.equals(method)){
                Tran_Youdao_Web(e);
            }else if(jscb.equals(method)){
                Tran_Iciba(e);
            }else if(youdaoapi.equals(method)){
                Tran_Youdao_Api(e);
            }else if(bingweb.equals(method)){
                Tran_Bing_Web(e);
            }else if(hujiangapi.equals(method)){
                Tran_Hj_Api(e);
            }else if(hujiangweb.equals(method)){
                Tran_Baidu(e);
            }else if(qqfyj.equals(method)){
                Tran_QQFYJApi(e);
            }else if(baidu.equals(method)){
                Tran_Hj_Web(e);
            }else {
                onFaileTranslate(e);
            }
        }else {
            e.onError(null);
        }
    }

    private String getTranslateMethod(){
        if(tranOrder != null && OrderTranCounter < tranOrder.length){
            return tranOrder[OrderTranCounter];
        }
        return "";
    }

    private void onFaileTranslate(ObservableEmitter<Record> e){
        OrderTranCounter++;
        DoTranslateByMethod(e);
    }

    private void Tran_Task() {
        Observable.create(new ObservableOnSubscribe<Record>() {
            @Override
            public void subscribe(ObservableEmitter<Record> e) throws Exception {
                DoTranslateByMethod(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Record>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Record mResult) {
                        listener.OnFinishTranslate(mResult);
                    }
                    @Override
                    public void onError(Throwable e) {
                        listener.OnFinishTranslate(null);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void Tran_Iciba(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_Iciba");
        LanguagehelperHttpClient.postIcibaNew(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                Record result = null;
                try {
                    result = tran_js_newapi(responseString);
                } catch (Exception ec) {
                    result = null;
                    ec.printStackTrace();
                } finally {
                    if(result != null){
                        e.onNext( result );
                    }else {
                        onFaileTranslate(e);
                    }
                }

            }
        });
    }

    private void Tran_QQAILabApi(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_QQAILAb");
        LanguagehelperHttpClient.postTranQQAILabAPi(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                LogUtil.DefalutLog("Tran_QQAILabApi:"+responseString);
                Record result = null;
                try {
                    QQTranAILabRoot root = JSON.parseObject(responseString,QQTranAILabRoot.class);
                    if(root != null && root.getRet() == 0 && root.getData() != null){
                        result = new Record(root.getData().getTrans_text(), Setings.q);
                    }
                } catch (Exception ec) {
                    result = null;
                    ec.printStackTrace();
                } finally {
                    if(result != null){
                        e.onNext( result );
                    }else {
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_QQFYJApi(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_QQFYJApi");
        LanguagehelperHttpClient.postTranQQFYJAPi(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                Record result = null;
                try {
                    QQTranAILabRoot root = JSON.parseObject(responseString,QQTranAILabRoot.class);
                    if(root != null && root.getRet() == 0 && root.getData() != null){
                        result = new Record(root.getData().getTarget_text(), Setings.q);
                    }
                } catch (Exception ec) {
                    result = null;
                    ec.printStackTrace();
                } finally {
                    if(result != null){
                        e.onNext( result );
                    }else {
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Youdao_Web(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_Youdao_Web");
        if(isNotWord()){
            onFaileTranslate(e);
            return ;
        }
        LanguagehelperHttpClient.get(Setings.YoudaoWeb + Setings.q + Setings.YoudaoWebEnd, new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }
            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                Record result = null;
                try {
                    if (mResponse.isSuccessful()) {
                        String responseString = mResponse.body().string();
                        if (!TextUtils.isEmpty(responseString)) {
                            result = getParseYoudaoWebHtml(responseString);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }finally {
                    if (result != null) {
                        e.onNext( result );
                    }else{
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Bing_Web(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_Bing_Web");
        if(isNotWord()){
            onFaileTranslate(e);
            return ;
        }
        LanguagehelperHttpClient.get(Setings.BingyingWeb + Setings.q, new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }

            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                Record result = null;
                try {
                    if (mResponse.isSuccessful()) {
                        String responseString = mResponse.body().string();
                        if (!TextUtils.isEmpty(responseString)) {
                            result = getParseBingyingWebHtml(responseString);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }finally {
                    if (result != null) {
                        e.onNext( result );
                    }else{
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Hj_Web(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_Hj_Web");
        if(isNotWord()){
            onFaileTranslate(e);
            return ;
        }
        Request request = new Request.Builder()
                .url(Setings.HjiangWeb + Setings.q)
                .header("User-Agent", LanguagehelperHttpClient.Header)
                .header("Cookie", TranslateHelper.HJCookie)
                .build();
        LanguagehelperHttpClient.get(request, new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }

            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                Record result = null;
                try {
                    if (mResponse.isSuccessful()) {
                        String responseString = mResponse.body().string();
                        if (!TextUtils.isEmpty(responseString)) {
                            result = getParseHjiangWebHtml(responseString);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }finally {
                    if (result != null) {
                        e.onNext( result );
                    }else{
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Youdao_Api(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_Youdao_Api");
        FormBody formBody = new FormBody.Builder()
                .add("i", Setings.q)
                .build();
        LanguagehelperHttpClient.post(Setings.YoudaoApi,formBody,new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                Record result = null;
                try {
                    result = parseYoudaoApiResult(responseString);
                } catch (Exception ec) {
                    ec.printStackTrace();
                }finally {
                    if(result != null){
                        e.onNext( result );
                    }else {
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Hj_Api(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---Tran_Hj_Api");
        LanguagehelperHttpClient.postHjApi(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                Record result = null;
                try {
                    result = tran_hj_api(responseString);
                } catch (Exception ec) {
                    ec.printStackTrace();
                }finally {
                    if(result != null){
                        e.onNext( result );
                    }else {
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Baidu(ObservableEmitter<Record> e) {
        LogUtil.DefalutLog("Result---zyhy server");
        String timestamp = String.valueOf(System.currentTimeMillis());
        String platform = SystemUtil.platform;
        String network = SystemUtil.network;
        String sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, Setings.q,
                platform, network, Setings.from, Setings.to);
        if (StringUtils.isEnglish(Setings.q)) {
            Setings.from = "en";
            Setings.to = "zh";
        } else {
            Setings.from = "zh";
            Setings.to = "en";
        }
        RetrofitApiService service = RetrofitApiService.getRetrofitApiService(Setings.TranApi,
                RetrofitApiService.class);
        retrofit2.Call<TranResultRoot<TranDictResult>> call = service.tranDict(Setings.q, Setings.from, Setings.to,
                network, platform, sign, 0, timestamp);
        call.enqueue(new retrofit2.Callback<TranResultRoot<TranDictResult>>() {
             @Override
             public void onResponse(retrofit2.Call<TranResultRoot<TranDictResult>> call, retrofit2.Response<TranResultRoot<TranDictResult>> response) {
                 Record result = null;
                 if (response.isSuccessful()) {
                     TranResultRoot<TranDictResult> mResult = response.body();
                     if (mResult != null){
                         TranDictResult tdResult = mResult.getResult();
                         if (tdResult != null && !TextUtils.isEmpty(tdResult.getResult())) {
                             String des = tdResult.getResult();
                             if (!TextUtils.isEmpty(tdResult.getSymbol())) {
                                 des = tdResult.getSymbol() + "\n" + tdResult.getResult();
                             }
                             result = new Record(des, Setings.q);
                             result.setPh_am_mp3(tdResult.getMp3_am());
                             result.setPh_en_mp3(tdResult.getMp3_en());
                             result.setBackup1(tdResult.getResult());
                         }
                     }
                 }
                 if(result != null){
                     e.onNext( result );
                 }else {
                     onFaileTranslate(e);
                 }
             }
             @Override
             public void onFailure(retrofit2.Call<TranResultRoot<TranDictResult>> call, Throwable t) {
                 onFaileTranslate(e);
             }
        });
    }

    private Record parseYoudaoApiResult(String mResult){
        Record currentDialogBean = null;
        try {
            if(!TextUtils.isEmpty(mResult)) {
                if (JsonParser.isJson(mResult)) {
                    YoudaoApiBean bean = JSON.parseObject(mResult, YoudaoApiBean.class);
                    if (bean != null && bean.getErrorCode() == 0 &&
                            bean.getTranslateResult() != null) {
                        List<List<YoudaoApiResult>> list = bean.getTranslateResult();
                        if(list.size() > 0){
                            List<YoudaoApiResult> item = list.get(0);
                            if(item != null && item.size() > 0){
                                YoudaoApiResult result = item.get(0);
                                if (result != null && !TextUtils.isEmpty(result.getTgt())) {
                                    currentDialogBean = new Record(result.getTgt(), Setings.q);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LogUtil.DefalutLog("parseYoudaoApiResult error");
            e.printStackTrace();
        }
        return currentDialogBean;
    }

    private Record tran_js_newapi(String mResult) throws Exception{
        Record currentDialogBean = null;
        if(!TextUtils.isEmpty(mResult)) {
            if (JsonParser.isJson(mResult)) {
                IcibaNew mIciba = JSON.parseObject(mResult, IcibaNew.class);
                if (mIciba != null && mIciba.getContent() != null) {
                    if (mIciba.getStatus().equals("0")) {
                        StringBuilder sb = new StringBuilder();
                        StringBuilder sbplay = new StringBuilder();
                        if (!TextUtils.isEmpty(mIciba.getContent().getPh_en())) {
                            sb.append("英[");
                            sb.append(mIciba.getContent().getPh_en());
                            sb.append("]    ");
                        }
                        if (!TextUtils.isEmpty(mIciba.getContent().getPh_am())) {
                            sb.append("美[");
                            sb.append(mIciba.getContent().getPh_am());
                            sb.append("]");
                        }
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        if (mIciba.getContent().getWord_mean() != null) {
                            for (String item : mIciba.getContent().getWord_mean()) {
                                sb.append(item.trim());
                                sb.append("\n");
                                sbplay.append(item.trim());
                                sbplay.append(",");
                            }
                        }
                        String resutlStr = "";
                        if(sb.lastIndexOf("\n") > 0){
                            resutlStr = sb.substring(0, sb.lastIndexOf("\n"));
                        }else {
                            resutlStr = sb.toString();
                        }
                        currentDialogBean = new Record(resutlStr, Setings.q);
                        currentDialogBean.setPh_am_mp3(mIciba.getContent().getPh_am_mp3());
                        currentDialogBean.setPh_en_mp3(mIciba.getContent().getPh_en_mp3());
                        currentDialogBean.setPh_tts_mp3(mIciba.getContent().getPh_tts_mp3());
                        currentDialogBean.setBackup1(sbplay.toString());
                        if (!TextUtils.isEmpty(mIciba.getContent().getPh_tts_mp3())) {
                            currentDialogBean.setBackup3(mIciba.getContent().getPh_tts_mp3());
                        }
                    } else if (mIciba.getStatus().equals("1")) {
                        currentDialogBean = new Record(mIciba.getContent().getOut().replaceAll("<br/>", "").trim(), Setings.q);
                    }
                }
            }
        }
        LogUtil.DefalutLog("tran_jscb_api");
        return currentDialogBean;
    }

    private Record tran_hj_api(String mResult) throws Exception{
        Record currentDialogBean = null;
        if(!TextUtils.isEmpty(mResult)) {
            if (JsonParser.isJson(mResult)) {
                HjTranBean mHjTranBean = JSON.parseObject(mResult, HjTranBean.class);
                if (mHjTranBean != null && mHjTranBean.getStatus() == 0
                        && mHjTranBean.getData() != null
                        && !TextUtils.isEmpty(mHjTranBean.getData().getContent())) {
                    currentDialogBean = new Record(mHjTranBean.getData().getContent(), Setings.q);
                    LogUtil.DefalutLog("tran_hj_api http:"+mHjTranBean.getData().getContent());
                }
            }
        }
        LogUtil.DefalutLog("tran_hj_api");
        return currentDialogBean;
    }

    public static void setHjCookie(String cookie){
        if(!TextUtils.isEmpty(cookie)){
            HJCookie = cookie;
        }
    }

    public Record getParseYoudaoWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        Record mrecord = null;

        Document doc = Jsoup.parse(html);
        Element error = doc.select("div.error-wrapper").first();
        if(error != null){
//            LogUtil.DefalutLog(error.text());
            return null;
        }
        Element feedback = doc.select("div.feedback").first();
        if(feedback != null){
//            LogUtil.DefalutLog(feedback.text());
            return null;
        }
        Element symblo = doc.select("h2.wordbook-js > div.baav").first();
        if(symblo != null){
            TranslateUtil.addContent(symblo,sb);
        }
        Element translate = doc.select("div#phrsListTab > div.trans-container").first();
        if(translate != null){
            Element lis = translate.getElementsByTag("ul").first();
            if(lis != null){
                for(Element li : lis.children()){
                    TranslateUtil.addContentAll(li,sb,sb_play);
                }
            }
            Element p = translate.select("p.additional").first();
            if(p != null){
                TranslateUtil.addContentAll(p,sb,sb_play);
            }
        }
        if(sb.length() > 0){
            String resutlStr = "";
            if(sb.lastIndexOf("\n") > 0){
                resutlStr = sb.substring(0, sb.lastIndexOf("\n"));
            }else {
                resutlStr = sb.toString();
            }
            mrecord = new Record(resutlStr,Setings.q);
            mrecord.setBackup1(sb_play.toString());
            return mrecord;
        }else{
            return null;
        }
    }

    public static Record getParseBingyingWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        Record mrecord = null;

        Document doc = Jsoup.parse(html);
        Element smt_hw = doc.select("div.smt_hw").first();
        if(smt_hw != null){
            Element p1_11 = doc.select("div.p1-11").first();
            if(p1_11 != null){
                TranslateUtil.addContentAll(p1_11,sb,sb_play);
            }
        }

        Element symblo = doc.select("div.hd_p1_1").first();
        if(symblo != null){
            TranslateUtil.addContent(symblo,sb);
        }
        Elements translates = doc.select("div.qdef > ul > li");
        if(translates != null && translates.size() > 0){
            for(Element li : translates){
                String content = li.text().trim();
                if(content.contains("网络")){
                    content = content.replace("网络","网络：");
                }
                sb.append(content);
                sb.append("\n");
                sb_play.append(content);
                sb_play.append(",");
            }
        }
        Element fusu = doc.select("div.qdef > div.hd_div1 > div.hd_if").first();
        if(fusu != null){
            TranslateUtil.addContentAll(fusu,sb,sb_play);
        }

        if(sb.length() > 0){
            String resutlStr = "";
            if(sb.lastIndexOf("\n") > 0){
                resutlStr = sb.substring(0, sb.lastIndexOf("\n"));
            }else {
                resutlStr = sb.toString();
            }
            mrecord = new Record(resutlStr,Setings.q);
            mrecord.setBackup1(sb_play.toString());
            return mrecord;
        }else{
            return null;
        }
    }

    public Record getParseHjiangWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        Record mrecord = null;

        Document doc = Jsoup.parse(html);
        Element error = doc.select("div.word-notfound").first();
        if(error != null){
            LogUtil.DefalutLog(error.text());
            return null;
        }

        Element symblo = doc.select("div.word-info > div.pronounces").first();
        if(symblo != null){
            TranslateUtil.addContent(symblo,sb);
        }
        Element translate = doc.select("div.simple").first();
        if(translate != null){
            for(Element li : translate.children()){
                TranslateUtil.addContentAll(li,sb,sb_play);
            }
        }
        if(sb.length() > 0){
            String resutlStr = "";
            if(sb.lastIndexOf("\n") > 0){
                resutlStr = sb.substring(0, sb.lastIndexOf("\n"));
            }else {
                resutlStr = sb.toString();
            }
            mrecord = new Record(resutlStr,Setings.q);
            mrecord.setBackup1(sb_play.toString());
            return mrecord;
        }else{
            return null;
        }
    }

    private int getWordsCount(String content){
        int count = 0;
        int len = content.length();
        for (int i = 0; i < len; i++) {
            char tem = Setings.q.charAt(i);
            if (tem == ' ') count++;
        }
        return count;
    }

    private boolean isNotWord(){
        if(getWordsCount(Setings.q.trim()) > 1){
            return true;
        }else if(StringUtils.isContainChinese(Setings.q.trim())){
            return true;
        }
        return false;
    }

    public static void initTranOrder(SharedPreferences sp){
        try {
            String orderStr = sp.getString(KeyUtil.TranOrder,"");
            if (!TextUtils.isEmpty(orderStr) && orderStr.contains("#")) {
                String[] keys = orderStr.split("#");
                if(keys != null && keys.length > 1){
                    if(!TextUtils.isEmpty(keys[0])){
                        OrderTran = keys[0];
                    }
                    if(!TextUtils.isEmpty(keys[1])){
                        DictHelper.OrderDic = keys[1];
                    }
                }
            }else {
                OrderTran = "jscb,youdaoweb,bingweb,hujiangweb,qqfyj,hujiangapi,baidu,youdaoapi";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
