package com.bm8.test;


import com.util.Util;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class test1 {
    /**
     * bm8网站搜索网址
     */
    public static String SEARCH_URL = "http://wuxing.bm8.com.cn/zi/%s.html";

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            Util.initHttpProxy();


//            URL url = new URL("http://wuxing.bm8.com.cn/zi/%D2%BB.html");
//            URL url = new URL("http://wuxing.bm8.com.cn/zi/%EA%96.html");
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//            // 必须设置false，否则会自动redirect到Location的地址
//            conn.setInstanceFollowRedirects(false);
//
//            conn.addRequestProperty("Accept-Charset", "UTF-8;");
//            conn.addRequestProperty("User-Agent",
//                    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
//            conn.addRequestProperty("Referer", "http://wuxing.bm8.com.cn/");
//
//            conn.setDoOutput(true);
//            InputStream in = null;
//            in = url.openStream();
//            String content = pipe(in, "GB2312");
//
//            System.out.println(content);


//			URL url = new URL("http://wuxing.bm8.com.cn/zi/%D2%BB.html");
//			URL url = new URL("http://wuxing.bm8.com.cn/zi/%EA%96.html");
//            URL url = new URL("http://wuxing.bm8.com.cn/zi/%C2%F0.html");
            String urlStr = String.format(SEARCH_URL, URLEncoder.encode("吗", "GBK"));
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 必须设置false，否则会自动redirect到Location的地址
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Host", "wuxing.bm8.com.cn");
            conn.setRequestProperty("Proxy-Connection", "keep-alive");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            conn.addRequestProperty("Referer", "http://wuxing.bm8.com.cn/");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.connect();
            InputStream inputstr = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputstr, "GBK"));
            String s;
            StringBuffer sb = new StringBuffer();
            while ((s = br.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            String content = sb.toString();
            System.out.println(content);
            if (content.contains("<a HREF=\"/\">here</a>")) {
                System.out.println("重定向");
            } else if (content.contains("<title>网站防火墙</title>")) {
                System.out.println("网站防火墙");
            }


//			// 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
//			String getURL = "http://wuxing.bm8.com.cn/zi/%D2%BB.html";
//			URL getUrl = new URL(getURL);
//// 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
//// 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
//			HttpURLConnection connection = (HttpURLConnection) getUrl
//					.openConnection();
//// 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
//// 服务器
//			connection.connect();
//// 取得输入流，并使用Reader读取
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
//			System.out.println("=============================");
//			System.out.println("Contents of get request");
//			System.out.println("=============================");
//			String lines;
//			while ((lines = reader.readLine()) != null) {
//// lines = new String(lines.getBytes(), "utf-8");
//				System.out.println(lines);
//			}
//			reader.close();
//// 断开连接
//			connection.disconnect();
//			System.out.println("=============================");
//			System.out.println("Contents of get request ends");
//			System.out.println("=============================");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String pipe(InputStream in, String charset) throws IOException {
        StringBuffer s = new StringBuffer();
        if (charset == null || "".equals(charset)) {
            charset = "utf-8";
        }
        String rLine = null;
//        BufferedReader bReader = new BufferedReader(new InputStreamReader(in, charset));
        BufferedReader bReader = new BufferedReader(new InputStreamReader(in, charset));
        PrintWriter pw = null;

        FileOutputStream fo = new FileOutputStream("../index.html");
        OutputStreamWriter writer = new OutputStreamWriter(fo, charset);
        pw = new PrintWriter(writer);
        while ((rLine = bReader.readLine()) != null) {
            String tmp_rLine = rLine;
            int str_len = tmp_rLine.length();
            if (str_len > 0) {
                s.append(tmp_rLine);
                s.append("\n");
                pw.println(tmp_rLine);
                pw.flush();
            }
            tmp_rLine = null;
        }
        in.close();
        pw.close();
        return s.toString();
    }
}
