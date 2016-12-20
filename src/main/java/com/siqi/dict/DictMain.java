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
        // 下载网页
        for (int i = UNICODE_MIN; i <= UNICODE_MAX; i++) {
            // 检查是否已经存在
            String filePath = String.format(FILEPATH, i); // 文件名
            File file = new File(filePath);
            File file1 = new File(filePath + ERROR);
            if (!file.exists() /*&& !file1.exists()*/) {
                new DownloadThread(i).start();
            }
        }

//        //解析网页，得到拼音信息，并保存到data.dat
//        StringBuffer sb = new StringBuffer();
//        for (int i = UNICODE_MIN; i <= UNICODE_MAX; i++) {
//            String word = new String(Character.toChars(i));
//            String pinyin = getPinYinFromWebpageFile(String.format(FILEPATH, i));
//            if (pinyin == null) {
//                continue;
//            }
//            if (!TextUtils.isEmpty(pinyin)) {
//                String str = String.format("%s,%s,%s\r\n", i, word, pinyin);
//                System.out.print(str);
//                sb.append(str);
//            } else {
//                String str = String.format("%s,%s,没有拼音", i, word);
//                System.err.println(str);
//            }
//        }
//
//        //保存到data.dat
//        try {
//            FileWriter fw = new FileWriter(DATA_FILENAME);
//            fw.write(sb.toString());
//            fw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
    private static String getPinYinFromWebpageFile(String file) {
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

            // /z/pyjs/?py=lai4" target="_blank">lài</a><script>spz(
            Matcher mat = Pattern.compile("(?<=/z/pyjs/\\?py=)([a-z1-4]{1,100})(.{3}target=\"_blank\">)(.{1,6})(</a><script>spz\\()",
                    Pattern.CASE_INSENSITIVE).matcher(content);
            while (mat.find()) {
                String group = mat.group(1);
                result += group + ",";
            }
            if (!TextUtils.isEmpty(result)) {
                return result;
            }
            //<span class="dicpy">cal</span> spf("xin1")
            mat = Pattern.compile("(?<=class=\"dicpy\">)[a-z1-4]{0,100}",
                    Pattern.CASE_INSENSITIVE).matcher(content);
            if (mat.find()) {
                return mat.group();
            }
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }
}
