package com.bm8.dict;

/**
 * Created by LukeSkywalker on 2016/12/22.
 */
public class FiveElements {
    private final String word;
    private final int isSurname;//是否是姓氏
    private final String fiveElements;//五行
    private final String goodOrIll;//凶吉

    public FiveElements(String fiveElements) {
        this.word = "";
        this.isSurname = 0;
        this.fiveElements = fiveElements;
        this.goodOrIll = "";
    }

    public FiveElements(String word, int isSurname, String fiveElements, String goodOrIll) {
        this.word = word;
        this.isSurname = isSurname;
        this.fiveElements = fiveElements;
        this.goodOrIll = goodOrIll;
    }

    public String getWord() {
        return word;
    }

    public int getIsSurname() {
        return isSurname;
    }

    public String getFiveElements() {
        return fiveElements;
    }

    public String getGoodOrIll() {
        return goodOrIll;
    }
}
