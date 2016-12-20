package com.siqi.http;

import com.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用SOCKET实现简单的网页GET和POST
 *
 * @author siqi
 */
public class Httpclient {

    /**
     * processUrl 参数 HTTP GET
     */
    public static final int METHOD_GET = 0;
    /**
     * processUrl 参数 HTTP POST
     */
    public static final int METHOD_POST = 1;
    /**
     * HTTP GET的报头，简化版
     */
    public static final String HEADER_GET = "GET %s HTTP/1.0\r\nHOST: %s\r\n\r\n";
    /**
     * HTTP POST的报头，简化版
     */
    public static final String HEADER_POST = "POST %s HTTP/1.0\r\nHOST: %s\r\nContent-Length: 0\r\n\r\n";
    /**
     * 网页报头和内容的分割符
     */
    public static final String CONTENT_SEPARATOR = "\r\n\r\n";
    /**
     * 网页请求响应内容byte
     */
    private byte[] bytes = new byte[0];
    /**
     * 网页报头
     */
    private String header = "";
    /**
     * 网页内容
     */
    private String content = "";

    /**
     * 网页编码，默认为UTF-8
     */
    public static final String CHARSET_DEFAULT = "UTF-8";
    /**
     * 网页编码
     */
    private String charset = CHARSET_DEFAULT;

    /**
     * 使用Httpclient的例子
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Httpclient httpclient = new Httpclient();
        // 请求百度首页（手机版）
        httpclient.processUrl("http://m.baidu.com/");
        System.out.println("获取网页http://m.baidu.com/");
        System.out.println("报头为：\r\n" + httpclient.getHeader());
        System.out.println("内容为：\r\n" + httpclient.getContent());
        System.out.println("编码为：\r\n" + httpclient.getCharset());
        System.out.println("************************************");

        // 使用百度搜索"中国"（手机版）
        // 这是手机百度搜索框的源码 <input id="word" type="text" size="20" maxlength="64"
        // name="word">
        String url = String.format("http://m.baidu.com/s?word=%s",
                URLEncoder.encode("中国", CHARSET_DEFAULT));
        httpclient.processUrl(url, METHOD_POST);
        System.out.println("获取网页http://m.baidu.com/s?word=中国");
        System.out.println("报头为：\r\n" + httpclient.getHeader());
        System.out.println("内容为：\r\n" + httpclient.getContent());
        System.out.println("编码为：\r\n" + httpclient.getCharset());
    }

    /**
     * 初始化，设置所有变量为默认值
     */
    private void init() {
        this.bytes = new byte[0];
        this.charset = CHARSET_DEFAULT;
        this.header = "";
        this.content = "";

    }

    /**
     * 获取网页报头header
     *
     * @return
     */
    public String getHeader() {
        return header;
    }

    /**
     * 获取网页内容content
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * 获取网页编码
     *
     * @return
     */
    public String getCharset() {
        return charset;
    }

    /**
     * 请求网页内容（使用HTTP GET）
     *
     * @param url
     * @throws Exception
     */
    public void processUrl(String url) throws Exception {
        processUrl(url, METHOD_GET);
    }

    /**
     * 使用Socket请求（获取）一个网页。<br/>
     * 例如:<br/>
     * processUrl("http://www.baidu.com/", METHOD_GET)会获取百度首页；<br/>
     *
     * @param url    这个网页或者网页内容的地址
     * @param method 请求网页的方法: METHOD_GET或者METHOD_POST
     * @throws Exception
     */
    public void processUrl(String url, int method) throws Exception {

        init();

        // url = "http://www.zdic.net/search/?c=2&q=%E5%A4%A7";
        // 规范化链接，当网址为http://www.baidu.com时，将网址变为：http://www.baidu.com/
        Matcher mat = Pattern.compile("https?://[^/]+").matcher(url);
        if (mat.find() && mat.group().equals(url)) {
            url += "/";
        }


        Socket socket;
        if (Util.inHome()) {
            socket = new Socket(getHostUrl(url), 80); // 设置要连接的服务器地址
            socket.setSoTimeout(3000); // 设置超时时间为3秒
        } else {
            Proxy proxy = new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress("10.199.75.12", 8080));
            socket = new Socket(proxy);
            socket.setSoTimeout(3000); // 设置超时时间为3秒
            socket.connect(new InetSocketAddress(getHostUrl(url), 80));
        }


        String request = null;
        // 构造请求，详情请参考HTTP协议(RFC2616)
        if (method == METHOD_POST) {
            request = String.format(HEADER_POST, getSubUrl(url),
                    getHostUrl(url));
        } else {
            request = String
                    .format(HEADER_GET, getSubUrl(url), getHostUrl(url));
        }

        socket.getOutputStream().write(request.getBytes());// 发送请求

        this.bytes = InputStream2ByteArray(socket.getInputStream());// 读取响应

        // 获取网页编码，我们只需要测试查找前4096个字节，一般编码信息都会在里面找到
        String temp = new String(this.bytes, 0,
                bytes.length < 4096 ? bytes.length : 4096);
        mat = Pattern.compile("(?<=<meta.{0,100}?charset=)[a-z-0-9]*",
                Pattern.CASE_INSENSITIVE).matcher(temp);
        if (mat.find()) {
            this.charset = mat.group();
        } else {
            this.charset = CHARSET_DEFAULT;
        }

        // 用正确的编码得到网页报头和内容
        temp = new String(this.bytes, this.charset);
        int headerEnd = temp.indexOf(CONTENT_SEPARATOR);
        this.header = temp.substring(0, headerEnd);
        this.content = temp.substring(headerEnd + CONTENT_SEPARATOR.length(),
                temp.length());

        socket.close(); // 关闭socket
    }

    /**
     * 根据网址，获取服务器地址<br/>
     * 例如：<br/>
     * http://m.weathercn.com/common/province.jsp
     * <p>
     * 返回：<br/>
     * m.weathercn.com
     *
     * @param url 网址
     * @return
     */
    public static String getHostUrl(String url) {
        String host = "";
        Matcher mat = Pattern.compile("(?<=https?://).+?(?=/)").matcher(url);
        if (mat.find()) {
            host = mat.group();
        }

        return host;
    }

    /**
     * 根据网址，获取网页路径 例如：<br/>
     * http://m.weathercn.com/common/province.jsp
     * <p>
     * 返回：<br/>
     * /common/province.jsp
     *
     * @param url
     * @return 如果没有获取到网页路径，返回"";
     */
    public static String getSubUrl(String url) {
        String subUrl = "";
        Matcher mat = Pattern.compile("https?://.+?(?=/)").matcher(url);
        if (mat.find()) {
            subUrl = url.substring(mat.group().length());
        }

        return subUrl;
    }

    /**
     * 将b1和b2两个byte数组拼接成一个, 结果=b1+b2
     *
     * @param b1
     * @param b2
     * @return
     */
    public static byte[] ByteArrayCat(byte[] b1, byte[] b2) {
        byte[] b = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, b, 0, b1.length);
        System.arraycopy(b2, 0, b, b1.length, b2.length);
        return b;
    }

    /**
     * 读取输入流并转为byte数组，不返回字符串， 是因为输入流的编码不确定，错误的编码会造成乱码。
     *
     * @param is 输入流inputstream
     * @return 字符串
     * @throws IOException
     */
    public static byte[] InputStream2ByteArray(InputStream is)
            throws IOException {
        byte[] b = new byte[0];
        byte[] bb = new byte[4096]; // 缓冲区

        int len = 0;
        while ((len = is.read(bb)) != -1) {
            byte[] newb = new byte[b.length + len];
            System.arraycopy(b, 0, newb, 0, b.length);
            System.arraycopy(bb, 0, newb, b.length, len);
            b = newb;
        }

        return b;
    }
}
