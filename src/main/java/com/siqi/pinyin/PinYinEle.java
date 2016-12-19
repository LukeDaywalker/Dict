package com.siqi.pinyin;

public class PinYinEle {
	private int unicode;
	private String ch;
	private String pinyin;
	
	public PinYinEle(){}
	
	public PinYinEle(String str){
		if(str!=null){
			String[] strs = str.split(",");
			if(strs.length == 3){
				try{
				this.unicode = Integer.parseInt(strs[0]);
				}catch(Exception e){
					
				}
				this.ch = strs[1];
				this.pinyin = strs[2];
			}
		}
		
	}
	
	public int getUnicode() {
		return unicode;
	}
	public void setUnicode(int unicode) {
		this.unicode = unicode;
	}
	public String getCh() {
		return ch;
	}
	public void setCh(String ch) {
		this.ch = ch;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	
}
