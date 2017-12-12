package com.messi.languagehelper.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParseUtil {

	public static String parseCHDicBaiduHtml(String html){
		StringBuilder sb = new StringBuilder();
		Document doc = Jsoup.parse(html);
		Element basicmean_py = doc.select("div#basicmean-wrapper > div.tab-content dt.pinyin").first();
		if(basicmean_py != null){
			sb.append("基本释义:");
			sb.append("\n");
			sb.append(basicmean_py.text());
			sb.append("\n");
		}
		Elements basicmean = doc.select("div#basicmean-wrapper > div.tab-content p");
		if(basicmean != null && basicmean.size() > 0){
			if(basicmean_py == null){
				sb.append("基本释义:");
				sb.append("\n");
			}
			for (Element element : basicmean){
				sb.append(element.text());
				sb.append("\n");
			}
		}
		Elements detailmean = doc.select("div#detailmean-wrapper > div.tab-content p");
		if(detailmean != null && detailmean.size() > 0){
			sb.append("\n");
			sb.append("详细释义:");
			sb.append("\n");
			for (Element element : detailmean){
				sb.append(element.text());
				sb.append("\n");
			}
		}else {
			Elements detailmean_li = doc.select("div#detailmean-wrapper > div.tab-content li");
			if(detailmean_li != null && detailmean_li.size() > 0){
				sb.append("\n");
				sb.append("详细释义:");
				sb.append("\n");
				for (Element element : detailmean_li){
					sb.append(element.text());
					sb.append("\n");
				}
			}
		}
		Element source = doc.select("div#source-wrapper div.tab-content").first();
		if(source != null){
			sb.append("\n");
			sb.append("出  处:");
			sb.append("\n");
			sb.append(source.text());
			sb.append("\n");
		}

		Elements liju = doc.select("div#liju-wrapper > div.tab-content p");
		if(liju != null && liju.size() > 0){
			sb.append("\n");
			sb.append("例  句:");
			sb.append("\n");
			for (Element element : liju){
				sb.append(element.text());
				sb.append("\t");
			}
			sb.append("\n");
		}

		Elements story = doc.select("div#story-wrapper > div.tab-content p");
		if(story != null && story.size() > 0){
			sb.append("\n");
			sb.append("典  故:");
			sb.append("\n");
			for (Element element : story){
				sb.append(element.text());
				sb.append("\t");
			}
			sb.append("\n");
		}

		Elements zuci = doc.select("div#zuci-wrapper > div.tab-content a");
		if(zuci != null && zuci.size() > 0){
			sb.append("\n");
			sb.append("相关组词:");
			sb.append("\n");
			for (Element element : zuci){
				sb.append(element.text());
				sb.append("\t");
			}
			sb.append("\n");
		}

		Elements synonym = doc.select("div#syn_ant_wrapper div#synonym a");
		if(synonym != null && synonym.size() > 0){
			sb.append("\n");
			sb.append("近义词:");
			sb.append("\n");
			for (Element element : synonym){
				sb.append(element.text());
				sb.append("  ");
			}
			sb.append("\n");
		}

		Elements antonym = doc.select("div#syn_ant_wrapper div#antonym a");
		if(antonym != null && antonym.size() > 0){
			sb.append("\n");
			sb.append("反义词:");
			sb.append("\n");
			for (Element element : antonym){
				sb.append(element.text());
				sb.append("  ");
			}
			sb.append("\n");
		}

		Elements jielong = doc.select("div#jielong-wrapper > div.tab-content a");
		if(jielong != null && jielong.size() > 0){
			sb.append("\n");
			sb.append("成语接龙:");
			sb.append("\n");
			for (Element element : jielong){
				sb.append(element.text());
				sb.append("  ");
			}
			sb.append("\n");
		}

		Elements miyu = doc.select("div#miyu-wrapper > div.tab-content p");
		if(miyu != null && miyu.size() > 0){
			sb.append("\n");
			sb.append("相关谜语:");
			sb.append("\n");
			for (Element element : miyu){
				sb.append(element.text());
				sb.append("\n");
			}
		}

		Element baike = doc.select("div#baike-wrapper div.tab-content").first();
		if(baike != null){
			sb.append("\n");
			sb.append("百科释义:");
			sb.append("\n");
			sb.append(baike.text());
			sb.append("\t");
			sb.append("注：来自百度百科，网友编辑。");
			sb.append("\n");
		}

		Element fanyi = doc.select("div#fanyi-wrapper div.tab-content").first();
		if(fanyi != null){
			sb.append("\n");
			sb.append("英文翻译:");
			sb.append("\n");
			sb.append(fanyi.text());
			sb.append("\n");
		}

		LogUtil.DefalutLog(sb.toString());
		return sb.toString().replace("查看百科", "");
	}

}
