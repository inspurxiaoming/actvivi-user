package com.chengym.activity.common.paginate;

import java.util.List;


/**
 * 分页实例
 * @版本 V 1.0
 */
public class Page {
	
	private int start ;//当前记录起始索引
	private int length=10;//每页显示记录数
	private Integer draw=1;
	private int iTotalRecords; //总记录数
	private int iTotalDisplayRecords; //总记录数
	private boolean auth = true;
	
	private List<?> data ;//存放查询的结果集

	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public int getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(int iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public int getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public List getData() {
		return data;
	}
	public void setData(List<?> data) {
		this.data = data;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}
}
