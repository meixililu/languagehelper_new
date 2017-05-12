package com.messi.languagehelper.bean;

import android.text.TextUtils;

import java.util.List;

public class ChDicBSPYDetailListResult {
	
	private List<ChDicBushouPinyinDetail> list;
	private int page;
	private int pagesize;
	private int totalpage;
	private int totalcount;

	public List<ChDicBushouPinyinDetail> getList() {
		setData();
		return list;
	}

	public void setData(){
		for(ChDicBushouPinyinDetail item : list){
			if (TextUtils.isEmpty(item.getJijieResult())) {
				item.setData();
			}
		}
	}

	public void setList(List<ChDicBushouPinyinDetail> list) {
		this.list = list;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getTotalcount() {
		return totalcount;
	}

	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
}