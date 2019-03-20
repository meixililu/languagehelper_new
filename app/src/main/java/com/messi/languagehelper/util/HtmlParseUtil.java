package com.messi.languagehelper.util;

import android.text.TextUtils;

import com.messi.languagehelper.bean.JuhaiBean;
import com.messi.languagehelper.bean.MiaosouLinkBean;
import com.messi.languagehelper.box.CNWBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParseUtil {

	public static String parseCHDicBaiduHtml(String html) {
		StringBuilder sb = new StringBuilder();
		Document doc = Jsoup.parse(html);
		Element basicmean_py = doc.select("div#basicmean-wrapper > div.tab-content dt.pinyin").first();
		if (basicmean_py != null) {
			sb.append("基本释义:");
			sb.append("\n");
			sb.append(basicmean_py.text());
			sb.append("\n");
		}
		Elements basicmean = doc.select("div#basicmean-wrapper > div.tab-content p");
		if (basicmean != null && basicmean.size() > 0) {
			if (basicmean_py == null) {
				sb.append("基本释义:");
				sb.append("\n");
			}
			for (Element element : basicmean) {
				sb.append(element.text());
				sb.append("\n");
			}
		}
		Elements detailmean = doc.select("div#detailmean-wrapper > div.tab-content p");
		if (detailmean != null && detailmean.size() > 0) {
			sb.append("\n");
			sb.append("详细释义:");
			sb.append("\n");
			for (Element element : detailmean) {
				sb.append(element.text());
				sb.append("\n");
			}
		} else {
			Elements detailmean_li = doc.select("div#detailmean-wrapper > div.tab-content li");
			if (detailmean_li != null && detailmean_li.size() > 0) {
				sb.append("\n");
				sb.append("详细释义:");
				sb.append("\n");
				for (Element element : detailmean_li) {
					sb.append(element.text());
					sb.append("\n");
				}
			}
		}
		Element source = doc.select("div#source-wrapper div.tab-content").first();
		if (source != null) {
			sb.append("\n");
			sb.append("出  处:");
			sb.append("\n");
			sb.append(source.text());
			sb.append("\n");
		}

		Elements liju = doc.select("div#liju-wrapper > div.tab-content p");
		if (liju != null && liju.size() > 0) {
			sb.append("\n");
			sb.append("例  句:");
			sb.append("\n");
			for (Element element : liju) {
				sb.append(element.text());
				sb.append("\t");
			}
			sb.append("\n");
		}

		Elements story = doc.select("div#story-wrapper > div.tab-content p");
		if (story != null && story.size() > 0) {
			sb.append("\n");
			sb.append("典  故:");
			sb.append("\n");
			for (Element element : story) {
				sb.append(element.text());
				sb.append("\t");
			}
			sb.append("\n");
		}

		Elements zuci = doc.select("div#zuci-wrapper > div.tab-content a");
		if (zuci != null && zuci.size() > 0) {
			sb.append("\n");
			sb.append("相关组词:");
			sb.append("\n");
			for (Element element : zuci) {
				sb.append(element.text());
				sb.append("\t");
			}
			sb.append("\n");
		}

		Elements synonym = doc.select("div#syn_ant_wrapper div#synonym a");
		if (synonym != null && synonym.size() > 0) {
			sb.append("\n");
			sb.append("近义词:");
			sb.append("\n");
			for (Element element : synonym) {
				sb.append(element.text());
				sb.append("  ");
			}
			sb.append("\n");
		}

		Elements antonym = doc.select("div#syn_ant_wrapper div#antonym a");
		if (antonym != null && antonym.size() > 0) {
			sb.append("\n");
			sb.append("反义词:");
			sb.append("\n");
			for (Element element : antonym) {
				sb.append(element.text());
				sb.append("  ");
			}
			sb.append("\n");
		}

		Elements jielong = doc.select("div#jielong-wrapper > div.tab-content a");
		if (jielong != null && jielong.size() > 0) {
			sb.append("\n");
			sb.append("成语接龙:");
			sb.append("\n");
			for (Element element : jielong) {
				sb.append(element.text());
				sb.append("  ");
			}
			sb.append("\n");
		}

		Elements miyu = doc.select("div#miyu-wrapper > div.tab-content p");
		if (miyu != null && miyu.size() > 0) {
			sb.append("\n");
			sb.append("相关谜语:");
			sb.append("\n");
			for (Element element : miyu) {
				sb.append(element.text());
				sb.append("\n");
			}
		}

		Element baike = doc.select("div#baike-wrapper div.tab-content").first();
		if (baike != null) {
			sb.append("\n");
			sb.append("百科释义:");
			sb.append("\n");
			sb.append(baike.text());
			sb.append("\t");
			sb.append("注：来自百度百科，网友编辑。");
			sb.append("\n");
		}

		Element fanyi = doc.select("div#fanyi-wrapper div.tab-content").first();
		if (fanyi != null) {
			sb.append("\n");
			sb.append("英文翻译:");
			sb.append("\n");
			sb.append(fanyi.text());
			sb.append("\n");
		}

		LogUtil.DefalutLog(sb.toString());
		return sb.toString().replace("查看百科", "");
	}

	public static List<JuhaiBean> parseJuhaiHtml(String html) {
		List<JuhaiBean> beans = null;
		if(!TextUtils.isEmpty(html)){
			Document doc = Jsoup.parse(html);
			Elements lis = doc.select("li");
			if(lis != null && lis.size() > 0){
				beans = new ArrayList<JuhaiBean>();
				for(Element item : lis){
					Elements ps = item.select("li > p");
					if(ps.size() > 1) {
						JuhaiBean bean = new JuhaiBean(ps.get(0).text().trim(),
								ps.get(1).text().trim());
						beans.add(bean);
					}
				}
			}
		}
		return beans;
	}

	public static List<JuhaiBean> parseJukuHtml(String html) {
		List<JuhaiBean> beans = null;
		if(!TextUtils.isEmpty(html)){
			Document doc = Jsoup.parse(html);
			Elements tr_es = doc.select("tr.e");
			Elements tr_cs = doc.select("tr.c");
			if(tr_es != null && tr_es.size() > 0){
				beans = new ArrayList<JuhaiBean>();
				for(int i = 0;i<tr_es.size();i++){
					String en = tr_es.get(i).text().trim();
					if(!TextUtils.isEmpty(en)) {
						String zh = "";
						if(tr_cs.size() > i){
							zh = tr_cs.get(i).text().trim();
						}
						JuhaiBean bean = new JuhaiBean(en, zh);
						beans.add(bean);
					}
				}
			}
		}
		return beans;
	}

	public static String parseEndicHtml(String html) {
		StringBuilder sb = new StringBuilder();
		Document doc = Jsoup.parse(html);
		Elements etyms = doc.select("section");
		for(Element item : etyms){
			if(item.attr("class").equals("gramb") ||
					item.attr("class").equals("etymology etym")){
				li_iteration(item,sb);
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	public static void li_iteration(Element li,StringBuilder sb){
		if(!TextUtils.isEmpty(li.ownText().trim())){
			addText(sb,li);
		}
		if(li.childNodeSize() > 0){
			for(Element lichild : li.children()){
				if(lichild.attr("class").equals("examples") ||
						lichild.attr("class").equals("synonyms")){
				}else {
					li_iteration(lichild,sb);
				}
			}
		}
	}

	public static void addText(StringBuilder sb,Element item){
		if(item != null){
			String content = item.text().trim();
			if(!TextUtils.isEmpty(content)){
				if(content.length() < 4){
					Pattern patPunc = Pattern.compile("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]$");
					Matcher matcher = patPunc.matcher(content);
					if(!matcher.find()){
						sb.append(content);
						sb.append("  ");
					}
				}else {
					sb.append(content);
					sb.append("\n");
				}
			}
		}
	}

	public static void addText(StringBuilder sb,Element item,String sign){
		if(item != null){
			String content = item.text().trim();
			if(!TextUtils.isEmpty(content)){
				sb.append(content);
				sb.append(sign);
			}
		}
	}

	public static List<CNWBean> parseMiaosouHtml(String url, String html) {
		List<CNWBean> list = new ArrayList<CNWBean>();
		try {
			Document doc = Jsoup.parse(html);
			Elements basicmean = doc.select("div.content > li > a");
			if (basicmean != null && basicmean.size() > 0) {
				for(Element item : basicmean){
					String mhref = item.attr("href");
					String title = "";
					String update = "";
					String img = "";
					if(!TextUtils.isEmpty(mhref)){
						mhref = joinUrl(url,mhref);
						Element titleItem = item.getElementsByTag("span").first();
						if(titleItem != null){
							title = titleItem.text();
						}
						Element updateItem = item.getElementsByTag("i").first();
						if(updateItem != null){
							update = updateItem.text();
						}
						Element imgItem = item.getElementsByTag("img").first();
						if(imgItem != null){
							img = joinUrl(url,imgItem.attr("src"));
							LogUtil.DefalutLog("img:"+img);
						}
						CNWBean mCNWBean = new CNWBean();
						mCNWBean.setItemId(MD5.encode(mhref));
						mCNWBean.setTable(AVOUtil.Caricature.Caricature);
						mCNWBean.setTitle(title);
						mCNWBean.setUpdate_des(update);
						mCNWBean.setImg_url(img);
						mCNWBean.setRead_url(mhref);
						mCNWBean.setSource_url(mhref);
						mCNWBean.setSource_name("找漫画");
						LogUtil.DefalutLog("CNWBean:"+mCNWBean.toString());
						list.add(mCNWBean);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void parseMiaosouDetailHtml(String html,CNWBean mCNWBean) {
		try {
			List<MiaosouLinkBean> beans = new ArrayList<>();
			Document doc = Jsoup.parse(html);
			String title = "";
			String update = "";
			String author = "";
			String img = "";
			String tag = "";
			String des = "";
			Element titleElement = doc.select("div.show > h1").first();
			if(titleElement != null){
				title = titleElement.text();
				if(!TextUtils.isEmpty(title)){
					mCNWBean.setTitle(title);
				}
			}
			Element imgElement = doc.select("div.show > img").first();
			if(imgElement != null){
				img = joinUrl(mCNWBean.getSource_url(),imgElement.attr("src"));
				if(!TextUtils.isEmpty(img)){
					mCNWBean.setImg_url(img);
				}
			}
			Element desElement = doc.select("pre > code").first();
			if(desElement != null){
				des = desElement.text();
				if(!TextUtils.isEmpty(des)){
					mCNWBean.setDes(title);
				}
			}
			Elements lis = doc.select("div.show > ul > li");
			if(lis != null && lis.size() > 4){
				tag = lis.get(1).text();
				author = lis.get(2).text();
				update = lis.get(3).text();
				if(!TextUtils.isEmpty(tag)){
					mCNWBean.setTag(tag);
				}
				if(!TextUtils.isEmpty(author)){
					mCNWBean.setAuthor(author);
				}
				if(!TextUtils.isEmpty(update)){
					mCNWBean.setUpdate_des(update);
				}
			}
			Elements pagers = doc.select("div#comic > a");
			if (pagers != null && pagers.size() > 0) {
				for(Element item : pagers){
					MiaosouLinkBean bean = new MiaosouLinkBean();
					bean.setDes(item.text());
					bean.setLink(joinUrl(mCNWBean.getSource_url(),item.attr("href")));
					beans.add(bean);
				}
				if(beans.size() > 0){
					mCNWBean.setRead_url(beans.get(beans.size()-1).getLink());
				}
				mCNWBean.setMiaosouLinks(beans);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<CNWBean> parseOwllookListHtml(String url,String html) {
		List<CNWBean> list = new ArrayList<CNWBean>();
		try {
			Document doc = Jsoup.parse(html);
			Elements basicmean = doc.select("div.result_item.col-sm-9.col-xs-12");
			if (basicmean != null && basicmean.size() > 0) {
				for(Element item : basicmean){
					CNWBean mCNWBean = new CNWBean();
					mCNWBean.setTable(AVOUtil.Novel.Novel);
					mCNWBean.setSource_name("找小说");
					Element slink = item.select("li > div.netloc > i > a").first();
					if(slink != null){
						String sourceUrl = slink.attr("href");
						mCNWBean.setSource_url(sourceUrl);
						mCNWBean.setItemId(MD5.encode(sourceUrl));
					}
					Element alink = item.select("li > a").first();
					if(alink != null){
						String currentUrl = joinUrl(url,alink.attr("href"));
						mCNWBean.setRead_url(currentUrl);
						mCNWBean.setLast_read_url(currentUrl);
						mCNWBean.setTitle(alink.text());
						if(TextUtils.isEmpty(mCNWBean.getItemId())){
							mCNWBean.setItemId(MD5.encode(currentUrl));
						}
					}
					Elements tagsElement = item.select("div.tags > span");
					String subTitle = "";
					for (Element tagElement : tagsElement){
						subTitle += tagElement.text();
						subTitle += "   ";
					}
					mCNWBean.setSub_title(subTitle.trim().replace("解析","优化"));
//					if(!mCNWBean.getSub_title().contains("未优化")){
					list.add(mCNWBean);
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String joinUrl(String curl,String file){
		URL url = null;
		String q = "";
		try {
			url = new   URL(new   URL(curl),file);
			q = url.toExternalForm();
		} catch (Exception e) {
			e.printStackTrace();
		}
		url = null;
		if(q.indexOf("#")!=-1)q = q.replaceAll("^(.+?)#.*?$", "$1");
		return q;
	}

}
