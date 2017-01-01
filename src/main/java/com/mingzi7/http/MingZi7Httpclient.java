package com.mingzi7.http;

import com.mingzi7.dict.MingZi7DictMain;
import com.util.Util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

/**
 * Created by LukeSkywalker on 2016/12/20.
 */
public class MingZi7Httpclient {
    /**
     * 网页内容
     */
    private String content = "";

    public void processUrl(String urlStr) throws Exception {
        Util.initHttpProxy();

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        // 必须设置false，否则会自动redirect到Location的地址
        conn.setInstanceFollowRedirects(false);
        String fakeIP = getRandomIp();
        conn.setRequestProperty("X-Forwarded-For", fakeIP);
        conn.setRequestProperty("Proxy-Connection", "keep-alive");
        conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
        conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        conn.connect();
        InputStream inputstr = new GZIPInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstr, MingZi7DictMain.CHARSET_NAME));
        String s;
        StringBuffer sb = new StringBuffer();
        while ((s = br.readLine()) != null) {
            sb.append(s);
            sb.append("\n");
        }
        content = sb.toString();
    }

    private String getRandomIp() {
        return r(1, 255) + "." + r(1, 255) + "." + r(1, 255) + "." + r(1, 255);
    }

    private int r(int min, int max) {
        return (int) Math.floor(min + Math.random() * (max - min));
    }


    public String getContent() {
        return content;
    }
}
