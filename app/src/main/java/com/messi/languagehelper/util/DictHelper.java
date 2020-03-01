package com.messi.languagehelper.util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.bean.HjTranBean;
import com.messi.languagehelper.bean.IcibaNew;
import com.messi.languagehelper.bean.YoudaoApiBean;
import com.messi.languagehelper.bean.YoudaoApiResult;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.http.BgCallback;
import com.messi.languagehelper.http.LanguagehelperHttpClient;

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

public class DictHelper {

    public static String OrderDic= "youdaoweb,bingweb,hujiangweb,jscb,youdaoapi,hujiangapi,baidu";
    public static final String youdaoweb = "youdaoweb";
    public static final String youdaoapi = "youdaoapi";
    public static final String bingweb = "bingweb";
    public static final String jscb = "jscb";
    public static final String hujiangapi = "hujiangapi";
    public static final String hujiangweb = "hujiangweb";
    public static final String baidu = "baidu";
    private int OrderTranCounter = 0;
    private String[] tranOrder;
    private Handler mHandler;

    public void Translate(Handler mHandler){
        this.mHandler = mHandler;
        tranOrder = OrderDic.split(",");
        OrderTranCounter = 0;
        Tran_Task();
    }

    private void DoTranslateByMethod(ObservableEmitter<Dictionary> e){
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
                Tran_Hj_Web(e);
            }else if(baidu.equals(method)){
                Tran_Baidu(e);
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

    private void onFaileTranslate(ObservableEmitter<Dictionary> e){
        OrderTranCounter++;
        DoTranslateByMethod(e);
    }

    private void Tran_Task() {
        Observable.create(new ObservableOnSubscribe<Dictionary>() {
            @Override
            public void subscribe(ObservableEmitter<Dictionary> e) throws Exception {
                DoTranslateByMethod(e);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Dictionary>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(Dictionary mResult) {
                        Setings.dataMap.put(KeyUtil.DataMapKey, mResult);
                        sendMessage(mHandler,1);
                    }
                    @Override
                    public void onError(Throwable e) {
                        sendMessage(mHandler,0);
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void Tran_Youdao_Web(ObservableEmitter<Dictionary> e) {
        LogUtil.DefalutLog("Result---youdaoweb");
        LanguagehelperHttpClient.get(Setings.YoudaoWeb + Setings.q + Setings.YoudaoWebEnd, new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }
            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                Dictionary mDictionary = null;
                try {
                    if (mResponse.isSuccessful()) {
                        String responseString = mResponse.body().string();
                        if (!TextUtils.isEmpty(responseString)) {
                            mDictionary = getParseYoudaoWebHtml(responseString);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (mDictionary != null) {
                        e.onNext(mDictionary);
                    }else {
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Bing_Web(ObservableEmitter<Dictionary> e) {
        LogUtil.DefalutLog("Result---Tran_Bing_Web");
        LanguagehelperHttpClient.get(Setings.BingyingWeb + Setings.q, new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }

            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                Dictionary mDictionary = null;
                try {
                    if (mResponse.isSuccessful()) {
                        String responseString = mResponse.body().string();
                        if (!TextUtils.isEmpty(responseString)) {
                            mDictionary = getParseBingyingWebHtml(responseString);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (mDictionary != null) {
                        e.onNext(mDictionary);
                    }else {
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    private void Tran_Iciba(ObservableEmitter<Dictionary> e) {
        LogUtil.DefalutLog("Result---Tran_Iciba");
        LanguagehelperHttpClient.postIcibaNew(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                Dictionary result = null;
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

    private void Tran_Youdao_Api(ObservableEmitter<Dictionary> e) {
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
                Dictionary result = null;
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

    private void Tran_Hj_Web(ObservableEmitter<Dictionary> e) {
        LogUtil.DefalutLog("Result---Tran_Hj_Web");
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
                Dictionary result = null;
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

    private void Tran_Hj_Api(ObservableEmitter<Dictionary> e) {
        LogUtil.DefalutLog("Result---Tran_Hj_Api");
        LanguagehelperHttpClient.postHjApi(new BgCallback(){
            @Override
            public void onFailured() {
                onFaileTranslate(e);
            }
            @Override
            public void onResponsed(String responseString) {
                Dictionary result = null;
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

    private void Tran_Baidu(ObservableEmitter<Dictionary> e) {
        LanguagehelperHttpClient.postBaidu(new Callback() {
            @Override
            public void onFailure(Call call, IOException ioe) {
                onFaileTranslate(e);
            }

            @Override
            public void onResponse(Call call, Response mResponse) throws IOException {
                Dictionary mDictionary = null;
                try {
                    if (mResponse.isSuccessful()) {
                        String responseString = mResponse.body().string();
                        if (!TextUtils.isEmpty(responseString)) {
                            LogUtil.DefalutLog("Result---baidu tran:" + responseString);
                            String dstString = JsonParser.getTranslateResult(responseString);
                            if (dstString.contains("error_msg:")) {
                                LogUtil.DefalutLog("Result---baidu tran---error_msg:" + dstString);
                            } else {
                                mDictionary = new Dictionary();
                                mDictionary.setType(KeyUtil.ResultTypeTranslate);
                                mDictionary.setWord_name(Setings.q);
                                mDictionary.setResult(dstString);
                                BoxHelper.insert(mDictionary);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (mDictionary != null) {
                        e.onNext(mDictionary);
                    }else {
                        onFaileTranslate(e);
                    }
                }
            }
        });
    }

    public static void sendMessage(Handler mHandler,int result_code){
        if (mHandler != null) {
            Message msg = Message.obtain(mHandler, result_code);
            mHandler.sendMessage(msg);
        }
    }

    public static Dictionary getParseYoudaoWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        Dictionary mDictionary = null;

        sb_play.append(Setings.q);
        sb_play.append("\n");
        Document doc = Jsoup.parse(html);
        Element error = doc.select("div.error-wrapper").first();
        if(error != null){
            LogUtil.DefalutLog(error.text());
            return null;
        }

        Element feedback = doc.select("div.feedback").first();
        if(feedback != null){
            LogUtil.DefalutLog(feedback.text());
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
                    TranslateUtil.addContent(li,sb);
                }
            }
            Element p = translate.select("p.additional").first();
            if(p != null){
                TranslateUtil.addContent(p,sb);
            }
        }
        Element title1 = doc.select("div#tWebTrans > div.wt-container > div.title > span").first();
        if(title1 != null){
            sb.append("\n");
            sb.append("网络释义:");
            sb.append("\n");
            TranslateUtil.addContentAll(title1,sb,sb_play);
        }

        Elements containers = doc.select("div#tWebTrans > div.wt-container.wt-collapse");
        if(containers != null){
            for(Element item : containers){
                Element title = item.select("div.title > span").first();
                if(title != null){
                    TranslateUtil.addContentAll(title,sb,sb_play);
                }
            }
        }
        Element webPhrase = doc.select("div#webPhrase").first();
        if(webPhrase != null){
            sb.append("\n");
            Element title = webPhrase.select("div.title").first();
            if(title != null){
                TranslateUtil.addContentAll(title,sb,sb_play);
            }
            Elements pTags = webPhrase.select("p");
            if(pTags != null){
                for(Element ptag : pTags){
                    TranslateUtil.addContentAll(ptag,sb,sb_play);
                }
            }
        }
        Element authDictTrans = doc.select("div#authTrans > div#authTransToggle > div#authDictTrans").first();
        if(authDictTrans != null){
            Element wordGroup = authDictTrans.select("h4.wordGroup").first();
            if(wordGroup != null){
                sb.append("\n");
                sb.append("21世纪大英汉词典:");
                sb.append("\n");
                TranslateUtil.addContentAll(wordGroup,sb,sb_play);
            }
            Elements lis = authDictTrans.select("div#authDictTrans > ul > li");
            if(lis != null && lis.size() > 0){
                for(Element li : lis){
                    li_iteration(li,sb,sb_play);
                }
            }
        }

        Elements wordGroup = doc.select("div#eTransform > div#transformToggle > div#wordGroup > p");
        if(wordGroup != null && wordGroup.size() > 0){
            sb.append("\n");
            sb.append("词组短语:");
            sb.append("\n");
            for(Element item : wordGroup){
                TranslateUtil.addContentAll(item,sb,sb_play);
            }
        }

        Element discriminate = doc.select("div#eTransform > div#transformToggle > div#discriminate > div.wt-container").first();
        if(discriminate != null){
            Element title = discriminate.select("div.title").first();
            if(title != null){
                sb.append("\n");
                sb.append("词语辨析:");
                sb.append("\n");
                TranslateUtil.addContentAll(title,sb,sb_play);
            }
            Elements wordGroups = discriminate.select("div.collapse-content > div.wordGroup");
            if(wordGroups != null){
                for(Element item : wordGroups){
                    TranslateUtil.addContentAll(item,sb,sb_play);
                }
            }
        }

        Elements examples = doc.select("div#examples > div#examplesToggle > div#bilingual > ul.ol > li");
        if(examples != null && examples.size() > 0){
            sb.append("\n");
            sb.append("双语例句:");
            sb.append("\n");
            for(Element item : examples){
                Elements pTags = item.getElementsByTag("p");
                if(pTags != null){
                    if(pTags.size() > 1){
                        for(int i=0; i<pTags.size()-1; i++){
                            TranslateUtil.addContentAll(pTags.get(i),sb,sb_play);
                        }
                    }
                }
            }
        }

        Elements originalSounds = doc.select("div#examples > div#examplesToggle > div#originalSound > ul.ol > li");
        if(originalSounds != null && originalSounds.size() > 0){
            sb.append("\n");
            sb.append("原声例句:");
            sb.append("\n");
            for(Element item : originalSounds){
                Elements pTags = item.getElementsByTag("p");
                if(pTags != null){
                    if(pTags.size() > 1){
                        for(int i=0; i<pTags.size()-1; i++){
                            TranslateUtil.addContentAll(pTags.get(i),sb,sb_play);
                        }
                    }
                }
            }
        }

        if(sb.length() > 1){
            mDictionary = new Dictionary();
            boolean isEnglish = StringUtils.isEnglish(Setings.q);
            mDictionary.setType(KeyUtil.ResultTypeShowapi);
            if(isEnglish){
                mDictionary.setFrom("en");
                mDictionary.setTo("zh");
            }else{
                mDictionary.setFrom("zh");
                mDictionary.setTo("en");
            }
            mDictionary.setWord_name(Setings.q);
            mDictionary.setResult(sb.substring(0, sb.lastIndexOf("\n")));
            mDictionary.setBackup1(sb_play.toString());
            BoxHelper.insert(mDictionary);
            return mDictionary;
        }else{
            return null;
        }
    }

    private Dictionary tran_js_newapi(String mResult){
        Dictionary mDictionary = null;
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
                        mDictionary = new Dictionary();
                        boolean isEnglish = StringUtils.isEnglish(Setings.q);
                        mDictionary.setType(KeyUtil.ResultTypeShowapi);
                        if(isEnglish){
                            mDictionary.setFrom("en");
                            mDictionary.setTo("zh");
                        }else{
                            mDictionary.setFrom("zh");
                            mDictionary.setTo("en");
                        }
                        mDictionary.setWord_name(Setings.q);
                        mDictionary.setResult(resutlStr);
                        mDictionary.setBackup1(sbplay.toString());
                        if (!TextUtils.isEmpty(mIciba.getContent().getPh_tts_mp3())) {
                            mDictionary.setBackup3(mIciba.getContent().getPh_tts_mp3());
                        }
                        BoxHelper.insert(mDictionary);
                    } else if (mIciba.getStatus().equals("1")) {
                        mDictionary = new Dictionary();
                        boolean isEnglish = StringUtils.isEnglish(Setings.q);
                        mDictionary.setType(KeyUtil.ResultTypeShowapi);
                        if(isEnglish){
                            mDictionary.setFrom("en");
                            mDictionary.setTo("zh");
                        }else{
                            mDictionary.setFrom("zh");
                            mDictionary.setTo("en");
                        }
                        mDictionary.setWord_name(Setings.q);
                        mDictionary.setResult(mIciba.getContent().getOut().replaceAll("<br/>", "").trim());
                        BoxHelper.insert(mDictionary);
                    }
                }
            }
        }
        LogUtil.DefalutLog("tran_jscb_api");
        return mDictionary;
    }

    public static void li_iteration(Element li,StringBuilder sb, StringBuilder sb_play){
        if(!TextUtils.isEmpty(li.ownText().trim())){
            TranslateUtil.addContentAll(li.ownText().trim(),sb,sb_play);
        }
        if(li.childNodeSize() > 0){
            for(Element lichild : li.children()){
                if(lichild.tagName().equals("ul") || lichild.tagName().equals("li")){
                    li_iteration(lichild,sb,sb_play);
                }else{
                    TranslateUtil.addContentAll(lichild,sb,sb_play);
                }
            }
        }
    }

    public static Dictionary getParseBingyingWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        Dictionary mDictionary = null;

        sb_play.append(Setings.q);
        sb_play.append("\n");
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
            }
        }
        Element fusu = doc.select("div.qdef > div.hd_div1 > div.hd_if").first();
        if(fusu != null){
            TranslateUtil.addContent(fusu,sb);
        }

        Elements dapeis = doc.select("div.wd_div > div#thesaurusesid > div#colid > div.df_div2");
        if(dapeis != null && dapeis.size() > 0){
            sb.append("\n");
            sb.append("搭配:");
            sb.append("\n");
            for(Element item : dapeis){
                TranslateUtil.addContentAll(item,sb,sb_play);
            }
        }

        Element authid = doc.select("div.df_div > div#defid > div#authid").first();
        if(authid != null){
            sb.append("\n");
            sb.append("权威英汉双解:");
            sb.append("\n");
            Element title = authid.select("div.hw_ti > div.hw_area2 > div.hd_div2 > span").first();
            if(title != null){
                TranslateUtil.addContentAll(title,sb,sb_play);
            }

            Elements each_seg = authid.select("div.li_sen > div.each_seg");
            for(Element item : each_seg){
                Element type = item.select("div.li_pos > div.pos_lin > div.pos").first();
                if(type != null){
                    TranslateUtil.addContentAll(type,sb,sb_play);
                }
                Elements de_segs = item.select("div.li_pos > div.de_seg > div");
                if(de_segs != null && de_segs.size() > 0){
                    for(Element se : de_segs){
                        if(se.className().contains("se_lis")){
                            TranslateUtil.addContentAll(se,sb,sb_play);
                        }else if(se.className().contains("li_exs")){
                            Elements li_exs = se.select("div.li_ex");
                            if(li_exs != null && li_exs.size() > 0){
                                sb.append("例句:");
                                sb.append("\n");
                                for(Element liex : li_exs){
                                    Elements exs = liex.select("div");
                                    if(exs != null && exs.size() > 1){
                                        TranslateUtil.addContentAll(exs.get(1),sb,sb_play);
                                        TranslateUtil.addContentAll(exs.get(2),sb,sb_play);
                                    }
                                }
                                sb.append("\n");
                            }
                        }
                    }
                }
                Elements idm_s = item.select("div.li_id > div.idm_seg > div.idm_s");
                if(idm_s != null && idm_s.size() > 0){
                    sb.append("IDM:");
                    for(Element idm : idm_s){
                        sb.append("\n");
                        TranslateUtil.addContentAll(idm,sb,sb_play);
                        Element li_ids_co = idm.nextElementSibling();
                        if(li_ids_co.className().contains("li_ids_co")){
                            Element idmdef_li = li_ids_co.select("div.li_sens > div.idmdef_li").first();
                            if(idmdef_li != null){
                                TranslateUtil.addContentAll(idmdef_li,sb,sb_play);
                            }
                            Element li_exs = li_ids_co.select("div.li_sens > div.li_exs").first();
                            if(li_exs != null){
                                Elements li_ex = li_exs.select("div.li_ex");
                                sb.append("例句:");
                                sb.append("\n");
                                for(Element liex : li_ex){
                                    Elements exs = liex.select("div");
                                    if(exs != null && exs.size() > 1){
                                        TranslateUtil.addContentAll(exs.get(1),sb,sb_play);
                                        TranslateUtil.addContentAll(exs.get(2),sb,sb_play);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Elements se_lis = doc.select("div#sentenceCon > div#sentenceSeg > div.se_li");
        if(se_lis != null && se_lis.size() > 0){
            sb.append("\n");
            sb.append("例句:");
            sb.append("\n");
            for(Element seli : se_lis){
                Element se_li1 = seli.select("div.se_li1").first();
                if(se_li1 != null){
                    Elements selis = se_li1.select("div");
                    if(selis != null && selis.size() > 1){
                        TranslateUtil.addContentAll(selis.get(1),sb,sb_play);
                        TranslateUtil.addContentAll(selis.get(2),sb,sb_play);
                    }
                }
            }
        }

        if(sb.length() > 1){
            mDictionary = new Dictionary();
            boolean isEnglish = StringUtils.isEnglish(Setings.q);
            if(isEnglish){
                mDictionary.setFrom("en");
                mDictionary.setTo("zh");
            }else{
                mDictionary.setFrom("zh");
                mDictionary.setTo("en");
            }
            mDictionary.setWord_name(Setings.q);
            mDictionary.setResult(sb.substring(0, sb.lastIndexOf("\n")));
            mDictionary.setBackup1(sb_play.toString());
            BoxHelper.insert(mDictionary);
            return mDictionary;
        }else{
            return null;
        }
    }

    public Dictionary getParseHjiangWebHtml(String html){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb_play = new StringBuilder();
        Dictionary mDictionary = null;

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
        Element detail = doc.select("div.word-details-item").select(".detail").first();
        if(detail != null){
            sb.append("\n");
            sb.append("详细释义:");
            sb.append("\n");
            Element tagen = detail.select("div.detail-tags-en").select(".clearfix").first();
            TranslateUtil.addContentAll(tagen,sb,sb_play);
            Elements sections = detail.select("section.detail-groups > dl");
            Hj_Section(sections,sb,sb_play);
        }

        if(sb.length() > 0){
            String resutlStr = "";
            if(sb.lastIndexOf("\n") > 0){
                resutlStr = sb.substring(0, sb.lastIndexOf("\n"));
            }else {
                resutlStr = sb.toString();
            }
            mDictionary = new Dictionary();
            boolean isEnglish = StringUtils.isEnglish(Setings.q);
            if(isEnglish){
                mDictionary.setFrom("en");
                mDictionary.setTo("zh");
            }else{
                mDictionary.setFrom("zh");
                mDictionary.setTo("en");
            }
            mDictionary.setWord_name(Setings.q);
            mDictionary.setResult(resutlStr);
            mDictionary.setBackup1(sb_play.toString());
            BoxHelper.insert(mDictionary);
            return mDictionary;
        }else{
            return null;
        }
    }

    public static void Hj_Section(Elements dls,StringBuilder sb, StringBuilder sb_play){
        if(dls != null && dls.size() > 0){
            for (Element dl : dls){
                if(dl != null && dl.childNodeSize() > 0){
                    for(Element lichild : dl.children()){
                        if(lichild.tagName().equals("dt")){
                            TranslateUtil.addContentAll(lichild,sb,sb_play);
                        }else if(lichild.tagName().equals("dd")){
                            addChildItem(lichild,sb,sb_play,"\n");
                        }
                    }
                }
            }
        }
    }

    public static void addChildItem(Element item,StringBuilder sb, StringBuilder sb_play, String space){
        if(item != null){
            for(Element lichild : item.children()){
                if(lichild.tagName().equals("h3")){
                    TranslateUtil.addContentAll(lichild,sb,sb_play,space);
                }else if(lichild.tagName().equals("p")){
                    TranslateUtil.addContentAll(lichild,sb,sb_play,space);
                }else {
                    addChildItem(lichild,sb,sb_play,space);
                }
            }
        }
    }

    private Dictionary parseYoudaoApiResult(String mResult){
        Dictionary mDictionary = null;
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
                                    mDictionary = new Dictionary();
                                    boolean isEnglish = StringUtils.isEnglish(Setings.q);
                                    if(isEnglish){
                                        mDictionary.setFrom("en");
                                        mDictionary.setTo("zh");
                                    }else{
                                        mDictionary.setFrom("zh");
                                        mDictionary.setTo("en");
                                    }
                                    mDictionary.setWord_name(Setings.q);
                                    mDictionary.setResult(result.getTgt());
                                    mDictionary.setBackup1(result.getTgt());
                                    BoxHelper.insert(mDictionary);
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
        return mDictionary;
    }

    private Dictionary tran_hj_api(String mResult) throws Exception{
        Dictionary mDictionary = null;
        if(!TextUtils.isEmpty(mResult)) {
            if (JsonParser.isJson(mResult)) {
                HjTranBean mHjTranBean = JSON.parseObject(mResult, HjTranBean.class);
                if (mHjTranBean != null && mHjTranBean.getStatus() == 0
                        && mHjTranBean.getData() != null
                        && !TextUtils.isEmpty(mHjTranBean.getData().getContent())) {
                    mDictionary = new Dictionary();
                    boolean isEnglish = StringUtils.isEnglish(Setings.q);
                    if(isEnglish){
                        mDictionary.setFrom("en");
                        mDictionary.setTo("zh");
                    }else{
                        mDictionary.setFrom("zh");
                        mDictionary.setTo("en");
                    }
                    mDictionary.setWord_name(Setings.q);
                    mDictionary.setResult(mHjTranBean.getData().getContent());
                    mDictionary.setBackup1(mHjTranBean.getData().getContent());
                    BoxHelper.insert(mDictionary);
                }
            }
        }
        LogUtil.DefalutLog("tran_hj_api");
        return mDictionary;
    }

}
