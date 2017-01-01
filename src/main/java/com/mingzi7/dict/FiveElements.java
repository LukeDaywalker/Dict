package com.mingzi7.dict;

/**
 * Created by LukeSkywalker on 2016/12/22.
 */
public class FiveElements {
    private final String word;
    private final String triWord;
    private final String fiveElements;//五行
    private final int storks;//简体笔画
    private final int triStorks;//繁体笔画
    private final int kxStorks;//康熙笔画
    private final String pingYin;//拼音

    public FiveElements(String fiveElements) {
        this.word = "";
        this.triWord = "";
        this.fiveElements = fiveElements;
        this.storks = 0;
        this.triStorks = 0;
        this.kxStorks = 0;
        this.pingYin = "";
    }

    public FiveElements(String word, String triWord, String fiveElements, int storks, int triStorks, int kxStorks, String pingYin) {
        this.word = word;
        this.triWord = triWord;
        this.fiveElements = fiveElements;
        this.storks = storks;
        this.triStorks = triStorks;
        this.kxStorks = kxStorks;
        this.pingYin = pingYin;
    }

    public String getWord() {
        return word;
    }

    public String getTriWord() {
        return triWord;
    }

    public String getFiveElements() {
        return fiveElements;
    }

    public int getStorks() {
        return storks;
    }

    public int getTriStorks() {
        return triStorks;
    }

    public int getKxStorks() {
        return kxStorks;
    }

    public String getPingYin() {
        return pingYin;
    }
}
