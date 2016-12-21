package com.siqi.dict;

/**
 * Created by LukeSkywalker on 2016/12/21.
 */
public class Word {
    private final String word;
    private final String ft;//繁体字
    private final int cc1;//《现代汉语常用字表》常用字
    private final int cc2;//《现代汉语常用字表》次常用字
    private final int tc;//《常用國字標準字體表》
    private final int ty;//《现代汉语通用字表》
    private final String py;//拼音
    private final String tone;//声调
    private final String pyt;//拼音带声调
    private final String kxWord;//对应的康熙字
    private final String set;//集合
    private final String radical;//部首
    private final int kxAllStork;//康熙筆画
    private final int kxOtherStork;//部外筆画

    public Word(String word, String ft, int cc1, int cc2, int tc, int ty, String py, String tone, String pyt, String kxWord, String set, String radical, int kxAllStork, int kxOtherStork) {
        this.word = word;
        this.ft = ft;
        this.cc1 = cc1;
        this.cc2 = cc2;
        this.tc = tc;
        this.ty = ty;
        this.py = py;
        this.tone = tone;
        this.pyt = pyt;
        this.kxWord = kxWord;
        this.set = set;
        this.radical = radical;
        this.kxAllStork = kxAllStork;
        this.kxOtherStork = kxOtherStork;
    }

    public String getWord() {
        return word;
    }

    public String getFt() {
        return ft;
    }

    public int getCc1() {
        return cc1;
    }

    public int getCc2() {
        return cc2;
    }

    public int getTc() {
        return tc;
    }

    public int getTy() {
        return ty;
    }

    public String getPy() {
        return py;
    }

    public String getTone() {
        return tone;
    }

    public String getPyt() {
        return pyt;
    }

    public String getKxWord() {
        return kxWord;
    }

    public String getSet() {
        return set;
    }

    public String getRadical() {
        return radical;
    }

    public int getKxAllStork() {
        return kxAllStork;
    }

    public int getKxOtherStork() {
        return kxOtherStork;
    }
}
