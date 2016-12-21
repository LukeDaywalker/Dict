package com.siqi.dict;

import com.util.TextUtils;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从汉典下载汉字网页，并提取拼音信息
 *
 * @author siqi
 */
public class DictMain {
    /**
     * 网页保存路径
     */
    public static final String SAVEPATH = String.format("dict%spages%s", File.separatorChar, File.separatorChar);
    /**
     * 下载的汉字网页名称
     */
    public static final String FILEPATH = SAVEPATH + "%s.html";
    public static final String ERROR = "error";
    /**
     * 字典数据文件名称
     */
    public static final String DATA_FILENAME = "data.txt";

    /**
     * 汉字unicode最小
     */
    public static final int UNICODE_MIN = 0x4E00;

    /**
     * 汉字unicode最大
     */
    public static final int UNICODE_MAX = 0x9FFF;

    /**
     * 准备工作:
     * 1.从汉典网站下载所有汉字的页面，注意，不要在eclipse中打开保存页面的文件夹，
     * 因为每个汉字一个页面，总共有20000+个页面，容易卡死eclipse
     * 2.从汉字页面获取汉字拼音信息，生成data.dat文件
     * 3.生成的data.dat复制到com.siqi.pinyin下面
     * 4.可以使用com.siqi.pinyin.PinYin.java了
     */
    static {
//        // 下载网页
//        for (int i = UNICODE_MIN; i <= UNICODE_MAX; i++) {
//            // 检查是否已经存在
//            String filePath = String.format(FILEPATH, i); // 文件名
//            File file = new File(filePath);
//            File file1 = new File(filePath + ERROR);
//            if (!file.exists() /*&& !file1.exists()*/) {
//                new DownloadThread(i).start();
//            }
//        }

        //解析网页，得到拼音信息，并保存到data.dat
        StringBuffer sb = new StringBuffer();
        for (int i = UNICODE_MIN; i <= UNICODE_MAX; i++) {
            String wordStr = new String(Character.toChars(i));
            Word word = getPinYinFromWebpageFile(wordStr, String.format(FILEPATH, i));
            if (word == null) {
                continue;
            }
            if (!TextUtils.isEmpty(word.getPy())) {
                String str = String.format("%s,%s,%s\r\n", i, wordStr, word.getPy());
                System.out.print(str);
                sb.append(str);
            } else {
                String str = String.format("%s,%s,没有拼音", i, wordStr);
                System.err.println(str);
            }
        }

        //保存到data.dat
        try {
            FileWriter fw = new FileWriter(DATA_FILENAME);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        System.out.println("All prepared!");
    }

    /**
     * 从网页文件获取拼音信息
     *
     * @param file
     * @return
     */
    private static Word getPinYinFromWebpageFile(String word, String file) {
        try {

            String result = "";
            char[] buff = new char[(int) new File(file).length()];

            FileReader reader = new FileReader(file);
            reader.read(buff);
            reader.close();

            String content = new String(buff);
            // spz("yi1")
//            Matcher mat = Pattern.compile("(?<=spz\\(\")[a-z1-4]{0,100}",
//                    Pattern.CASE_INSENSITIVE).matcher(content);

            String info = "";
            String kangXi = "";
            Matcher mat = Pattern.compile("(<div class=\"notice\" id=\"ziif\">[\\s\\S]*)<div id=\"kx\"[\\s\\S]*<div id=\"kxnr\">([\\s\\S]*)<hr class=\"dichr\"/>",
                    Pattern.CASE_INSENSITIVE).matcher(content);
            if (mat.find()) {
                //1 该字基本信息 2 康熙字典
                info = mat.group(1);
                kangXi = mat.group(2);
            }
            if (TextUtils.isEmpty(info) || TextUtils.isEmpty(kangXi)) {
                return null;
            }

            int cc1 = 0;//《现代汉语常用字表》常用字
            int cc2 = 0;//《现代汉语常用字表》次常用字
            int tc = 0;//《常用國字標準字體表》
            int ty = 0;//《现代汉语通用字表》

            if (info.contains("/images/cc1.gif")) {
                cc1 = 1;
            }

            if (info.contains("/images/cc2.gif")) {
                cc2 = 1;
            }

            if (info.contains("/images/tc1.gif")) {
                tc = 1;
            }

            if (info.contains("/images/ty.gif")) {
                ty = 1;
            }


            String ftArea = "";
            //<span id="ft"><a href="/z/29/js/9DBF.htm" target="_blank">鶿</a> <a href="/z/29/js/9DC0.htm" target="_blank">鷀</a> </span>
            mat = Pattern.compile("<span id=\"ft\">(<a .* target=\"_blank\">.</a> )</span>",
                    Pattern.CASE_INSENSITIVE).matcher(info);
            if (mat.find()) {
                //繁体区域
                ftArea = mat.group(1);
            }
            String ft = "";//繁体字
            if (!TextUtils.isEmpty(ftArea)) {
                //"_blank">幺</a>
                mat = Pattern.compile("\"_blank\">(.)</a>",
                        Pattern.CASE_INSENSITIVE).matcher(ftArea);
                while (mat.find()) {
                    //繁体
                    String group = mat.group(1);
                    ft += group + ",";
                }
            }


            String py = "";//拼音
            String tone = "";//声调
            String pyt = "";//拼音带声调
            int duoYin = 0;
            // /z/pyjs/?py=lai4" target="_blank">lài</a><script>spz(
            mat = Pattern.compile("(?<=/z/pyjs/\\?py=)([a-z]{1,6})([0-4])\" target=\"_blank\">(.{1,6})</a><script>spz\\(",
                    Pattern.CASE_INSENSITIVE).matcher(info);
            while (mat.find()) {
                //1 拼音 2 声调 3 拼音带声调
                py += mat.group(1) + ",";
                tone += mat.group(2) + ",";
                pyt += mat.group(3) + ",";
                duoYin++;
            }


            String kxWord = "";//对应的康熙字
            String set = "";//集合
            String radical = "";//部首
            /**
             * >【<a href="/z/kxzd/?kxzm=%E6%9C%AA%E9%9B%86%E4%B8%8B" target="_blank">未集下</a>】【<a href="/z/kxzd/?kxzm=%E6%9C%AA%E9%9B%86%E4%B8%8B&kxbs=%E8%88%8C" target="_blank">舌部</a>】 釬
             */
            mat = Pattern.compile(">【<a href=\"[\\S]*\" target=\"_blank\">(.{3,6})</a>】【<a href=\"[\\S]*\" target=\"_blank\">(.{2})</a>】 (.)",
                    Pattern.CASE_INSENSITIVE).matcher(kangXi);
            if (mat.find()) {
                set = mat.group(1);
                radical = mat.group(2);
                kxWord = mat.group(3);
            }


            int kxAllStork = 0;//康熙筆画
            int kxOtherStork = 0;//部外筆画
            /**
             * ·康熙筆画：<a href="/z/kxzd/zbh/?kxbh=9" target="_blank">9</a>　·部外筆画：1
             *</p>
             */
            mat = Pattern.compile("\n·康熙筆画：<a href=\"[\\S]*\" target=\"_blank\">(.{1,2})</a>　·部外筆画：(.{1,2})\n</p>",
                    Pattern.CASE_INSENSITIVE).matcher(kangXi);
            if (mat.find()) {
                kxAllStork = Integer.parseInt(mat.group(1));
                kxOtherStork = Integer.parseInt(mat.group(2));
            }
            if (kxAllStork > 0) {
                return new Word(word, ft, cc1, cc2, tc, ty, py, tone, pyt, kxWord, set, radical, kxAllStork, kxOtherStork);
            }

            if (!TextUtils.isEmpty(kxWord)) {
                KXStork kxStork = getStork(kxWord);
                if (kxStork != null) {
                    kxWord = kxStork.getKxWord();
                    kxAllStork = kxStork.getAllStork();
                    kxOtherStork = kxStork.getOtherStork();
                    return new Word(word, ft, cc1, cc2, tc, ty, py, tone, pyt, kxWord, set, radical, kxAllStork, kxOtherStork);
                }
            }

        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    private static KXStork getStork(String kxWord) throws FileNotFoundException, Exception {
        int u = (int) kxWord.charAt(0);
        String file = String.format(FILEPATH, u);
        char[] buff = new char[(int) new File(file).length()];

        FileReader reader = new FileReader(file);
        reader.read(buff);
        reader.close();

        String content = new String(buff);

        Matcher mat = Pattern.compile("·康熙筆画：<a href=\"[\\S]*\" target=\"_blank\">(.{1,2})</a>　·部外筆画：(.{1,2})\n</p>",
                Pattern.CASE_INSENSITIVE).matcher(content);
        if (mat.find()) {
            String kxAllStork = mat.group(1);
            String kxOtherStork = mat.group(2);
            return new KXStork(kxWord, Integer.parseInt(kxAllStork), Integer.parseInt(kxOtherStork));
        }
        //内容重定向自“檯”
        mat = Pattern.compile("内容重定向自“(.)”",
                Pattern.CASE_INSENSITIVE).matcher(content);
        if (mat.find()) {
            String word = mat.group(1);
            return getStork(word);
        }
        return null;
    }
}
