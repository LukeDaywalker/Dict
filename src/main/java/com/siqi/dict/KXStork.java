package com.siqi.dict;

/**
 * Created by LukeSkywalker on 2016/12/21.
 */
public class KXStork {
    private final String kxWord;
    private final int allStork;
    private final int otherStork;

    public KXStork(String kxWord, int allStork, int otherStork) {
        this.kxWord = kxWord;
        this.allStork = allStork;
        this.otherStork = otherStork;
    }

    public String getKxWord() {
        return kxWord;
    }

    public int getAllStork() {
        return allStork;
    }

    public int getOtherStork() {
        return otherStork;
    }
}
