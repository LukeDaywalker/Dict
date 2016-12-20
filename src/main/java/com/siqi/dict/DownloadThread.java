package com.siqi.dict;

import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.siqi.http.Httpclient;

/**
 * 将汉字页面从汉典网站下载下来，存储到本地
 * http://www.zdic.net/search/?c=2
 *
 * @author siqi
 */
public class DownloadThread extends Thread {

    /**
     * 线程最大数目
     */
    public static int THREAD_MAX = 10;

    /**
     * 下载最大重复次数
     */
    public static int RETRY_MAX = 5;

    /**
     * 汉典网站搜索网址
     */
    public static String SEARCH_URL = "http://www.zdic.net/search/?q=%s";

    /**
     * 当前线程数目
     */
    private static int threadCnt = 0;
    private final String filePath;
    private final String errFilePath;

    /**
     * 当前线程处理汉字的unicode编码
     */
    private int unicode = 0;

    /**
     * 如果PATH文件夹不存在，那么创建它
     */
    static {
        try {
            File file = new File(DictMain.SAVEPATH);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 返回当前线程数量
     *
     * @param i 修改当前线程数量 threadCnt += i;
     * @return 返回修改后线程数量
     */
    public static synchronized int threadCnt(int i) {
        threadCnt += i;
        return threadCnt;
    }

    /**
     * 下载UNICODE编码为unicode的汉字网页
     *
     * @param unicode
     */
    public DownloadThread(int unicode) {
        //等待，直到当前线程数量小于THREAD_MAX
        while (threadCnt(0) > THREAD_MAX) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }

        threadCnt(1);    //线程数量+1
        this.unicode = unicode;
        this.filePath = String.format(DictMain.FILEPATH, unicode); // 文件名
        this.errFilePath = filePath + DictMain.ERROR;
    }

    @Override
    public void run() {
        long t1 = System.currentTimeMillis(); // 记录时间


        String word = new String(Character.toChars(unicode)); // 将unicode转换为数字

        boolean downloaded = false;
        int retryCnt = 0; // 下载失败重复次数
        while (!downloaded && retryCnt < RETRY_MAX) {
            try {
                String content = DownloadPage(word);
                if (content.contains("<b>查无此汉字</b>") || content.contains("<p>&nbsp;&nbsp;&nbsp;&nbsp;您所访问的资源不存在:&nbsp;&nbsp;")) {
                    String str = String.format("%s, %s, 查无此汉字", unicode, word);
                    System.err.println(str);
                    SaveToFile(errFilePath, content);
                } else {
                    if (content.contains("<title>康熙字典")) {
                        SaveToFile(filePath, content);
                    } else {
                        System.out.println(word + ":获取失败");
                        retryCnt++;
                        threadCnt(-1);
                        System.err.println(String.format("%s, %s, 下载失败！线程数目：%s 用时：%s", unicode,
                                word, threadCnt(0), System.currentTimeMillis() - t1));
                        return;
                    }
                }
                downloaded = true;

                threadCnt(-1);
                System.out.println(String.format("%s, %s, 下载成功！线程数目：%s 用时：%s",
                        unicode, word, threadCnt(0), System.currentTimeMillis()
                                - t1));
                return;
            } catch (Exception e) {
                System.out.println(e.toString());
                retryCnt++;
            }
        }

        threadCnt(-1);
        System.err.println(String.format("%s, %s, 下载失败！线程数目：%s 用时：%s", unicode,
                word, threadCnt(0), System.currentTimeMillis() - t1));
    }

    /**
     * 在汉典网站上查找汉字，返回汉字字典页面内容
     *
     * @param word
     * @return
     * @throws Exception
     */
    public String DownloadPage(String word) throws Exception {
        //查找word
        Httpclient httpclient = new Httpclient();
        String url = String.format(SEARCH_URL, URLEncoder.encode(word, "UTF-8"));
        httpclient.processUrl(url, Httpclient.METHOD_POST);

        //返回的是一个跳转页
        //获取跳转的链接
        String content = httpclient.getContent();
        Matcher mat = Pattern.compile("(?<=HREF=\")[^\"]+").matcher(content);
        if (mat.find()) {
            String group = mat.group();
            group = group.replace("/js/", "/kx/");
            group = "http://www.zdic.net" + group;
            httpclient.processUrl(group);
        }

        return httpclient.getContent();
    }

    /**
     * 将内容content写入file文件
     *
     * @param file
     * @param content
     */
    public void SaveToFile(String file, String content) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(content);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
