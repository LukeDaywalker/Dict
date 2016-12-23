package com.union;

/**
 * Created by LukeSkywalker on 2016/12/22.
 */
public class UnionWord {
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

    private final String shengMu;//声母
    private final String yunTou;//韵头
    private final String yunFu;//韵腹
    private final String yunWei;//韵尾

    private final String kxWord;//对应的康熙字
    private final String wordSet;//集合
    private final String radical;//部首
    private final int kxAllStork;//康熙筆画
    private final int kxOtherStork;//部外筆画
    private final int isSurname;//是否是姓氏
    private final String fiveElements;//五行
    private final String goodOrIll;//凶吉

    public UnionWord(String word, String jt, String ft, int cc1, int cc2, int tc, int ty,
                     String py, String tone, String pyt, int duoYin,
                     String shengMu, String yunTou, String yunFu, String yunWei,
                     String kxWord, String wordSet, String radical, int kxAllStork, int kxOtherStork, int isSurname, String fiveElements, String goodOrIll) {
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

        this.shengMu = shengMu;
        this.yunTou = yunTou;
        this.yunFu = yunFu;
        this.yunWei = yunWei;

        this.kxWord = kxWord;
        this.wordSet = wordSet;
        this.radical = radical;
        this.kxAllStork = kxAllStork;
        this.kxOtherStork = kxOtherStork;
        this.isSurname = isSurname;
        this.fiveElements = fiveElements;
        this.goodOrIll = goodOrIll;
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

    public String getShengMu() {
        return shengMu;
    }

    public String getYunTou() {
        return yunTou;
    }

    public String getYunFu() {
        return yunFu;
    }

    public String getYunWei() {
        return yunWei;
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
