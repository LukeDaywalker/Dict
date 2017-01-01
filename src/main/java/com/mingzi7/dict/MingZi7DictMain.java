package com.mingzi7.dict;

import com.util.TextUtils;
import org.sqlite.SQLiteException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 从汉典下载汉字网页，并提取拼音信息
 *
 * @author siqi
 */
public class MingZi7DictMain {
    /**
     * 网页保存路径
     */
    public static final String SAVEPATH = String.format("dict%spagesMingZi7%s", File.separatorChar, File.separatorChar);

    public static final String CHARSET_NAME = "UTF-8";
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

    private static final List<FiveElements> mFiveElementList = new ArrayList<FiveElements>();

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
//                new MingZi7DownloadThread(i).start();
//            }
//        }

        for (int i = UNICODE_MIN; i <= UNICODE_MAX; i++) {
            String word = new String(Character.toChars(i));
            FiveElements fiveElements = getPinYinFromWebpageFile(word, String.format(FILEPATH, i));
            if (fiveElements == null) {
                continue;
            }
            String elements = fiveElements.getFiveElements();
            if (elements == null) {
                continue;
            }
            if (!TextUtils.isEmpty(elements)) {
                mFiveElementList.add(fiveElements);
                String str = String.format("%s,%s,%s\r\n", i, word, elements);
                System.out.print(str);
            } else {
                String str = String.format("%s,%s,没有拼音", i, word);
                System.err.println(str);
            }
        }
//
        saveFiveElements(mFiveElementList);

    }

    private static void saveFiveElements(List<FiveElements> fiveElementList) {
        try {
            //连接SQLite的JDBC

            Class.forName("org.sqlite.JDBC");

            //建立一个数据库名comm_word.db的连接，如果不存在就在当前目录下创建之

            Connection conn = DriverManager.getConnection("jdbc:sqlite:union.db");

            Statement stat = conn.createStatement();

            stat.executeUpdate("create table IF NOT EXISTS  five_elements_2 (word  VARCHAR UNIQUE,  triWord VARCHAR,fiveElements VARCHAR,storks INTEGER,triStorks INTEGER,kxStorks INTEGER,pingYin VARCHAR);");
            PreparedStatement prep = conn.prepareStatement(
                    "replace into five_elements_2 values (?,?,?,?,?,?,?);");

            for (FiveElements word : fiveElementList) {
                prep.setString(1, word.getWord());
                prep.setString(2, word.getTriWord());
                prep.setString(3, word.getFiveElements());
                prep.setInt(4, word.getStorks());
                prep.setInt(5, word.getTriStorks());
                prep.setInt(6, word.getKxStorks());
                prep.setString(7, word.getPingYin());
                prep.addBatch();
            }

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);

            conn.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
    private static FiveElements getPinYinFromWebpageFile(String word, String file) {
        try {

            char[] buff = new char[(int) new File(file).length()];

            File f = new File(file);
            InputStreamReader read = new InputStreamReader(new FileInputStream(f), CHARSET_NAME);
            BufferedReader reader = new BufferedReader(read);

            reader.read(buff);
            reader.close();

            String content = new String(buff);

            String simWord="";
            String triWord="";
            String fiveElements="";//五行
            int storks=0;//简体笔画
            int triStorks=0;//繁体笔画
            int kxStorks=0;//康熙笔画
            String pingYin="";//拼音

            //<ul><li>龙</li><li>龍</li><li class="lz1">火</li><li>5</li><li>16</li><li>16</li><li>lóng</li></ul>
            Matcher mat = Pattern.compile("<ul><li>(.)</li><li>(.)</li><li class=\"lz1\">(.)</li><li>(\\d{1,2})</li><li>(\\d{1,2})</li><li>(\\d{1,2})</li><li>([\\s\\S]*)</li></ul>",
                    Pattern.CASE_INSENSITIVE).matcher(content);
            if (mat.find()) {
                simWord = mat.group(1);
                triWord = mat.group(2);
                fiveElements = mat.group(3);
                storks = Integer.parseInt(mat.group(4)) ;
                triStorks = Integer.parseInt(mat.group(5));
                kxStorks = Integer.parseInt(mat.group(6));
                pingYin = mat.group(7);
                return new FiveElements(simWord,triWord,fiveElements,storks,triStorks,kxStorks,pingYin);
            }
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new FiveElements("");

    }
}
