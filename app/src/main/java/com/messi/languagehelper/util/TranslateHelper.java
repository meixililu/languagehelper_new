package com.messi.languagehelper.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.bean.HjTranBean;
import com.messi.languagehelper.bean.IcibaNew;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.http.BgCallback;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.impl.OnTranslateFinishListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TranslateHelper {

    public static String OrderTran = "bingweb,youdaoweb,jscb,hujiang,baidu";
    public static final String youdaoweb = "youdaoweb";
    public static final String bingweb = "bingweb";
    public static final String jscb = "jscb";
    public static final String hujiang = "hujiang";
    public static final String baidu = "baidu";
    public static String HJCookie = "HJ_UID=d8c93333-bb13-8ce9-e6a8-84919213f184;";
    private int OrderTranCounter = 0;
    private String[] tranOrder;
    private OnTranslateFinishListener listener;

    public void Translate(OnTranslateFinishListener listener) throws Exception{
        this.listener = listener;
        tranOrder = OrderTran.split(",");
        OrderTranCounter = 0;
        Tran_Task();
    }

    private void DoTranslateByMethod(ObservableEmitter<record> e){
        String method = getTranslateMethod();
        LogUtil.DefalutLog("DoTranslateByMethod---"+method);
        if(!TextUtils.isEmpty(method)){
            if(youdaoweb.equals(method)){
                Tran_youdao_web(e);
            }else if(jscb.equals(method)){
                Tran_Iciba(e);
            }else if(bingweb.equals(method)){
                if(getWordsCount() > 1){
                    onFaileTranslate(e);
                }else {
                    Tran_bing_web(e);
                }
            }else if(hujiang.equals(method)){
                Tran_HjApi(e);
            }else if(baidu.equals(method)){
                Tran_Baidu(e);
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

    private void onFaileTranslate(ObservableEmitter<record> e){
        OrderTranCounter++;
        DoTranslateByMethod(e);
    }

    private void Tran_Task() {
        Observable.create(new ObservableOnSubscribe<record>() {
            @Override
            public void subscribe(ObservableEmitter<record> e) throws Exception {
                DoTranslateByMethod(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<record>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(record mResult) {
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

    private void Tran_Iciba(ObservableEmitter<record> e) {
        LogUtil.DefalutLog("Result---Iciba");
        LanguagehelperHttpClient.postIcibaNew(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                record result = null;
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

    private void Tran_youdao_web(ObservableEmitter<record> e) {
        LogUtil.DefalutLog("Result---youdaoweb");
        LanguagehelperHttpClient.get(Setings.YoudaoWeb + Setings.q + Setings.YoudaoWebEnd, new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }
            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                record result = null;
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

    private void Tran_bing_web(ObservableEmitter<record> e) {
        LogUtil.DefalutLog("Result---bingyingweb");
        LanguagehelperHttpClient.get(Setings.BingyingWeb + Setings.q, new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }

            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                record result = null;
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

    private void Tran_HjApi(ObservableEmitter<record> e) {
        LogUtil.DefalutLog("Result---HjWeb");
        LanguagehelperHttpClient.postHjApi(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                record result = null;
                try {
                    result = tran_hj_api(responseString);
                } catch (Exception ec) {
                    ec.printStackTrace();
                }
                if(result != null){
                    e.onNext( result );
                }else {
                    onFaileTranslate(e);
                }
            }
        });
    }

    private void Tran_Baidu(ObservableEmitter<record> e) {
        LanguagehelperHttpClient.postBaidu(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                record result = null;
                try {
                    result = tran_bd_api(responseString);
                } catch (Exception ec) {
                    ec.printStackTrace();
                }
                if(result != null){
                    e.onNext( result );
                }else {
                    onFaileTranslate(e);
                }
            }
        });
    }

    private static record tran_bd_api(String mResult) {
        record currentDialogBean = null;
        try {
            if(!TextUtils.isEmpty(mResult)) {
                if (JsonParser.isJson(mResult)) {
                    String dstString = JsonParser.getTranslateResult(mResult);
                    if (!dstString.contains("error_msg:")) {
                        currentDialogBean = new record(dstString, Setings.q);
                        LogUtil.DefalutLog("tran_bd_api http:"+dstString);
                    }
                }
            }
        }catch (Exception e){
            LogUtil.DefalutLog("tran_bd_api error");
            e.printStackTrace();
        }
        return currentDialogBean;
    }

    private record tran_js_newapi(String mResult) throws Exception{
        record currentDialogBean = null;
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
                        currentDialogBean = new record(resutlStr, Setings.q);
                        currentDialogBean.setBackup1(sbplay.toString());
                        if (!TextUtils.isEmpty(mIciba.getContent().getPh_tts_mp3())) {
                            currentDialogBean.setBackup3(mIciba.getContent().getPh_tts_mp3());
                        }
                    } else if (mIciba.getStatus().equals("1")) {
                        currentDialogBean = new record(mIciba.getContent().getOut().replaceAll("<br/>", "").trim(), Setings.q);
                    }
                }
            }
        }
        LogUtil.DefalutLog("tran_jscb_api");
        return currentDialogBean;
    }

    private record tran_hj_api(String mResult) throws Exception{
        record currentDialogBean = null;
        if(!TextUtils.isEmpty(mResult)) {
            if (JsonParser.isJson(mResult)) {
                HjTranBean mHjTranBean = JSON.parseObject(mResult, HjTranBean.class);
                if (mHjTranBean != null && mHjTranBean.getStatus() == 0
                        && mHjTranBean.getData() != null
                        && !TextUtils.isEmpty(mHjTranBean.getData().getContent())) {
                    currentDialogBean = new record(mHjTranBean.getData().getContent(), Setings.q);
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

    public record getParseYoudaoWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        record mrecord = null;

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
            mrecord = new record(resutlStr,Setings.q);
            mrecord.setBackup1(sb_play.toString());
            return mrecord;
        }else{
            return null;
        }
    }

    public static record getParseBingyingWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        record mrecord = null;

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
            mrecord = new record(resutlStr,Setings.q);
            mrecord.setBackup1(sb_play.toString());
            return mrecord;
        }else{
            return null;
        }
    }

    private int getWordsCount(){
        int count = 0;
        int len = Setings.q.length();
        for (int i = 0; i < len; i++) {
            char tem = Setings.q.charAt(i);
            if (tem == ' ') count++;
        }
        return count;
    }

}
