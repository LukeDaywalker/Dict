package com.siqi.dict;

/**
 * Created by LukeSkywalker on 2016/12/21.
 */
public class Word {
    private final String word;
    private final String jt;//简体字
    private final String ft;//繁体字
    private final int cc1;//《现代汉语常用字表》常用字
    private final int cc2;//《现代汉语常用字表》次常用字
    private final int tc;//《常用國字標準字體表》
    private final int ty;//《现代汉语通用字表》
    private final String py;//拼音
    private final String tone;//声调
    private final String pyt;//拼音带声调
    private final int duoYin;//是否多音，>0为多音（多音个数）
    private final String kxWord;//对应的康熙字
    private final String wordSet;//集合
    private final String radical;//部首
    private final int kxAllStork;//康熙筆画
    private final int kxOtherStork;//部外筆画

    public Word(String py) {
        this.py = py;

        this.word = "";
        this.jt = "";
        this.ft = "";
        this.cc1 = 0;
        this.cc2 = 0;
        this.tc = 0;
        this.ty = 0;

        this.tone = "";
        this.pyt = "";
        this.duoYin = 0;
        this.kxWord = "";
        this.wordSet = "";
        this.radical = "";
        this.kxAllStork = 0;
        this.kxOtherStork = 0;
    }

    public Word(String word, String jt, String ft, int cc1, int cc2, int tc, int ty, String py, String tone, String pyt, int duoYin, String kxWord, String wordSet, String radical, int kxAllStork, int kxOtherStork) {
        this.word = word;
        this.jt = jt;
        this.ft = ft;
        this.cc1 = cc1;
        this.cc2 = cc2;
        this.tc = tc;
        this.ty = ty;
        this.py = py;
        this.tone = tone;
        this.pyt = pyt;
        this.duoYin = duoYin;
        this.kxWord = kxWord;
        this.wordSet = wordSet;
        this.radical = radical;
        this.kxAllStork = kxAllStork;
        this.kxOtherStork = kxOtherStork;
    }

    public String getWord() {
        return word;
    }

    public String getJt() {
        return jt;
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

    public int getDuoYin() {
        return duoYin;
    }

    public String getKxWord() {
        return kxWord;
    }

    public String getWordSet() {
        return wordSet;
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
