package com.union;

import com.siqi.dict.Word;
import com.util.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LukeSkywalker on 2016/12/23.
 */
public class PinYin {
    private static final List<String> shengMuFuList = Arrays.asList("zh", "ch", "sh");
    private static final List<String> shengMuList = Arrays.asList("b", "p", "m", "f", "d", "t", "n", "l", "g", "k", "h", "j", "q", "x", "z", "c", "s", "y", "w", "r");
    private static final List<String> yunMuList = Arrays.asList("a", " o", "e", " i", "u");

    /**
     * 拼音原始文件
     */
    public static final String FILEPATH = String.format("dict%spinyin.txt", File.separatorChar);

    public static void main(String[] args) {
        List<String> pinyinList = getPinYinFromWebpageFile(FILEPATH);
        if (pinyinList == null) {
            return;
        }
        for (String pinyin : pinyinList) {
            if (!TextUtils.isEmpty(pinyin)) {
                String str = String.format("%s\r\n", pinyin);
                System.out.print(str);
            } else {
                String str = String.format("没有拼音");
                System.err.println(str);
            }
        }
    }

    /**
     * 从网页文件获取拼音信息
     *
     * @param file
     * @return
     */
    private static List<String> getPinYinFromWebpageFile(String file) {
        List<String> pyList = new ArrayList<String>();
        try {

            char[] buff = new char[(int) new File(file).length()];

            FileReader reader = new FileReader(file);
            reader.read(buff);
            reader.close();

            String content = new String(buff);


            //<dt><a href="javascript:void(0);" onclick="shd(1,'ang');">ang</a></dt>
            Matcher mat = Pattern.compile("<dt><a href=\"javascript:void\\(0\\);\" onclick=\"shd\\([\\d]{1,2},'(.{1,6})'\\);\">(.{1,6})</a></dt>",
                    Pattern.CASE_INSENSITIVE).matcher(content);
            while (mat.find()) {
                //拼音
                String pinYin = mat.group(1);
                String pinYinTone = mat.group(2);
                String shengMu = "";
                String yunTou = "";
                String yunFu = "";
                String yunWei = "";
                if (pinYin.length() > 1) {
                    String start = pinYin.substring(0, 2);
                    if (shengMuFuList.contains(start)) {
                        shengMu = start;
                    } else {
                        start = pinYin.substring(0, 1);
                        if (shengMuList.contains(start)) {
                            shengMu = start;
                        }
                    }
                    if (pinYin.endsWith("ng")) {
                        yunWei = "ng";
                        if(pinYin.length()==2){
                            shengMu="";
                        }
                    }
                    if (pinYin.endsWith("n")) {
                        yunWei = "n";
                    }
                    if (pinYin.endsWith("m")) {
                        yunWei = "m";
                    }
                    if (pinYin.contains("a")) {
                        yunFu = "a";
                    }
                    if (pinYin.contains("e")) {
                        yunFu = "e";
                    }
                    if (pinYin.contains("i")) {
                        yunFu = "i";
                    }
                    if (pinYin.contains("o")) {
                        yunFu = "o";
                    }
                    if (pinYin.contains("u")) {
                        yunFu = "u";
                    }
                    if (pinYin.contains("v")) {
                        yunFu = "v";
                    }
                    if (pinYin.contains("ia")) {
                        yunTou = "i";
                        yunFu = "a";
                    }
                    if (pinYin.endsWith("ai")) {
                        yunFu = "a";
                        yunWei = "i";
                    }
                    if (pinYin.endsWith("an")) {
                        yunFu = "a";
                        yunWei = "n";
                    }
                    if (pinYin.endsWith("ang")) {
                        yunFu = "a";
                        yunWei = "ng";
                    }
                    if (pinYin.endsWith("ao")) {
                        yunFu = "a";
                        yunWei = "o";
                    }
                    if (pinYin.endsWith("ei")) {
                        yunFu = "e";
                        yunWei = "i";
                    }
                    if (pinYin.endsWith("en")) {
                        yunFu = "e";
                        yunWei = "n";
                    }
                    if (pinYin.endsWith("eng")) {
                        yunFu = "e";
                        yunWei = "ng";
                    }
                    if (pinYin.endsWith("er")) {
                        yunFu = "e";
                        yunWei = "r";
                    }
                    if (pinYin.endsWith("ia")) {
                        yunTou = "i";
                        yunFu = "a";
                    }
                    if (pinYin.endsWith("ie")) {
                        yunTou = "i";
                        yunFu = "e";
                    }
                    if (pinYin.endsWith("iong")) {
                        yunTou = "i";
                        yunFu = "o";
                        yunWei = "ng";
                    }
                    if (pinYin.endsWith("iu")) {
                        yunTou = "i";
                        yunFu = "u";
                    }
                    if (pinYin.endsWith("ou")) {
                        yunFu = "o";
                        yunWei = "u";
                    }
                    if (pinYin.contains("ua")) {
                        yunTou = "u";
                        yunFu = "a";
                    }
                    if (pinYin.endsWith("ue")) {
                        yunTou = "u";
                        yunFu = "e";
                    }
                    if (pinYin.endsWith("ui")) {
                        yunFu = "u";
                        yunWei = "i";
                    }
                    if (pinYin.endsWith("uo")) {
                        yunTou = "u";
                        yunFu = "o";
                    }
                    if (pinYin.endsWith("ve")) {
                        yunTou = "v";
                        yunFu = "e";
                    }
                } else if (pinYin.length() == 1) {
                    if (shengMuList.contains(pinYin)) {
                        shengMu = pinYin;
                    }
                    if (yunMuList.contains(pinYin)) {
                        yunFu = pinYin;
                    }
                    if (pinYin.endsWith("n")) {
                        shengMu="";
                        yunWei = "n";
                    }
                    if (pinYin.endsWith("m")) {
                        shengMu="";
                        yunWei = "m";
                    }
                    if (pinYin.endsWith("o")) {
                        yunFu="";
                        yunWei = "o";
                    }
                }
                if(!pinYin.equals(shengMu+yunTou+yunFu+yunWei)){
                    System.err.println(pinYin + "," + pinYinTone + "," + shengMu + "," + yunTou + "," + yunFu + "," + yunWei);
                    pyList.add("");
                }else {
                    pyList.add(pinYin + "," + pinYinTone + "," + shengMu + "," + yunTou + "," + yunFu + "," + yunWei);
                }
            }
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pyList;
    }


}
